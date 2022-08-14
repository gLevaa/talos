package util;

import connections.RequestManager;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public record CommonInfo(BufferedReader input, PrintWriter output, RequestManager requestManager,
                         PageFrontier pageFrontier, Socket connectionSocket) {
}