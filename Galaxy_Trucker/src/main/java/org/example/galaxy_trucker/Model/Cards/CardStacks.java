package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.GAGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardStacks {
    private int level;
    private ArrayList<Card> VisibleCards1;
    private ArrayList<Card> VisibleCards2;
    private ArrayList<Card> VisibleCards3;
    private ArrayList<Card> HiddenCards;
    private ArrayList<Card> FullAdventure;
    private ArrayList<Card> Level1Deck;
    private ArrayList<Card> Level2Deck;
    private GAGen GaG;


    public CardStacks(GAGen Gag, int lv) throws IOException {
        //dipende da come vien creato dal json
        this.GaG=Gag;
        this.HiddenCards = new ArrayList<>();
        this.FullAdventure = new ArrayList<>();
        this.Level1Deck = new ArrayList<>();
        this.Level2Deck = new ArrayList<>();
        VisibleCards1 = new ArrayList<>();
        VisibleCards2 = new ArrayList<>();
        VisibleCards3 = new ArrayList<>();

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

        if(lv==1){
            //caso tutorial fai finta de nulla
        }
        else{
            for(int i=0;i<4;i++){
                if (i==1){
                    int curr;
                    curr= r.nextInt(Level1Deck.size());
                    VisibleCards1.add(Level1Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards1.add(Level2Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards1.add(Level2Deck.remove(curr));

                }
                else if (i==2){
                    int curr;
                    curr= r.nextInt(Level1Deck.size());
                    VisibleCards2.add(Level1Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards2.add(Level2Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards2.add(Level2Deck.remove(curr));
                }
                else if (i==3){
                    int curr;
                    curr= r.nextInt(Level1Deck.size());
                    VisibleCards3.add(Level1Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards3.add(Level2Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    VisibleCards3.add(Level2Deck.remove(curr));
                }
                else{
                    int curr;
                    curr= r.nextInt(Level1Deck.size());
                    HiddenCards.add(Level1Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    HiddenCards.add(Level2Deck.remove(curr));
                    curr= r.nextInt(Level2Deck.size());
                    HiddenCards.add(Level2Deck.remove(curr));
                }


            }
        }
    }

    public void mergeDecks(){
        FullAdventure.addAll(VisibleCards1);
        FullAdventure.addAll(VisibleCards2);
        FullAdventure.addAll(VisibleCards3);
        FullAdventure.addAll(HiddenCards);

        Collections.shuffle(FullAdventure);
        while(FullAdventure.get(0).getLevel() != level){
            Collections.shuffle(FullAdventure);
        }
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

}