package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientActions;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.ServerActions;
import org.junit.jupiter.api.Test;

public class RMITest {

    //--add-opens org.example.galaxy_trucker/org.example.galaxy_trucker.Controller.RMI=java.rmi

    @Test
    public void Server(){

        try {
            new ServerActions().StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void Client(){

        try {
            new ClientActions().StartClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }



