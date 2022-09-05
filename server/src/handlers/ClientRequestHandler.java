package handlers;

import connections.Authenticator;
import org.json.JSONArray;
import org.json.JSONObject;
import startup.Server;
import connections.Session;
import util.Page;

public class ClientRequestHandler implements Handler {
    private final Session session;

    public ClientRequestHandler(Session session) {
        this.session = session;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            session.requestManager().serveResponseCode("302"); // SERVER.NO_SEED
            return;
        }

        if (Authenticator.isClientAuthenticated(session)) {
            handle();
        }
        // else => isClientAuthenticated() servers error to client, thread ends here
    }

    private void handle() {
        JSONObject fetchRequest = session.requestManager().awaitRequest("fetch");
        if (fetchRequest == null) { return; } // awaitRequest() serves error to client, thread ends here

        // TODO: DEAD FLAG
        Page toServe = session.pageFrontier().fetchNewPage();
        session.requestManager().serveFetchResponse(toServe);

        JSONObject fetchData = session.requestManager().awaitRequest("fetch_data");
        if (fetchData == null) { return; } // awaitRequest() serves error to client, thread ends here

        if (fetchData.get("status").equals("success")) {
            session.pageFrontier().removePageFromCache(toServe);
            // TODO: Cannot complete until parser finished

            Page source = new Page(fetchData.get("source").toString());
            JSONArray urls =(JSONArray) fetchData.get("urls");
            for (int i = 0; i < urls.length(); i++) {
                session.pageFrontier().appendNewPage(new Page(urls.get(i).toString()));
            }
            session.pageFrontier().appendNewPage(source);

            System.out.println(session.pageFrontier());

        } else {
            session.pageFrontier().restorePageFromCache(toServe);
        }
    }
}
