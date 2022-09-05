package util;

public class Page {
    private final String url;

    public Page(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isPost() {
        return url.contains("/comments/") && url.contains("old.reddit.com");
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
