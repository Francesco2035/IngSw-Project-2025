package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class AcceptKilling implements InputHandler {
    private ArrayList<IntegerPair> coords;
    private boolean accepted;
    private Card card;

    public AcceptKilling( Card card) {
        this.card = card;
        this.coords = new ArrayList<>();
        this.accepted = false;
    }

    public void setInput(ArrayList<IntegerPair> coords, boolean accepted){
        this.coords=coords;
        this.accepted=accepted;
    }

    public void action(){
        this.card.continueCard(this.coords, this.accepted);
    }
}
