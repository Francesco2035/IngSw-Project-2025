package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class ClientActions extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private Player me;
    private Game myGame;


    public ClientActions() throws RemoteException{
        me =  new Player();
        myGame = null;
    }


    @Override
    public void StartClient() throws IOException, NotBoundException {

        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

        this.server = (ServerInterface) registry.lookup("CommandReader");
        this.server.login(this);

        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));


        System.out.println("Insert player name: ");
        String name = br.readLine();
        System.out.println("Insert game name: ");
        String GName = br.readLine();
        server.CreateGame(this, name, GName, 2);


        this.inputLoop();

    }

    private void inputLoop() throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        String cmd;
        while (!Objects.equals(cmd = br.readLine(), "end")){
            server.command("{\"GameId\" : " + myGame.getID() + ", \"PlayerId\" : " + me.GetID() + ", \"Command\" : " + cmd + "}");
        }
    }

    @Override
    public Player getPlayer() throws RemoteException{return me;}

    @Override
    public Game getGame() throws RemoteException{return myGame;}

    @Override
    public void setGame(Game game) throws RemoteException{myGame = game;}

    @Override
    public void setPlayerId(String id) throws RemoteException {me.setId(id);}


    //--add-opens org.example.galaxy_trucker/org.example.galaxy_trucker.Controller.RMI=java.rmi
    public static void main(String[] args) throws RemoteException, NotBoundException {

        try {
            new ClientActions().StartClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
