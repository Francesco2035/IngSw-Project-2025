package org.example.galaxy_trucker.Model.Cards;

import com.sun.nio.sctp.AbstractNotificationHandler;
import org.example.galaxy_trucker.Controller.Listeners.CardListner;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.GAGen;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class CardStacks implements Serializable {
    private int level;
    private ArrayList<Card> VisibleCards1;
    private ArrayList<Card> VisibleCards2;
    private ArrayList<Card> VisibleCards3;
    private ArrayList<Card> HiddenCards;
    private ArrayList<Card> FullAdventure; //prendi questo bro
    private ArrayList<Card> Level1Deck;
    private ArrayList<Card> Level2Deck;
    private GAGen GaG;
    private HashMap<String, CardListner> cardListnerHashMap;

    public CardStacks(GAGen Gag, int lv) {
        this.level = lv;
        this.GaG=Gag;
        this.HiddenCards = new ArrayList<>();
        this.FullAdventure = new ArrayList<>();
        this.Level1Deck = new ArrayList<>();
        this.Level2Deck = new ArrayList<>();
        VisibleCards1 = new ArrayList<>();
        VisibleCards2 = new ArrayList<>();
        VisibleCards3 = new ArrayList<>();
        this.cardListnerHashMap = new HashMap<>();

        Card Currentcard;
        Random r = new Random();
        for(int i=0;i<GaG.getCardsDeck().size();i++){
            Currentcard=GaG.getCardsDeck().get(i);
            if(Currentcard.getLevel()==1){
                Level1Deck.add(Currentcard);
            }
            else if(Currentcard.getLevel()==2){
                Level2Deck.add(Currentcard);
            }

        }

        if(level==1){
            HiddenCards = Level1Deck; /// non è proprio l'intero level 1 deck ma una specifica sezione
        }

        else{
            int curr;

            curr= r.nextInt(Level1Deck.size());
            VisibleCards1.add(Level1Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards1.add(Level2Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards1.add(Level2Deck.remove(curr));

            curr= r.nextInt(Level1Deck.size());
            VisibleCards2.add(Level1Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards2.add(Level2Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards2.add(Level2Deck.remove(curr));

            curr= r.nextInt(Level1Deck.size());
            VisibleCards3.add(Level1Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards3.add(Level2Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            VisibleCards3.add(Level2Deck.remove(curr));

            curr= r.nextInt(Level1Deck.size());
            HiddenCards.add(Level1Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            HiddenCards.add(Level2Deck.remove(curr));
            curr= r.nextInt(Level2Deck.size());
            HiddenCards.add(Level2Deck.remove(curr));
        }
    }


    public void mergeDecks(){
        if(level==2){
            FullAdventure.addAll(VisibleCards1);
            FullAdventure.addAll(VisibleCards2);
            FullAdventure.addAll(VisibleCards3);
        }

        FullAdventure.addAll(HiddenCards);

        do {
            Collections.shuffle(FullAdventure);
        } while (FullAdventure.getFirst().getLevel() != level);
    }

    public ArrayList<Card> getFullAdventure(){
        return FullAdventure;
    }
    public ArrayList<Card> getVisibleCards1(){
        return VisibleCards1;
    }
    public ArrayList<Card> getVisibleCards2(){
        return VisibleCards2;
    }
    public ArrayList<Card> getVisibleCards3(){
        return VisibleCards3;
    }

    public Card PickNewCard(){
        return FullAdventure.removeFirst();
    }

    public void addListener(String player, CardListner listener){
        cardListnerHashMap.putIfAbsent(player,listener);
    }

    public void removeListener(String player){
        cardListnerHashMap.remove(player);
    }

    public void notify(String player, int i){
        if (i > 3 || i < 1){
            throw new InvalidInput("Selected deck doesnt exists or you are not allow to see it");
        }
        ArrayList<CardEvent> deck = new ArrayList<>();
        if (i == 1){
            for (Card card : VisibleCards1) {
                deck.add(new CardEvent(card.getId()));
            }
        }
        if (i == 2){
            for (Card card : VisibleCards2) {
                deck.add(new CardEvent(card.getId()));
            }
        }
        if (i == 3){
            for (Card card : VisibleCards3) {
                deck.add(new CardEvent(card.getId()));
            }
        }
        cardListnerHashMap.get(player).seeDeck(deck);
    }

}