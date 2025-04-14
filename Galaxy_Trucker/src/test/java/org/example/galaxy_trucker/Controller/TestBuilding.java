package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Boards.Hourglass;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
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
        GameLists gl = new GameLists();
        GameHandler gh = new GameHandler(gl);

        try {

            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


            gh.Receive(br.readLine());
            //assertFalse(gh.getControllerMap().isEmpty());

            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            gh.Receive(br2.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput3.getBytes()));
            BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
            gh.Receive(br3.readLine());

            System.setIn(new ByteArrayInputStream(simulatedInput4.getBytes()));
            BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
            gh.Receive(br4.readLine());

            assertEquals(1, gl.getGames().size());
            String simulatedInput5 = "{\"title\":\"Ready\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"ready\":true}";
            String simulatedInput6 = "{\"title\":\"Ready\", \"gameID\":\"num1\", \"playerID\":\"pietro\", \"ready\":true}";
            String simulatedInput7 = "{\"title\":\"Ready\", \"gameID\":\"num1\", \"playerID\":\"gay\", \"ready\":true}";
            String simulatedInput8 = "{\"title\":\"Ready\", \"gameID\":\"num1\", \"playerID\":\"paluGay\", \"ready\":true}";

            System.setIn(new ByteArrayInputStream(simulatedInput5.getBytes()));
            BufferedReader br5 = new BufferedReader(new InputStreamReader(System.in));


            //assertFalse(gh.getControllerMap().isEmpty());

            System.setIn(new ByteArrayInputStream(simulatedInput6.getBytes()));
            BufferedReader br6 = new BufferedReader(new InputStreamReader(System.in));

            System.setIn(new ByteArrayInputStream(simulatedInput7.getBytes()));
            BufferedReader br7 = new BufferedReader(new InputStreamReader(System.in));

            System.setIn(new ByteArrayInputStream(simulatedInput8.getBytes()));
            BufferedReader br8 = new BufferedReader(new InputStreamReader(System.in));
            gh.Receive(br5.readLine());
            gh.Receive(br6.readLine());
            gh.Receive(br7.readLine());
            gh.Receive(br8.readLine());

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                File file = new File("C:/Users/franc/Desktop/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/Galaxy_Trucker/src/main/resources/testJson/building_commands.json");

                JsonNode rootNode = objectMapper.readTree(file);

                Iterator<JsonNode> elements = rootNode.elements();

                Hourglass hourglass = gl.getGames().get(0).getGameBoard().getHourglass();
                do {
                    JsonNode commandNode = elements.next();

                    String title = JsonHelper.getRequiredText(commandNode, "title");
                    String playerid = JsonHelper.getRequiredText(commandNode, "playerID");
                    String commandString = commandNode.toString();

                    System.out.println(commandString);

                    gh.Receive(commandString);
                }while (elements.hasNext() );
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalIn);
        }

        //si può testare solo con comandi sotto i 985 ms per il resto dovrebbe funzioanre
        assertEquals(CheckValidity.class, gl.getGames().getFirst().getPlayers().get("paolo").getPlayerState().getClass());

    }
}
