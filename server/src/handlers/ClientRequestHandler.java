package handlers;

import connections.RequestManager;
import startup.Server;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientRequestHandler {
    private final BufferedReader input;
    private final PrintWriter output;
    private final RequestManager requestManager;
    private final PageFrontier pageFrontier;

    public ClientRequestHandler(BufferedReader input, PrintWriter output,
                                RequestManager requestManager, PageFrontier pageFrontier) {
        this.input = input;
        this.output = output;
        this.requestManager = requestManager;
        this.pageFrontier = pageFrontier;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            requestManager.serveResponse("302");
            return;
        }

        // TODO
    }
}
