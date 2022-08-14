package connections;

import org.json.JSONObject;
import util.CommonInfo;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authenticator {
    private final String SECRET = "LB4eTi!L";

    private final CommonInfo commonInfo;

    public Authenticator(CommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    public Boolean isClientAuthenticated() {
        sendBeginAuthRequest();
        JSONObject authResponse = commonInfo.requestManager().awaitRequest("auth_response");

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

        commonInfo.output().println(beginAuthRequest);
    }

    public void sendAuthConfirmationResponse(boolean isAuthorised) {
        JSONObject authConfirmationRequest = new JSONObject();
        authConfirmationRequest.put("request_type", "auth_confirm");
        authConfirmationRequest.put("authorised", String.valueOf(isAuthorised));

        commonInfo.output().println(authConfirmationRequest);
    }

    private String generateValidKey() {
        String ipAddress = ConnectionMonitor.getIpAddress(commonInfo.connectionSocket());
        return getMd5(ipAddress + SECRET);
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
