package org.example.galaxy_trucker.View.TUI;

/**
 * ASCII_ART class provides utility methods and fields related to ASCII representation
 * and composition of various game elements, phases, and titles.
 * It includes static methods to manipulate and compose ASCII art strings and a comprehensive
 * range of predefined ASCII representations for game phases, titles, and objects.
 */
public class ASCII_ART {


    /**
     * A static string variable representing an ASCII art depiction of a "No Game" scenario.
     * The ASCII art is primarily composed of decorative and symbolic characters.
     * It is intended for use in UI displays, placeholders, or as a thematic element
     * in a game or application to indicate an absence of active gameplay.
     */
    public static String noGame =
            "\n" +
            "\n" +
            "                                                                                                                                ┳┓               ┏       ┓╻  ┏┓                                 ╻                                                                                                                    \n" +
            "                                                                                                                                ┃┃┏┓  ┏┓┏┓┏┳┓┏┓  ╋┏┓┓┏┏┓┏┫┃  ┃ ┏┓┏┓┏┓╋┏┓  ┏┓  ┏┓┏┓┓┏┏  ┏┓┏┓┏┳┓┏┓┃                                                                                                                    \n" +
            "                                                                                                                                ┛┗┗┛  ┗┫┗┻┛┗┗┗   ┛┗┛┗┻┛┗┗┻•  ┗┛┛ ┗ ┗┻┗┗   ┗┻  ┛┗┗ ┗┻┛  ┗┫┗┻┛┗┗┗ •                                                                                                                    \n" +
            "                                                                                                                                       ┛                                                ┛                                                                                                                            \n" +
            "\n";

    /**
     * A static string variable named Title that contains a long and intricate
     * decorative textual pattern often used for visual or aesthetic purposes.
     * The content of the pattern consists of repeated symbols, characters,
     * and spaces formatted in a structured manner to form an artistic display.
     */
    public static String Title =
            "                                                                 ░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░       ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓████████▓▒░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓███████▓▒░                                                                 \n" +
            "                                                                ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░         ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░                                                                \n" +
            "                                                                ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░         ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░                                                                \n" +
            "                                                                ░▒▓█▓▒▒▓███▓▒░▒▓████████▓▒░▒▓█▓▒░      ░▒▓████████▓▒░░▒▓██████▓▒░ ░▒▓██████▓▒░          ░▒▓█▓▒░   ░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓███████▓▒░░▒▓██████▓▒░ ░▒▓███████▓▒░                                                                 \n" +
            "                                                                ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░             ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░                                                                \n" +
            "                                                                ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░             ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░                                                                \n" +
            "                                                                 ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░             ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░                                                                \n" +
            "                                                                                                                                                                                                                                                                                                                   \n" +
            "                                                                                                                                                                                                                                                                                                                   \n" +
            "\n";




    /**
     * A static variable representing an ASCII art board, formatted as a string.
     * This board may be used for visual representation or game layout purposes within the application.
     */
    public static String Board =
            "\n" +
            "\n" +
            "                                                                                                                                                        ┳┓       ┓                                                                                                                                              \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┣┫┏┓┏┓┏┓┏┫  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                                        ┻┛┗┛┗┻┛ ┗┻                                                                                                                                              \n" +
            "                                                                                                                                                                                                                                                                                                                \n" +
            "\n";






    /**
     * Represents an ASCII art depiction of a game board using special characters.
     * This static string field provides a visual representation of the game board and
     * includes decorative elements for formatting.
     *
     * The ASCII art consists of several lines with varying symbols designed to mimic
     * the appearance of a game-related interface. Its primary purpose is to enhance visual
     * appeal in text-based game outputs.
     */
    public static String GameBoard  =
            "\n" +
            "\n" +
            "                                                                                                                                                  ┏┓       ┳┓       ┓                                                                                                                                          \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃┓┏┓┏┳┓┏┓┣┫┏┓┏┓┏┓┏┫  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                                  ┗┛┗┻┛┗┗┗ ┻┛┗┛┗┻┛ ┗┻                                                                                                                                          \n" +
            "                                                                                                                                                                                                                                                                                                               \n" +
            "\n";




    /**
     * A static String variable representing ASCII art labeled as "Hand".
     * The Hand variable contains a multi-line string that visually represents
     * stylized components through ASCII characters. It is primarily used for
     * rendering visual or decorative elements in text-based applications.
     */
    public static String Hand =
            "\n" +
            "\n" +
            "                                                                                                                                                      ┓┏     ┓                                                                                                                                                \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┣┫┏┓┏┓┏┫  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                                      ┛┗┗┻┛┗┗┻                                                                                                                                                \n" +
            "                                                                                                                                                                                                                                                                                                              \n" +
            "\n";




    /**
     * The `Card` variable represents an ASCII art graphic defined as a static string.
     * This graphic could potentially serve as a decorative or illustrative element
     * within the context of the containing class.
     *
     * It consists of a specific sequence of characters to create a visual representation,
     * deliberately formatted with spaces, newlines, and symbols to convey a graphical design.
     */
    public static String Card =
            "\n" +
            "\n" +
            "                                                                                                                                                        ┏┓     ┓                                                                                                                                              \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃ ┏┓┏┓┏┫  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                                        ┗┛┗┻┛ ┗┻                                                                                                                                              \n" +
            "                                                                                                                                                                                                                                                                                                              \n" +
            "\n";



