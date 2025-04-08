package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

public class DefendFromShotsCommand implements Command {

    private Card card;
    private IntegerPair shield;
    private IntegerPair BatteryComp;
    public DefendFromShotsCommand(Card card, IntegerPair shield,IntegerPair BatteryComp){
        this.card = card;
        this.shield = shield;
        this.BatteryComp = BatteryComp;
    }




    @Override
    public void execute() {
        card.continueCard(shield,BatteryComp);
    }
}
