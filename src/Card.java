public class Card {
    public Card nextChoice;
    public String rank, suit;
    public boolean faceDown;

    public Card next;
    public Card prev;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        this.faceDown = false;
    }
}
