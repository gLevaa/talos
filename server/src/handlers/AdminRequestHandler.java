package handlers;

import connections.Authenticator;
import connections.RequestManager;

import java.net.Socket;

public class AdminRequestHandler implements Handler {
    public final RequestManager requestManager;
    public final Socket connectionSocket;

    public AdminRequestHandler(RequestManager requestManager, Socket connectionSocket) {
        this.requestManager = requestManager;
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        if (Authenticator.isAdminAuthenticated(this)) {
            handle();
        }
        // else => isAdminAuthenticated() servers error to client, thread ends here
    }

    private void handle() {
        // TODO
    }
}