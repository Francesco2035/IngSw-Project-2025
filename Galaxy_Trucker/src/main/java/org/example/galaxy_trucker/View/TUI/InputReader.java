package org.example.galaxy_trucker.View.TUI;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    DynamicCompleter completer;
    KeyMap<Binding> mainKeyMap;
    StringBuilder lastRender = new StringBuilder();
    BackgroundGenerator generator ;
    private boolean background = true;



    public InputReader(BlockingQueue<String> inputQueue) throws IOException {

        lastRender.append("");
        completer = new CommandCompleter();


        this.inputQueue = inputQueue;
        terminal = TerminalBuilder.builder()
                .name("GalaxyTrucker")
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .jansi(true)
                .build();
        generator = new BackgroundGenerator();


        highlighter = new Highlighter() {
            @Override
            public AttributedString highlight(LineReader lineReader, String s) {
                return new AttributedString(s, AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE).background(AttributedStyle.BLUE));
            }
        };


        this.Lreader = LineReaderBuilder.builder()
                .terminal(terminal)
                .highlighter(highlighter)
                .completer(completer)
                .build();

        mainKeyMap = Lreader.getKeyMaps().get(LineReader.MAIN);


        mainKeyMap.bind(new Macro("SeeBoards"), "\u0002"); // Ctrl+B
        mainKeyMap.bind(new Macro("MainTerminal"), "\u0014");   // Ctrl+T
        mainKeyMap.bind(new Macro("Log"), "\u000C");   // Ctrl+L
        //mainKeyMap.bind(new Macro("Help"), "\u0008");   // Ctrl+H


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

    public synchronized void printServerMessage(String message) {
        //Lreader.callWidget(LineReader.CLEAR);          // Pulisce la riga corrente
        //String[] lines = message.split("\n");
        //for (int i = 0; i < lines.length; i++) {
            Lreader.printAbove(message);
        //}
        //Lreader.callWidget("redisplay");
        //Lreader.callWidget("redisplay");               // Ridisegna l'input buffer
    }


    public synchronized void clearScreen() {
        System.out.print("\033[3J");
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        Lreader.callWidget("redisplay");

    }


    public synchronized void renderScreen(StringBuilder content) {

        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException _){
            ;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();

        String partialInput = Lreader.getBuffer().toString();
//        System.out.print("\033[3J");
//        terminal.puts(InfoCmp.Capability.clear_screen);
//        terminal.flush();
        if (background) {
            AttributedString colored = fillBackground(content.toString());
            terminal.writer().print(colored.toAnsi());
        }
        else {
            terminal.writer().print(content.toString());
        }
        terminal.writer().println();
        terminal.flush();
        terminal.flush();
        Lreader.getBuffer().clear();
        Lreader.getBuffer().write(partialInput);
        Lreader.getBuffer().cursor(partialInput.length());
        AttributedString highlighted = new AttributedString(
                ">" + partialInput,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE).background(AttributedStyle.BLUE)
        );

        terminal.writer().print(highlighted.toAnsi());
        terminal.flush();


        //Lreader.callWidget("redisplay");
        Lreader.callWidget("redisplay");


    }

    public DynamicCompleter getCompleter() {
        return completer;
    }

    public AttributedString fillBackground(String input) {
        AttributedStringBuilder asb = new AttributedStringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == ' ') {
                if (i + 1 < input.length() && input.charAt(i + 1) == ' ') {
                    AttributedString symbol = generator.getRandomSymbol();
                    String symbolStr = symbol.toString();

                    asb.append(symbol);

                    if (symbolStr.length() > 1) {
                        i++;
                    }
                } else {
                    asb.append(String.valueOf(c));
                }
            } else {
                asb.append(String.valueOf(c));
            }
            i++;
        }
        return asb.toAttributedString();
    }


    public void ChangeBackground() {
        background = !background;
    }
}


