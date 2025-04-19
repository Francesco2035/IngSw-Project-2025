package org.example.galaxy_trucker.Model.Cards.CardEffect;

import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public interface ConsumeEnergy extends CardEffect {
    public abstract void consumeEnergy(ArrayList<IntegerPair> coordinates);
}
