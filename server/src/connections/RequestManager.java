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

    public JSONObject awaitInitRequest() {
        try {
            JSONObject initRequest = new JSONObject(input.readLine());

            if (initRequest.has("request_type") && initRequest.has("who")) {
                return initRequest;
            } else {
                serveResponse("503"); // REQUEST.FAILED_INIT_READ
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 201 - TIMEOUT.INIT
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            serveResponse("303"); // SERVER.IO_ERROR
            e.printStackTrace();

            return null;
        } catch (JSONException e) {
            serveResponse("503"); // REQUEST.FAILED_INIT_READ
            e.printStackTrace();

            return null;
        }
    }

    public void serveResponse(String code) {
        JSONObject response = new JSONObject();
        response.put("request_type", "server_response");
        response.put("code", code);

        output.println(response);
    }
}
