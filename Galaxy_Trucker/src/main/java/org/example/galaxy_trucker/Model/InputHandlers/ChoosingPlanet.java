package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;

public class ChoosingPlanet implements InputHandler{
    private Card card;
    private int planet;
    private boolean accepted;

    public ChoosingPlanet(Card card) {
        this.card = card;
    }

    public void setInput(int planet, boolean accepted) {
        this.planet = planet;
        this.accepted = accepted;
    }

    @Override
    public void action() {
        this.card.choosePlanet(this.planet,this.accepted);
    }

}
