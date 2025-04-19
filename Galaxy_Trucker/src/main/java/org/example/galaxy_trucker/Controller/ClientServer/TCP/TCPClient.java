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


public class TCPClient{

    private Socket echoSocket;
    private PrintWriter out = null;
    private BufferedReader in;
    private BufferedReader stdIn = null;
    private long lastPongTime = 0;


    private void PongListener() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals("pong")) {
                    lastPongTime = System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            disconnect();
        }
    }


    private void PingLoop() {
        while (!echoSocket.isClosed()) {
            out.println("ping");
            //System.out.println("ping");
            try {
                Thread.sleep(5000);

                if (System.currentTimeMillis() - lastPongTime > 15000) {
                    System.out.println("Connection timed out. Disconnecting...");
                    disconnect();
                    break;
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }


    public void disconnect(){
        try {
            echoSocket.close();
            System.out.println("\nServer Disconnected.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void main() throws IOException {


        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT);
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }



        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }


        PrintWriter finalOut = out;

        new Thread(this::PongListener).start();
        new Thread(this::PingLoop).start();


        Gson gson = new Gson();
        CommandInterpreter commandInterpreter;

        System.out.println("Connection started\n");

        System.out.print("Inserisci il tuo nome (player ID): ");
        String playerId = stdIn.readLine();
        System.out.print("Inserisci il Game ID: ");
        String gameId = stdIn.readLine();
        System.out.print("Inserisci il livello della partita (livello): ");
        String gameLevel = stdIn.readLine();

        String loginString = "Login" + " " + playerId + " " + gameId + " " + gameLevel;
        commandInterpreter = new CommandInterpreter(playerId, gameId);
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


