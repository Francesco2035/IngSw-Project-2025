package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.RandomCardEffectEvent;

public interface RandomCardEffectListener {
    public void Effect(RandomCardEffectEvent event) ;
}
