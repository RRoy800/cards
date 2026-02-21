import processing.core.PApplet;

public class TexasHoldemApp extends PApplet {
TexasHoldem cardGame = new TexasHoldem();
    private int timer;

    public static void main(String[] args) {
        PApplet.main("TexasHoldemApp");
    }
    @Override
    public void settings() {
        size(1000, 600);   
    }

    @Override
    public void draw() {
        background(255);
        // Draw player hands
        for (int i = 0; i < cardGame.playerOneHand.getSize(); i++) {
            Card card = cardGame.playerOneHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }
        // Draw computer hand
        for (int i = 0; i < cardGame.playerTwoHand.getSize(); i++) {
            Card card = cardGame.playerTwoHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        //draw dealer hand
        for (int i = 0; i < cardGame.dealer.getSize(); i++) {
            Card card = cardGame.dealer.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        //draw center hand
         for (int i = 0; i < cardGame.centerCards.getSize(); i++) {
            Card card = cardGame.centerCards.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }

        // Display deck size
        text("Deck Size: " + cardGame.getDeckSize(), width / 2,
                height - 20);
        
        
        if (cardGame.getCurrentPlayer() == "Player Two") {
            fill(0);
            textSize(16);
            text("Computer is thinking...", width / 2 - 80, height / 2 + 80);
            timer++;
            if (timer == 100) {
                cardGame.handleComputerTurn();
                timer = 0;
            }
        }

        if (cardGame.getCurrentPlayer() == "Dealer") {
            fill(0);
            textSize(16);
            text("Dealer is thinking...", width / 2 - 80, height / 2 + 80);
            timer++;
            if (timer == 100) {
                cardGame.handleComputerTurn();
                timer = 0;
            }
        }

        cardGame.drawChoices(this);
    }

}
