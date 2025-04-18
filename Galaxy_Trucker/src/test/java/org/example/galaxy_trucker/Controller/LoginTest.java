package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Boards.Hourglass;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    public void testMultipleLogins(){
//        GameLists gl = new GameLists();
//        GameHandler gh = new GameHandler(gl);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            File file = new File("src/main/resources/testJson/Login/login_commands.json");
//
//            JsonNode rootNode = objectMapper.readTree(file);
//
//            Iterator<JsonNode> elements = rootNode.elements();
//
//            do {
//                JsonNode commandNode = elements.next();
//
//                String commandString = commandNode.toString();
//
//                System.out.println(commandString);
//
//                gh.Receive(commandString);
//            }while (elements.hasNext() );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        gl.getGames().forEach(game -> {
//
//            assertTrue(game.getPlayers().size() <= 4, "More than 4 players");
//
//            long uniqueCount = game.getPlayers().values().stream()
//                    .map(Player::GetID)
//                    .distinct()
//                    .count();
//
//            assertEquals(game.getPlayers().size(), uniqueCount, "Duplicated playersId in the same game");
//        });
    }

}