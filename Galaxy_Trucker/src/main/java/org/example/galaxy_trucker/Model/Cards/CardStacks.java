package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Controller.Listeners.CardListner;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.GAGen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * The CardStacks class represents a collection of card stacks and decks used
 * for game mechanics, such as visible and hidden cards, and allows for card
 * management and game level adjustments. It also supports registering listeners
 * for player-based actions and notifying them during specific events.
 *
 * This class implements the Serializable interface, ensuring that instances
 * can be serialized for persistence or network transmission.
 */
public class CardStacks implements Serializable {
    /**
     * Represents the current level of the card stack or game.
     * This variable is used to determine the progress or state in the gameplay.
     */
    private int level;
    /**
     * Represents the first visible card stack in the CardStacks class.
     * This ArrayList holds `Card` objects that are currently visible and accessible
     * for operations in the game or application logic.
     */
    private ArrayList<Card> VisibleCards1;
    /**
     * A list of Card objects representing the second set of visible cards in the CardStacks class.
     * This field is used to manage and track the cards currently visible in the second set
     * during card stack operations.
     */
    private ArrayList<Card> VisibleCards2;
    /**
     * Stores the third set of visible cards in the card stack.
     * This list is part of the card stack management structure and is used to keep track of cards
     * that are currently visible and accessible in a specific section or phase of the game.
     */
    private ArrayList<Card> VisibleCards3;
    /**
     * Represents a collection of cards that are hidden from view and not immediately accessible or visible,
     * typically used in the context of managing card stacks within a game or application logic.
     * The contents of this list remain concealed until they are moved to a visible collection or explicitly accessed.
     */
    private ArrayList<Card> HiddenCards;
    /**
     * Represents the complete set of cards included in the "Full Adventure" deck.
     * This collection typically contains all the cards to be used during the adventure,
     * encompassing both the visible and hidden card decks.
     */
    private ArrayList<Card> FullAdventure; //prendi questo bro
    /**
     * Represents a deck of level 1 cards in a card-based system.
     * This collection is used to store and manage cards categorized under level 1.
     * It is primarily intended for internal tracking and game logic within the CardStacks class.
     */
    private ArrayList<Card> Level1Deck;
    /**
     * Represents a deck of Level 2 cards in the CardStacks class.
     * This deck is managed as an ArrayList of Card objects and is
     * used to store and retrieve cards specifically designated for Level 2.
     *
     * It is a part of the card management system within the CardStacks class,
     * which organizes cards into levels and handles operations related to
     * the gameplay and card interactions.
     */
    private ArrayList<Card> Level2Deck;
    /**
     * Represents an instance of the GAGen class, which is responsible for loading,
     * managing, and providing access to decks of tiles and cards used within the
     * application's CardStacks system.
     *
     * This variable serves as the central mechanism to manage the game's card and
     * tile data, allowing operations such as retrieval, merging, and conversion of
     * decks to distinct sets. It is initialized through``` thejava Card
     Stacks/**
     class * constructor Represents.
     an */
    private GAGen GaG;
    /**
     * A HashMap that associates player identifiers with their corresponding CardListner instances.
     * This mapping allows for event-driven communication where specific listeners can be
     * notified about card-related actions or changes on a per-player basis.
     *
     * Key: A String representing the unique identifier for a player.
     * Value: A CardListner instance that handles events related to card operations for the associated player.
     */
    private HashMap<String, CardListner> cardListnerHashMap;


    /**
     * Retrieves the GAGen instance associated with this CardStacks object.
     *
     * @return the GAGen instance representing the deck and card management utility.
     */
    public GAGen getGaG(){
        return GaG;
    }