    /**
     * Represents an ASCII art depiction in string format, named "UncoveredTiles".
     * This variable is a static constant defined as a multiline string.
     * Primarily used to visually convey or depict uncovered tiles, typically
     * in a board or tile-based game context.
     *
     * The string includes specific ASCII characters arranged in a way to form
     * a visual representation. Its layout and design are static and predefined.
     */
    public static String UncoveredTiles = "\n" +
            "\n" +
            "                                                                                                                                          ┳┳              ┓  ┏┳┓•┓     ┏┓                                                                                                                                     \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃┃┏┓┏┏┓┓┏┏┓┏┓┏┓┏┫   ┃ ┓┃┏┓┏  ┗┓┏┓╋  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                          ┗┛┛┗┗┗┛┗┛┗ ┛ ┗ ┗┻   ┻ ┗┗┗ ┛  ┗┛┗ ┗                                                                                                                                  \n" +
            "                                                                                                                                                                                                                                                                                                              \n" +
            "\n";




    /**
     * Represents a predefined ASCII art string stored as a static variable. This string
     * utilizes special characters to create a visual representation of covered tiles,
     * likely part of a graphical or visual display in an application.
     *
     * This variable is intended to be used for visual or decorative purposes within
     * the context of the ASCII_ART class or related functionalities.
     */
    public static String CoveredTiles =
            "\n" +
            "\n" +
            "                                                                                                                                          ┏┓           ┓  ┏┳┓•┓     ┏┓                                                                                                                                       \n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃ ┏┓┓┏┏┓┏┓┏┓┏┫   ┃ ┓┃┏┓┏  ┗┓┏┓╋  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "                                                                                                                                          ┗┛┗┛┗┛┗ ┛ ┗ ┗┻   ┻ ┗┗┗ ┛  ┗┛┗ ┗                                                                                                                                    \n" +
            "                                                                                                                                                                                                                                                                                                               \n" +
            "\n";



    /**
     * A static string constant representing a decorative border used for ASCII art
     * or visual separation within the application. The border is created using
     * Unicode box-drawing characters and spans multiple lines for visual emphasis.
     *
     * This variable is intended to act as a visual separator or decorative component
     * in the context of the ASCII_ART class, complementing various phases, titles,
     * and other artistic elements utilized in the application.
     *
     * Modifying this constant is not recommended, as it could disrupt the intended
     * formatting and alignment of the visual elements where this border is used.
     */
    public static String Border =
                    "\n" +
                    "\n" +
                    "                                                                                                                                                                                                                                                                                                                \n" +
                    "════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n" +
                    "                                                                                                                                                                                                                                                                                                                \n" +
                    "                                                                                                                                                                                                                                                                                                                \n" +
                    "\n";


    /**
     * Represents the ASCII art title for the "Abandoned Ship" scenario. This is
     * a statically defined string containing artistic text rendered in an
     * elaborate format to depict the title visually.
     *
     * The string includes decorative elements such as borders and stylized text
     * to provide an aesthetically pleasing presentation that can be displayed
     * within a console or other text-based interfaces in the application.
     */
    public static String TitleAbandonedShip =
                    "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                          █████╗ ██████╗  █████╗ ███╗   ██╗██████╗  ██████╗ ███╗   ██╗███████╗██████╗     ███████╗██╗  ██╗██╗██████╗                                                                                          ║\n" +
                    "║                                                                                                         ██╔══██╗██╔══██╗██╔══██╗████╗  ██║██╔══██╗██╔═══██╗████╗  ██║██╔════╝██╔══██╗    ██╔════╝██║  ██║██║██╔══██╗                                                                                         ║\n" +
                    "║                                                                                                         ███████║██████╔╝███████║██╔██╗ ██║██║  ██║██║   ██║██╔██╗ ██║█████╗  ██║  ██║    ███████╗███████║██║██████╔╝                                                                                         ║\n" +
                    "║                                                                                                         ██╔══██║██╔══██╗██╔══██║██║╚██╗██║██║  ██║██║   ██║██║╚██╗██║██╔══╝  ██║  ██║    ╚════██║██╔══██║██║██╔═══╝                                                                                          ║\n" +
                    "║                                                                                                         ██║  ██║██████╔╝██║  ██║██║ ╚████║██████╔╝╚██████╔╝██║ ╚████║███████╗██████╔╝    ███████║██║  ██║██║██║                                                                                              ║\n" +
                    "║                                                                                                         ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝     ╚══════╝╚═╝  ╚═╝╚═╝╚═╝                                                                                              ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * A string constant representing the ASCII art title for "Abandoned Station."
     * This string includes elaborate ASCII decorations and styling for visual representation.
     *
     * This constant is part of the ASCII_ART class and is primarily used to display
     * the thematic header for sections or scenarios related to an abandoned station
     * within the context of the application.
     */
    public static String TitleAbandonedStation =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                              █████╗ ██████╗  █████╗ ███╗   ██╗██████╗  ██████╗ ███╗   ██╗███████╗██████╗     ███████╗████████╗ █████╗ ████████╗██╗ ██████╗ ███╗   ██╗                                                                        ║\n" +
                    "║                                                                                             ██╔══██╗██╔══██╗██╔══██╗████╗  ██║██╔══██╗██╔═══██╗████╗  ██║██╔════╝██╔══██╗    ██╔════╝╚══██╔══╝██╔══██╗╚══██╔══╝██║██╔═══██╗████╗  ██║                                                                        ║\n" +
                    "║                                                                                             ███████║██████╔╝███████║██╔██╗ ██║██║  ██║██║   ██║██╔██╗ ██║█████╗  ██║  ██║    ███████╗   ██║   ███████║   ██║   ██║██║   ██║██╔██╗ ██║                                                                        ║\n" +
                    "║                                                                                             ██╔══██║██╔══██╗██╔══██║██║╚██╗██║██║  ██║██║   ██║██║╚██╗██║██╔══╝  ██║  ██║    ╚════██║   ██║   ██╔══██║   ██║   ██║██║   ██║██║╚██╗██║                                                                        ║\n" +
                    "║                                                                                             ██║  ██║██████╔╝██║  ██║██║ ╚████║██████╔╝╚██████╔╝██║ ╚████║███████╗██████╔╝    ███████║   ██║   ██║  ██║   ██║   ██║╚██████╔╝██║ ╚████║                                                                        ║\n" +
                    "║                                                                                             ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝     ╚══════╝   ╚═╝   ╚═╝  ╚═╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝                                                                        ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";



