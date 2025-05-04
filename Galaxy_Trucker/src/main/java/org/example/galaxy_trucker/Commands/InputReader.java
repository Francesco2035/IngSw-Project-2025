package org.example.galaxy_trucker.Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class InputReader implements Runnable {
    private final BlockingQueue<String> inputQueue;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private volatile boolean running = true;

    public InputReader(BlockingQueue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (reader.ready()) {
                    String line = reader.readLine();
                    if (line != null) {
                        inputQueue.put(line);
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void start(){running = true;}
}
