package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class Killing implements InputHandler {
    private Card currentCard;
    private ArrayList<IntegerPair> coords;

    public Killing(Card card){
        this.currentCard = card;
        this.coords = new ArrayList<>();
    }

    public void setInput(ArrayList<IntegerPair> coords){
        this.coords = coords;
    }
    public void action(){
        this.currentCard.killHumans(coords);
    }
}
