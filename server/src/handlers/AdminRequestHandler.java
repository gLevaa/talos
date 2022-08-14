package handlers;

import connections.RequestManager;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class AdminRequestHandler {
    private final BufferedReader input;
    private final PrintWriter output;
    private final RequestManager requestManager;
    private final PageFrontier pageFrontier;

    public AdminRequestHandler(BufferedReader input, PrintWriter output,
                                RequestManager requestManager, PageFrontier pageFrontier) {
        this.input = input;
        this.output = output;
        this.requestManager = requestManager;
        this.pageFrontier = pageFrontier;
    }

    public void run() {
        // TODO
    }
}
