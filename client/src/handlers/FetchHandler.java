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
        if (authResponse.get("code").equals("601")) {
            handle();
        }
    }

    private void handle() {
        requestManager.serveFetchRequest();
        JSONObject fetchResponse = requestManager.awaitResponse("fetch");
        if (fetchResponse == null) { return; }

        Page page = new Page(fetchResponse.get("url").toString());

        try {
            PageDownloader.downloadPage(page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject fetchedPages = ParserInterface.requestParsedPages(true);
        requestManager.serveFetchData(fetchedPages);
    }
}