    /**
     * Represents an ASCII art banner for the "Combat Zone" title in a game.
     *
     * This variable contains a decorative string using ASCII characters to visually display
     * the "Combat Zone" title. It can be used in the context of game user interfaces, logs,
     * or any section where a visually engaging representation of the title is required.
     *
     * The ASCII art includes boxed stylized text and is formatted with line breaks and
     * proper alignment for aesthetic presentation.
     */
    public static String TitleCombatZone =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                             ██████╗ ██████╗ ███╗   ███╗██████╗  █████╗ ████████╗    ███████╗ ██████╗ ███╗   ██╗███████╗                                                                                                      ║\n" +
                    "║                                                                                                            ██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝    ╚══███╔╝██╔═══██╗████╗  ██║██╔════╝                                                                                                      ║\n" +
                    "║                                                                                                            ██║     ██║   ██║██╔████╔██║██████╔╝███████║   ██║         ███╔╝ ██║   ██║██╔██╗ ██║█████╗                                                                                                        ║\n" +
                    "║                                                                                                            ██║     ██║   ██║██║╚██╔╝██║██╔══██╗██╔══██║   ██║        ███╔╝  ██║   ██║██║╚██╗██║██╔══╝                                                                                                        ║\n" +
                    "║                                                                                                            ╚██████╗╚██████╔╝██║ ╚═╝ ██║██████╔╝██║  ██║   ██║       ███████╗╚██████╔╝██║ ╚████║███████╗                                                                                                      ║\n" +
                    "║                                                                                                             ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═════╝ ╚═╝  ╚═╝   ╚═╝       ╚══════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝                                                                                                      ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * Contains the ASCII art representation of the title "Epidemic"
     * stylized with graphical text borders and text in large font size.
     * Primarily designed for displaying in console or terminal-based
     * applications for decorative or informative purposes.
     */
    public static String TitleEpidemic =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                                                 ███████╗██████╗ ██╗██████╗ ███████╗███╗   ███╗██╗ ██████╗                                                                                                                    ║\n" +
                    "║                                                                                                                                 ██╔════╝██╔══██╗██║██╔══██╗██╔════╝████╗ ████║██║██╔════╝                                                                                                                    ║\n" +
                    "║                                                                                                                                 █████╗  ██████╔╝██║██║  ██║█████╗  ██╔████╔██║██║██║                                                                                                                         ║\n" +
                    "║                                                                                                                                 ██╔══╝  ██╔═══╝ ██║██║  ██║██╔══╝  ██║╚██╔╝██║██║██║                                                                                                                         ║\n" +
                    "║                                                                                                                                 ███████╗██║     ██║██████╔╝███████╗██║ ╚═╝ ██║██║╚██████╗                                                                                                                    ║\n" +
                    "║                                                                                                                                 ╚══════╝╚═╝     ╚═╝╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝ ╚═════╝                                                                                                                    ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";

