public class Game {
    private Deck fullDeck = new Deck();
    private Deck tail, heartsFoundation, spadesFoundation, diamondsFoundation, clubsFoundation;
    public Tableau tableau;
    public int turn = 0;

    public Game() {}

    // setup(String input) method
    // Set up game with file-based card/deck specifications

    public void setup(String input) {
        // Create deck from input
        initFullDeck(input);

        // Set up tableau from first 49 cards of fullDeck
        initTableau();

        // Set up tail from last 3 cards of fullDeck
        initTail();

        // Set up foundations, ready to be supplied with complete runs
        initFoundations();
    }

    // Printing Methods

    // print() method
    // Show user the board

    public void print() {
        // Print Tail and Foundation

        // Tail
        if (tail.firstCard != null) {
            for (int i = 0; i < tail.getLength(); i++) {
                System.out.print("#");
            }
        } else {
            for (int i = 0; i < tail.getLength(); i++) {
                System.out.print(" ");
            }
        }
        

        // Foundation
        System.out.print("          * * * *");

        System.out.println();
        System.out.println();

        // Print Tableau

        // Find longest column
        int lCol = -1;
        for (int i = 0; i < tableau.columns.length; i++) {
            if (tableau.columns[i].getLength() > lCol) {
                lCol = tableau.columns[i].getLength();
            }
        }

        // Create array of runners to visually populate tableau row by row from accessing each of the columns
        Card[] runners = new Card[]{tableau.columns[0].firstCard, tableau.columns[1].firstCard, tableau.columns[2].firstCard, tableau.columns[3].firstCard, tableau.columns[4].firstCard, tableau.columns[5].firstCard, tableau.columns[6].firstCard};

        // Print each row
        for (int i = 0; i < lCol; i++) {

            // Prints each card from each column, if it exists
            for (int j = 0; j < runners.length; j++) {

                // Only print something if there is a card, otherwise print a blank area
                if (runners[j] != null) {
                    Card printed = runners[j];
                    if (printed.faceDown) {
                        System.out.print("## ");
                    } else {
                        System.out.print(printed.rank + printed.suit + " ");
                    }
                    runners[j] = runners[j].next;
                } else {
                    System.out.print("   ");
                }
            }

            // Print new line to separate rows
            System.out.println();
        }

        // Print new line to separate turns.
        System.out.println();
    }

    // print(int turn) method
    // show user computer playing the game by turn

    public void print(int turn) {
        // Print Tail and Foundation
        System.out.println("Turn " + ++turn);

        // Tail
        for (int i = 0; i < tail.getLength(); i++) {
            System.out.print("#");
        }

        // Foundation
        System.out.print("           ");

        if (heartsFoundation.firstCard != null && heartsFoundation.lastCard != null) {
            System.out.print("H ");
        } else {
            System.out.print("* ");
        }

        if (spadesFoundation.firstCard != null && spadesFoundation.lastCard != null) {
            System.out.print("S ");
        } else {
            System.out.print("* ");
        }

        if (diamondsFoundation.firstCard != null && diamondsFoundation.lastCard != null) {
            System.out.print("D ");
        } else {
            System.out.print("* ");
        }

        if (clubsFoundation.firstCard != null && clubsFoundation.lastCard != null) {
            System.out.print("C ");
        } else {
            System.out.print("* ");
        }

        System.out.println();
        System.out.println();

        // Print Tableau

        // Find longest column
        int lCol = -1;
        for (int i = 0; i < tableau.columns.length; i++) {
            if (tableau.columns[i].getLength() > lCol) {
                lCol = tableau.columns[i].getLength();
            }
        }

        // Create array of runners to visually populate tableau row by row from accessing each of the columns
        Card[] runners = new Card[]{tableau.columns[0].firstCard, tableau.columns[1].firstCard, tableau.columns[2].firstCard, tableau.columns[3].firstCard, tableau.columns[4].firstCard, tableau.columns[5].firstCard, tableau.columns[6].firstCard};

        // Print each row
        for (int i = 0; i < lCol; i++) {

            // Prints each card from each column, if it exists
            for (int j = 0; j < runners.length; j++) {

                // Only print something if there is a card, otherwise print a blank area
                if (runners[j] != null) {
                    Card printed = runners[j];
                    if (printed.faceDown) {
                        System.out.print("## ");
                    } else {
                        System.out.print(printed.rank + printed.suit + " ");
                    }
                    runners[j] = runners[j].next;
                } else {
                    System.out.print("   ");
                }
            }

            // Print new line to separate rows
            System.out.println();
        }

        // Print new line to separate turns.
        System.out.println();
    }

