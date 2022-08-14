package handlers;

import connections.Authenticator;
import connections.Session;

public class AdminRequestHandler implements Handler {
    private final Session session;

    public AdminRequestHandler(Session session) {
        this.session = session;
    }

    public void run() {
        if (Authenticator.isAdminAuthenticated(session)) {
            handle();
        }
        // else => isAdminAuthenticated() servers error to client, thread ends here
    }

    private void handle() {
        // TODO
    }
}