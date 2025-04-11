//package org.example.galaxy_trucker.Controller.CardControllers;
//
////import org.example.galaxy_trucker.Model.InputHandlers.AcceptKilling;
//import org.example.galaxy_trucker.Model.IntegerPair;
//import org.example.galaxy_trucker.Model.Player;
//
//import java.util.ArrayList;
//
//
//public class AcceptKillingController implements CardInputController{
//    Player cur;
//    ArrayList<IntegerPair> coordinates;
//    boolean choice;
//
//    public AcceptKillingController(ArrayList<IntegerPair> coords, boolean accepted, Player p) {
//        coordinates = coords;
//        choice = accepted;
//        cur = p;
//    }
//
//    @Override
//    public void setInput() {
//        AcceptKilling handler = (AcceptKilling) cur.getInputHandler();
//        handler.setInput(coordinates, choice);
//        cur.execute();
//    }
//}
