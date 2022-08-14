package util;

import connections.ConnectionMonitor;

import java.util.LinkedList;
import java.util.Queue;

public class PageFrontier {
    private final Queue<Page> posts = new LinkedList<>();
    private final Queue<Page> sources = new LinkedList<>();

    public synchronized void addNewSeed(Page seed) {
        sources.add(seed);
    }

    public synchronized void appendNewPage(Page page) {
        if (page.isPost()) {
            posts.add(page);
        } else {
            sources.add(page);
        }
    }

    public synchronized Page fetchNewPage() {
        if (shouldDie()) {
            return  null;
        } else if (shouldServeSource()) {
            return sources.remove();
        } else {
            return posts.remove();
        }
    }

    private boolean shouldDie() {
        return posts.isEmpty() && sources.isEmpty();
    }

    private boolean shouldServeSource() {
        return !sources.isEmpty() && isPostsLow();
    }

    private boolean isPostsLow() {
        return posts.size() <= (ConnectionMonitor.getConnectedClients() + 1);
    }

    public String toString() {
        // TEMP
        return sources.toString() + "\n" + posts.toString();
    }
}