package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.ConsumingEnergy;

import java.util.ArrayList;

public class ConsumeEnergyCommand implements Command {

    private Card card;
    private ArrayList<IntegerPair> coordinate;

    public ConsumeEnergyCommand(Card card, ArrayList<IntegerPair> coordinate) {
        this.card = card;
        this.coordinate = coordinate;
    }

    @Override
    public void execute() {
        card.consumeEnergy(coordinate);
    }
}
