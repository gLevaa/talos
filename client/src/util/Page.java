package util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

public class Page {
    private final String url;

    public Page(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isSource() {
        return !(url.contains("/comments/")) && url.contains("old.reddit.com");
    }

    public int getCount() {
        try {
            String count = Stream.of(url.split("\\?")[1].split("&"))
                    .map(c -> c.split("="))
                    .filter(c -> "count".equalsIgnoreCase(c[0]))
                    .map(c -> c[1])
                    .findFirst()
                    .orElse("");

            return Integer.parseInt(count);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


    public String toString() {
        return this.url;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Page) {
            return url.equals(((Page) o).getUrl());
        }

        return false;
    }
}
