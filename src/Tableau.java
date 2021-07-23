public class Tableau {
    public Column[] columns = new Column[7];

    public Tableau(){
        super();
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new Column();
        }
    }

    public void add(Card card, int column) {
        Card newCard = new Card(card.rank, card.suit);
        if (column >= 0 && column < 4 && columns[column].getLength() < 3) {
            newCard.faceDown = true;
        }
        columns[column].add(newCard);
    }
}
