package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;

public class DefendingFromMeteorites implements InputHandler {
    private Card card;
    private IntegerPair shield;
    private IntegerPair cannon;
    private IntegerPair energy;

    public DefendingFromMeteorites(Card card) {
        this.card = card;
        this.shield = new IntegerPair(0, 0);
        this.cannon = new IntegerPair(0, 0);
        this.energy = new IntegerPair(0, 0);
    }

    public void setInput(IntegerPair cannon,IntegerPair shield,IntegerPair energy) {
        this.shield = shield;
        this.cannon = cannon;
        this.energy = energy;
    }

    @Override
    public void action(){
        this.card.DefendFromMeteorites(cannon, shield,energy);
    }

}
