package connections;

import util.PageFrontier;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public record Session(BufferedReader input, PrintWriter output, RequestManager requestManager, PageFrontier pageFrontier, Socket connectionSocket) {}