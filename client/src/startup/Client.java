package startup;

import handlers.ConnectionHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 9320;

    private static final int THREADS = 1;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);

    // TODO: Deal with exceptions, globally

    public static void main(String[] args) throws IOException {
        for(int i = 0; i < THREADS; i++) {
            spawnNewSessionThread();
        }
    }

    public static void spawnNewSessionThread() throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        threadPool.execute(new ConnectionHandler(socket));
    }
}