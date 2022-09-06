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

    public void serveRequest(String type, Object argument) {
        try {
            switch (type) {
                case "fetch_data" -> serveFetchData(argument);
            };
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private void serveInitRequest() {
        JSONObject initRequest = new JSONObject();
        initRequest.put("request_type", "init");
        initRequest.put("who", "client");

        output.println(initRequest);
    }

    private void serveFetchRequest() {
        JSONObject fetchRequest = new JSONObject();
        fetchRequest.put("request_type", "fetch");

        output.println(fetchRequest);
    }

    private void serveFetchData(Object response) {
        if (response instanceof JSONObject) {
            output.println(response);
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

    private JSONObject awaitAuthResponse() throws IOException, JSONException {
        JSONObject authResponse = new JSONObject(input.readLine());

        if (authResponse.has("code")) {
            return authResponse;
        } else {
            return null;
        }
    }

    private JSONObject awaitFetchResponse() throws IOException, JSONException {
        JSONObject fetchResponse = new JSONObject(input.readLine());

        if (fetchResponse.has("request_type") && fetchResponse.has("url")) {
            return fetchResponse;
        } else {
            return null;
        }
    }
}
