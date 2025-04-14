package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBuilding {

    @Test
    public void testBuilding(){
        String simulatedInput = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"lvl\":2}";
        String simulatedInput2 = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"pietro\", \"lvl\":2}";

        String simulatedInput3 = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"gay\", \"lvl\":2}";
        String simulatedInput4 = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"paluGay\", \"lvl\":2}";

        InputStream originalIn = System.in;

        try {
            GameLists gl = new GameLists();
            GameHandler gh = new GameHandler(gl);
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


            gh.initPlayer(br.readLine());
            //assertFalse(gh.getControllerMap().isEmpty());

            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            gh.initPlayer(br2.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput3.getBytes()));
            BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
            gh.initPlayer(br3.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput4.getBytes()));
            BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
            gh.initPlayer(br4.readLine());

            assertEquals(1, gl.getGames().size());
            String simulatedInput5 = "{\"title\":\"ready\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"readyState\":true}";
            String simulatedInput6 = "{\"title\":\"ready\", \"gameID\":\"num1\", \"playerID\":\"pietro\", \"readyState\":true}";
            String simulatedInput7 = "{\"title\":\"ready\", \"gameID\":\"num1\", \"playerID\":\"gay\", \"readyState\":true}";
            String simulatedInput8 = "{\"title\":\"ready\", \"gameID\":\"num1\", \"playerID\":\"paluGay\", \"readyState\":true}";

            System.setIn(new ByteArrayInputStream(simulatedInput5.getBytes()));
            BufferedReader br5 = new BufferedReader(new InputStreamReader(System.in));


            gh.changeState(br5.readLine());
            //assertFalse(gh.getControllerMap().isEmpty());

            System.setIn(new ByteArrayInputStream(simulatedInput6.getBytes()));
            BufferedReader br6 = new BufferedReader(new InputStreamReader(System.in));
            gh.changeState(br6.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput7.getBytes()));
            BufferedReader br7 = new BufferedReader(new InputStreamReader(System.in));
            gh.changeState(br7.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput8.getBytes()));
            BufferedReader br8 = new BufferedReader(new InputStreamReader(System.in));
            gh.changeState(br8.readLine());
            assertEquals(PrepController.class, gh.getControllerMap().get("paolo").getClass());
            assertEquals(PrepController.class, gh.getControllerMap().get("pietro").getClass());
            assertEquals(PrepController.class, gh.getControllerMap().get("gay").getClass());
            assertEquals(PrepController.class, gh.getControllerMap().get("paluGay").getClass());


            ObjectMapper objectMapper = new ObjectMapper();
            try {
                File file = new File("C:/Users/franc/Desktop/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/Galaxy_Trucker/src/main/resources/testJson/building_commands.json");

                JsonNode rootNode = objectMapper.readTree(file);

                Iterator<JsonNode> elements = rootNode.elements();
                while (elements.hasNext()) {
                    JsonNode commandNode = elements.next();

                    String title = JsonHelper.getRequiredText(commandNode, "title");
                    String playerid = JsonHelper.getRequiredText(commandNode, "playerID");
                    String commandString = commandNode.toString();

                    System.out.println(commandString);

                    if (title.equals("ready")) {
                        gh.changeState(commandString);
                    } else {
                        gh.getControllerMap().get(playerid).action(commandString);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalIn);
        }
    }
}
