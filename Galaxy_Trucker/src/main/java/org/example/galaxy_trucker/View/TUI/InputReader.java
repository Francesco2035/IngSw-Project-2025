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
    /**
     * A thread-safe blocking queue for storing input lines provided by the user.
     * This queue facilitates communication between threads by allowing
     * user inputs to be passed for further processing.
     *
     * It is used within the {@code InputReader} class to manage and handle
     * user commands entered via the terminal.
     *
     * The queue's capacity and behavior follow the specifications of {@link BlockingQueue},
     * ensuring proper thread synchronization and preventing input processing bottlenecks.
     */
    private final BlockingQueue<String> inputQueue;
    /**
     * A BufferedReader instance used for reading input from the standard input stream (System.in).
     * This is a final field to ensure that the reader is not re-assigned and is consistently used
     * for processing input throughout the lifecycle of the InputReader class.
     *
     * The reader is wrapped around an InputStreamReader to facilitate character stream reading from
     * the standard input. It supports efficient reading of text and handles character encoding properly.
     *
     * This field is designed to assist with command-line interactions, enabling the InputReader class
     * to process user inputs effectively in a synchronized manner.
     */
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    /**
     * A flag indicating whether the input reading and processing loop in the `InputReader`
     * should continue running. This variable is volatile to ensure visibility across
     * multiple threads, allowing safe and consistent updates to the thread managing
     * input reading.
     *
     * When set to `true`, the loop executes normally, reading user input and placing it
     * into the input queue. Setting this flag to `false` signals the thread to stop
     * processing, effectively halting the input reading operation.
     */
    private volatile boolean running = true;
    /**
     * Represents a {@link LineReader} instance used for handling line-based input
     * operations in the InputReader class. This variable is a critical part of the
     * InputReader, facilitating user input handling and processing commands entered
     * into the terminal.
     *
     * The {@code Lreader} is initialized during the construction of the InputReader
     * object and is utilized to read input lines from the terminal while supporting
     * features such as line editing, input history, and auto-completion.
     *
     * This instance is defined as {@code final}, ensuring it cannot be reassigned
     * after initialization. It operates in conjunction with other InputReader components,
     * including the {@code completer}, {@code highlighter}, and {@code terminal}, to
     * provide a seamless command-line interface.
     */
    private final LineReader Lreader;
    /**
     * Represents the prompt displayed to the user for input.
     * This AttributedString is used to format and render the
     * input prompt on the terminal, providing a visual cue
     * for user interaction. The prompt may include attributes
     * such as colors, styles, or additional formatting to
     * enhance usability and aesthetics.
     */
    AttributedString prompt;
    /**
     * The highlighter instance used for syntax highlighting or other display enhancements
     * in the command-line interface. It processes user input to visually distinguish
     * elements such as keywords, commands, or invalid syntax, enhancing readability
     * and user experience during input handling.
     */
    Highlighter highlighter;
    /**
     * Represents the terminal interface used for input and output operations
     * within the InputReader class. This variable provides access to the
     * terminal functionalities, enabling configuration and control of the
     * terminal's behavior, such as rendering textual or graphical output,
     * managing user input, and handling terminal screen updates.
     *
     * The `terminal` variable facilitates interaction with the underlying
     * terminal system, which can include rendering messages, modifying the
     * display, and managing user commands. It is a core component for handling
     * user interaction in the InputReader class.
     */
    Terminal terminal;
    /**
     * A DynamicCompleter instance used for providing dynamic auto-completion
     * functionality during command-line interactions. This member allows for
     * managing context-aware and runtime-adaptable command suggestions,
     * improving user experience by offering relevant completions based on
     * the current application state or user input.
     *
     * The completer can update its list of commands at runtime using the
     * methods defined in the DynamicCompleter interface, ensuring that
     * auto-completion behavior remains flexible and responsive to changes.
     */
    DynamicCompleter completer;
    /**
     * Holds the primary {@code KeyMap} configuration for managing key bindings
     * in the terminal input reader. This map associates specific key sequences
     * with {@code Binding} actions, enabling user interaction through customized
     * shortcuts and commands.
     *
     * The {@code mainKeyMap} is used internally by the {@code InputReader} class
     * to define how specific keys or key combinations are interpreted and processed
     * during input handling. It acts as the central mapping for key functionalities.
     */
    KeyMap<Binding> mainKeyMap;
    /**
     * A {@code StringBuilder} instance used to store the content of the last
     * rendered terminal output. This variable is updated each time the
     * terminal content is rendered to preserve the current state of the
     * displayed content.
     *
     * The {@code lastRender} variable is primarily used to ensure consistency
     * in the terminal output and to manage scenarios where redrawing or
     * redisplaying of content is required. It provides a reference for the
     * previous state, enabling proper formatting and updates during subsequent
     * rendering operations.
     *
     * This field plays a key role in maintaining smooth terminal operations
     * and ensuring any updates made to the screen do not conflict with or
     * overwrite previously displayed content unexpectedly.
     */
    StringBuilder lastRender = new StringBuilder();
    /**
     * The `generator` variable represents an instance of the `BackgroundGenerator` class
     * that is used to generate random or predefined background elements, typically utilized
     * for visual effects or formatting. It is primarily responsible for providing symbols or
     * patterns to replace certain elements in terminal outputs, such as replacing spaces
     * with decorative symbols.
     *
     * This generator plays a key role in the terminal's visual customization by enabling
     * dynamic background modifications. It is used extensively in methods that involve
     * modifying or rendering textual content with enhanced visual styling.
     */
    BackgroundGenerator generator ;
    /**
     * Represents the background type used for rendering or filling spaces
     * in the terminal interface. This variable stores an integer value
     * that determines the current background style. The background value
     * is used in rendering logic to customize the visual appearance
     * of the terminal output or patterns.
     *
     * This variable is dynamically updated within the class based on
     * user interactions, commands, or specific internal processes. It
     * plays a role in generating or modifying display content, particularly
     * when using the terminal for input or graphical message handling.
     *
     * Default value is initialized to 0.
     */
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


        mainKeyMap.bind(new Macro("SeeBoards"), "\u0013"); // Ctrl+S
        mainKeyMap.bind(new Macro("MainTerminal"), "\u0014");   // Ctrl+T
        mainKeyMap.bind(new Macro("Log"), "\u000C");   // Ctrl+L
        mainKeyMap.bind(new Macro("Background"), "\u0002"); // Ctrl+B



        prompt = new AttributedStringBuilder()
                .append("> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .toAttributedString();


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

            Lreader.printAbove(message);
    }



    /**
     * Renders the screen content by clearing the terminal, applying background processing
     * on the given content, and displaying it along with the current input line. This method
     * ensures proper terminal rendering and resets certain terminal states for a clear display.
     *
     * @param content a StringBuilder containing the text content to be rendered on the screen.
     *                This text may be modified based on the background processing logic.
     */
    public synchronized void renderScreen(StringBuilder content) {


        String partialInput = Lreader.getBuffer().toString();
        // Usa InfoCmp per tornare su
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.flush();

// Poi, cancella anche il buffer di scroll manualmente (solo ANSI, JLine non espone direttamente questa funzionalità)
        terminal.writer().print("\033[3J"); // cancella scrollback
        terminal.flush();
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.puts(InfoCmp.Capability.clr_eos);
        terminal.flush();

        if (background != 2) {
            AttributedString colored = fillBackground(content.toString());
            terminal.writer().print(colored.toAnsi());
        }
        else {
            terminal.writer().print(content.toString());
        }
        terminal.puts(InfoCmp.Capability.carriage_return); // Riporta a inizio riga
        terminal.puts(InfoCmp.Capability.clr_eol);         // Pulisce il resto della riga
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


