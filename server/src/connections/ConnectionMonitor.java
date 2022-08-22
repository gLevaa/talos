package connections;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;

public class ConnectionMonitor {
    private static final HashMap<String, LocalTime> connectionCache = new HashMap<>();

    public static void logConnection(Socket socket) {
        String ipAddress = getIpAddress(socket);

        synchronized (connectionCache) {
            connectionCache.put(ipAddress, LocalTime.now());
        }
    }

    public static int getConnectedClients() {
        return connectionCache.size();
    }

    public static String getIpAddress(Socket socket) {
        InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
        return socketAddress.getAddress().toString().split("/")[1];
    }
}