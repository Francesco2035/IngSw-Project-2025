package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.google.gson.Gson;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.Command;
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
        BufferedReader in;
        BufferedReader stdIn = null;

        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }

        Gson gson = new Gson();
        CommandInterpreter commandInterpreter ;

        System.out.print("Inserisci il tuo nome (player ID): ");
        String playerId = stdIn.readLine();
        System.out.print("Inserisci il Game ID: ");
        String gameId = stdIn.readLine();
        System.out.print("Inserisci il livello della partita (livello): ");
        String gameLevel = stdIn.readLine();

        String loginString = "Login"+ " " + playerId + " " + gameId + " " + gameLevel;
        commandInterpreter = new CommandInterpreter(playerId,gameId);
        Command loginCommand = commandInterpreter.interpret(loginString);

        String jsonLogin = gson.toJson(loginCommand);
        out.println(jsonLogin);
        System.out.println("Comando di login inviato: " + jsonLogin);

        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
            System.out.println("Input ricevuto: " + userInput);
            try {
                Command command = commandInterpreter.interpret(userInput);

                String json = gson.toJson(command);

                out.println(json);
                System.out.println("Comando inviato: " + json);

                System.out.println("Nuovo loop");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
