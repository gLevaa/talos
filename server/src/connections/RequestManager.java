package connections;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

public class RequestManager {
    private final BufferedReader input;
    private final PrintWriter output;

    public RequestManager(BufferedReader input, PrintWriter output) {
        this.input = input;
        this.output = output;
    }

    public JSONObject awaitRequest(String type) {
        try {
            return switch (type) {
                case "init" -> awaitInitRequest();
                default -> null;
            };
        } catch (SocketTimeoutException e) {
            // 201 - TIMEOUT.INIT
            // Response redundant, timed out
            e.printStackTrace();

            return null;
        } catch (IOException e) {
            serveFinalResponse("303"); // SERVER.IO_ERROR
            e.printStackTrace();

            return null;
        } catch (JSONException e) {
            switch (type) {
                case "init" -> serveFinalResponse("503"); // REQUEST.FAILED_INIT_READ
            }

            e.printStackTrace();
            return null;
        }
    }

    private JSONObject awaitInitRequest() throws IOException, SocketTimeoutException, JSONException {
        JSONObject initRequest = new JSONObject(input.readLine());

        if (initRequest.has("request_type") && initRequest.has("who")) {
            return initRequest;
        } else {
            serveFinalResponse("503"); // REQUEST.FAILED_INIT_READ
            return null;
        }
    }

    public void serveFinalResponse(String code) {
        output.println(new JSONObject("{\"request_type\": \"server_response\", \"code\":\"" + code + "\"}"));
    }
}
