package org.example.galaxy_trucker.View.TUI;

import org.jline.reader.Completer;

import java.util.List;

public interface DynamicCompleter extends Completer {
    public void setCommands(List<String> commands);
}
