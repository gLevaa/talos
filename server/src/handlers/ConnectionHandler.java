package handlers;

import connections.ConnectionMonitor;
import connections.RequestManager;
import org.json.JSONObject;
import connections.Session;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler implements Runnable, Handler {
    private final Socket connectionSocket;
    private final PageFrontier pageFrontier;

    private boolean failedToSetTimeout = false;

    public ConnectionHandler(Socket connectionSocket, PageFrontier pageFrontier) {
        this.connectionSocket = connectionSocket;
        this.pageFrontier = pageFrontier;

        try {
            connectionSocket.setSoTimeout(15 * 1000);
        } catch (SocketException e) {
            failedToSetTimeout = true;
        }
    }

    @Override
    public void run() {
        if (failedToSetTimeout) {
            return;
        }

        try {
            BufferedReader input = fetchInput();
            PrintWriter output = fetchOutput();

            RequestManager requestManager = new RequestManager(input, output);
            JSONObject initRequest = requestManager.awaitRequest("init");

            if (initRequest != null) {
                ConnectionMonitor.logConnection(connectionSocket);

                Session session = new Session(input, output, requestManager, pageFrontier, connectionSocket);
                forwardInitRequestToHandlers(initRequest, session);
            }
            // else => awaitRequest() serves error to client, thread ends here
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connectionSocket.close();
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void forwardInitRequestToHandlers(JSONObject initRequest, Session session) {
        boolean clientConnecting = String.valueOf(initRequest.get("who")).equals("client");

        Handler handler = clientConnecting ? new ClientRequestHandler(session) : new AdminRequestHandler(session);
        handler.run();
    }

    private BufferedReader fetchInput () throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(connectionSocket.getInputStream());
        return new BufferedReader(inputStreamReader);
    }

    private PrintWriter fetchOutput() throws IOException {
        return new PrintWriter(connectionSocket.getOutputStream(), true);
    }
}
