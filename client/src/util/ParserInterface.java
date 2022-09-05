package util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ParserInterface {
    public static JSONObject requestParsedPages(boolean isSource) {
        if (isSource) {
            return requestSourceParse();
        }

        return null;
    }

    private static JSONObject requestSourceParse() {
        ProcessBuilder pb = new ProcessBuilder("python", "talos_parser.py", "s", "0");
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new JSONObject(in.lines().collect(Collectors.joining(System.lineSeparator())));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
