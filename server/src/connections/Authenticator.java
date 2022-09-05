package connections;

import handlers.AdminRequestHandler;
import handlers.ClientRequestHandler;

import java.util.Arrays;
import java.util.List;

public class Authenticator {
    // TEMP - will be set by admin panel
    private static final String ADMIN_IP = "127.0.0.1";
    private static final List<String> ALLOWED = Arrays.asList("127.0.0.1");

    public static boolean isClientAuthenticated(ClientRequestHandler clientHandler) {
        boolean isAuthenticated = ALLOWED.contains(ConnectionMonitor.getIpAddress(clientHandler.connectionSocket));

        String statusCode = isAuthenticated ? "600" : "601";
        clientHandler.requestManager.serveResponseCode(statusCode);

        return isAuthenticated;
    }

    public static boolean isAdminAuthenticated(AdminRequestHandler adminHandler) {
        boolean isAuthenticated = ConnectionMonitor.getIpAddress(adminHandler.connectionSocket).equals(ADMIN_IP);

        String statusCode = isAuthenticated ? "600" : "601";
        adminHandler.requestManager.serveResponseCode(statusCode);

        return isAuthenticated;
    }
}
