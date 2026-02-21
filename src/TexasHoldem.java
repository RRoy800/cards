import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;

public class TexasHoldem extends CardGame {

    static String[] Suit = { "Hearts", "Diamonds", "Clubs", "Spades" };
    static String[] Rank = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    Hand dealer;
    private int dealernumcards = 5;
    Hand centerCards;
    int playerturn = 0;

    public TexasHoldem() {
        initializeGame();
        dealCards(2);

        playerOneHand.positionCards(50, 450, 80, 120, 20);
        playerTwoHand.positionCards(500, 450, 80, 120, 20);
        dealer.positionCards(50, 50, 80, 120, 20);
    }

    @Override
    protected void createDeck() {
        // Create deck (Poker has 52 cards)
        // Create standard cards (Numbers 2-10, Jack, Queen, King, Ace with 4 respective
        // suits)
        for (String suit : Suit) {

            for (String rank : Rank) {
                deck.add(createCard(suit, rank));

            }
        }
    }

    private Card createCard(String suit, String rank) {
        Card card = new Card(suit, rank);
        card.suit = suit;
        card.value = rank;
        return card;

    }

    @Override
    protected void initializeGame() {
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        playerOneHand = new Hand();
        playerTwoHand = new Hand();
        centerCards = new Hand();
        dealer = new Hand();
        gameActive = true;

        createDeck();
      

    }

    @Override
    protected void dealCards(int numCards) {
        Collections.shuffle(deck);
        for (int i = 0; i < numCards; i++) {
            playerOneHand.addCard(deck.remove(0));
            Card card = deck.remove(0);
            card.setTurned(true);
            playerTwoHand.addCard(card);

        }
        for (int i = 0; i < dealernumcards; i++) {
            Card card = deck.remove(0);
            card.setTurned(true);
            dealer.addCard(card);

        }
    }

    @Override
    public void handleComputerTurn() {
        //TODO: add logic here
        switchTurns();
    }

    public void handledealerTurn() {
        Card card = dealer.getCard(0);
        card.setTurned(false);
        playCard(card, dealer);
        //TODO: Make it so multiple cards can be played depending on the turn
        switchTurns();
    }

    @Override
     public boolean playCard(Card card, Hand hand) {
     if (super.playCard(card, hand)){
        centerCards.addCard(card);
        card.setTurned(false);
        return true;
     }
     return false;

     }

     @Override
    public void switchTurns() {
    playerturn += 1 % 3;
    playerOneHand.positionCards(50, 450, 80, 120, 20);
    playerTwoHand.positionCards(50, 50, 80, 120, 20);
    dealer.positionCards(50, 50, 80, 120, 20);
    centerCards.positionCards(200, 200, 80, 120, 90);
    }

    @Override
    public String getCurrentPlayer() {
        if (playerturn == 2){
            return "Player One";
        }else if (playerturn == 0){
            return "Player Two";
        }else 
            return "Dealer";
        
    }

}
