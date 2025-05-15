package org.example.galaxy_trucker.View;

import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.ServersHandler;

import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java -jar app.jar <server|client> [CLIENT ARGUMENTS...]");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();

        switch (mode) {
            case "server":
                ServersHandler serversHandler = new ServersHandler();
                serversHandler.run();
                break;
            case "client":
                Client client = new Client();
                //String[] clientArgs = Arrays.copyOfRange(args, 1, args.length);
                client.run();
                break;
            default:
                System.err.println("Unknown mode: " + mode);
                System.err.println("Usage: java -jar app.jar <server|client> [CLIENT ARGUMENTS...]");
                System.exit(1);
        }
    }

}
