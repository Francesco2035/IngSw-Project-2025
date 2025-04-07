package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class Kill implements Command {
    Card card;
    ArrayList<IntegerPair> coordinates;

    public Kill(Card card, ArrayList<IntegerPair> coordinates) {
        this.coordinates = coordinates;
        this.card = card;
    }

    @Override
    public void execute() {
        card.continueCard(coordinates);
        //chiama la carta passando le coordinate, il controllo se queste coordinate siano effettivamente
        //housing unit può essere fatto qui o come ora è delegato alla specifica azione della playerboard
    }
}

