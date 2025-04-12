package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.GameLists;
import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {

    @Test
    public void testInitPlayer() throws Exception {
        String simulatedInput = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"lvl\":2}";

        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            GameLists gl = new GameLists();
            GameHandler gh = new GameHandler(gl);
            gh.initPlayer(br.readLine());
            assertFalse(gh.getControllerMap().isEmpty());

        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void changeState() throws IOException {

        String simulatedInput = "{\"title\":\"ready\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"readyState\":true}";
        String simulatedInput2 = "{\"title\":\"login\", \"gameID\":\"num1\", \"playerID\":\"paolo\", \"lvl\":2}";
        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));

            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            GameLists gl = new GameLists();
            GameHandler gh = new GameHandler(gl);

            gh.initPlayer(br2.readLine());
            gh.changeState(br.readLine());


            assertEquals(PrepController.class, gh.getControllerMap().get("paolo").getClass());

        } finally {
            System.setIn(originalIn);
        }
    }
}