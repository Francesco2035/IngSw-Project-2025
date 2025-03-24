package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class GiveAttack implements InputHandler {

    private Card ActiveCard;
    private ArrayList<IntegerPair> coords;

    public GiveAttack(Card ActiveCard) {
       this.ActiveCard = ActiveCard;
       this.coords = new ArrayList<>();
    }

    public void setInput(ArrayList<IntegerPair> coords) {
        this.coords=coords;
    }

    @Override
    public void action() {
        this.ActiveCard.continueCard(coords);
    }

}
