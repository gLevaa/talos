package handlers;

import connections.PageDownloader;
import connections.RequestManager;
import org.json.JSONObject;
import util.Page;
import util.ParserInterface;

import java.io.IOException;

public class FetchHandler {
    private final RequestManager requestManager;

    public FetchHandler(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public void run() {
        JSONObject authResponse = requestManager.awaitResponse("auth");

        if (authResponse.get("code").equals("600")) {
            handle();
        }
    }

    private void handle() {
        // Request the fetch exchange, let server know we are waiting for
        // a page to parse
        requestManager.serveRequest("fetch");

        // Await the page served by server
        JSONObject fetchResponse = requestManager.awaitResponse("fetch");
        if (fetchResponse == null) { return; }
        Page page = new Page(fetchResponse.get("url").toString());

        try {
            PageDownloader.downloadPage(page);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Execute parser script, return the parsed information to the server
        JSONObject fetchedPages = ParserInterface.requestParsedPages(page);
        requestManager.serveRequest("fetch_data", fetchedPages);
    }
}
