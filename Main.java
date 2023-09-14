import java.util.*;

/**
 * Chess in Terminal
 * <p>
 * A very bare bones and industry nonstandard chess game that I made my junior year of high school.
 * Select 2 player or 1 player mode (you play against random moves), and select moves with chess coordinates.
 * Have fun!
 *
 * @author Max Luo
 * @version May 2022
 */

public class Main {
    public static void main(String[] args) {
        Chess chs = new Chess();
        Scanner sc = new Scanner(System.in);

        final String RESET = "\u001B[0m";
        final String[] color = {"\u001B[34m", "\u001B[31m"};

        int win = 0, mode = 0, player = 0, plays = 0;
        int prevRow, prevCol, newRow, newCol;
        String addToNotation = "", notation = "";

        String input = "xx", prom = "x";
        String[] playerName = {"blue", "red"};
        String[] piecesOf = {"P R N B Q K", "p r n b q k"};

        System.out.println("Welcome to Chess:");
        while (true) {
            System.out.print("Input mode: 1 player | 2 player: ");
            mode = sc.nextInt();

            // 999 runs 2 rng games to completion
            if (mode == 1 || mode == 2 || mode == 999)
                break;

            System.out.println("Invalid: input 1 or 2\n");
        }

        input = sc.nextLine();

        chs.Chess();
        chs.printBoard();

        while (win == 0) {

            // alternate play
            if (plays % 2 == 0)
                player = 0;
            if (plays % 2 == 1)
                player = 1;

            if ((mode == 2 || player == 0) && mode != 999) {
                System.out.print(color[player]);
                System.out.println(playerName[player] + "'s turn (" + piecesOf[player] + ")");

                // validates if user selected legally movable piece
                while (true) {
                    // validates piece selection input syntax
                    while (true) {
                        System.out.print("Enter location of piece (a-h)(1-8): ");
                        input = sc.nextLine();

                        if (input.length() != 2) {
                            System.out.println(RESET + "\nInvalid: incorrect # of inputs\n" + color[player]);
                        }
                        else {
                            prevRow = 8 - ((int) (input.charAt(1)) - 48);
                            prevCol = chs.alphaToNumeric(input.charAt(0)) - 1;
                            if (prevRow > 8 || prevRow < 0 || prevCol > 8 || prevCol < 0) {
                                System.out.println(RESET + "\nInvalid: out of bounds\n" + color[player]);
                            } else {
                                break;
                            }
                        }
                    }

                    if (player == 0 && !chs.isPieceUpper(prevRow, prevCol) || player == 1 && chs.isPieceLower(prevRow, prevCol)) {
                        System.out.println(RESET + "\nInvalid: no " + playerName[player] + " piece here\n" + color[player]);
                        continue;
                    }
                    if (!chs.isMovablePiece(prevRow, prevCol)) {
                        System.out.println(RESET + "\nInvalid: " + chs.pieceToName(prevRow, prevCol) + " does not have a move\n" + color[player]);
                        continue;
                    }
                    break;
                }

                chs.showPotentialMoves(prevRow, prevCol);

                // validates user move syntax
                while (true) {
                    System.out.print(color[player] + "Move " + chs.pieceToName(prevRow, prevCol) + " to (a-h)(1-8): ");
                    input = sc.nextLine();

                    if (input.length() != 2) {
                        System.out.println(RESET + "\nInvalid: incorrect # of inputs\n" + color[player]);
                        continue;
                    }
                    newRow = 8 - ((int) (input.charAt(1)) - 48);
                    newCol = chs.alphaToNumeric(input.charAt(0)) - 1;
                    if (newRow > 7 || newRow < 0 || newCol > 7 || newCol < 0) {
                        System.out.println(RESET + "Invalid: out of bounds\n" + color[player]);
                        continue;
                    }
                    break;
                }

                // validates legal move
                if (!chs.isLegalMove(prevRow, prevCol, newRow, newCol)) {
                    System.out.println(RESET + "\nInvalid: not a legal move\n " + color[player]);
                    continue;
                }
                // validates move puts player out of check
                if (!chs.outOfCheck(prevRow, prevCol, newRow, newCol)) {
                    System.out.println(RESET + "\nInvalid: puts you in check\n " + color[player]);
                    continue;
                }

                plays++;

                // notation
                addToNotation = RESET; //reset
                if (player == 0)
                    addToNotation += ((plays + 1) / 2) + ". "; //move#
                addToNotation += color[player];

                int castle = chs.doCastle(prevRow, prevCol, newCol);

                if (castle == 0) {
                    if (Character.toLowerCase(chs.getPiece(prevRow, prevCol)) != 'p') {
                        addToNotation += Character.toUpperCase(chs.getPiece(prevRow, prevCol)); //piece
                        addToNotation += chs.notationInfo(prevRow, prevCol, newRow, newCol); //piece(ambiguous)
                    }

                    if (player == 0 && Character.isLowerCase(chs.getPiece(newRow, newCol)) ||
                            player == 1 && Character.isUpperCase(chs.getPiece(newRow, newCol))) {
                        if (Character.toLowerCase(chs.getPiece(prevRow, prevCol)) == 'p') {
                            addToNotation += Character.toLowerCase(chs.numericToAlpha(prevCol)); //pawn file
                        }
                        addToNotation += "x"; //takes
                    }

                    addToNotation += Character.toLowerCase(chs.numericToAlpha(newCol));   //new move file
                    addToNotation += (8 - newRow);                                  //new move rank
                }
                if (castle == 1) {
                    addToNotation += "0-0";
                }
                if (castle == 2) {
                    addToNotation += "0-0-0";
                }

                chs.doEnPassant(prevRow, prevCol, newRow, newCol);
                chs.doMove(prevRow, prevCol, newRow, newCol);

                if (player == 0)
                    chs.removeEnPassantBlack();
                if (player == 1)
                    chs.removeEnPassantWhite();

                if (chs.canWhitePromote(newRow, newCol)) {
                    while (true) {
                        System.out.print("\nPromote pawn (R N B Q): ");
                        prom = sc.nextLine();

                        if (prom.equals("R") || prom.equals("N") || prom.equals("B") || prom.equals("Q")) {
                            chs.setPiece(newRow, newCol, prom.charAt(0));
                            addToNotation += "=" + prom; //promote
                            break;
                        }
                        System.out.println(RESET + "Invalid: chooese one piece\n" + color[player]);
                    }
                }
                if (chs.canBlackPromote(newRow, newCol)) {
                    while (true) {
                        System.out.print("\nPromote pawn (r n b q): ");
                        prom = sc.nextLine();

                        if (prom.equals("r") || prom.equals("n") || prom.equals("b") || prom.equals("q")) {
                            chs.setPiece(newRow, newCol, prom.charAt(0));
                            addToNotation += "=" + prom; //promote
                            break;
                        }
                        System.out.println(RESET + "Invalid: chooese one piece\n" + color[player]);
                    }
                }

                chs.showPreviousMove(prevRow, prevCol, newRow, newCol);

                // check win conditions
                if (chs.drawByNoMaterial()) {
                    System.out.println("Tie in " + plays + " plays! Drawn position");
                    win = 2;
                }
                if (player == 1 && chs.winAgainstWhite() == 2) {
                    System.out.println("Tie in " + plays + " plays! Blue in stalemate");
                    win = 2;
                }
                if (player == 0 && chs.winAgainstBlack() == 2) {
                    System.out.println("Tie in " + plays + " plays! Red in stalemate");
                    win = 2;
                }
                if (chs.winAgainstWhite() == 1) {
                    System.out.println("Red has won in " + plays + " plays!");
                    win = 1;
                }
                if (chs.winAgainstBlack() == 1) {
                    System.out.println("Blue has won in " + plays + " plays!");
                    win = 1;
                }
                if (win == 1) {
                    notation += addToNotation + "#";
                    System.out.println(notation + "\n");
                }
                if (win == 2) {
                    notation += addToNotation + " 1/2-1/2";
                    System.out.println(notation + "\n");
                }

                if (win == 0 && chs.whiteInCheck(chs.whiteKingRow, chs.whiteKingCol)) {
                    System.out.println("Blue is in check\n");
                    addToNotation += "+";
                }
                if (win == 0 && chs.blackInCheck(chs.blackKingRow, chs.blackKingCol)) {
                    System.out.println("Red is in check\n");
                    addToNotation += "+";
                }

                addToNotation += " ";
                notation += addToNotation;

            } //player

            if (mode == 1 && player == 1 || mode == 999) //computer
            {
                while (true) {
                    prevRow = (int) (Math.random() * 8);
                    prevCol = (int) (Math.random() * 8);

                    if ((player == 0 && chs.isPieceUpper(prevRow, prevCol) || player == 1 && chs.isPieceLower(prevRow, prevCol)) && chs.isMovablePiece(prevRow, prevCol)) {
                        break;
                    }
                }

                while (true) {
                    newRow = (int) (Math.random() * 8);
                    newCol = (int) (Math.random() * 8);

                    if (chs.isLegalMove(prevRow, prevCol, newRow, newCol) && chs.outOfCheck(prevRow, prevCol, newRow, newCol)) {
                        plays++;
                        if (mode != 999) {
                            System.out.print(color[player]);
                            System.out.println("Computer plays " + chs.numericToAlpha(prevCol) + (8 - prevRow) + " " + chs.pieceToName(prevRow, prevCol) + " to " + chs.numericToAlpha(newCol) + (8 - newRow));
                            if (chs.isPieceUpper(newRow, newCol) == true) {
                                System.out.println("Captures " + color[0] + chs.pieceToName(newRow, newCol));
                            }
                        }

                        //notation{
                        addToNotation = RESET; //reset
                        if (player == 0)
                            addToNotation += ((plays + 1) / 2) + ". "; //move#
                        addToNotation += color[player];

                        int castle = chs.doCastle(prevRow, prevCol, newCol);
                        if (castle == 0) {
                            if (Character.toLowerCase(chs.getPiece(prevRow, prevCol)) != 'p') {
                                addToNotation += Character.toUpperCase(chs.getPiece(prevRow, prevCol)); //piece
                                addToNotation += chs.notationInfo(prevRow, prevCol, newRow, newCol); //piece(ambiguous)
                            }

                            if (player == 0 && Character.isLowerCase(chs.getPiece(newRow, newCol)) ||
                                    player == 1 && Character.isUpperCase(chs.getPiece(newRow, newCol))) {
                                if (Character.toLowerCase(chs.getPiece(prevRow, prevCol)) == 'p') {
                                    addToNotation += Character.toLowerCase(chs.numericToAlpha(prevCol)); //pawn file
                                }
                                addToNotation += "x"; //takes
                            }

                            addToNotation += Character.toLowerCase(chs.numericToAlpha(newCol)); //new move file
                            addToNotation += (8 - newRow);                                               //new move rank
                        }
                        if (castle == 1) {
                            addToNotation += "0-0";
                        }
                        if (castle == 2) {
                            addToNotation += "0-0-0";
                        }
                        //}

                        chs.doEnPassant(prevRow, prevCol, newRow, newCol);
                        chs.doMove(prevRow, prevCol, newRow, newCol);

                        if (player == 0)
                            chs.removeEnPassantBlack();
                        if (player == 1)
                            chs.removeEnPassantWhite();

                        if (chs.canWhitePromote(newRow, newCol)) {
                            chs.setPiece(newRow, newCol, 'Q');
                            addToNotation += "=" + "Q"; //promote
                        }
                        if (chs.canBlackPromote(newRow, newCol)) {
                            chs.setPiece(newRow, newCol, 'q');
                            addToNotation += "=" + "q"; //promote
                        }

                        if (mode != 999)
                            chs.showPreviousMove(prevRow, prevCol, newRow, newCol);

                        //win{
                        if (chs.drawByNoMaterial()) {
                            System.out.println("Tie in " + plays + " plays! Drawn position");
                            win = 2;
                        }
                        if (player == 1 && chs.winAgainstWhite() == 2) {
                            System.out.println("Tie in " + plays + " plays! Blue in stalemate");
                            win = 2;
                        }
                        if (player == 0 && chs.winAgainstBlack() == 2) {
                            System.out.println("Tie in " + plays + " plays! Red in stalemate");
                            win = 2;
                        }
                        if (chs.winAgainstWhite() == 1) {
                            System.out.println("Red has won in " + plays + " plays!");
                            win = 1;
                        }
                        if (chs.winAgainstBlack() == 1) {
                            System.out.println("Blue has won in " + plays + " plays!");
                            win = 1;
                        }
                        if (win == 1) {
                            if (mode == 999)
                                chs.showPreviousMove(prevRow, prevCol, newRow, newCol);

                            notation += addToNotation + "#";
                            System.out.println(notation + RESET + "\n");
                        }
                        if (win == 2) {
                            if (mode == 999)
                                chs.showPreviousMove(prevRow, prevCol, newRow, newCol);

                            notation += addToNotation + " 1/2-1/2";
                            System.out.println(notation + RESET + "\n");
                        }
                        //}

                        if (mode != 999 && win == 0 && chs.whiteInCheck(chs.whiteKingRow, chs.whiteKingCol)) {
                            System.out.println("Blue is in check\n");
                            addToNotation += "+";
                        }
                        if (mode != 999 && win == 0 && chs.blackInCheck(chs.whiteKingRow, chs.whiteKingCol)) {
                            System.out.println("Red is in check\n");
                            addToNotation += "+";
                        }

                        addToNotation += " ";
                        notation += addToNotation;

                        break;
                    }
                }
            } //computer

            if (win != 0) {
                chs.Chess();
                notation = "";
                plays = 0;
                player = 0;

                while (true) {
                    System.out.print(RESET + "New Game? (y/n): ");
                    String yn = sc.nextLine();
                    if (yn.equals("y")) {
                        win = 0;
                        break;
                    }
                    if (yn.equals("n"))
                        break;

                    System.out.println("\nEnter y or n\n");
                }
            }

        } //game while loop
    }
}
