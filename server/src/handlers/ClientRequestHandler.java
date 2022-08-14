package handlers;

import connections.Authenticator;
import connections.RequestManager;
import startup.Server;
import util.CommonInfo;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler {
    private final CommonInfo commonInfo;

    public ClientRequestHandler(CommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            commonInfo.requestManager().serveFinalResponse("302");
            return;
        }

        // Authenticate
        Authenticator authenticator = new Authenticator(commonInfo);
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
