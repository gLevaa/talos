package util;

import connections.ConnectionMonitor;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PageFrontier {
    private static final Queue<Page> posts = new LinkedList<>();
    private static final Queue<Page> sources = new LinkedList<>();

    private static final ArrayList<Page> servedPageCache = new ArrayList<>();

    public static synchronized void addNewSeed(Page seed) {
        sources.add(seed);
    }

    public static synchronized void appendNewPage(Page page) {
        if (page.isSource()) {
            sources.add(page);
        } else {
            posts.add(page);
        }
    }

    public static synchronized Page fetchNewPage() {
        if (shouldDie()) {
            return  null;
        } else if (shouldServeSource()) {
            Page toServe = sources.remove();
            servedPageCache.add(toServe);

            return toServe;
        } else {
            Page toServe = posts.remove();
            servedPageCache.add(toServe);

            return toServe;
        }
    }

    public static synchronized void removePageFromCache(Page page) {
        // Relies on Page.equals() method, strict comparison of URL
        servedPageCache.remove(page);
    }

    public static synchronized void restorePageFromCache(Page page) {
        removePageFromCache(page);
        appendNewPage(page);
    }

    private static boolean shouldDie() {
        return posts.isEmpty() && sources.isEmpty();
    }

    private static boolean shouldServeSource() {
        return !sources.isEmpty() && isPostsLow();
    }

    private static boolean isPostsLow() {
        return posts.size() <= (ConnectionMonitor.getConnectedClients() + 1);
    }
}