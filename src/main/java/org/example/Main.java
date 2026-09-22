package org.example;

import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        SensorType sensorType = args.length == 0
                ? SensorType.TEMP
                : SensorType.valueOf(args[0].toUpperCase());

        PrintWriter out = new PrintWriter(System.out, true);
        SensorClient sensorClient = new SensorClient(sensorType, out);

        Runtime.getRuntime().addShutdownHook(new Thread(sensorClient::stop));
        sensorClient.start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            sensorClient.stop();
        }
    }
}
