package connections;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConnectionMonitor {
    private static final HashMap<String, List<String>> connectionCache = new HashMap<>();

    public static void logConnection(Socket socket) {
        String ipAddress = getIpAddress(socket);

        synchronized (connectionCache) {
            List<String> entry = connectionCache.getOrDefault(ipAddress, Arrays.asList("", "NA"));
            entry.set(0, String.valueOf(LocalTime.now()));

            connectionCache.put(ipAddress, entry);
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
