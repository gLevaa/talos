package handlers;

import connections.ConnectionMonitor;
import connections.RequestManager;
import org.json.JSONObject;
import util.PageFrontier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler implements Runnable {
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
            // 304 SERVER.FAILED_TIMEOUT_SET
            // Response redundant, TCP error
            return;
        }

        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(connectionSocket.getInputStream());
            BufferedReader input = new BufferedReader(inputStreamReader);
            PrintWriter output = new PrintWriter(connectionSocket.getOutputStream(), true);

            RequestManager requestManager = new RequestManager(input, output);
            JSONObject initRequest = requestManager.awaitRequest("init");

            ConnectionMonitor.logConnection(connectionSocket);

            if (initRequest != null) {
                forwardInitRequestToHandlers(initRequest, input, output, requestManager);
            }
        } catch (IOException e) {
            // TODO
        } finally {
            try {
                connectionSocket.close();
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                // TODO
            }
        }
    }

    private void forwardInitRequestToHandlers(JSONObject initRequest, BufferedReader input,
                                              PrintWriter output, RequestManager requestManager) {
        String whoIsConnecting = String.valueOf(initRequest.get("who"));

        if (whoIsConnecting.equals("client")) {
            ClientRequestHandler clientHandler = new ClientRequestHandler(input, output, requestManager, pageFrontier, connectionSocket);
            clientHandler.run();
        } else if (whoIsConnecting.equals("admin")) {
            AdminRequestHandler adminRequestHandler = new AdminRequestHandler(input, output, requestManager, pageFrontier, connectionSocket);
            adminRequestHandler.run();
        }
    }
}
