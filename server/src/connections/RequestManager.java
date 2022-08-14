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

    public void serveFinalResponse(String code) {
        JSONObject response = new JSONObject();
        response.put("request_type", "server_response");
        response.put("code", code);

        output.println(response);
    }

    public JSONObject awaitRequest(String type) {
        try {
            return switch (type) {
                case "init" -> awaitInitRequest();
                case "auth_response" -> awaitAuthRequest();
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
                case "auth_response" -> serveFinalResponse("504"); // REQUEST.FAILED_AUTH_READ
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

    private JSONObject awaitAuthRequest()  throws IOException, SocketTimeoutException, JSONException {
        JSONObject authResponse = new JSONObject(input.readLine());

        if (authResponse.has("request_type") && (authResponse.has("key") || authResponse.has("session_id"))) {
            return authResponse;
        } else {
            serveFinalResponse("504"); // REQUEST.FAILED_AUTH_READ
            return null;
        }
    }
}
