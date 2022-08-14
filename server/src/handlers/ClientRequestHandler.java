package handlers;

import connections.Authenticator;
import startup.Server;
import connections.Session;

public class ClientRequestHandler implements Handler {
    private final Session session;

    public ClientRequestHandler(Session session) {
        this.session = session;
    }

    public void run() {
        if (!Server.IS_SEED_SET) {
            session.requestManager().serveFinalResponse("302"); // SERVER.NO_SEED
            return;
        }

        if (Authenticator.isClientAuthenticated(session)) {
            handle();
        }
        // else => isClientAuthenticated() servers error to client, thread ends here
    }

    private void handle() {
        // TODO
    }
}
