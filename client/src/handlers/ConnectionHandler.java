package handlers;

import connections.RequestManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler implements Runnable {
    private final Socket connectionSocket;

    private boolean failedToSetTimeout = false;

    public ConnectionHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;

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
            requestManager.serveRequest("init");

            FetchHandler fetchHandler = new FetchHandler(requestManager);
            fetchHandler.run();
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

    private BufferedReader fetchInput() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(connectionSocket.getInputStream());
        return new BufferedReader(inputStreamReader);
    }

    private PrintWriter fetchOutput() throws IOException {
        return new PrintWriter(connectionSocket.getOutputStream(), true);
    }
}
