package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;

        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT);
        }
        catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }

        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;

        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }

        String userInput = "";

        System.out.println("Connection started\n");


        while (true) {
            try {
                System.out.println("Insert Json command: ");
                if ((userInput = stdIn.readLine()) == null) break;
                out.println(userInput);
                //System.out.println("Server received: " + in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}