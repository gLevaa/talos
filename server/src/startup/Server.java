
package startup;

import handlers.ConnectionHandler;
import util.PageFrontier;
import util.Page;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 9320;
    private static final int THREADS = 1;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);

    // TEMP
    private static final String SEED = "https://old.reddit.com/r/newzealand";
    public static final boolean IS_SEED_SET = true;

    public static void main(String[] args) {
        try(ServerSocket receiver = new ServerSocket((PORT))) {
            PageFrontier pageFrontier = new PageFrontier();
            pageFrontier.addNewSeed(new Page(SEED));

            while(true) {
                // .accept() is blocking, no impact on performance
                threadPool.execute(new ConnectionHandler(receiver.accept(), pageFrontier));
            }
        } catch (IOException e) {
            // TODO
        }
    }
}