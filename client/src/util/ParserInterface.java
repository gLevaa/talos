package util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ParserInterface {
    public static JSONObject requestParsedPages(Page page) {
        String sourceOrPageParameter = page.isSource() ? "s" : "p";
        ProcessBuilder pythonProcessBuilder = new ProcessBuilder("python", "talos_parser.py", sourceOrPageParameter, String.valueOf(page.getCount()));

        try {
            Process pythonProcess = pythonProcessBuilder.start();

            BufferedReader scriptOutputReader = new BufferedReader(new InputStreamReader((pythonProcess.getInputStream())));
            String scriptOutput = scriptOutputReader.lines().collect(Collectors.joining(System.lineSeparator()));

            return new JSONObject(scriptOutput);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
