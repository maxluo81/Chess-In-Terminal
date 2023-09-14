/**
 * Chess class to keep track of board and piece information. Logic methods to check for game conditions.
 *
 * @author Max Luo
 * @version May 2022
 */

public class Chess
{
    private char[][] chessBoard = new char[8][8];
    private final int[] whiteEnPassant = new int[8];
    private final int[] blackEnPassant = new int[8];

    private final boolean USE_SYMBOL = false;
    private boolean blackKingCastle, blackQueenCastle, whiteKingCastle, whiteQueenCastle;
    public int whiteKingRow, whiteKingCol, blackKingRow, blackKingCol;

    // colors
    private final String RESET = "\u001B[0m";
    private final String BLACK_PIECE_COLOR = "\u001B[31m";
    private final String WHITE_PIECE_COLOR = "\u001B[34m";
    private final String DARK_BACKGROUND = "\u001B[40m";
    private final String LIGHT_BACKGROUND = "\u001B[47m";
    private final String DARK_HIGHLIGHT_BACKGROUND = "\033[43m";
    private final String LIGHT_HIGHLIGHT_BACKGROUND = "\u001B[103m"; //}

// setters

    public void Chess() {
        setBoard();
        removeEnPassantWhite();
        removeEnPassantBlack();
    }

    public void setTest() {
        char[][] arr = 
            {{'r',' ',' ',' ','k',' ',' ','r'},
             {' ','p',' ',' ',' ','b',' ',' '},
             {' ','n',' ',' ',' ',' ',' ',' '},
             {' ',' ','P',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ','B',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {'R',' ',' ',' ','K',' ',' ','R'}};
                 
        for (int row = 0; row < 8; row++) {
            for (int col = 2; col < 8; col++) {
                if (arr[row][col] == 'K') {
                    whiteKingRow = row;
                    whiteKingCol = col;
                }
                if (arr[row][col] == 'k') {
                    blackKingRow = row;
                    blackKingCol = col;
                }
            }
        }
        
        whiteKingCastle = true;
        whiteQueenCastle = true;
        blackKingCastle = true;
        blackQueenCastle = true;

        chessBoard = arr;
    }
    
    public void setBoard() {
        char[][] arr = 
            {{'r','n','b','q','k','b','n','r'},
             {'p','p','p','p','p','p','p','p'},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {'P','P','P','P','P','P','P','P'},
             {'R','N','B','Q','K','B','N','R'}};
                 
        chessBoard = arr;
        
        whiteKingRow = 7;
        whiteKingCol = 4;
        blackKingRow = 0;
        blackKingCol = 4;
        
        whiteKingCastle = true;
        whiteQueenCastle = true;
        blackKingCastle = true;
        blackQueenCastle = true;
    }
    
    public void setPiece(int r, int c, char p)
    {
        chessBoard[r][c] = p;
    }

// doers

    public char getPiece(int r, int c) {
        return chessBoard[r][c];
    }
    
    public void doMove(int prevRow, int prevCol, int newRow, int newCol) {
        char piece = chessBoard[prevRow][prevCol];
        
        if (piece == 'R' && prevCol == 7)
            whiteKingCastle = false;
        if (piece == 'R' && prevCol == 0)
            whiteQueenCastle = false;
            
        if (piece == 'r' && prevCol == 7)
            blackKingCastle = false;
        if (piece == 'r' && prevCol == 0)
            blackQueenCastle = false;
           
        if (piece == 'K')
        {
            whiteKingCastle = false;
            whiteQueenCastle = false;
            whiteKingRow = newRow;
            whiteKingCol = newCol;
        }
        if (piece == 'k')
        {
            blackKingCastle = false;
            blackQueenCastle = false;
            blackKingRow = newRow;
            blackKingCol = newCol;
        }
           
        chessBoard[prevRow][prevCol] = ' ';
        chessBoard[newRow][newCol] = piece;
        
        if (piece == 'P' && newRow == 2 && blackEnPassant[newCol] == 1) {
            chessBoard[3][newCol] = ' ';
        }
        if (piece == 'p' && newRow == 5 && whiteEnPassant[newCol] == 1) {
            chessBoard[4][newCol] = ' ';
        }
    }
    
    public int doCastle(int prevRow, int prevCol, int newCol) {
        char piece = chessBoard[prevRow][prevCol];
        if (piece == 'K') {
            if (whiteKingCastle && newCol == 6) {
                chessBoard[7][5] = 'R';
                chessBoard[7][7] = ' ';
                return 1;
            }
            if (whiteQueenCastle && newCol == 2) {
                chessBoard[7][3] = 'R';
                chessBoard[7][0] = ' ';
                return 2;
            }
        }
        if (piece == 'k') {
            if (blackKingCastle && newCol == 6) {
                chessBoard[0][5] = 'r';
                chessBoard[0][7] = ' ';
                return 1;
            }
            if (blackQueenCastle && newCol == 2) {
                chessBoard[0][3] = 'r';
                chessBoard[0][0] = ' ';
                return 2;
            }
        }
        return 0;
    }

    public void doEnPassant(int prevRow, int prevCol, int newRow, int newCol)
    {
        char p = chessBoard[prevRow][prevCol];
        if (p == 'P' && prevCol == newCol && prevRow == 6 && newRow == 4 && chessBoard[5][prevCol] == ' ') {
            whiteEnPassant[prevCol] = 1;
        }
        if (p == 'p' && prevCol == newCol && prevRow == 1 && newRow == 3 && chessBoard[2][prevCol] == ' ') {
            blackEnPassant[prevCol] = 1;
        }
    }
    
    public void removeEnPassantWhite() {
        for (int i = 0; i < 8; i++) {
            whiteEnPassant[i] = 0;
        }
    }
    
    public void removeEnPassantBlack() {
        for (int i = 0; i < 8; i++) {
            blackEnPassant[i] = 0;
        }
    }

// print

    public void printBoard()
    {
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");

        for (int row = 0; row < 8; row++) {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++) {
	            if (Character.isUpperCase(chessBoard[row][col]) || chessBoard[row][col] == '!') {
                    System.out.print(WHITE_PIECE_COLOR);
                }
                if (Character.isLowerCase(chessBoard[row][col]) || chessBoard[row][col] == '?') {
                    System.out.print(BLACK_PIECE_COLOR);
                }
	            if ((row + col) % 2 == 1) {
	                System.out.print(DARK_BACKGROUND);
	            }
	            else {
	                System.out.print(LIGHT_BACKGROUND);
	            }
	            System.out.print(pieceToSymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " + (8 - row));
	    }
	    System.out.println("  A B C D E F G H\n");
    }
    
    public void showPotentialMoves(int r, int c) {
        char p = chessBoard[r][c];
        
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");
        for (int row = 0; row < 8; row++) {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++) {
	            if (isLegalMove(r, c, row, col) && outOfCheck(r, c, row, col)) {
	                if ((row + col) % 2 == 1) {
    	                System.out.print(DARK_HIGHLIGHT_BACKGROUND);
    	            }
    	            else {
    	                System.out.print(LIGHT_HIGHLIGHT_BACKGROUND);
    	            }
	            }
	            else {
	                if ((row + col) % 2 == 1) {
    	                System.out.print(DARK_BACKGROUND);
    	            }
    	            else {
    	                System.out.print(LIGHT_BACKGROUND);
    	            }
	            }
	            if (Character.isUpperCase(chessBoard[row][col]) || chessBoard[row][col] == '!') {
                    System.out.print(WHITE_PIECE_COLOR);
                }
                if (Character.isLowerCase(chessBoard[row][col]) || chessBoard[row][col] == '?') {
                    System.out.print(BLACK_PIECE_COLOR);
                }
	            System.out.print(pieceToSymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " +(8 - row) + " ");
	    }
	    System.out.println("  A B C D E F G H\n");
    }

    public void showPreviousMove(int pr, int pc, int nr, int nc) {
        char p = chessBoard[pr][pc];
        
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");
        for (int row = 0; row < 8; row++) {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++) {
	            if (row == pr && col == pc || row == nr && col == nc) {
	                if ((row + col) % 2 == 1) {
    	                System.out.print(DARK_HIGHLIGHT_BACKGROUND);
    	            }
    	            else {
    	                System.out.print(LIGHT_HIGHLIGHT_BACKGROUND);
    	            }
	            }
	            else {
	                if ((row + col) % 2 == 1) {
    	                System.out.print(DARK_BACKGROUND);
    	            }
    	            else {
    	                System.out.print(LIGHT_BACKGROUND);
    	            }
	            }
	            if (Character.isUpperCase(chessBoard[row][col]) || chessBoard[row][col] == '!')
                {
                    System.out.print(WHITE_PIECE_COLOR);
                }
                if (Character.isLowerCase(chessBoard[row][col]) || chessBoard[row][col] == '?')
                {
                    System.out.print(BLACK_PIECE_COLOR);
                }
	            System.out.print(pieceToSymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " +(8 - row) + " ");
	    }
	    System.out.println("  A B C D E F G H\n");
    }

//basic computation

    public int alphaToNumeric(char c) {
        return c - 'a' + 1;
    }
    
    public char numericToAlpha(int i) {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        return alphabet[i];
    }
    
    public String pieceToName(int r, int c) {
        char piece = Character.toLowerCase(chessBoard[r][c]);
        
        if (piece =='p')
            return "pawn";
        
        if (piece =='r')
            return "rook";
        
        if (piece =='n')
            return "knight";
        
        if (piece =='b')
            return "bishop";
        
        if (piece =='q')
            return "queen";
        
        if (piece =='k')
            return "king";
        
        return "blank";
    }
    
    public char pieceToSymbol(int r, int c)
    {
        char piece = Character.toLowerCase(chessBoard[r][c]);

        if (USE_SYMBOL == false)
            return piece;
        
        if (piece =='p')
            return '♙';
        
        if (piece =='r')
            return '♖';
        
        if (piece =='n')
            return '♘';
        
        if (piece =='b')
            return '♗';
        
        if (piece =='q')
            return '♕';
        
        if (piece =='k')
            return '♔';
        
        return ' ';
    }
    
    public boolean canWhitePromote(int r, int c) {
        return (chessBoard[r][c] == 'P' && r == 0);
    }
    
    public boolean canBlackPromote(int r, int c) {
        return (chessBoard[r][c] == 'p' && r == 7);
    }
    
    public boolean isPieceUpper(int r, int c) {
        return Character.isUpperCase(chessBoard[r][c]);
    }
    
    public boolean isPieceLower(int r, int c) {
        return Character.isLowerCase(chessBoard[r][c]);
    }

    
// win conditions

    public boolean drawByNoMaterial() {
        int W = 0, B = 0;
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            if (chessBoard[row][col] == 'N' || chessBoard[row][col] == 'B')
	                W++;
                if (chessBoard[row][col] == 'n' || chessBoard[row][col] == 'b')
                    B++;
	       
	            if (chessBoard[row][col] == 'P' || chessBoard[row][col] == 'p' ||
	               chessBoard[row][col] == 'R' || chessBoard[row][col] == 'r' ||
	               chessBoard[row][col] == 'Q' || chessBoard[row][col] == 'q') {
                    return false;
	            }
	            if (W >= 2 || B >= 2) {
	                return false;
	            }
	        }
	    }
	    return true;
    }

    public int winAgainstWhite() {
        boolean tf = false;
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
                if (Character.isUpperCase(chessBoard[row][col])) {
                    for (int r = 0; r < 8; r++) {
            	        for (int c = 0; c < 8; c++) {
                            if (isLegalMove(row, col, r, c) && outOfCheck(row, col, r, c)) {
                                tf = true;
                            }
            	        }
            	    }
                }
	        }   
	    }
	    if (!tf && whiteInCheck(whiteKingRow, whiteKingCol))
	        return 1;
	        
	    if (!tf && !whiteInCheck(whiteKingRow, whiteKingCol))
	        return 2;
	   
	    return 0;
    }
    
    public int winAgainstBlack() {
        boolean tf = false;
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
                if (Character.isLowerCase(chessBoard[row][col])) {
                    for (int r = 0; r < 8; r++) {
            	        for (int c = 0; c < 8; c++) {
                            if (isLegalMove(row, col, r, c) && outOfCheck(row, col, r, c)) {
                                tf = true;
                            }
            	        }
            	    }
                }
	        }   
	    }
        if (!tf && blackInCheck(blackKingRow, blackKingCol)) {
            return 1;
        }
	        