    /**
     * A static string variable that represents the title 'Meteor Swarm' in ASCII art format.
     * The string uses an elaborate ASCII art style to visually represent*/
    public static String TitleMeteorSwarm =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                   ███╗   ███╗███████╗████████╗███████╗ ██████╗ ██████╗     ███████╗██╗    ██╗ █████╗ ██████╗ ███╗   ███╗                                                                                                     ║\n" +
                    "║                                                                                                   ████╗ ████║██╔════╝╚══██╔══╝██╔════╝██╔═══██╗██╔══██╗    ██╔════╝██║    ██║██╔══██╗██╔══██╗████╗ ████║                                                                                                     ║\n" +
                    "║                                                                                                   ██╔████╔██║█████╗     ██║   █████╗  ██║   ██║██████╔╝    ███████╗██║ █╗ ██║███████║██████╔╝██╔████╔██║                                                                                                     ║\n" +
                    "║                                                                                                   ██║╚██╔╝██║██╔══╝     ██║   ██╔══╝  ██║   ██║██╔══██╗    ╚════██║██║███╗██║██╔══██║██╔══██╗██║╚██╔╝██║                                                                                                     ║\n" +
                    "║                                                                                                   ██║ ╚═╝ ██║███████╗   ██║   ███████╗╚██████╔╝██║  ██║    ███████║╚███╔███╔╝██║  ██║██║  ██║██║ ╚═╝ ██║                                                                                                     ║\n" +
                    "║                                                                                                   ╚═╝     ╚═╝╚══════╝   ╚═╝   ╚══════╝ ╚═════╝ ╚═╝  ╚═╝    ╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝                                                                                                     ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * A static string variable that represents the ASCII art title banner for the "Open Space" phase
     * of the game. The value of this string is an elaborate ASCII design meant to visually enhance and
     * represent the theme of the "Open Space" phase, including stylized text and decorative borders.
     */
    public static String TitleOpenSpace =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                       ██████╗ ██████╗ ███████╗███╗   ██╗    ███████╗██████╗  █████╗  ██████╗███████╗                                                                                                                         ║\n" +
                    "║                                                                                                      ██╔═══██╗██╔══██╗██╔════╝████╗  ██║    ██╔════╝██╔══██╗██╔══██╗██╔════╝██╔════╝                                                                                                                         ║\n" +
                    "║                                                                                                      ██║   ██║██████╔╝█████╗  ██╔██╗ ██║    ███████╗██████╔╝███████║██║     █████╗                                                                                                                           ║\n" +
                    "║                                                                                                      ██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║    ╚════██║██╔═══╝ ██╔══██║██║     ██╔══╝                                                                                                                           ║\n" +
                    "║                                                                                                      ╚██████╔╝██║     ███████╗██║ ╚████║    ███████║██║     ██║  ██║╚██████╗███████╗                                                                                                                         ║\n" +
                    "║                                                                                                       ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝    ╚══════╝╚═╝     ╚═╝  ╚═╝ ╚═════╝╚══════╝                                                                                                                         ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * The `TitlePirates` variable contains a string representation of an ASCII art title
     * specifically designed for the section or theme "Pirates". It includes decorative borders
     * and stylized textual elements that visually emphasize the pirate theme, making it suitable
     * for display purposes in a terminal, console application, or text-based user interface.
     *
     * This variable is intended to enhance user experience by providing visually distinctive
     * headers or titles that represent the "Pirates" theme in the overall context of the application.
     *
     * Note: The content of this variable is static and uses special ASCII characters, which
     * may not display correctly on certain platforms or systems that do not support
     * extended ASCII or UTF-8 character encoding.
     */
    public static String TitlePirates =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                                         ██████╗ ██╗██████╗  █████╗ ████████╗███████╗███████╗                                                                                                                                 ║\n" +
                    "║                                                                                                                         ██╔══██╗██║██╔══██╗██╔══██╗╚══██╔══╝██╔════╝██╔════╝                                                                                                                                 ║\n" +
                    "║                                                                                                                         ██████╔╝██║██████╔╝███████║   ██║   █████╗  ███████╗                                                                                                                                 ║\n" +
                    "║                                                                                                                         ██╔═══╝ ██║██╔══██╗██╔══██║   ██║   ██╔══╝  ╚════██║                                                                                                                                 ║\n" +
                    "║                                                                                                                         ██║     ██║██║  ██║██║  ██║   ██║   ███████╗███████║                                                                                                                                 ║\n" +
                    "║                                                                                                                         ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚══════╝                                                                                                                                 ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * TitlePlanets is an ASCII art representation of a decorative title banner
     * specifically designed for the "Planets" title within the broader context of the game.
     * The banner is styled using a combination of text and box drawing characters
     * to visually enhance the game's user interface or output.
     *
     * This variable is a static and immutable string containing the formatted ASCII art.
     * It provides aesthetic and thematic alignment to in-game elements relating
     * to the concept of planets.
     */
    public static String TitlePlanets =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                                         ██████╗ ██╗      █████╗ ███╗   ██╗███████╗████████╗███████╗                                                                                                                          ║\n" +
                    "║                                                                                                                         ██╔══██╗██║     ██╔══██╗████╗  ██║██╔════╝╚══██╔══╝██╔════╝                                                                                                                          ║\n" +
                    "║                                                                                                                         ██████╔╝██║     ███████║██╔██╗ ██║█████╗     ██║   ███████╗                                                                                                                          ║\n" +
                    "║                                                                                                                         ██╔═══╝ ██║     ██╔══██║██║╚██╗██║██╔══╝     ██║   ╚════██║                                                                                                                          ║\n" +
                    "║                                                                                                                         ██║     ███████╗██║  ██║██║ ╚████║███████╗   ██║   ███████║                                                                                                                          ║\n" +
                    "║                                                                                                                         ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝                                                                                                                          ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     * A static string variable that contains an ASCII art representation of the "Slavers" title.
     * This ASCII art includes stylized text and a bordered design for visual emphasis.
     * It is primarily intended for display purposes, such as enhancing the visual appeal of
     * textual outputs in the program.
     */
    public static String TitleSlavers =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                                        ███████╗██╗      █████╗ ██╗   ██╗███████╗██████╗ ███████╗                                                                                                                             ║\n" +
                    "║                                                                                                                        ██╔════╝██║     ██╔══██╗██║   ██║██╔════╝██╔══██╗██╔════╝                                                                                                                             ║\n" +
                    "║                                                                                                                        ███████╗██║     ███████║██║   ██║█████╗  ██████╔╝███████╗                                                                                                                             ║\n" +
                    "║                                                                                                                        ╚════██║██║     ██╔══██║╚██╗ ██╔╝██╔══╝  ██╔══██╗╚════██║                                                                                                                             ║\n" +
                    "║                                                                                                                        ███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║███████║                                                                                                                             ║\n" +
                    "║                                                                                                                        ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝╚══════╝                                                                                                                             ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";

    /**
     * Represents the title banner for the "Smugglers" phase or section of the game in ASCII art format.
     * This constant is a multi-line string formatted with a distinct ASCII art design, intended for
     * use as a visual representation within the context of the game's user interface or output.
     */
    public static String TitleSmugglers =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                           ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ ███████╗                                                                                                                     ║\n" +
                    "║                                                                                                           ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗██╔════╝                                                                                                                     ║\n" +
                    "║                                                                                                           ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝███████╗                                                                                                                     ║\n" +
                    "║                                                                                                           ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗╚════██║                                                                                                                     ║\n" +
                    "║                                                                                                           ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║███████║                                                                                                                     ║\n" +
                    "║                                                                                                           ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝╚══════╝                                                                                                                     ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";



