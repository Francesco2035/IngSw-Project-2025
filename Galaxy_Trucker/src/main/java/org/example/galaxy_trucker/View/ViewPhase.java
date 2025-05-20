package org.example.galaxy_trucker.View;

public enum ViewPhase {
    LOBBY,
    LOGIN,
    BUILDING_SHIP,
    BUILDING_SHIP2,
    ADD_CREW, //è meglio che magari il server mandi al client un semplice messaggio per far variare lo stato
    FLIGHT,
    GAME_OVER
}
