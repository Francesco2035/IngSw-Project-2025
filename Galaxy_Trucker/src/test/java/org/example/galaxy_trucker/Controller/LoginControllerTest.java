package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.ServersHandler;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    @Test
    void nextState() throws Exception {
        String[] args = {"RMI", "TUI"};

        Thread serverThread = new Thread(() -> {
            try {
                ServersHandler serversHandler = new ServersHandler();
                serversHandler.run();
            } catch (Exception e) {
                e.printStackTrace();
                fail("Errore nell'avvio del server: " + e.getMessage());
            }
        });

        Thread clientThread = new Thread(() -> {
            try {
                Client client = new Client();
                client.run(args);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Errore nell'avvio del client: " + e.getMessage());
            }
        });

        serverThread.start();
        Thread.sleep(1000);
        clientThread.start();

        serverThread.join(5000);
        clientThread.join(5000);
    }

}