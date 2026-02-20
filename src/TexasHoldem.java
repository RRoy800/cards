import processing.core.PApplet;

public class TexasHoldem extends CardGame {

    static String[] Suit = { "Hearts", "Diamonds", "Clubs", "Spades" };
    static String[] Rank = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

    public TexasHoldem() {
        initializeGame();
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

    private Card createCard(String suit, String rank){ 
        Card card = new Card(suit, rank); 
         card.suit = suit;
        card.value = rank;
        return card;
    
    }

    @Override

    protected void initializeGame() {
        super.initializeGame();
        dealCards(2); // each player gets 2 cards
    }
}
