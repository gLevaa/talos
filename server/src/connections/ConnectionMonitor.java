package connections;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ConnectionMonitor {
    private static final HashMap<String, ArrayList<String>> connectionCache = new HashMap<>();

    public static void logConnection(Socket socket) {
        InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
        String ipAddress = socketAddress.getAddress().toString().split("/")[1];

        ArrayList<String> entry = new ArrayList<>();
        entry.add(0, ""); entry.add(1, "");

        if (connectionCache.containsKey(ipAddress)) {
            entry = connectionCache.get(ipAddress);
        }

        entry.set(0, String.valueOf(ZonedDateTime.now()));

        connectionCache.put(ipAddress, entry);
    }
}
