public class LinkedList {
    // openCard refers to the bottom most card on each column
    public Card firstCard, lastCard;
    public LinkedList() {

    }
    // public LinkedList()
    public int getLength() {
        if (firstCard == null) {
            return 0;
        } else {
            int i = 1;
            Card runner = firstCard;
            while (runner != null && runner.next != null  ) {
                i++;
                runner = runner.next;
            }
            return i;
        }
        
    }

    // add
    public void add(Card card) {
        if (firstCard == null) {
            firstCard = card;
            lastCard = card;
        } else {
            card.prev = lastCard;
            lastCard.next = card;
            lastCard = card;
        }
    }

    
}
