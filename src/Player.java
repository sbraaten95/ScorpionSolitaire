public class Player {
    private Game currentGame;

    public Player() {}

    // startGame method

    public void startGame(Game g, String input) {
        // Begin game with supplied specifications and then display to user
        currentGame = g;
        g.setup(input);

        System.out.println("Scorpion Solitaire");
        System.out.println();
        g.print();
    }

    // playGame method

    public void playGame() {
        // Computer's indicator for whether to continue
        boolean possibleMoves = true;

        // Computer's attempts at solution
        while (possibleMoves) {
            // firstMovesOnly iteration for moves executes the first move it finds
            possibleMoves = firstMovesOnly();

            // freeLargeCards iteration for moves finds all the choices and makes the one that leaves the highest card free to play on 
            // possibleMoves = freeLargeCards();

            // Update viewer of computer's moves
            currentGame.print(currentGame.turn++);
            // Check for complete run
            scanColumns();
        }

        currentGame.useTail();
        currentGame.print();
        possibleMoves = true;

        while (possibleMoves) {
            
            // Search iteration for moves, currently executes the first move it finds
            possibleMoves = firstMovesOnly();
            
            // freeLargeCards iteration for moves finds all the choices and makes the one that leaves the highest card free to play on 
            // possibleMoves = freeLargeCards();

            if (!possibleMoves) {
                // Print information for when no moves are found.
                System.out.println();

                System.out.println("Computer Out of Moves - Final Board:");
                System.out.println();
                currentGame.print();
            } else {
                // Update viewer of computer's moves
                currentGame.print(currentGame.turn++);
                // Check for complete run
                scanColumns();
            } 
        }

        
    }

    // This method uses two runners to go through the tableau and find a proper match for the last card in each Column
    // When it finds a possibility, it immediately goes through with it
    private boolean firstMovesOnly() {
        // These variables indicate the rank for each of the runners as integers for the sake of simplicity (considering K, Q, J, A)
        int r1 = -1;
        int r2 = -1;

        // First loop is for getting the last card in each Column, which could be played on.
        for (int i = 0; i < currentGame.tableau.columns.length; i++) {

            // If there are one or more cards in the Column in focus
            if (currentGame.tableau.columns[i].lastCard != null) {
                
                // Get last card in current Column (tableau.columns[i].lastCard)
                Card targetCard = currentGame.tableau.columns[i].lastCard;

                // Specify r1 according to targetCard.rank
                if (targetCard.rank.equals("J")) {
                    r1 = 11;
                } else if (targetCard.rank.equals("Q")) {
                    r1 = 12;
                } else if (targetCard.rank.equals("K")) {
                    r1 = 13;
                } else if (targetCard.rank.equals("A")) {
                    r1 = 1;
                } else {
                    r1 = Integer.parseInt(targetCard.rank);
                }

                // First nested loop is for accessing each column, in order to check for possible cards to play on
                // the last card retrieved above.
                for (int j = 0; j < currentGame.tableau.columns.length; j++) {

                    // Only check columns other than the one targetCard is in.
                    if (i == j) {
                        continue;
                    } else {

                        // Get first card of each column
                        Card runner2 = currentGame.tableau.columns[j].firstCard;
                    
                        // Iterate through the column
                        for (int k = 0; k < currentGame.tableau.columns[j].getLength(); k++) {

                            // Specify r2 according to runner2.rank
                            if (runner2.rank.equals("J")) {
                                r2 = 11;
                            } else if (runner2.rank.equals("Q")) {
                                r2 = 12;
                            } else if (runner2.rank.equals("K")) {
                                r2 = 13;
                            } else if (runner2.rank.equals("A")) {
                                r2 = 1;
                            } else {
                                r2 = Integer.parseInt(runner2.rank);
                            }

                            // Useful information printing to ensure effective operation:

                            // System.out.println((r2 == r1 - 1 && runner2.suit.equals(runner.suit) && !runner2.faceDown) + " i:" + i + " j:" + j + " r1: " + r1 + " or rank1:" + runner.rank + " r2:" + r2 + " or rank 2:" + runner2.rank + " r2suit:" + runner2.suit + " r1suit:" + runner.suit + " r2.faceDown" + runner2.faceDown);

                            // Check conditions that would allow for runner2 to be moved to targetCard.
                            if (r2 == r1 - 1 && runner2.suit.equals(targetCard.suit) && !runner2.faceDown) {

                                // If conditions are met, move the card and end the iteration by returning true,
                                // continuing the while loop in playGame()
                                currentGame.moveCard(runner2.rank, runner2.suit, targetCard.rank, targetCard.suit);
                                return true;
                            }

                            // If conditions are not met, check the next card in the column.
                            runner2 = runner2.next;
                        }
                    }
                }
            } else {
                // Outer nested loop is for accessing each Column, in order to check for possible Cards to play on the empty Column
                for (int j = 0; j < currentGame.tableau.columns.length; j++) {

                    // Only check Columns other than the one targetCard is in.
                    if (i == j) {
                        continue;
                    } else {

                        // Get first card of each Column
                        Card runner2 = currentGame.tableau.columns[j].firstCard;
                    
                        // Inner nested loop iterates through the Column
                        for (int k = 0; k < currentGame.tableau.columns[j].getLength(); k++) {

                            // Specify r2 according to runner2.rank
                            if (runner2.rank.equals("J")) {
                                r2 = 11;
                            } else if (runner2.rank.equals("Q")) {
                                r2 = 12;
                            } else if (runner2.rank.equals("K")) {
                                r2 = 13;
                            } else if (runner2.rank.equals("A")) {
                                r2 = 1;
                            } else {
                                r2 = Integer.parseInt(runner2.rank);
                            }

                            // Check conditions that would allow for runner2 to be moved to the empty Column.
                            if (runner2.rank.equals("K") && !runner2.faceDown && runner2.prev != null) {

                                // If conditions are met, move the Card and end the iteration by returning true,
                                // continuing the while loop in playGame()
                                currentGame.moveCard(runner2.rank, runner2.suit, i);
                                return true;
                            }

                            // If conditions are not met, check the next Card in the Column.
                            runner2 = runner2.next;
                        }
                    }
                }
            }
        }
        // Return false, ending the while loop in playGame()
        return false;
    }

    private boolean freeLargeCards() {
        // These variables indicate the rank for each of the runners as integers for the sake of simplicity (considering K, Q, J, A)
        int r1 = -1;
        int r2 = -1;

        Deck possibileMovableCards = new Deck();
        Deck possibileDestinationCards = new Deck();

        boolean freeKings = false;

        // First loop is for getting the last card in each Column, which could be played on.
        for (int i = 0; i < currentGame.tableau.columns.length; i++) {

            // If there are one or more cards in the Column in focus
            if (currentGame.tableau.columns[i].lastCard != null) {
                
                // Get last card in current Column (tableau.columns[i].lastCard)
                Card targetCard = currentGame.tableau.columns[i].lastCard;

                // Specify r1 according to targetCard.rank
                if (targetCard.rank.equals("J")) {
                    r1 = 11;
                } else if (targetCard.rank.equals("Q")) {
                    r1 = 12;
                } else if (targetCard.rank.equals("K")) {
                    r1 = 13;
                } else if (targetCard.rank.equals("A")) {
                    r1 = 1;
                } else {
                    r1 = Integer.parseInt(targetCard.rank);
                }

                // First nested loop is for accessing each column, in order to check for possible cards to play on
                // the last card retrieved above.
                for (int j = 0; j < currentGame.tableau.columns.length; j++) {

                    // Only check columns other than the one targetCard is in.
                    if (i == j) {
                        continue;
                    } else {

                        // Get first card of each column
                        Card runner2 = currentGame.tableau.columns[j].firstCard;
                    
                        // Iterate through the column
                        for (int k = 0; k < currentGame.tableau.columns[j].getLength(); k++) {

                            // Specify r2 according to runner2.rank
                            if (runner2.rank.equals("J")) {
                                r2 = 11;
                            } else if (runner2.rank.equals("Q")) {
                                r2 = 12;
                            } else if (runner2.rank.equals("K")) {
                                r2 = 13;
                            } else if (runner2.rank.equals("A")) {
                                r2 = 1;
                            } else {
                                r2 = Integer.parseInt(runner2.rank);
                            }

                            // Useful information printing to ensure effective operation:

                            // System.out.println((r2 == r1 - 1 && runner2.suit.equals(runner.suit) && !runner2.faceDown) + " i:" + i + " j:" + j + " r1: " + r1 + " or rank1:" + runner.rank + " r2:" + r2 + " or rank 2:" + runner2.rank + " r2suit:" + runner2.suit + " r1suit:" + runner.suit + " r2.faceDown" + runner2.faceDown);

                            if (runner2.rank == "K" && !runner2.faceDown) {
                                freeKings = true;
                            }

                            // Check conditions that would allow for runner2 to be moved to targetCard.
                            if (r2 == r1 - 1 && runner2.suit.equals(targetCard.suit) && !runner2.faceDown) {
                                Card choice;
                                if (runner2.prev != null) {
                                    choice = new Card(runner2.prev.rank, runner2.prev.suit);
                                    choice.nextChoice = runner2;
                                } else {
                                    choice = new Card("free", runner2.suit);
                                    choice.nextChoice = runner2;
                                }
                                
                                possibileMovableCards.add(choice);
                                possibileDestinationCards.add(new Card(targetCard.rank, targetCard.suit));
                            }

                            // If conditions are not met, check the next card in the column.
                            runner2 = runner2.next;
                        }
                    }
                }
            } else {
                // Outer nested loop is for accessing each Column, in order to check for possible Cards to play on the empty Column
                for (int j = 0; j < currentGame.tableau.columns.length; j++) {

                    // Only check Columns other than the one targetCard is in.
                    if (i == j) {
                        continue;
                    } else {

                        // Get first card of each Column
                        Card runner2 = currentGame.tableau.columns[j].firstCard;
                    
                        // Inner nested loop iterates through the Column
                        for (int k = 0; k < currentGame.tableau.columns[j].getLength(); k++) {

                            // Specify r2 according to runner2.rank
                            if (runner2.rank.equals("J")) {
                                r2 = 11;
                            } else if (runner2.rank.equals("Q")) {
                                r2 = 12;
                            } else if (runner2.rank.equals("K")) {
                                r2 = 13;
                            } else if (runner2.rank.equals("A")) {
                                r2 = 1;
                            } else {
                                r2 = Integer.parseInt(runner2.rank);
                            }

                            // Check conditions that would allow for runner2 to be moved to the empty Column.
                            if (runner2.rank.equals("K") && !runner2.faceDown && runner2.prev != null) {

                                Card choice;
                                if (runner2.prev != null) {
                                    choice = new Card(runner2.prev.rank, runner2.prev.suit);
                                    choice.nextChoice = runner2;
                                } else {
                                    choice = new Card("free", runner2.suit);
                                    choice.nextChoice = runner2;
                                }
                                
                                possibileMovableCards.add(choice);
                                possibileDestinationCards.add(new Card(Integer.toString(i), ""));
                            }

                            // If conditions are not met, check the next Card in the Column.
                            runner2 = runner2.next;
                        }
                    }
                }
            }
        }

        Card p1 = possibileMovableCards.firstCard;
        Card p2 = possibileDestinationCards.firstCard;

        Card bestCard = new Card("", "");
        Card bestDest = new Card("", "");
        int highest = -1;

        while (p1 != null) {
            r2 = -1;
            if (p1.rank.equals("J")) {
                r2 = 11;
            } else if (p1.rank.equals("Q")) {
                r2 = 12;
            } else if (p1.rank.equals("K")) {
                r2 = 13;
            } else if (p1.rank.equals("A")) {
                r2 = 1;
            } else if (p1.rank.equals("free")) {
                r2 = 14;
            } else {
                r2 = Integer.parseInt(p1.rank);
            }

            if (r2 > highest) {
                highest = r2;
                bestCard = p1.nextChoice;
                bestDest = p2;
            }

            p1 = p1.next;
            p2 = p2.next;
        }

        if (highest > 0 && bestDest.suit != "") {
            System.out.println("This choice leaves the highest cards open: " + bestCard.rank+bestCard.suit);
            currentGame.moveCard(bestCard.rank, bestCard.suit, bestDest.rank,bestDest.suit);
            return true;
        } else if (highest > 0 && bestDest.suit == "") {
            System.out.println("This choice leaves the highest cards open: " + bestCard.rank+bestCard.suit);
            currentGame.moveCard(bestCard.rank, bestCard.suit, Integer.parseInt(bestDest.rank));
            return true;
        } else {
            return false;
        }
    }

    private void scanColumns() {
        for (int i = 0; i < currentGame.tableau.columns.length; i++) {
            Card runner = currentGame.tableau.columns[i].firstCard;
            while (runner != null) {
                if (runner.faceDown && runner.next == null) {
                    runner.faceDown = false;
                }
                runner = runner.next;
            }
            if (currentGame.tableau.columns[i].getLength() != 13) {
                break;
            }
            runner = currentGame.tableau.columns[i].firstCard;
            boolean complete = true;
            int counter = 0;
            while (runner != null) {
                if (!runner.rank.equals("K") && counter == 0) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("Q") && counter == 1) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("J") && counter == 2) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("10") && counter == 3) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("9") && counter == 4) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("8") && counter == 5) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("7") && counter == 6) {
                    complete = false;
                    break;
                } else if (!runner.rank.equals("6") && counter == 7) {
                    complete = false;
                    break;
                }  else if (!runner.rank.equals("5") && counter == 8) {
                    complete = false;
                    break;
                }  else if (!runner.rank.equals("4") && counter == 9) {
                    complete = false;
                    break;
                }  else if (!runner.rank.equals("3") && counter == 10) {
                    complete = false;
                    break;
                }  else if (!runner.rank.equals("2") && counter == 11) {
                    complete = false;
                    break;
                }  else if (!runner.rank.equals("A") && counter == 12) {
                    complete = false;
                    break;
                }

                counter++;
                runner = runner.next;
            }

            if (complete) {
                currentGame.addToFoundation(i, runner.prev.suit);
            }
        }
    }
}
