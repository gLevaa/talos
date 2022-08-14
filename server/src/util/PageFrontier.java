package util;

import java.util.LinkedList;
import java.util.Queue;

public class PageFrontier {
    private final Queue<Page> posts = new LinkedList<>();
    private final Queue<Page> sources = new LinkedList<>();

    // TEMP
    private final int connectedClients = 1;

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
        /* Let Pe = "Posts empty", let Se = "Sources empty", let Pl = "sources low"

	       Pe | Se | Pl | return
	        0 |  0 |  0 | post                      DEAD    = (Pe && Se)
	        0 |  0 |  1 | source                    Source  = !Se && Pl (<=> !Se && (Pl || Pe))
 	        0 |  1 |  0 | post                      Post    else
 	        0 |  1 |  1 | post
 	        1 |  0 |  0 | source
	        1 |  0 |  1 | source                    Correctness logically proven
	        1 |  1 |  0 | DEAD (TODO)
	        1 |  1 |  1 | DEAD (TODO)  */

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
        return posts.size() <= (connectedClients + 1);
    }

    public String toString() {
        // TEMP
        return sources.toString() + "\n" + posts.toString();
    }
}