package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

public class DefendFromSmallCommand implements Command {

    private Card card;
    private IntegerPair shield;
    private IntegerPair batteryComp;


    public DefendFromSmallCommand(Card card, IntegerPair batteryComp){
        this.card = card;
        this.batteryComp = batteryComp;
    }

    @Override
    public void execute() {
//        card.DefendFromSmall(batteryComp);
    }
}
