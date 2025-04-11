package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

public class DefendingFromShots implements InputHandler {
private Card card;
private IntegerPair coord;

DefendingFromShots(Card card) {
    this.card = card;
    this.coord=null;
}

public void setInput(IntegerPair coords) {
    this.coord=coords;
    }

@Override
public void action() {
    this.card.DefendFromSmall(coord);
}


}