    /**
     * A static string, `TitleStardust`, representing an ASCII art depiction of the title "Stardust."
     *
     * The string contains detailed block-style ASCII formatting with decorative borders and
     * art that resembles the word "Stardust." It is used within the context of the `ASCII_ART` class.
     *
     * This title may serve as a visual representation or banner for a particular phase, screen, or
     * thematic section in an application or game that makes use of ASCII art for UI presentation.
     */
    public static String TitleStardust =
            "\n" +
                    "\n" +
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║                                                                                                                     ███████╗████████╗ █████╗ ██████╗ ██████╗ ██╗   ██╗███████╗████████╗                                                                                                                      ║\n" +
                    "║                                                                                                                     ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██║   ██║██╔════╝╚══██╔══╝                                                                                                                      ║\n" +
                    "║                                                                                                                     ███████╗   ██║   ███████║██████╔╝██║  ██║██║   ██║███████╗   ██║                                                                                                                         ║\n" +
                    "║                                                                                                                     ╚════██║   ██║   ██╔══██║██╔══██╗██║  ██║██║   ██║╚════██║   ██║                                                                                                                         ║\n" +
                    "║                                                                                                                     ███████║   ██║   ██║  ██║██║  ██║██████╔╝╚██████╔╝███████║   ██║                                                                                                                         ║\n" +
                    "║                                                                                                                     ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝                                                                                                                         ║\n" +
                    "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
                    "\n";


    /**
     *
     */
    public static String[] damage =
                    {"                              ",
                    "                              " ,
                    "░░░█▀▄░█▀█░█▄█░█▀█░█▀▀░█▀▀░░░░" ,
                    "░░░█░█░█▀█░█░█░█▀█░█░█░█▀▀░░░░" ,
                    "░░░▀▀░░▀░▀░▀░▀░▀░▀░▀▀▀░▀▀▀░░░░" ,
                    "                              "};

    /**
     *
     */
    public static String[] cargoValue = {
                "                          " ,
                "                          " ,
                "░░░█▀▀░█▀█░█▀▄░█▀▀░█▀█░░░░" ,
                "░░░█░░░█▀█░█▀▄░█░█░█░█░░░░" ,
                "░░░▀▀▀░▀░▀░▀░▀░▀▀▀░▀▀▀░░░░" ,
                "                          "
    };

    /**
     * The {@code credits} variable contains an ASCII art representation for credit text,
     * specifically designed using unique symbols and spacing.
     * This array is primarily used to display styl*/
    public static String[] credits  =
                    {"                                  " ,
                    "                                  " ,
                    "░░░█▀▀░█▀▄░█▀▀░█▀▄░▀█▀░▀█▀░█▀▀░░░░" ,
                    "░░░█░░░█▀▄░█▀▀░█░█░░█░░░█░░▀▀█░░░░" ,
                    "░░░▀▀▀░▀░▀░▀▀▀░▀▀░░▀▀▀░░▀░░▀▀▀░░░░" ,
                    "                                  "};

    /**
     * A static string array representing ASCII art depictions that highlight
     * the visual construction*/
    public static String[] exposedConnectors =
                    {"                                                                            " ,
                    "                                                                            " ,
                    "░░░█▀▀░█░█░█▀█░█▀█░█▀▀░█▀▀░█▀▄░░░█▀▀░█▀█░█▀█░█▀█░█▀▀░█▀▀░▀█▀░█▀█░█▀▄░█▀▀░░░░" ,
                    "░░░█▀▀░▄▀▄░█▀▀░█░█░▀▀█░█▀▀░█░█░░░█░░░█░█░█░█░█░█░█▀▀░█░░░░█░░█░█░█▀▄░▀▀█░░░░" ,
                    "░░░▀▀▀░▀░▀░▀░░░▀▀▀░▀▀▀░▀▀▀░▀▀░░░░▀▀▀░▀▀▀░▀░▀░▀░▀░▀▀▀░▀▀▀░░▀░░▀▀▀░▀░▀░▀▀▀░░░░" ,
                    "                                                                            "};

    /**
     *
     */
    public static String[] crew  =
                    {
                    "                      " ,
                    "                      " ,
                    "░░░█▀▀░█▀▄░█▀▀░█░█░░░░" ,
                    "░░░█░░░█▀▄░█▀▀░█▄█░░░░" ,
                    "░░░▀▀▀░▀░▀░▀▀▀░▀░▀░░░░" ,
                    "                      "};

    /**
     * Represents an ASCII art representation of the engine power in the form of a string array.
     * Each element of the array corresponds to a line of ASCII art representing the power level.
     *
     * The string array includes:
     * - Padding or empty lines for spacing.
     * - ASCII art formatted text visually representing "engine power" and its symbolic styling.
     *
     * The contents are structured and stylized to visually convey the intended theme when displayed.
     */
    public static String[] enginePower =
                   {"                                                        " ,
                    "                                                        " ,
                    "░░░█▀▀░█▀█░█▀▀░▀█▀░█▀█░█▀▀░█▀▀░░░█▀█░█▀█░█░█░█▀▀░█▀▄░░░░" ,
                    "░░░█▀▀░█░█░█░█░░█░░█░█░█▀▀░▀▀█░░░█▀▀░█░█░█▄█░█▀▀░█▀▄░░░░" ,
                    "░░░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░░░▀░░░▀▀▀░▀░▀░▀▀▀░▀░▀░░░░" ,
                    "                                                        "};

    /**
     *
     */
    public static String[] plasmDrillPower =
            {
                            "                                                    " ,
                            "                                                    " ,
                            "░░░█▀▄░█▀▄░▀█▀░█░░░█░░░█▀▀░░░█▀█░█▀█░█░█░█▀▀░█▀▄░░░░",
                            "░░░█░█░█▀▄░░█░░█░░░█░░░▀▀█░░░█▀▀░█░█░█▄█░█▀▀░█▀▄░░░░",
                            "░░░▀▀░░▀░▀░▀▀▀░▀▀▀░▀▀▀░▀▀▀░░░▀░░░▀▀▀░▀░▀░▀▀▀░▀░▀░░░░",
                            "                                                    "
            };

