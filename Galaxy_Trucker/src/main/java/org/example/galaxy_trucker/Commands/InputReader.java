package org.example.galaxy_trucker.Commands;

import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class InputReader implements Runnable {
    private final BlockingQueue<String> inputQueue;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private volatile boolean running = true;
    private final LineReader Lreader;
    AttributedString prompt;
    Highlighter highlighter;
    Terminal terminal;

    public InputReader(BlockingQueue<String> inputQueue) throws IOException {
        this.inputQueue = inputQueue;
        terminal = TerminalBuilder.builder()
                .name("GalaxyTrucker")
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .jansi(true)
                .build();



        highlighter = new Highlighter() {
            @Override
            public AttributedString highlight(LineReader lineReader, String s) {
                return new AttributedString(s, AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).background(AttributedStyle.BLUE));
            }
        };


        this.Lreader = LineReaderBuilder.builder()
                .terminal(terminal)
                .highlighter(highlighter)
                .build();


        prompt = new AttributedStringBuilder()
                .append("> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .toAttributedString();

        //The TerminalBuilder will figure out the current Operating System and which actual Terminal implementation to use.
        // Note that on the Windows platform you need to have either Jansi or JNA library in your classpath.
    }

    @Override
    public void run() {
        new Thread(() -> {
            while (running) {
                try {
                    String line = Lreader.readLine(">");
                    if (line != null) {
                        inputQueue.put(line);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }).start();

    }

    public void stop() {
        inputQueue.clear();
        inputQueue.add("");
    }

    public void start(){
        running = true;
    }

    public void printServerMessage(String message) {
        //Lreader.callWidget(LineReader.CLEAR);          // Pulisce la riga corrente
        Lreader.printAbove(message);
        //Lreader.callWidget("redisplay");               // Ridisegna l'input buffer
    }

    public synchronized void clearScreen() {
        // 1) Pulisci tutto il terminale (schermo)
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();

        // 2) Ridisegna il prompt e l’input che l’utente aveva scritto (buffer)
        Lreader.callWidget("redisplay");
    }

}
