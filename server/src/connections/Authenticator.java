package connections;

import java.util.Arrays;
import java.util.List;

public class Authenticator {
    // TEMP - will be set by admin panel
    private static final String ADMIN_IP = "127.0.0.1";
    private static final List<String> ALLOWED = Arrays.asList("127.0.0.1");

    public static boolean isClientAuthenticated(Session session) {
        boolean isAuthenticated = ALLOWED.contains(ConnectionMonitor.getIpAddress(session.connectionSocket()));

        if (!isAuthenticated) {
            session.requestManager().serveResponseCode("600"); // AUTH.BAD
        } else {
            session.requestManager().serveResponseCode("601"); // AUTH.OK
        }
        return isAuthenticated;
    }

    public static boolean isAdminAuthenticated(Session session) {
        boolean isAuthenticated = ConnectionMonitor.getIpAddress(session.connectionSocket()).equals(ADMIN_IP);

        if (!isAuthenticated) {
            session.requestManager().serveResponseCode("600");
        } else {
            session.requestManager().serveResponseCode("601");
        }
        return isAuthenticated;
    }
}