    /**
     * Represents an array of strings that visually resembles ASCII art patterns.
     * This array can be used for rendering or displaying graphical elements
     * through textual representation.
     */
    public static String[] batteries  =
            {
                            "                                          ",
                            "                                          ",
                            "░░░█▀▄░█▀█░▀█▀░▀█▀░█▀▀░█▀▄░▀█▀░█▀▀░█▀▀░░░░",
                            "░░░█▀▄░█▀█░░█░░░█░░█▀▀░█▀▄░░█░░█▀▀░▀▀█░░░░",
                            "░░░▀▀░░▀░▀░░▀░░░▀░░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀▀▀░░░░",
                            "                                          "
            };

    /**
     * A static String array that represents an ASCII art component.
     *
     * The "equals" field contains a predefined set of strings for constructing a visual
     * representation, possibly used for graphical output or display in text-based
     * interfaces.
     *
     * Each element of the array corresponds to a line of the ASCII art. This particular
     * array's content is structured with spaces and symbols to represent a specific pattern.
     */
    public static String[] equals = {
                    "          " ,
                    "          " ,
                    "░░░░░░░░░░" ,
                    "░░░▀▀▀░░░░" ,
                    "░░░▀▀▀░░░░" ,
                    "          "
    };

    /**
     * Represents a predefined ASCII art design stored as a string array.
     * Each string in the array corresponds to a line in the ASCII art.
     *
     * This particular design is structured to create a visual representation using characters.
     * It includes blank spaces and symbols to form an abstract shape or symbol.
     *
     * Note: The elements of this array are immutable and are used as a static resource
     * in the enclosing class for rendering or further composition in ASCII art.
     */
    public static String[] point = {
                    "   " ,
                    "   " ,
                    "░░░" ,
                    "░░░" ,
                    "░▀░" ,
                    "   "
    };

    /**
     * Represents an ASCII art depiction of the digit zero.
     * This is structured as an array of strings, where each string corresponds to a row
     * in the ASCII representation.
     *
     * This variable is part of a collection of ASCII art components for digits
     * that can be used to construct visuals or enhance textual output.
     */
    public static String[] zero = {
                    "          " ,
                    "          " ,
                    "░░░▄▀▄░░░░" ,
                    "░░░█ █░░░░" ,
                    "░░░░▀░░░░░" ,
                    "          "
    };

    /**
     * A static String array representing an ASCII art component.
     * This array contains six lines, each represented as a string.
     * The content of the array may be visualized as an ASCII design
     * with blank spaces and special characters.
     */
    public static String[] one = {
                    "          " ,
                    "          " ,
                    "░░░▀█░░░░░" ,
                    "░░░░█░░░░░" ,
                    "░░░▀▀▀░░░░" ,
                    "          "
    };

    /**
     * A static string array representing an ASCII art depiction of the number "two".
     * Each element of the array corresponds to a specific line or row in the visual representation.
     * The strings in the array include spaces and special characters to define the shape of the ASCII art.
     */
    public static String[] two = {
                    "          " ,
                    "          " ,
                    "░░░▀▀▄░░░░" ,
                    "░░░▄▀░░░░░" ,
                    "░░░▀▀▀░░░░" ,
                    "          "
    };

    /**
     * Represents a stylized ASCII art representation of the number three
     * as a string array. Each element of the array corresponds to a specific
     * horizontal line in the visual depiction of the number.
     *
     * This variable is typically used for rendering text-based graphics or
     * as part of a larger ASCII art-based visualization system.
     */
    public static String[] three = {
                    "          " ,
                    "          " ,
                    "░░░▀▀█░░░░" ,
                    "░░░░▀▄░░░░" ,
                    "░░░▀▀░░░░░" ,
                    "          "
    };

    /**
     * A static string array representing an ASCII art depiction of the number "four".
     *
     * Each element in the array corresponds to a line in the ASCII art rendering.
     * The content includes a mix of spaces and characters used to create a
     * visual representation of the number "four" in a fixed-width format.
     */
    public static String[] four = {
                    "          " ,
                    "          " ,
                    "░░░█░█░░░░" ,
                    "░░░░▀█░░░░" ,
                    "░░░░░▀░░░░" ,
                    "          "
    };

    /**
     * Represents the ASCII art for the number "5" as a string array.
     *
     * Each element of the array corresponds to a row in the ASCII art.
     * Blank spaces or special characters are used to represent the shape
     * of the number in a visually structured manner.
     */
    public static String[] five = {
                    "          " ,
                    "          " ,
                    "░░░█▀▀░░░░" ,
                    "░░░▀▀▄░░░░" ,
                    "░░░▀▀░░░░░" ,
                    "          "
    };

    /**
     * The variable `six` is a static array of strings representing an ASCII art depiction
     * of the number "6". Each element in the array corresponds to a line in the ASCII art.
     * The ASCII art uses specific characters to visually convey the shape of the number.
     *
     * This variable can be used in applications where stylized numeric representations
     * are needed, such as games, dashboards, or any visual text-based interfaces.
     */
    public static String[] six = {
                    "          " ,
                    "          " ,
                    "░░░▄▀▀░░░░" ,
                    "░░░█▀▄░░░░" ,
                    "░░░░▀░░░░░" ,
                    "          "
    };

    /**
     * The ASCII representation of the number seven, drawn using characters
     * to form a visual depiction. This representation uses a multi-line
     * string array where each element corresponds to a line in the
     * graphical design of the number.
     *
     * Each string in this array contains a combination of spaces and other
     * characters (e.g., "░", "▀") designed to create the appearance of the
     * number seven when rendered together.
     *
     * Note: The format of the ASCII art is specific to the context of
     * the {@code ASCII_ART} class and may need adjustments for use
     * outside of this framework.
     */
    public static String[] seven = {
                    "          " ,
                    "          " ,
                    "░░░▀▀█░░░░" ,
                    "░░░▄▀░░░░░" ,
                    "░░░▀░░░░░░" ,
                    "          "
    };

