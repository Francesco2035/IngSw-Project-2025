package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class KillCommand implements Command {
    Card card;
    ArrayList<IntegerPair> coordinates;

    public KillCommand(Card card, ArrayList<IntegerPair> coordinates) {
        this.coordinates = coordinates;
        this.card = card;
    }

    @Override
    public void execute() {
        card.killHumans(coordinates);
    }

}