    // ---

    // Gameplay Methods

    // moveCard(String origRank, String origSuit, String destRank, String destSuit) method
    // Handles moving a face-up Card to one of the last Cards of another Column.

    public void moveCard(String origRank, String origSuit, String destRank, String destSuit) {
        
        // Declare and initialize variables to retrieve correct Cards and the columns they are in
        Card selectedCard = new Card("", "");
        Card destinationCard = new Card("", "");
        int selectedIndex = -1;
        int destinationIndex = -1;

        // Add input validity testing
        // if (origRank)

        // Iterate through tableau to find:
        // - the Card wished to be moved (selectedCard)
        // - and the Card wished to be moved to (destinationCard)

        // Outer loop accesses each Column
        for (int i = 0; i < tableau.columns.length; i++) {

            // Retrieve first Card of each Column
            Card runner = tableau.columns[i].firstCard;

            // Inner loop accesses each Card in the Column
            for (int j = 0; j < tableau.columns[i].getLength(); j++) {

                // Check for condition where selectedCard is found
                if (origRank.equals(runner.rank) && origSuit.equals(runner.suit)) {

                    // If found, set previously initialized variables
                    selectedCard = runner;
                    selectedIndex = i;
                }
                // Check for condition where destinationCard is found
                else if (destRank.equals(runner.rank) && destSuit.equals(runner.suit)) {

                    // If found, set previously initialized variables
                    destinationCard = runner;
                    destinationIndex = i;
                }
                
                // Set runner to next Card in Column
                runner = runner.next;
            }
        }

        // Declare new Card to access Cards underneath selectedCard, which are also moved
        Card runner2 = selectedCard;

        // Iterate to potential cards underneath selectedCard by using runner2
        while (runner2 != null && runner2.next != null) {
            runner2 = runner2.next;
        }
        
        // Make required changes to Column that selectedCard and destinationCard were initially in

        // If selectedCard was not the firstCard of the Column that it was in,
        if (selectedCard.prev != null) {
            // Set the 'next' attribute of the Card that was above selectedCard to null
            selectedCard.prev.next = null;
            // Update the 'lastCard' attribute to the Card that was above selectedCard
            tableau.columns[selectedIndex].lastCard = selectedCard.prev;
        }
        // If selectedCard was the firstCard of the Column that it was in,
        else {
            // Re-initialize the Column to an empty Column.
            tableau.columns[selectedIndex] = new Column();
        }

        // Make required configurations to selectedCard, destinationCard, and the Column that destinationCard is in
        selectedCard.prev = destinationCard;
        destinationCard.next = selectedCard;
        tableau.columns[destinationIndex].lastCard = runner2;

        // Communicate what move the computer is making
        System.out.println("Move " + origRank + origSuit + " to " + destRank + destSuit + ".");
        System.out.println();
    }

    public void moveCard(String origRank, String origSuit, int destColumn) {
        
        // Declare and initialize variables to retrieve correct Card and Columns
        Card selectedCard = new Card("", "");
        int selectedIndex = -1;
        int destinationIndex = destColumn;

        // Add input validity testing
        // if (origRank)

        // Iterate through tableau to find the Card wished to be moved (selectedCard)

        // Outer loop accesses each Column
        for (int i = 0; i < tableau.columns.length; i++) {

            // Retrieve first Card of each Column
            Card runner = tableau.columns[i].firstCard;

            // Inner loop accesses each Card in the Column
            for (int j = 0; j < tableau.columns[i].getLength(); j++) {

                // Check for condition where selectedCard is found
                if (origRank.equals(runner.rank) && origSuit.equals(runner.suit)) {

                    // If found, set previously initialized variables
                    selectedCard = runner;
                    selectedIndex = i;
                }
                
                // Set runner to next Card in Column
                runner = runner.next;
            }
        }

        // Declare new Card to access Cards underneath selectedCard, which are also moved
        Card runner2 = selectedCard;

        // Iterate to potential cards underneath selectedCard by using runner2
        while (runner2 != null && runner2.next != null) {
            runner2 = runner2.next;
        }
        
        // Make required changes to Column that selectedCard was initially in

        // Set the 'next' attribute of the Card that was above selectedCard to null
        selectedCard.prev.next = null;
        // Update the 'lastCard' attribute to the Card that was above selectedCard
        tableau.columns[selectedIndex].lastCard = selectedCard.prev;

        // Make required configurations to selectedCard and the specified empty Column
        selectedCard.prev = null;
        tableau.columns[destinationIndex].firstCard = selectedCard;
        tableau.columns[destinationIndex].lastCard = runner2;

        // Communicate what move the computer is making
        System.out.println("Move " + origRank + origSuit + " to empty column #" + (destinationIndex + 1) + ".");
        System.out.println();
    }