    /**
     * A string array representing an ASCII art pattern for the number eight (8)
     * or a related symbol. Each element of the array corresponds to a line
     * in the ASCII art representation.
     */
    public static String[] eight = {
                    "          " ,
                    "          " ,
                    "░░░▄▀▄░░░░" ,
                    "░░░▄▀▄░░░░" ,
                    "░░░░▀░░░░░" ,
                    "          "
    };

    /**
     * Represents the ASCII art design for the number nine in a visual representation.
     * The array stores each line of the ASCII art as an individual string.
     * This variable can be used in conjunction with other ASCII art components
     * to display or manipulate text-based graphics.
     */
    public static String[] nine = {
                    "          " ,
                    "          " ,
                    "░░░▄▀▄░░░░" ,
                    "░░░░▀█░░░░" ,
                    "░░░▀▀░░░░░" ,
                    "          "
    };

    /**
     * Adds a number, represented as a string, to each element of the input string array
     * using ASCII art components for representation.
     *
     * @param s the input string array where each element will be modified and the number will be appended
     * @param n the number to be added, represented as a string
     * @return a new string array where each input string has been modified to include the appended number
     * @throws IllegalArgumentException if the number string contains unsupported characters
     */
    public static String[] addNumber(String[] s, String n) {

        int height = equals.length;
        String[] normalized = new String[height];
        for (int i = 0; i < height; i++) {
            normalized[i] = (i < s.length) ? s[i] : "";
        }
        s = normalized;

        String[] result = new String[height];

        StringBuilder[] builders = new StringBuilder[height];
        for (int i = 0; i < height; ++i) {
            builders[i] = new StringBuilder();
            builders[i].append(s[i]);
            builders[i].append(equals[i]);
        }
        for (char digit : n.toCharArray()) {
            switch (digit) {
                case '.': {for (int i = 0; i < point.length; i ++){
                    builders[i].append(point[i]);
                }
                    break;
                }

                case '0': {for (int i = 0; i < zero.length; i ++){
                    builders[i].append(zero[i]);
                }
                    break;
                }
                case '1': {for (int i = 0; i < one.length; i ++){
                    builders[i].append(one[i]);
                }
                    break;
                }
                case '2': {for (int i = 0; i < two.length; i ++){
                    builders[i].append(two[i]);
                }
                    break;
                }
                case '3': {for (int i = 0; i < three.length; i ++){
                    builders[i].append(three[i]);
                }
                    break;
                }
                case '4': {for (int i = 0; i < four.length; i ++){
                    builders[i].append(four[i]);
                }
                    break;
                }
                case '5': {for (int i = 0; i < five.length; i ++){
                    builders[i].append(five[i]);
                }
                    break;
                }
                case '6': {for (int i = 0; i < six.length; i ++){
                    builders[i].append(six[i]);
                }
                    break;
                }
                case '7': {for (int i = 0; i < seven.length; i ++){
                    builders[i].append(seven[i]);
                }
                    break;
                }
                case '8': {for (int i = 0; i < eight.length; i ++){
                    builders[i].append(eight[i]);
                }
                    break;
                }
                case '9': {for (int i = 0; i < nine.length; i ++){
                    builders[i].append(nine[i]);
                }
                    break;
                }
                default: throw new IllegalArgumentException("Carattere non supportato: " + digit);
            }
        }


        for (int i = 0; i < s.length; ++i) {
            result[i] = builders[i].toString();
        }

        return result;
    }


    /**
     * Composes a string by concatenating elements from multiple string arrays,
     * separating them with a specified delimiter and appending a newline after each iteration.
     *
     * @param s the delimiter to be inserted between elements of the string arrays
     * @param toCompose a variable number of string arrays to be combined
     * @return a {@code StringBuilder} containing the composed string
     */
    public static StringBuilder compose(String s, String[]... toCompose){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < toCompose[1].length; ++i) {
            for (String[] c : toCompose){
                result.append(c[i]);
                result.append(s);
            }
            result.append("\n");
        }

