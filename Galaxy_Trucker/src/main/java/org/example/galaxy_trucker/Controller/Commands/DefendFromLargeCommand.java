package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

public class DefendFromLargeCommand implements Command{

    private IntegerPair plasmaDrill;
    private IntegerPair batteryComp;
    private Card card;

    public DefendFromLargeCommand(Card card, IntegerPair plasmaDrill, IntegerPair batteryComp) {
        this.card = card;
        this.plasmaDrill = plasmaDrill;
        this.batteryComp = batteryComp;
    }



    @Override
    public void execute() {
        card.DefendFromLarge(plasmaDrill, batteryComp);
    }
}
