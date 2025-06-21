package org.example.galaxy_trucker.View.TUI;

import org.jline.reader.Completer;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandCompleter implements DynamicCompleter {
    private List<String> commands = new ArrayList<>(List.of("Create", "Join", "Lobby", "Reconnect"));

    private HashMap<String,String> description;

    private InputReader inputReader;


    public CommandCompleter() {
        //TODO: scrivere meglio qualche descrizione
        description = new HashMap<>();
        description.put("join", "Join an existing game");
        description.put("reconnect", "insert token");
        description.put("lobby", "Show available games");
        description.put("create", "create new game, you can also join one [level have to match the existing game's one!]");
        description.put("kill", "x y x y ... : kill 1 crew member in the selected tile x,y");
        description.put("inserttile", "x y rotation");
        description.put("picktile", "no arg-> covered : index -> uncovered");
        description.put("addcrew", "x y : adds 2 humans in the selected tile");
        description.put("addbrownalien", "x y");
        description.put("addpurplealien", "x y");
        description.put("getreward", "x y i : put the good in position i in the selected tile");
        description.put("finishcargo", ":finish HandleCargo phase");
        description.put("defendlarge", "xc yc xb yb : plasmaDrill coordinate, battery coordinate");
        description.put("defendsmall", "x y : battery coordinate");
        description.put("seeboards", "see other players' board");
        description.put("log", "see all events");
        description.put("Hourglass", "start Hourglass (only for full game)");
        description.put("frombuffer", "i : pick tile from buffer from position i");
        description.put("tobuffer", "rotation : put tile in buffer with a specific rotation");
        description.put("consumeenergy", "x y x y ...: consume a battery from each specified tile (x y pairs)");
        description.put("givespeed", "x y x y ...: activate selected hotWaterHeaters");
        description.put("giveattack", "x y x y ...: activate the selected plasmaDrills");
        description.put("finishbuilding", "for tutorial no params, for full game select starting position [1,2,3,4]");
        description.put("switch", "x1 y1 i1 x2 y2 i2: switch cargo_1 with cargo_2");
        description.put("discardcargo", "x y i: remove the cargo in position i of the selected tile (x,y)");
        description.put("ready", "");
        description.put("notready", "");
        description.put("quit", "abandon race");
        description.put("removetile", "x y : remove tile in position x,y");
        description.put("selectchunk", "x y : keep the chunk with tile x,y");
        description.put("theft", "x y i : give the cargo in position i from the tile x,y ");
        description.put("discard", "discard the tile from you hand");
        description.put("seedeck", "i: shows the i-th deck (only in full game mode)");

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
