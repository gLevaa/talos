package connections;

import org.json.JSONException;
import org.json.JSONObject;
import util.Page;

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
                case "fetch" -> awaitFetchRequest();
                case "fetch_data" -> awaitFetchData();
                default -> null;
            };
        } catch (SocketTimeoutException e) {
            // 201 - TIMEOUT.INIT
            // Response redundant, timed out

            e.printStackTrace();
            return null;
        } catch (IOException e) {
            serveResponseCode("303"); // SERVER.IO_ERROR

            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            switch (type) {
                case "init" -> serveResponseCode("503"); // REQUEST.FAILED_INIT_READ
                case "fetch" -> serveResponseCode("505"); // REQUEST.FAILED_FETCH_READ
            }

            e.printStackTrace();
            return null;
        }
    }

    public void serveFetchResponse(Page toServe) {
        output.println(new JSONObject("{\"request_type\": \"fetch_response\", \"url\":\"" + toServe.getUrl() + "\"}"));
    }

    private JSONObject awaitFetchRequest() throws IOException, SocketTimeoutException, JSONException {
        JSONObject fetchRequest = new JSONObject(input.readLine());

        if (fetchRequest.has("request_type") && fetchRequest.get("request_type").equals("fetch")) {
            return fetchRequest;
        } else {
            serveResponseCode("505"); // REQUEST.FAILED_FETCH_READ
            return null;
        }
    }

    private JSONObject awaitFetchData() throws IOException, SocketTimeoutException, JSONException {
        JSONObject fetchData = new JSONObject(input.readLine());

        if (fetchData.has("status") && fetchData.has("urls") && fetchData.has("source")) {
            return fetchData;
        } else {
            serveResponseCode("505"); // REQUEST.FAILED_FETCH_READ
            return null;
        }
    }

    private JSONObject awaitInitRequest() throws IOException, SocketTimeoutException, JSONException {
        JSONObject initRequest = new JSONObject(input.readLine());

        if (initRequest.has("request_type") && initRequest.has("who")) {
            return initRequest;
        } else {
            serveResponseCode("503"); // REQUEST.FAILED_INIT_READ
            return null;
        }
    }

    public void serveResponseCode(String code) {
        output.println(new JSONObject("{\"request_type\": \"server_response\", \"code\":\"" + code + "\"}"));
    }
}
