package handlers;

import connections.RequestManager;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminRequestHandler {
    private final BufferedReader input;
    private final PrintWriter output;
    private final RequestManager requestManager;
    private final PageFrontier pageFrontier;
    private final Socket connectionSocket;

    public AdminRequestHandler(BufferedReader input, PrintWriter output,
                                RequestManager requestManager, PageFrontier pageFrontier,
                                Socket connectionSocket) {
        this.input = input;
        this.output = output;
        this.requestManager = requestManager;
        this.pageFrontier = pageFrontier;
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        // TODO
    }
}