    /**
     * Constructs a new CardStacks object, initializing various internal lists and structures
     * based on the specified level and the provided GAGen instance. Depending on the level,
     * the method distributes cards into visible and hidden decks for the game.
     *
     * @param Gag An instance of GAGen, used to retrieve the deck of cards for initialization.
     * @param lv An integer specifying the level for the CardStacks. Determines how the decks
     *           are split into visible and hidden card groups.
     */
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
            HiddenCards.clear();
            HiddenCards.add(Gag.getCardsDeck().get(2));
            HiddenCards.add(Gag.getCardsDeck().get(7));
            HiddenCards.add(Gag.getCardsDeck().get(11));
            HiddenCards.add(Gag.getCardsDeck().get(14));
            HiddenCards.add(Gag.getCardsDeck().get(35)); //REMEMBER
//            HiddenCards.add(Gag.getCardsDeck().get(36)); //REMEMBER
            HiddenCards.add(Gag.getCardsDeck().get(21));
            HiddenCards.add(Gag.getCardsDeck().get(37));
            HiddenCards.add(Gag.getCardsDeck().get(29));

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


    /**
     * Combines multiple card decks into one unified deck based on the current level,
     * ensuring the first card in the shuffled deck matches the required level.
     *
     * This method merges visible card decks (VisibleCards1, VisibleCards2, VisibleCards3)
     * into the FullAdventure deck when the level is set to 2. Regardless of level,
     * the hidden deck (HiddenCards) is always added to the FullAdventure deck.
     * Finally, the deck is shuffled, with a check ensuring the first card of the deck
     * matches the defined level.
     *
     * The merging behavior is influenced by the level field:
     * - If the level is 2, all three visible card decks are included in the merge.
     * - Hidden cards are always included regardless of the level.
     *
     * After constructing the deck, repeated shuffling is performed until the
     * first card in the deck matches the specified level.
     *
     * This method modifies the state of FullAdventure.
     */
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

    /**
     * Retrieves the full collection of adventure cards available in the current card stack.
     *
     * @return an ArrayList containing all the cards in the FullAdventure collection.
     */
    public ArrayList<Card> getFullAdventure(){
        return FullAdventure;
    }

    /**
     * Picks and returns the next card from the FullAdventure stack.
     *
     * @return the next Card object from the FullAdventure collection.
     */
    public Card PickNewCard(){
        return FullAdventure.removeFirst();
    }

    /**
     * Adds a listener for the specified player. The listener will handle card-related events for that player.
     *
     * @param player   the name or identifier of the player to associate with the listener
     * @param listener the CardListener instance to handle events related to the player's cards
     */
    public void addListener(String player, CardListner listener){
        cardListnerHashMap.putIfAbsent(player,listener);
    }

    /**
     * Removes a listener associated with the specified player from the card listener tracking system.
     *
     * @param player the identifier of the player whose listener should be removed
     */
    public void removeListener(String player){
        cardListnerHashMap.remove(player);
    }

    /**
     * Notifies the specified player with the deck of visible cards based on the provided deck identifier.
     * Depending on the level and the deck identifier, this method gathers relevant card data and sends
     * a DeckEvent to the player's associated listener. If the input is invalid, or the player is not
     * permitted to access the requested deck, an exception is thrown.
     *
     * @param player the name or identifier of the player to notify
     * @param i the number representing the specific visible card deck (1, 2, or 3)
     * @throws InvalidInput if the deck identifier is out of range or the player is restricted by level
     */
    public void notify(String player, int i) {
        if (i > 3 || i < 1){
            throw new InvalidInput("Selected deck doesnt exists or you are not allow to see it");
        }
        if( level==1){
            throw new InvalidInput("You are not allowed to see the cards in the tutorial");
        }
        ArrayList<Integer> ids = new ArrayList<>();
        if (i == 1){
            for (Card card : VisibleCards1) {
                ids.add(card.getId());
            }
        }
        if (i == 2){
            for (Card card : VisibleCards2) {
                ids.add(card.getId());
            }
        }
        if (i == 3){
            for (Card card : VisibleCards3) {
                ids.add(card.getId());
            }
        }
        DeckEvent deck = new DeckEvent(ids);
        if(cardListnerHashMap.get(player) != null)
            cardListnerHashMap.get(player).seeDeck(deck);
    }

}