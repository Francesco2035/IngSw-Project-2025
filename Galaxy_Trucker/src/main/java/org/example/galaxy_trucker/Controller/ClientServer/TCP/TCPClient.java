package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;

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
    private Client client = null;

    public TCPClient(Client c) {
        this.client = c;
    }

    private void PingListener() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals("pong")) {
                    lastPongTime = System.currentTimeMillis();

                    //System.out.println("Pong");
                }
            else {
                   // System.out.println("Received msg: " + msg);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Event event = objectMapper.readValue(msg, Event.class);
                    client.receiveEvent(event);
            }
            }
        } catch (IOException e) {
            System.out.println("IOException in PingListener " + e.getMessage());
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






    public void startClient() throws IOException {


        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT); //SOCKET CLIENT
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

        new Thread(this::PingListener).start();
        new Thread(this::PingLoop).start();


        Gson gson = new Gson();
        CommandInterpreter commandInterpreter;

        System.out.println("Connection started\n");

        client.getView().askInput("PlayerID: ");

        System.out.print("Inserisci il tuo nome (player ID): ");
        String playerId = stdIn.readLine();
        System.out.print("Inserisci il Game ID: ");
        String gameId = stdIn.readLine();
        System.out.print("Inserisci il livello della partita (livello): ");
        int gameLevel = Integer.parseInt(stdIn.readLine());

        LoginCommand loginCommand = new LoginCommand(gameId,playerId, gameLevel, "Login");

        commandInterpreter = new CommandInterpreter(playerId, gameId);
        commandInterpreter.setlv(gameLevel);

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


