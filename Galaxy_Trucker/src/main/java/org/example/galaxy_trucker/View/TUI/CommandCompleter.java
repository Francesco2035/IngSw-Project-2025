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
        description.put("create", "create new game");
        description.put("kill", "x y x y ... : kill 1 human in the selected tile x,y");
        description.put("inserttile", "x y rotation");
        description.put("discard", "");
        description.put("picktile", "no arg-> covered : index -> uncovered");
        description.put("addcrew", "x y : adds 2 humans in the selected tile");
        description.put("addbrownalien", "x y");
        description.put("addpurplealien", "x y");
        description.put("getreward", "i_1 x y i_2 : put the good in position i_1 \n" +
                                 "in the selected tile and in the i_2 position");
        description.put("finishcargo", ":finish handlecargo phase");
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
