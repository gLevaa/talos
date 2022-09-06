package handlers;

import connections.Authenticator;
import connections.RequestManager;
import startup.Server;
import util.Page;
import util.PageFrontier;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;

public class ClientRequestHandler implements Handler {
    public final RequestManager requestManager;
    public final Socket connectionSocket;

    public ClientRequestHandler(RequestManager requestManager, Socket connectionSocket) {
        this.requestManager = requestManager;
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            requestManager.serveResponseCode("302"); // SERVER.NO_SEED
            return;
        }

        if (Authenticator.isClientAuthenticated(this)) {
            handle();
        }
        // else => isClientAuthenticated() servers error to client, thread ends here
    }

    private void handle() {
        // Await a URL fetch request from the client
        JSONObject fetchRequest = requestManager.awaitRequest("fetch");
        if (fetchRequest == null) { return; } // awaitRequest() serves error to client, thread ends here

        // At this point, we have received a {"request_type":"fetch"} from client, who is awaiting a URL
        // Fetch the page, respond to the client (TODO: Handle DEAD status)
        Page pageForClient = PageFrontier.fetchNewPage();
        requestManager.serveFetchResponse(pageForClient);

        // Now, the client is downloading the page and parsing the data. We await their response
        JSONObject fetchDataFromClient = requestManager.awaitRequest("fetch_data");
        if (fetchDataFromClient == null) { return; } // awaitRequest() serves error to client, thread ends here

        String fetchedDataStatus = fetchDataFromClient.get("status").toString();
        if (fetchedDataStatus.equals("success")) {
            if (pageForClient.isSource()) {
                // Source page, containing new pages for parsing
                // Add the new pages
                Page newSourcePage = new Page(fetchDataFromClient.get("source").toString());
                PageFrontier.appendNewPage(newSourcePage);

                for (Object url : (JSONArray) fetchDataFromClient.get("urls")) {
                    PageFrontier.appendNewPage(new Page(url.toString()));
                }
            } else {
                // TODO
            }

            PageFrontier.removePageFromCache(pageForClient);;
        } else {
            // TODO: Error tracking, is there an issue with the client, or the stored page? Validation script?
            // Error with client, restore the page to the URL frontier
            PageFrontier.restorePageFromCache(pageForClient);
        }
    }
}
