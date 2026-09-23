package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HQServer {
    private static final int PORT = 5000;
    private static final int WORKER_COUNT = 5;

    public static void main(String[] args) {
        try (ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_COUNT);
             ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("HQServer started on port " + PORT);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                workerPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException exception) {
            printError("HQServer failed: " + exception.getMessage());
        }
    }

    static void printError(String message) {
        System.err.println("[ERROR] " + message);
    }
}
