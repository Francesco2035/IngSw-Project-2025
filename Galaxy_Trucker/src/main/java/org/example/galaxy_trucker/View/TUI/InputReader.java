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

/**
 * The InputReader class is responsible for managing user input from the terminal and
 * relaying it to a designated input queue for processing. It handles rendering the
 * terminal output, providing command-line auto-completion, highlighting, and managing
 * a custom interactive console experience.
 *
 * The class integrates with the LineReader and Terminal functionalities, supports
 * dynamic command-line auto-completion, and provides methods for manipulating
 * and displaying data in the terminal.
 *
 * Threading is utilized internally to allow continuous user input while performing
 * other tasks, ensuring an interactive and responsive terminal application.
 */
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
    private int background = 0;



    /**
     * Constructs an InputReader instance, initializing the terminal, command completer,
     * highlighter, line reader, background generator, and key bindings for user input handling.
     * This class is used for facilitating command-line interactions and managing user input.
     *
     * @param inputQueue the blocking queue that stores input lines provided by the user.
     *                   This queue is used to pass user inputs between threads for processing.
     * @throws IOException if an I/O error occurs during the initialization of the terminal.
     */
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


        prompt = new AttributedStringBuilder()
                .append("> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .toAttributedString();


        //The TerminalBuilder will figure out the current Operating System and which actual Terminal implementation to use.
        // Note that on the Windows platform you need to have either Jansi or JNA library in your classpath.
    }


    /**
     * Executes the main logic for continuously reading input from the user and
     * placing it into the input queue for processing. This method starts a new
     * thread that runs in the background. The thread reads a line of input from
     * a line reader and adds it to the input queue. If the thread encounters an
     * exception, it will terminate the input-reading loop.
     *
     * The input reading continues as long as the `running` flag is set to true.
     * Once `running` is set to false,*/
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


    /**
     * Stops the input processing by clearing the input queue and
     * adding an empty string to it. This method can be used to
     * signal the end of input handling and to reset the input queue
     * to its initial state.
     */
    public void stop() {
        inputQueue.clear();
        inputQueue.add("");
    }


    /**
     * Starts the input processing by setting the `running` flag to true.
     * This flag indicates that the input reading process is active and
     * allows the InputReader instance to continue processing user inputs.
     */
    public void start(){
        running = true;
    }


    /**
     * Displays a server message above the current input line in the terminal.
     * The method ensures synchronized access to avoid conflicts when
     * multiple threads attempt to display messages simultaneously.
     *
     * @param message the message to be displayed above the current input line.
     */
    public synchronized void printServerMessage(String message) {
        //Lreader.callWidget(LineReader.CLEAR);          // Pulisce la riga corrente
        //String[] lines = message.split("\n");
        //for (int i = 0; i < lines.length; i++) {
            Lreader.printAbove(message);
        //}
        //Lreader.callWidget("redisplay");
        //Lreader.callWidget("redisplay");               // Ridisegna l'input buffer
    }


    /**
     * Displays a graphical message on the terminal. The method ensures
     * synchronized access, maintaining thread safety when multiple threads
     * attempt to modify the terminal output. Clears the screen, maintains
     * formatting, and invokes a widget redisplay for consistent output.
     *
     * @param s the graphical message to be displayed, preserving spaces,
     *          newlines, and any special characters.
     */
    public synchronized void printGraphicMessage(String s) {
//        terminal.puts(InfoCmp.Capability.clear_screen); // pulisce lo schermo
//        terminal.flush();
        System.out.print(s); // mantiene spazi, newline, ecc.
        Lreader.callWidget("redisplay");

    }


    /**
     * Clears the terminal screen and resets the display, ensuring consistent
     * formatting and visibility of terminal output. This method uses terminal-specific
     * commands to perform the screen clearing and then invokes a widget redisplay
     * to update the terminal state.
     *
     * The method is synchronized to ensure thread safety, preventing concurrent
     * modifications or output inconsistencies when multiple threads access the terminal.
     */
    public synchronized void clearScreen() {
        System.out.print("\033[3J");
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        Lreader.callWidget("redisplay");

    }


    /**
     * Renders the current screen content on the terminal. This method clears
     * the terminal screen, formats the content, and redisplays the terminal
     * state with updated*/
    public synchronized void renderScreen(StringBuilder content) {

        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();

        String partialInput = Lreader.getBuffer().toString();
        System.out.print("\033[3J");
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        if (background != 2) {
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


    /**
     * Retrieves the current DynamicCompleter instance associated with this InputReader.
     * The DynamicCompleter provides functionality for dynamic command-line auto-completion,
     * enabling flexible and context-aware suggestions based on the application's state
     * or user input.
     *
     * @return the DynamicCompleter instance used for managing command-line auto-completion.
     */
    public DynamicCompleter getCompleter() {
        return completer;
    }


    /**
     * Processes the input string and replaces consecutive spaces with randomly generated symbols
     * while preserving the non-space characters. The method ensures single spaces are retained,
     * whereas double spaces are replaced by symbols generated using the background generator.
     *
     * @param input the input string to be processed and modified. This string may contain spaces,
     *               which will be handled based on the specified replacement logic.
     * @return an AttributedString representing the modified content with background symbols
     *         applied where double spaces were encountered, and other characters preserved.
     */
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
        background++;
        background = background % 3;
        generator.setSpecial(background);
    }
}


