import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

public class TexasHoldemApp extends PApplet {
    TexasHoldem cardGame;
    HashMap<String, PImage> cardImages = new HashMap<>();
    private int timer;

    public static void main(String[] args) {
        PApplet.main("TexasHoldemApp");
    }

    @Override
    public void settings() {
        size(1000, 600);
        for(String rank : TexasHoldem.Rank){
            for(String suit : TexasHoldem.Suit){
                cardImages.put(rank + suit.toLowerCase(), loadImage("Data/" + rank + suit.toLowerCase() + ".png"));
            }
        }
        cardImages.put("cardback", loadImage("Data/cardback.png"));
        cardGame = new TexasHoldem(cardImages);
    }

    @Override
    public void draw() {
        background(0, 122, 24);
        // check button
        fill(200);
        cardGame.checkButton.draw(this);
        cardGame.raiseButton.draw(this);
        cardGame.foldButton.draw(this);
        cardGame.callButton.draw(this);
        fill(0);
        textAlign(CENTER, CENTER);
        text("Check", cardGame.checkButton.x + cardGame.checkButton.width / 2,
                cardGame.checkButton.y + cardGame.checkButton.height / 2);
        text("Raise", cardGame.raiseButton.x + cardGame.raiseButton.width / 2,
                cardGame.raiseButton.y + cardGame.raiseButton.height / 2);
        text("Fold", cardGame.foldButton.x + cardGame.foldButton.width / 2,
                cardGame.foldButton.y + cardGame.foldButton.height / 2);
        text("Call", cardGame.callButton.x + cardGame.callButton.width / 2,
                cardGame.callButton.y + cardGame.callButton.height / 2);

        // Draw player hands
        text("You have $" + cardGame.playerMoney, 50, 590);
        for (int i = 0; i < cardGame.playerOneHand.getSize(); i++) {
            Card card = cardGame.playerOneHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }
        // Draw computer hand
        text("The Computer has $" + cardGame.computerMoney, 450, 590);
        for (int i = 0; i < cardGame.playerTwoHand.getSize(); i++) {
            Card card = cardGame.playerTwoHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        // draw dealer hand
         text("The Pot has $" + cardGame.potMoney, 500, 110);
        for (int i = 0; i < cardGame.dealer.getSize(); i++) {
            Card card = cardGame.dealer.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        // draw center hand
        for (int i = 0; i < cardGame.centerCards.getSize(); i++) {
            Card card = cardGame.centerCards.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        // Display deck size
        text("Deck Size: " + cardGame.getDeckSize(), 500,
                10);

        if (cardGame.getCurrentPlayer().equals("Player Two")) {
            fill(0);
            textSize(16);
            text("Computer is thinking...", width / 2 - 80, height / 2 + 80);
            timer++;
            if (timer == 100) {
                cardGame.handleComputerTurn();
                timer = 0;
            }
        }

        if (cardGame.getCurrentPlayer().equals("Dealer")) {
            fill(0);
            textSize(16);
            text("Dealer is thinking...", width / 2 - 80, height / 2 + 80);
            timer++;
            if (timer == 100) {
                cardGame.handledealerTurn();
                timer = 0;
            }
        }

        cardGame.drawChoices(this);
    }
     
    @Override
    public void mousePressed() {
        cardGame.handleCheckButtonClick(mouseX, mouseY);
        cardGame.handleFoldButtonClick(mouseX, mouseY);
        cardGame.handleRaiseButtonClick(mouseX, mouseY);
        cardGame.handleCallButtonClick(mouseX, mouseY);
    }

}
