package org.example.galaxy_trucker.View.TUI;

import org.jline.reader.Completer;

import java.util.List;

/**
 * The DynamicCompleter interface extends the Completer interface to provide
 * functionality for dynamic command-line auto-completion. This interface is designed
 * to allow setting and updating a list of commands at runtime, enabling flexible
 * and context-aware suggestions based on the application's state or user input.
 */
public interface DynamicCompleter extends Completer {
    /**
     * Sets the list of commands for dynamic auto-completion.
     *
     * @param commands the list of command strings to be used for auto-completion
     */
    public void setCommands(List<String> commands);
}
