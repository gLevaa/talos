package connections;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authenticator {
    private final String SECRET = "LB4eTi!L";

    private final BufferedReader input;
    private final PrintWriter output;
    private final RequestManager requestManager;
    private final Socket connectionSocket;

    public Authenticator(BufferedReader input, PrintWriter output,
                         RequestManager requestManager, Socket connectionSocket) {
        this.input = input;
        this.output = output;
        this.requestManager = requestManager;
        this.connectionSocket = connectionSocket;
    }

    public Boolean isClientAuthenticated() {
        sendBeginAuthRequest();
        JSONObject authResponse = awaitAuthResponse();

        if (authResponse != null) {
            if(authResponse.has("session_id")) {
                // Already authorised, check ConnectionMonitor
                // TODO
            } else if (authResponse.has("key")) {
                // Not already authorised
                // TODO
            }

            return authResponse.get("key").equals(generateValidKey());
        }

        return null;
    }

    private void sendBeginAuthRequest() {
        JSONObject beginAuthRequest = new JSONObject();
        beginAuthRequest.put("request_type", "begin_auth");

        output.println(beginAuthRequest);
    }

    public void sendAuthConfirmationResponse(boolean isAuthorised) {
        JSONObject authConfirmationRequest = new JSONObject();
        authConfirmationRequest.put("request_type", "auth_confirm");
        authConfirmationRequest.put("authorised", String.valueOf(isAuthorised));

        output.println(authConfirmationRequest);
    }

    private String generateValidKey() {
        String ipAddress = ConnectionMonitor.getIpAddress(connectionSocket);
        return getMd5(ipAddress + SECRET);
    }

    private JSONObject awaitAuthResponse() {
        try {
            JSONObject authResponse = new JSONObject(input.readLine());

            if (authResponse.has("request_type") && (authResponse.has("key") || authResponse.has("session_id"))) {
                return authResponse;
            } else {
                requestManager.serveResponse("504"); // REQUEST.FAILED_AUTH_READ
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 201 - TIMEOUT.AUTH
            // Response redundant, timed out
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            requestManager.serveResponse("303"); // SERVER.IO_ERROR
            e.printStackTrace();

            return null;
        } catch (JSONException e) {
            requestManager.serveResponse("504"); // REQUEST.FAILED_AUTH_READ
            e.printStackTrace();

            return null;
        }
    }

    private String getMd5(String input) {
        // https://www.geeksforgeeks.org/md5-hash-in-java/
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } // Do nothing, static algorithm name
    }
}
