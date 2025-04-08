package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;

public class ChosingPlanetsCommand implements Command{

    Card card;
    int planet;

    public ChosingPlanetsCommand(Card card, int planet) {
        this.card = card;
        this.planet = planet;
    }

    @Override
    public void execute() {

        card.choosePlanet(planet);

    }
}
