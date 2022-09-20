package util;

import util.Page;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageDownloader {
    public static final String USERAGENT
            = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    public static void downloadPage(Page page) throws IOException {
        InputStream inputStream;
        try {
            inputStream = getInputStream(new URL(page.getUrl()));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        copyInputStreamToFile(inputStream, new File("page.json"));
    }

    private static InputStream getInputStream(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", USERAGENT);

        return urlConnection.getInputStream();
    }

    private static void copyInputStreamToFile(InputStream input, File file) throws IOException {
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        }
    }
}
