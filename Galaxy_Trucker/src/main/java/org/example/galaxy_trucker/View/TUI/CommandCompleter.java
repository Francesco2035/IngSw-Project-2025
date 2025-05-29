package org.example.galaxy_trucker.View.TUI;

import org.jline.reader.Completer;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandCompleter implements DynamicCompleter {
    private List<String> commands = new ArrayList<>(List.of("Login", "Lobby"));

    private HashMap<String,String> description;

    private InputReader inputReader;


    public CommandCompleter() {
        description = new HashMap<>();
        description.put("login", "Login");
        description.put("lobby", "Lobby");
        description.put("kill", "x y");
        description.put("inserttile", "x y rotation");
        description.put("discard", "");
    }
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String buffer = line.word().toLowerCase();
        for (String cmd : commands) {
            if (cmd.toLowerCase().startsWith(buffer)) {
                String desc = description.get(cmd.toLowerCase());
                if (desc == null) {
                    desc = "";
                }
                candidates.add(new Candidate(
                        cmd,      // value
                        cmd,      // display
                        null,     // group
                        desc,     // description
                        null,     // suffix
                        null,     // key binding
                        false     // complete = false → non aggiunge spazio
                ));
            }
        }
    }

    @Override
    public void setCommands(List<String> commands) {
        this.commands.clear();
        this.commands.addAll(commands);
    }


}
