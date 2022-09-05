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

    public void serveRequest(String type) {
        try {
            switch (type) {
                case "init" -> serveInitRequest();
                case "fetch" -> serveFetchRequest();
            };
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public JSONObject awaitResponse(String type) {
        try {
            return switch (type) {
                case "auth" -> awaitAuthResponse();
                case "fetch" -> awaitFetchResponse();
                default -> null;
            };
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void serveInitRequest() {
        output.println(new JSONObject("{\"request_type\":\"init\", \"who\":\"client\"}"));
    }

    public void serveFetchRequest() {
        output.println(new JSONObject("{\"request_type\":\"fetch\"}"));
    }

    private JSONObject awaitAuthResponse() throws IOException, SocketTimeoutException, JSONException {
        JSONObject authResponse = new JSONObject(input.readLine());

        if (authResponse.has("code")) {
            return authResponse;
        } else {
            return null;
        }
    }

    private JSONObject awaitFetchResponse() throws IOException, SocketTimeoutException, JSONException {
        JSONObject fetchResponse = new JSONObject(input.readLine());

        if (fetchResponse.has("request_type") && fetchResponse.has("url")) {
            return fetchResponse;
        } else {
            return null;
        }
    }
}
