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

    public String getSubreddit() {
        return "TODO";
    }
}