	    if (!tf && !blackInCheck(blackKingRow, blackKingCol))
	        return 2;
	   
	    return 0;
    }

//complex computation

    public String notationInfo(int prevRow, int prevCol, int newRow, int newCol) {
        char p = chessBoard[prevRow][prevCol];
        String ret = "";
        
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            if (chessBoard[row][col] == p && (row != prevRow || col != prevCol) &&
                        isLegalMove(row, col, newRow, newCol) && outOfCheck(row, col, newRow, newCol)) {
                    if (row == prevRow) {
	                    ret += Character.toLowerCase(numericToAlpha(prevCol));
                        continue;
	                }
	                if (col == prevCol) {
	                    ret += (8 - prevRow);
                        continue;
	                }
                    ret += Character.toLowerCase(numericToAlpha(prevCol));
	            }
	        }
	    }
        return ret;
    }
    
    public boolean outOfCheck(int prevRow, int prevCol, int newRow, int newCol) {
        char p = chessBoard[prevRow][prevCol];
        boolean tf = true;
        int wtRow = whiteKingRow, wtCol = whiteKingCol, btRow = blackKingRow, btCol = blackKingCol;
        
        char temp = chessBoard[newRow][newCol];
        chessBoard[prevRow][prevCol] = ' ';
        chessBoard[newRow][newCol] = p;
        
        if (p == 'K') {
            wtRow = newRow;
            wtCol = newCol;
        }
        if (p == 'k') {
            btRow = newRow;
            btCol = newCol;
        }
        
        if (Character.isUpperCase(p)) {
            tf = !whiteInCheck(wtRow, wtCol);
        }
        if (Character.isLowerCase(p)) {
            tf = !blackInCheck(btRow, btCol);
        }

        chessBoard[prevRow][prevCol] = p;
        chessBoard[newRow][newCol] = temp;
        return tf;
    }

    public boolean whiteInCheck(int r, int c) {
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            if (Character.isLowerCase(chessBoard[row][col]) && isLegalMove(row, col, r, c)) {
	                return true;
	            }
	        }
	    }
        return false;
    }
    
    public boolean blackInCheck(int r, int c) {
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            if (Character.isUpperCase(chessBoard[row][col]) && isLegalMove(row, col, r, c)) {
	                return true;
	            }
	        }
	    }
        return false;
    }
    
    public boolean isMovablePiece(int prevRow, int prevCol) {
        for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            if (isLegalMove(prevRow, prevCol, row, col) && outOfCheck(prevRow, prevCol, row, col)) {
	                return true;
	            }
	        }
	    }
	    return false;
    }
    
    public boolean isLegalMove(int prevRow, int prevCol, int newRow, int newCol) {
        char p = chessBoard[prevRow][prevCol];

        if (prevRow == newRow && prevCol == newCol)
            return false;
        
        if (p == 'p')
            return isLegal_p(prevRow, prevCol, newRow, newCol);
    
        if (p == 'r')
            return isLegal_r(prevRow, prevCol, newRow, newCol);
        
        if (p == 'n')
            return isLegal_n(prevRow, prevCol, newRow, newCol);
        
        if (p == 'b')
            return isLegal_b(prevRow, prevCol, newRow, newCol);
        
        if (p == 'q')
            return isLegal_q(prevRow, prevCol, newRow, newCol);
        
        if (p == 'k')
            return isLegal_k(prevRow, prevCol, newRow, newCol);
        
        if (p == 'P')
            return isLegal_P(prevRow, prevCol, newRow, newCol);
        
        if (p == 'R')
            return isLegal_R(prevRow, prevCol, newRow, newCol);
        
        if (p == 'N')
            return isLegal_N(prevRow, prevCol, newRow, newCol);
        
        if (p == 'B')
            return isLegal_B(prevRow, prevCol, newRow, newCol);
        
        if (p == 'Q')
            return isLegal_Q(prevRow, prevCol, newRow, newCol);
        
        if (p == 'K')
            return isLegal_K(prevRow, prevCol, newRow, newCol);

        return false;
    }
    
    public boolean isLegal_p(int prevRow, int prevCol, int newRow, int newCol) {
        if (prevCol == newCol && (newRow - prevRow == 1) && chessBoard[newRow][newCol] == ' ')
            return true;
            
        if (prevCol == newCol && prevRow == 1 && newRow == 3 && chessBoard[2][prevCol] == ' ' && chessBoard[3][prevCol] == ' ') {
            return true;
        }
            
        if ((newCol - prevCol == 1) && (newRow - prevRow == 1) && (Character.isUpperCase(chessBoard[newRow][newCol]) || (newRow == 5 && whiteEnPassant[newCol] == 1)))
            return true;
            
        if ((newCol - prevCol == -1) && (newRow - prevRow == 1) && (Character.isUpperCase(chessBoard[newRow][newCol]) || (newRow == 5 && whiteEnPassant[newCol] == 1)))
            return true;
            
        return false;
    }   
    
    public boolean isLegal_r(int prevRow, int prevCol, int newRow, int newCol) {
        if (prevRow == newRow || prevCol == newCol) {
            if (prevRow == newRow && newCol > prevCol) {
                for (int i = prevCol + 1; i < newCol; i++) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            if (prevRow == newRow && newCol < prevCol) {
                for (int i = prevCol - 1; i > newCol; i--) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            
            if (prevCol == newCol && newRow > prevRow) {
                for (int i = prevRow + 1; i < newRow; i++) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            if (prevCol == newCol && newRow < prevRow) {
                for (int i = prevRow - 1; i > newRow; i--) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            
            if (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegal_n(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newRow - prevRow) == 2 && Math.abs(newCol - prevCol) == 1 && (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol])))
            return true;
            
        if (Math.abs(newRow - prevRow) == 1 && Math.abs(newCol - prevCol) == 2 && (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol])))
            return true;
            
        return false;
    }
    
    public boolean isLegal_b(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol-prevCol) == Math.abs(newRow - prevRow)) {
            if (prevRow - newRow == prevCol - newCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow+i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == prevCol - newCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow-i][prevCol-i] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == newCol - prevCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow-i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == newCol - prevCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow+i][prevCol-i] != ' ')
                        return false;
                }
            }
            if (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_q(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol-prevCol) == Math.abs(newRow - prevRow) || prevRow == newRow || prevCol == newCol) {
            
            if (prevRow == newRow && newCol > prevCol) {
                for (int i = prevCol + 1; i < newCol; i++) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            if (prevRow == newRow && newCol < prevCol) {
                for (int i = prevCol - 1; i > newCol; i--) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            
            if (prevCol == newCol && newRow > prevRow) {
                for (int i = prevRow + 1; i < newRow; i++) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            if (prevCol == newCol && newRow < prevRow) {
                for (int i = prevRow - 1; i > newRow; i--) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == prevCol - newCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow+i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == prevCol - newCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow-i][prevCol-i] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == newCol - prevCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++)
                {if (chessBoard[prevRow-i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == newCol - prevCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow+i][prevCol-i] != ' ')
                        return false;
                }
            }
            
            if (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_k(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol - prevCol) <= 1 && Math.abs(newRow - prevRow) <= 1) {
            if (chessBoard[newRow][newCol] == ' ' || Character.isUpperCase(chessBoard[newRow][newCol])) {
                return true;
            }
        }
        
        if (blackKingCastle && newCol == 6 && newRow == 0 && chessBoard[0][5] == ' ' && chessBoard[0][6] == ' ' &&
                !blackInCheck(0, 4) && !blackInCheck(0, 5) && !blackInCheck(0, 6)) {
            return true;   
        }
        return blackQueenCastle && newCol == 2 && newRow == 0 && chessBoard[0][3] == ' ' && chessBoard[0][2] == ' ' && chessBoard[0][1] == ' ' &&
                !blackInCheck(0, 4) && !blackInCheck(0, 3) && !blackInCheck(0, 2);
    }
    
    public boolean isLegal_P(int prevRow, int prevCol, int newRow, int newCol) {
        if (prevCol == newCol && prevRow - newRow == 1 && chessBoard[newRow][newCol] == ' ') {
            return true;
        }
            
        if (prevCol == newCol && prevRow == 6 && newRow == 4 && chessBoard[5][prevCol] == ' ' && chessBoard[4][prevCol] == ' ') {
            return true;
        }
            
        if ((newCol - prevCol == 1) && (prevRow - newRow == 1) && (Character.isLowerCase(chessBoard[newRow][newCol]) || (newRow == 2 && blackEnPassant[newCol] == 1))) {
            return true;
        }
            
        if ((newCol - prevCol == -1) && (prevRow - newRow == 1) && (Character.isLowerCase(chessBoard[newRow][newCol]) || (newRow == 2 && blackEnPassant[newCol] == 1))) {
            return true;
        }
            
        return false;
    }   
    
    public boolean isLegal_R(int prevRow, int prevCol, int newRow, int newCol) {
        if (prevRow == newRow || prevCol == newCol) {
            if (prevRow == newRow && newCol > prevCol) {
                for (int i = prevCol + 1; i < newCol; i++) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            if (prevRow == newRow && newCol < prevCol) {
                for (int i = prevCol - 1; i > newCol; i--) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            
            if (prevCol == newCol && newRow > prevRow) {
                for (int i = prevRow + 1; i < newRow; i++) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            if (prevCol == newCol && newRow < prevRow) {
                for (int i = prevRow - 1; i > newRow; i--) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            
            if (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegal_N(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newRow - prevRow) == 2 && Math.abs(newCol - prevCol) == 1 && (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol])))
            return true;
            
        if (Math.abs(newRow - prevRow) == 1 && Math.abs(newCol - prevCol) == 2 && (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol])))
            return true;
            
        return false;
    }
    
    public boolean isLegal_B(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol-prevCol) == Math.abs(newRow - prevRow)) {
            if (prevRow - newRow == prevCol - newCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow+i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == prevCol - newCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow-i][prevCol-i] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == newCol - prevCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow-i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == newCol - prevCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow+i][prevCol-i] != ' ')
                        return false;
                }
            }
            if (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_Q(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol-prevCol) == Math.abs(newRow - prevRow) || prevRow == newRow || prevCol == newCol) {
            if (prevRow == newRow && newCol > prevCol) {
                for (int i = prevCol + 1; i < newCol; i++) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            if (prevRow == newRow && newCol < prevCol) {
                for (int i = prevCol - 1; i > newCol; i--) {
                    if (chessBoard[prevRow][i] != ' ')
                        return false;
                }
            }
            if (prevCol == newCol && newRow > prevRow) {
                for (int i = prevRow + 1; i < newRow; i++) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            if (prevCol == newCol && newRow < prevRow) {
                for (int i = prevRow - 1; i > newRow; i--) {
                    if (chessBoard[i][prevCol] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == prevCol - newCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow+i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == prevCol - newCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow-i][prevCol-i] != ' ')
                        return false;
                }
            }
            
            if (prevRow - newRow == newCol - prevCol && newCol > prevCol) {
                for (int i = 1; i < (newCol - prevCol); i++) {
                    if (chessBoard[prevRow-i][prevCol+i] != ' ')
                        return false;
                }
            }
            if (prevRow - newRow == newCol - prevCol && newCol < prevCol) {
                for (int i = 1; i < (prevCol - newCol); i++) {
                    if (chessBoard[prevRow+i][prevCol-i] != ' ')
                        return false;
                }
            }
            if (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_K(int prevRow, int prevCol, int newRow, int newCol) {
        if (Math.abs(newCol - prevCol) <= 1 && Math.abs(newRow - prevRow) <= 1) {
            if (chessBoard[newRow][newCol] == ' ' || Character.isLowerCase(chessBoard[newRow][newCol])) {
                return true;
            }
        }

        if (whiteKingCastle && newCol == 6 && newRow == 7 && chessBoard[7][5] == ' ' && chessBoard[7][6] == ' ' &&
                !whiteInCheck(7, 4) && !whiteInCheck(7, 5) && !whiteInCheck(7, 6)) {
            return true;
        }
        if (whiteQueenCastle && newCol == 2 && newRow == 7 && chessBoard[7][3] == ' ' && chessBoard[7][2] == ' ' && chessBoard[7][1] == ' ' &&
                !whiteInCheck(7, 4) && !whiteInCheck(7, 3) && !whiteInCheck(7, 2)) {
            return true;
        }

        return false;
    }
}
