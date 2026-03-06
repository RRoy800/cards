import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

public class TexasHoldemApp extends PApplet {
    TexasHoldem cardGame;
   
   //Card Images: https://code.google.com/archive/p/vector-playing-cards/downloads
    HashMap<String, PImage> cardImages = new HashMap<>();
    private int timer;
   
   //Chip images: https://www.pngaaa.com/detail/1408506#google_vignette
    PImage chip5Img;
    PImage chip10Img;
    PImage chip25Img;
    Pokerchips playerChips;
    Pokerchips computerChips;

    public static void main(String[] args) {
        PApplet.main("TexasHoldemApp");
    }

    @Override
    public void settings() {
        size(1000, 600);
        for (String rank : TexasHoldem.Rank) {
            for (String suit : TexasHoldem.Suit) {
                cardImages.put(rank + suit.toLowerCase(), loadImage("Data/" + rank + suit.toLowerCase() + ".png"));
            }
        }
        cardImages.put("cardback", loadImage("Data/cardback.png"));
        cardGame = new TexasHoldem(cardImages);
   
   
    chip5Img = loadImage("Data/5chip.png");
    chip10Img = loadImage("Data/10chip.png");
    chip25Img = loadImage("Data/25chip.png");

    playerChips = new Pokerchips();
    computerChips = new Pokerchips();

cardGame.playerChips = playerChips;
cardGame.computerChips = computerChips;
   
    }

    @Override
    public void draw() {
        if (cardGame.gamestart == false) {
            background(0, 122, 24);
            fill(200);
            cardGame.startButton.draw(this);

            fill(0);
            textSize(20);
            textAlign(CENTER, CENTER);
            text("Start Next Round",
                    cardGame.startButton.x + cardGame.startButton.width / 2,
                    cardGame.startButton.y - 10 + cardGame.startButton.height / 2);

            textSize(10);
            text("(This will also pay your anti)",
                    cardGame.startButton.x + cardGame.startButton.width / 2,
                    cardGame.startButton.y + 10 + cardGame.startButton.height / 2);
            textSize(16);
             
            if (cardGame.whofolded.equals("Computer") || cardGame.whofolded.equals("Player One")) {
                text(cardGame.whofolded + " folded. The Money has been transferred", cardGame.startButton.x + cardGame.startButton.width / 2, cardGame.startButton.y - 200 + cardGame.startButton.height / 2);
            }

        } else {
            if (!cardGame.IsGameOver()) {
                drawscreen();
                cardGame.whofolded = "No one";

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

            } else {
                for (int i = 0; i < cardGame.playerTwoHand.getSize(); i++) {
                    Card card = cardGame.playerTwoHand.getCard(i);
                    card.setTurned(false);

                }

            timer++;
            if (timer == 200) {
                cardGame.handledealerTurn();
                String winner = cardGame.getWinner();
                drawscreen();
                fill(0);
                textSize(20);
                text("The Winner is " + winner + "!", width / 2 - 80, height / 2 + 80);
            }
            if (timer == 600) {
                String winner = cardGame.getWinner();
                if (winner.equals("Player One")) {
                    cardGame.playerMoney += cardGame.potMoney;
                    cardGame.potMoney = 0;
                } else {
                    cardGame.computerMoney += cardGame.potMoney;
                    cardGame.potMoney = 0;

                    }
                    timer = 0;
                    cardGame.switchTurns();
                    cardGame.initializeGame();
                    cardGame.gamestart = false;
                }
            }
        }
    }

    @Override
    public void mousePressed() {
        if (cardGame.gamestart == false) {
            cardGame.handleStartButtonClick(mouseX, mouseY);
        } else {
            cardGame.handleCheckButtonClick(mouseX, mouseY);
            cardGame.handleFoldButtonClick(mouseX, mouseY);
            cardGame.handleRaiseButtonClick(mouseX, mouseY);
            cardGame.handleCallButtonClick(mouseX, mouseY);
        }
    }

    public void drawscreen() {
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

        if (cardGame.adjustingRaise) {
            fill(200);
            cardGame.plusButton.draw(this);
            cardGame.minusButton.draw(this);
            cardGame.confirmRaiseButton.draw(this);

            fill(0);
            text("+", cardGame.plusButton.x + cardGame.plusButton.width / 2,
                    cardGame.plusButton.y + cardGame.plusButton.height / 2);
            text("-", cardGame.minusButton.x + cardGame.minusButton.width / 2,
                    cardGame.minusButton.y + cardGame.minusButton.height / 2);
            text("Bet $" + cardGame.raiseAmount,
                    cardGame.confirmRaiseButton.x + cardGame.confirmRaiseButton.width / 2,
                    cardGame.confirmRaiseButton.y + cardGame.confirmRaiseButton.height / 2);
        }


        ///Draw Chips
        // Player Chips
for (int i = 0; i < playerChips.chips25; i++) {
    image(chip25Img, 120, 340 - i * 40, 50, 50);
}
for (int i = 0; i < playerChips.chips10; i++) {
    image(chip10Img, 60, 340 - i * 40, 70, 50);
}
for (int i = 0; i < playerChips.chips5; i++) {
    image(chip5Img, 20, 340 - i * 40, 50, 50);
}

// Computer Chips
for (int i = 0; i < computerChips.chips25; i++) {
    image(chip25Img, 850, 500 - i * 40, 50, 50);
}
for (int i = 0; i < computerChips.chips10; i++) {
    image(chip10Img, 890, 500 - i * 40, 70, 50);
}
for (int i = 0; i < computerChips.chips5; i++) {
    image(chip5Img, 950, 500 - i * 40, 50, 50);
}

        // Draw player hands
        text("You have $" + cardGame.playerMoney, 60, 585);
        for (int i = 0; i < cardGame.playerOneHand.getSize(); i++) {
            Card card = cardGame.playerOneHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }
        // Draw computer hand
        text("The Computer has $" + cardGame.computerMoney, 460, 585);
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
    }

}