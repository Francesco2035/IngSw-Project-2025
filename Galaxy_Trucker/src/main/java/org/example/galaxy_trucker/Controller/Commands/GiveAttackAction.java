package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class GiveAttackAction implements Command{

    private Card card;
    private ArrayList<IntegerPair> coordinates;
    public GiveAttackAction(Card card, ArrayList<IntegerPair> coordinates) {
        this.coordinates = coordinates;
        this.card = card;
    }

    @Override
    public void execute() {

        card.continueCard(coordinates);
    }

}