    // addToFoundation(int index, String suit) method

    public void addToFoundation(int index, String suit) {
        if (suit.equals("H")) {
            heartsFoundation.firstCard = tableau.columns[index].firstCard;
            heartsFoundation.lastCard = tableau.columns[index].lastCard;
        } else if (suit.equals("S")) {
            spadesFoundation.firstCard = tableau.columns[index].firstCard;
            spadesFoundation.lastCard = tableau.columns[index].lastCard;
        } else if (suit.equals("D")) {
            diamondsFoundation.firstCard = tableau.columns[index].firstCard;
            diamondsFoundation.lastCard = tableau.columns[index].lastCard;
        } else if (suit.equals("C")) {
            clubsFoundation.firstCard = tableau.columns[index].firstCard;
            clubsFoundation.lastCard = tableau.columns[index].lastCard;
        }

        tableau.columns[index] = new Column();
    }

    // useTail() method

    public void useTail() {
        System.out.println();
        System.out.println("Retrieving Tail");
        System.out.println();
        Card runner = tail.lastCard;
        int index = 0;

        while (runner != null) {
            tableau.columns[index].add(new Card(runner.rank, runner.suit));
            runner = runner.prev;
            index++;
        }
        
        tail = new Deck();
    }

    // Initialization Methods

    // initTableau() method

    private void initTableau() {
        // Initialize current Game object's tableau attribute to a new Tableau
        this.tableau = new Tableau();

        // Declare and initialize a new Card to the firstCard of fullDeck
        Card runner = fullDeck.firstCard;
        
        // Iterate through fullDeck to add Cards from fullDeck to the Tableau

        // Outer loop specifies the current row
        for (int i = 0; i < 7; i++) {

            // Declare and initialize index integer to populate Tableau row by row
            int index = 0;

            // Loop to populate each of the seven Columns
            while (index < 7) {

                // Use tableau's add(Card runner, int index) method to add to Tableau
                tableau.add(runner, index);

                // Set runner to next card in fullDeck
                runner = runner.next;

                // Increment index to add the next Card to the next Column in the current row
                index++;
            }
        }
        
    }

    // initTail() method

    private void initTail() {
        // Initialize current Game object's tail attribute to a new Deck
        this.tail = new Deck();

        // Declare and initialize a new Card to the firstCard of fullDeck
        Card runner = fullDeck.firstCard;

        // Specify where to begin in fullDeck
        int tableauNumber = 49;

        // Loop through fullDeck in order to get to the last three Cards
        while (tableauNumber > 0) {
            runner = runner.next;
            tableauNumber--;
        }

        // Loop so that the last three cards of fullDeck are added to the Tail
        while (runner != null) {
            tail.add(runner);
            runner = runner.next;
        }

        tail.firstCard.prev = null;
        tail.lastCard.next = null;
    }

    // initFoundations() method

    private void initFoundations() {
        heartsFoundation = new Deck();
        spadesFoundation = new Deck();
        diamondsFoundation = new Deck();
        clubsFoundation = new Deck();
    }

    // initFullDeck(String input) method
    // Convert String retrieved by using BufferedReader to Cards in a Deck

    private void initFullDeck(String input) {
        this.fullDeck = new Deck();

        for (int i = 0; i < input.length(); i+=3) {
            if (i == 157) {
                // Get Rank as character
                String rank = Character.toString(input.charAt(i));
                // Get Suit as character
                String suit = Character.toString(input.charAt(i + 1));
                // Add card to deck
                fullDeck.add(new Card(rank, suit));
                break;
            }
            else if (i + 2 < input.length() && i + 1 <= input.length()) {
                if (input.charAt(i + 2) == ' ') {
                    // Get Rank as character
                    String rank = Character.toString(input.charAt(i));
                    // Get Suit as character
                    String suit = Character.toString(input.charAt(i + 1));
                    // Add card to deck
                    fullDeck.add(new Card(rank, suit));
                } else {
                    // Get Rank as character
                    String rank = Character.toString(input.charAt(i)) + Character.toString(input.charAt(i + 1));
                    // Get Suit as character
                    String suit = Character.toString(input.charAt(i + 2));;
                    // Add card to deck
                    fullDeck.add(new Card(rank, suit));
                    i++;
                }
            }
        }
    }
}