        return result;
    }

    /**
     * A string representation of an ASCII art drawing resembling the end of an hourglass.
     * This variable holds a large, multi-line ASCII design intended for graphical representation
     * within text-based applications, such as a game or terminal-based output.
     */
    public static String hourglassEnd =
            "                                                                                                                                                                                                                                                                                        \n" +
            "                                  ▓▓▓██████▓▓▓▓▓▒▒▒▓▓▓                                                                                                                                                                                                                                  \n" +
            "                                  █▓▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▓▓                                                                                                                                                                                                                                  \n" +
            "                                   ▒░░   ░░░░░░░░░░░░                                                                                                                                                                                                                                   \n" +
            "                                   ▒░░   ░░░░░░░░░░░▒                                                                                                                                                                                                                                   \n" +
            "                                   ▒░░     ░░░░░░░░░▓                                                                                                                                                                                                                                   \n" +
            "                                    ▒░      ░░░░░░░▒                                                                                                                                                                                                                                    \n" +
            "                                     ▒░       ░░░░▓                                                                                                                                                                                                                                     \n" +
            "                                      ▓░         ▓                                                                                                                                                                                                                                      \n" +
            "                                        ▒░     ▓                                                                                                                                                                                                                                        \n" +
            "                                          ▒  ▓▒                                                                                                                                                                                                                                         \n" +
            "                                           ▓▓                                                                                                                                                                                                                                           \n" +
            "                                           ▓▓                                                                                                                                                                                                                                           \n" +
            "                                          ▓  ▒                                                                                                                                                                                                                                          \n" +
            "                                        ▒   ░░░▒                                                                                                                                                                                                                                        \n" +
            "                                      ▓░   ░░░░░░▓                                                                                                                                                                                                                                      \n" +
            "                                    ▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒                                                                                                                                                                                                                                    \n" +
            "                                    ▒▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒                                                                                                                                                                                                                                    \n" +
            "                                   ▒▒▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▒                                                                                                                                                                                                                                   \n" +
            "                                   ▒▒▓▓▓▓▓▓▓▒▒▒▒▒▒▒▒▒                                                                                                                                                                                                                                   \n" +
            "                                   ▒▒▒▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒                                                                                                                                                                                                                                   \n" +
            "                                  ▓▒▓▓██████▓▓▓▓▒▒▒▒▓▓                                                                                                                                                                                                                                  \n" +
            "                                                                                                                                                                                                                                                                                        \n";



    /**
     * A static string variable that represents the ASCII art for the start of an hourglass.
     * This is a predefined visual representation in ASCII format, encapsulated within
     * a multi-line string.
     *
     * This ASCII art can be used for decorative or illustrative purposes in scenarios that
     * require an hourglass symbol, such as denoting the passage of time or progression.
     */
    public static String hourglassStart =
            "                                                                                                                                                                                                                                                         \n" +
            "                                  ▓▓▓██████▓▓▓▓▓▒▒▒▓▓▓                                                                                                                                                                                                   \n" +
            "                                  █▓▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▓▓                                                                                                                                                                                                   \n" +
            "                                    ▒░░   ░░░░░░░░░░░                                                                                                                                                                                                    \n" +
            "                                    ▒░░   ░░░░░░░░░░▒                                                                                                                                                                                                     \n" +
            "                                    ▓▓▓████████████▓▓                                                                                                                                                                                                      \n" +
            "                                     ▓▓████████████▒                                                                                                                                                                                                    \n" +
            "                                      ▓▓██████████▒                                                                                                                                                                                                         \n" +
            "                                       ▓▓▓██████▒▒                                                                                                                                                                                                          \n" +
            "                                        ▒░██▓███▓                                                                                                                                                                                                                                        \n" +
            "                                          ▒█▓▓▒                                                                                                                                                                                                                                         \n" +
            "                                           ▓▓                                                                                                                                                                                                            \n" +
            "                                           ▓▓                                                                                                                                                                                                            \n" +
            "                                          ▓ ▓▒                                                                                                                                                                                                           \n" +
            "                                        ▒   ▓ ░▒                                                                                                                                                                                                         \n" +
            "                                      ▓░    ▓  ░░▓                                                                                                                                                                                                       \n" +
            "                                     ▒░    ░▓  ░░░░▒                                                                                                                                                                                                      \n" +
            "                                    ▒░     ░▓   ░░░▒                                                                                                                                                                                                     \n" +
            "                                   ▒░      ░▓    ░░░▒                                                                                                                                                                                                    \n" +
            "                                   ▓░     ▓▓▓▓  ░░░░▒                                                                                                                                                                                                    \n" +
            "                                   ▒░  ▓▓▓▓▓▓▓▓▓ ░░░░                                                                                                                                                                                                    \n" +
            "                                  ▓▒▓▓██████▓▓▓▓▒▒▒▒▓▓                                                                                                                                                                                                   \n" +
            "                                                                                                                                                                                                                                                         \n";


    /**
     * The `lose` variable is a static string containing ASCII art, which represents a
     * visual display when a losing event occurs in the context of the game.
     *
     * This string uses a combination of visual characters and formatting designed for
     * large-scale display of the "LOSE" message with stylistic effects, like borders
     * and spacing. It is used in scenarios where the game intends to show a defeat
     * screen or outcome to users in an artistic manner.
     *
     * The ASCII art spans multiple lines and includes decorative borders to enhance
     * presentation. It is a read-only value and does not change during runtime.
     */
    public static String lose = "" +
            "\n" +
            "\n" +
            "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                     ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗      ██████╗ ███████╗███████╗                                                                                                                         ║\n" +
            "║                                                                                                                     ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║     ██╔═══██╗██╔════╝██╔════╝                                                                                                                         ║\n" +
            "║                                                                                                                      ╚████╔╝ ██║   ██║██║   ██║    ██║     ██║   ██║███████╗█████╗                                                                                                                           ║\n" +
            "║                                                                                                                       ╚██╔╝  ██║   ██║██║   ██║    ██║     ██║   ██║╚════██║██╔══╝                                                                                                                           ║\n" +
            "║                                                                                                                        ██║   ╚██████╔╝╚██████╔╝    ███████╗╚██████╔╝███████║███████╗                                                                                                                         ║\n" +
            "║                                                                                                                        ╚═╝    ╚═════╝  ╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝╚══════╝                                                                                                                         ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
            "\n";


    /**
     * Represents an ASCII art depiction of the word "WIN" surrounded by a decorative border.
     * This variable is a static string containing multiple lines of textual artwork that
     * celebrates a win condition in a visually appealing way using ASCII characters.
     */
    public static String win = "\n" +
            "\n" +
            "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                        ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗██╗███╗   ██╗                                                                                                                                ║\n" +
            "║                                                                                                                        ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██║████╗  ██║                                                                                                                                ║\n" +
            "║                                                                                                                         ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║██╔██╗ ██║                                                                                                                                ║\n" +
            "║                                                                                                                          ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║██║╚██╗██║                                                                                                                                ║\n" +
            "║                                                                                                                           ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝██║██║ ╚████║                                                                                                                                ║\n" +
            "║                                                                                                                           ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝                                                                                                                                ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "║                                                                                                                                                                                                                                                                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n" +
            "\n";


}
