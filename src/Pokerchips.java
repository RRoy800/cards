public class Pokerchips {

    public int chips25 = 2;  // number of $25 chips
    public int chips10 = 3;  // number of $10 chips
    public int chips5 = 4;   // number of $5 chips

    // Remove chips for a bet
    public void RaiseBet(int raiseAmount) {
        int remaining = raiseAmount;

        // 25 dollar chips - Use first
        while (remaining >= 25 && chips25 > 0) {
            chips25 = chips25 - 1;  
            remaining = remaining - 25;
        }

        // 20 dollar chips - use second
        while (remaining >= 10 && chips10 > 0) {
            chips10 = chips10 - 1;
            remaining = remaining - 10;
        }

        // 5 dollar chips - use third 
        while (remaining >= 5 && chips5 > 0) {
            chips5 = chips5 - 1;
            remaining = remaining - 5;
        }
    }

public void AddChips(int amount) {
    int remaining = amount;

    while (remaining >= 25) {
        chips25 = chips25 + 1;  
        remaining -= 25;
    }

    while (remaining >= 10) {
        chips10 = chips10 + 1;
        remaining -= 10;
    }

    while (remaining >= 5) {
        chips5 = chips5 + 1;
        remaining -= 5;
    }
}


public void resetChips(int money) {
    chips25 = 0;
    chips10 = 0;
    chips5 = 0;
    
    int remaining = money;
    while (remaining >= 25) {
        chips25++;
        remaining -= 25;
    }
    while (remaining >= 10) {
        chips10++;
        remaining -= 10;
    }
    while (remaining >= 5) {
        chips5++;
        remaining -= 5;
    }
}


}









