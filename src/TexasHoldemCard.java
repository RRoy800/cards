import processing.core.PApplet;
import processing.core.PImage;

public class TexasHoldemCard extends Card {
    PImage bimage;

    public TexasHoldemCard(String value, String suit, PImage img, PImage bimg) {
        super(value, suit, img);
        this.bimage = bimg;

    }

    @Override
    public void draw(PApplet sketch) {
        super.draw(sketch);
        if (turned && bimage != null)
            sketch.image(bimage, x, y, width, height);

    }
}
