package handlers;

import connections.Authenticator;
import connections.RequestManager;
import startup.Server;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler {
    private final BufferedReader input;
    private final PrintWriter output;
    private final RequestManager requestManager;
    private final PageFrontier pageFrontier;
    private final Socket connectionSocket;

    public ClientRequestHandler(BufferedReader input, PrintWriter output,
                                RequestManager requestManager, PageFrontier pageFrontier,
                                Socket connectionSocket) {
        this.input = input;
        this.output = output;
        this.requestManager = requestManager;
        this.pageFrontier = pageFrontier;
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            requestManager.serveResponse("302");
            return;
        }

        // Authenticate
        Authenticator authenticator = new Authenticator(input, output, requestManager, connectionSocket);
        Boolean isAuthenticated = authenticator.isClientAuthenticated();

        if (isAuthenticated == null) {
            // Error returned to client within Authenticator
            return;
        }

        authenticator.sendAuthConfirmationResponse(isAuthenticated);
        if (!isAuthenticated) {
            return;
        }

        // At this point, client is authenticated
        // TODO
    }
}
