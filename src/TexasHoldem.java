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
    Random x;
    Random d;
    int willcomputerbid;
    int willcomputerfold;
    int whatwillcompdo;

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
    boolean isGameOver;
    ClickableRectangle startButton;

    int raiseAmount = 5;
    boolean adjustingRaise = false;
    int raiseIncrement = 5;
    int currentPlayerBet = 0;

    boolean gamestart = false;

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

        startButton = new ClickableRectangle();
        startButton.width = 160;
        startButton.height = 50;
        startButton.x = 420;
        startButton.y = 260;

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
        isGameOver = false;
        // computerMoney = computerMoney - 10; // TODO: Add anti animation/BUTTON
        // playerMoney = playerMoney - 10;
        // potMoney = potMoney + 20;
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
        ArrayList<Card> playeroneinterimhand = new ArrayList<>();
        playeroneinterimhand.addAll(playerOneHand.getCards());
        playeroneinterimhand.addAll(centerCards.getCards());
        ArrayList<Card> playertwointerimhand = new ArrayList<>();
        playertwointerimhand.addAll(playerTwoHand.getCards());
        playertwointerimhand.addAll(centerCards.getCards());
        PokerHandEvaluator.HandResult playeroneres = PokerHandEvaluator.evaluate(playeroneinterimhand);
        PokerHandEvaluator.HandResult playertwores = PokerHandEvaluator.evaluate(playertwointerimhand);
        if (playeroneres.compareTo(playertwores) < 0) {
            if (currentPlayerBet > 0) {
                int callAmount = Math.min(currentPlayerBet - computerBid, computerMoney);
                computerMoney -= callAmount;
                potMoney += callAmount;
                currentPlayerBet = 0;
                computerBid = 0;
                switchTurns();
            } else {
                z = new Random();
                willcomputerbid = z.nextInt(10);
                if (willcomputerbid >= 1) {
                    r = new Random();
                    if (computerMoney >= 40) {
                        computerBid = (1 + r.nextInt(20)) * 5;
                    } else {
                        computerBid = (5 * (1 + r.nextInt(computerMoney - 1)));
                    }
                    computerMoney = computerMoney - computerBid;
                    potMoney = potMoney + computerBid;

                    switchTurns();
                    switchTurns();
                } else {
                    playerMoney = playerMoney + potMoney;
                    potMoney = 0;
                    switchTurns();
                    switchTurns();
                    initializeGame();
                }
            }
        } else {
            if (currentPlayerBet > 0) {
                d = new Random();
                whatwillcompdo = d.nextInt(6);
                if (whatwillcompdo > 3) {
                    playerMoney = playerMoney + potMoney;
                    potMoney = 0;
                    switchTurns();
                    switchTurns();
                    initializeGame();
                } else {
                    int callAmount = Math.min(currentPlayerBet - computerBid, computerMoney);
                    computerMoney -= callAmount;
                    potMoney += callAmount;
                    currentPlayerBet = 0;
                    computerBid = 0;
                    switchTurns();
                    return;
                }
            } else {
                switchTurns();
            }
        }

    }

    public void handleplayerTurn() {
        switchTurns();
    }

    public void handledealerTurn() {
        if (numturns == 0) {
            for (int i = 0; i < 3; i++) {
                Card card = dealer.getCard(0);
                card.setTurned(false);
                playCard(card, dealer);
            }
            numturns = numturns + 1;
            switchTurns();
        } else if (numturns == 1 || numturns == 2) {
            Card card = dealer.getCard(0);
            card.setTurned(false);
            playCard(card, dealer);
            numturns = numturns + 1;
            switchTurns();
        } else {
            isGameOver = true;
            return;
        }

    }

    public boolean IsGameOver() {
        return isGameOver;
    }

    public String getWinner() {
        ArrayList<Card> playeronefinalhand = new ArrayList<>();
        playeronefinalhand.addAll(playerOneHand.getCards());
        playeronefinalhand.addAll(centerCards.getCards());
        ArrayList<Card> playertwofinalhand = new ArrayList<>();
        playertwofinalhand.addAll(playerTwoHand.getCards());
        playertwofinalhand.addAll(centerCards.getCards());
        PokerHandEvaluator.HandResult playeroneresults = PokerHandEvaluator.evaluate(playeronefinalhand);
        PokerHandEvaluator.HandResult playertworesults = PokerHandEvaluator.evaluate(playertwofinalhand);
        if (playeroneresults.compareTo(playertworesults) < 0) {
            return "Player Two";
        }
        return "Player One";
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
                adjustingRaise = false;
                currentPlayerBet = 0;
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
            gamestart = false;
        }

    }

    public void handleCallButtonClick(int mouseX, int mouseY) {
        if (callButton.isClicked(mouseX, mouseY) && getCurrentPlayer().equals("Player One")) {
            if (computerBid > 0 && playerMoney >= computerBid) {
                playerMoney = playerMoney - computerBid;
                potMoney = potMoney + computerBid;
                computerBid = 0;
                adjustingRaise = false;
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
                if (playerMoney >= raiseAmount && raiseAmount >= computerBid) {
                    currentPlayerBet = raiseAmount;
                    playerMoney -= raiseAmount;
                    potMoney += raiseAmount;
                    adjustingRaise = false;
                    raiseAmount = 5;
                    switchTurns();
                } else {
                    return;
                }
            }
        }
    }

    public void handleStartButtonClick(int mouseX, int mouseY) {
        if (startButton.isClicked(mouseX, mouseY)) {
            gamestart = true;
            computerMoney -= 10;
            playerMoney -= 10;
            potMoney += 20;
        }
    }

}
