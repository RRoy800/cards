import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import processing.core.PImage;

public class TexasHoldem extends CardGame {

    static String[] Suit = { "Hearts", "Diamonds", "Clubs", "Spades" };
    static String[] Rank = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    Hand dealer;
    private int dealernumcards = 5;
    Hand centerCards;
    int playerturn = 0;
    private int numturns = 0;
    int playerMoney;
    int computerMoney;
    int potMoney;
    Random r;
    int computerBid;
    Random z;
    int willcomputerbid;

    ClickableRectangle checkButton;
    ClickableRectangle raiseButton;
    ClickableRectangle callButton;
    ClickableRectangle foldButton;
    int ButtonX = 170;
    int startButtonY = 420;
    int buttonSpacer = 40;
    int ButtonWidth = 100;
    int ButtonHeight = 35;
    HashMap<String, PImage> cardImages;

    ClickableRectangle plusButton;
    ClickableRectangle minusButton;
    ClickableRectangle confirmRaiseButton;

    int raiseAmount = 5; 
    boolean adjustingRaise = false; 
    int raiseIncrement = 5; 
    int currentPlayerBet = 0;

    public TexasHoldem(HashMap<String, PImage> cardImages) {
        this.cardImages = cardImages;
        initializeGame();

        checkButton = new ClickableRectangle();
        checkButton.x = ButtonX;
        checkButton.y = startButtonY;
        checkButton.width = ButtonWidth;
        checkButton.height = ButtonHeight;

        callButton = new ClickableRectangle();
        callButton.x = ButtonX;
        callButton.y = startButtonY + buttonSpacer;
        callButton.width = ButtonWidth;
        callButton.height = ButtonHeight;

        raiseButton = new ClickableRectangle();
        raiseButton.x = ButtonX;
        raiseButton.y = startButtonY + 2 * buttonSpacer;
        raiseButton.width = ButtonWidth;
        raiseButton.height = ButtonHeight;

        foldButton = new ClickableRectangle();
        foldButton.x = ButtonX;
        foldButton.y = startButtonY + 3 * buttonSpacer;
        foldButton.width = ButtonWidth;
        foldButton.height = ButtonHeight;

        playerMoney = 1000;
        computerMoney = 1000;

        //
        plusButton = new ClickableRectangle();
        plusButton.width = 40;
        plusButton.height = 40;
        plusButton.x = ButtonX + ButtonWidth + 10; 
        plusButton.y = raiseButton.y;

        minusButton = new ClickableRectangle();
        minusButton.width = 40;
        minusButton.height = 40;
        minusButton.x = ButtonX + ButtonWidth + 10;
        minusButton.y = raiseButton.y + 50;

        confirmRaiseButton = new ClickableRectangle();
        confirmRaiseButton.width = 100;
        confirmRaiseButton.height = 40;
        confirmRaiseButton.x = plusButton.x + 50;
        confirmRaiseButton.y = plusButton.y - 100;
        //
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
        Card card = new TexasHoldemCard(suit, rank, cardImages.get(rank + suit.toLowerCase()),
                cardImages.get("cardback"));
        card.suit = suit;
        card.value = rank;
        return card;

    }

    @Override
    protected void initializeGame() {
        if (cardImages == null) {
            return;
        }
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        playerOneHand = new Hand();
        playerTwoHand = new Hand();
        centerCards = new Hand();
        dealer = new Hand();
        gameActive = true;

        numturns = 0;
        createDeck();
        dealCards(2);

        playerOneHand.positionCards(50, 450, 80, 120, 20);
        playerTwoHand.positionCards(500, 450, 80, 120, 20);
        dealer.positionCards(50, 50, 80, 120, 20);

    }

    @Override
    protected void dealCards(int numCards) {
        if (cardImages == null) {
            return;
        }
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
        if (currentPlayerBet > 0) {
            int callAmount = Math.min(currentPlayerBet, computerMoney); 
            computerMoney -= callAmount;
            potMoney += callAmount;
            currentPlayerBet = 0;
            switchTurns();
        } else {
            z = new Random(); // TODO: Make it so this prints what the Computer is doing
            willcomputerbid = z.nextInt(4);
            if (willcomputerbid >= 1) {
                r = new Random();
                computerBid = 1 + r.nextInt(30);
                computerMoney = computerMoney - computerBid;
                potMoney = potMoney + computerBid;

                switchTurns();
                switchTurns();
            } else {
                switchTurns();
            }
        }
    }

    public void handleplayerTurn() { // Not Used
        switchTurns();
    }

    public void handledealerTurn() {
        if (numturns == 0) {
            for (int i = 0; i < 3; i++) {
                Card card = dealer.getCard(0);
                card.setTurned(false);
                playCard(card, dealer);
            }
        } else {
            Card card = dealer.getCard(0);
            card.setTurned(false);
            playCard(card, dealer);
        }
        numturns = numturns + 1;
        switchTurns();
    }

    @Override
    public boolean playCard(Card card, Hand hand) {
        hand.removeCard(card);
        card.setTurned(false);
        discardPile.add(card);
        centerCards.addCard(card);
        return true;
    }

    @Override
    public void switchTurns() {
        playerturn = (playerturn + 1) % 3;
        playerOneHand.positionCards(50, 450, 80, 120, 20);
        playerTwoHand.positionCards(500, 450, 80, 120, 20);
        dealer.positionCards(50, 50, 80, 120, 20);
        centerCards.positionCards(200, 200, 80, 120, 90);
    }

    @Override
    public String getCurrentPlayer() {
        if (playerturn == 0) {
            return "Player One";
        } else if (playerturn == 1) {
            return "Player Two";
        } else
            return "Dealer";

    }

    public void handleCheckButtonClick(int mouseX, int mouseY) {
        if (checkButton.isClicked(mouseX, mouseY) && getCurrentPlayer().equals("Player One")) {
            if (computerBid == 0) {
                switchTurns();
            }
        }

    }

    public void handleFoldButtonClick(int mouseX, int mouseY) {
        if (foldButton.isClicked(mouseX, mouseY) && getCurrentPlayer().equals("Player One")) {
            computerMoney += potMoney;
            adjustingRaise = false;
            potMoney = 0;
            initializeGame();
        }

    }

    public void handleCallButtonClick(int mouseX, int mouseY) {
        if (callButton.isClicked(mouseX, mouseY) && getCurrentPlayer().equals("Player One")) {
            if (computerBid > 0 && playerMoney >= computerBid) {
                playerMoney = playerMoney - computerBid;
                potMoney = potMoney + computerBid;
                computerBid = 0;
                switchTurns();
                switchTurns();
            }
        }

    }

    public void handleRaiseButtonClick(int mouseX, int mouseY) {
        if (raiseButton.isClicked(mouseX, mouseY) && getCurrentPlayer().equals("Player One")) {
            adjustingRaise = true;
        }

        if (adjustingRaise) {

            if (plusButton.isClicked(mouseX, mouseY) && playerMoney >= raiseAmount + raiseIncrement) {
                raiseAmount += raiseIncrement;
            }

            if (minusButton.isClicked(mouseX, mouseY) && raiseAmount - raiseIncrement >= 5) {
                raiseAmount -= raiseIncrement;
            }

            if (confirmRaiseButton.isClicked(mouseX, mouseY)) {
                if (playerMoney >= raiseAmount) {
                    currentPlayerBet = raiseAmount;
                    playerMoney -= raiseAmount;
                    potMoney += raiseAmount;
                    adjustingRaise = false;
                    raiseAmount = 5;
                    switchTurns();
                }
            }
        }
    }

}
