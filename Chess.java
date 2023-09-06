import java.lang.Math.*;

/**
 * Chess class to keep track of board and piece information. Logic methods to check for game conditions.
 *
 * @author Max Luo
 * @version May 2022
 */

public class Chess
{
    private char[][] _board = new char[8][8];
    private int[] whiteEP = new int[8];
    private int[] blackEP = new int[8];

    private boolean sym = false, kkCastle, kqCastle, KKCastle, KQCastle;
    public int wkRow, wkCol, bkRow, bkCol;

    //color{
    private final String RESET = "\u001B[0m";
    private final String BlackColor = "\u001B[31m";
    private final String WhiteColor = "\u001B[34m";
    private final String DarkBG = "\u001B[40m";
    private final String LightBG = "\u001B[47m";
    private final String DarkHL = "\033[43m";
    private final String LightHL = "\u001B[103m"; //}

//setters{

    public void Chess()
    {
        setBoard();
        removeEPW();
        removeEPB();
    }

    public void setTest()
    {
        char[][] arr = 
            {{'r',' ',' ',' ','k',' ',' ','r'},
             {' ','p',' ',' ',' ','b',' ',' '},
             {' ','n',' ',' ',' ',' ',' ',' '},
             {' ',' ','P',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ','B',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {'R',' ',' ',' ','K',' ',' ','R'}};
                 
        for(int row = 0; row < 8; row++)
        {
            for(int col = 2; col < 8; col++)
            {
                if(arr[row][col] == 'K')
                {
                    wkRow = row;
                    wkCol = col;
                }
                if(arr[row][col] == 'k')
                {
                    bkRow = row;
                    bkCol = col;
                }
            }
        }
        
        KKCastle = true;
        KQCastle = true;
        kkCastle = true;
        kqCastle = true;

        _board = arr;
    }
    
    public void setBoard()
    {
        char[][] arr = 
            {{'r','n','b','q','k','b','n','r'},
             {'p','p','p','p','p','p','p','p'},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {' ',' ',' ',' ',' ',' ',' ',' '},
             {'P','P','P','P','P','P','P','P'},
             {'R','N','B','Q','K','B','N','R'}};
                 
        _board = arr;
        
        wkRow = 7;
        wkCol = 4;
        bkRow = 0;
        bkCol = 4;
        
        KKCastle = true;
        KQCastle = true;
        kkCastle = true;
        kqCastle = true;
    }
    
    public void setPiece(int r, int c, char p)
    {
        _board[r][c] = p;
    }
    
//}

//doers{  

    public char getPiece(int r, int c)
    {
        return _board[r][c];
    }
    
    public void doMove(int pRow, int pCol, int nRow, int nCol)
    {
        char p = _board[pRow][pCol];
        
        if(p == 'R' && pCol == 7)
            KKCastle = false;
        if(p == 'R' && pCol == 0)
            KQCastle = false;
            
        if(p == 'r' && pCol == 7)
            kkCastle = false;
        if(p == 'r' && pCol == 0)
            kqCastle = false;
           
        if(p == 'K')  
        {
            KKCastle = false;
            KQCastle = false;
            wkRow = nRow;
            wkCol = nCol;
        }
        if(p == 'k')  
        {
            kkCastle = false;
            kqCastle = false;
            bkRow = nRow;
            bkCol = nCol;
        }
           
        _board[pRow][pCol] = ' ';
        _board[nRow][nCol] = p;
        
        if(p == 'P' && nRow == 2 && blackEP[nCol] == 1)
        {
            _board[3][nCol] = ' ';
        }
        if(p == 'p' && nRow == 5 && whiteEP[nCol] == 1)
        {
            _board[4][nCol] = ' ';
        }
    }
    
    public int doCastle(int pRow, int pCol, int nCol)
    {
        char p = _board[pRow][pCol];
        if(p == 'K')
        {
            if(KKCastle == true && nCol == 6)
            {
                _board[7][5] = 'R';
                _board[7][7] = ' ';
                return 1;
            }
            if(KQCastle == true && nCol == 2)
            {
                _board[7][3] = 'R';
                _board[7][0] = ' ';
                return 2;
            }
        }
        if(p == 'k')
        {
            if(kkCastle == true && nCol == 6)
            {
                _board[0][5] = 'r';
                _board[0][7] = ' ';
                return 1;
            }
            if(kqCastle == true && nCol == 2)
            {
                _board[0][3] = 'r';
                _board[0][0] = ' ';
                return 2;
            }
        }
        return 0;
    }

    public void doEP(int pRow, int pCol, int nRow, int nCol)
    {
        char p = _board[pRow][pCol];
        if(p == 'P' && pCol == nCol && pRow == 6 && nRow == 4 && _board[5][pCol] == ' ')
        {
            whiteEP[pCol] = 1;
        }
        if(p == 'p' && pCol == nCol && pRow == 1 && nRow == 3 && _board[2][pCol] == ' ')
        {
            blackEP[pCol] = 1;
        }
    }
    
    public void removeEPW()
    {
        for(int i = 0; i < 8; i++)
        {
            whiteEP[i] = 0;
        }
    }
    
    public void removeEPB()
    {
        for(int i = 0; i < 8; i++)
        {
            blackEP[i] = 0;
        }
    }
//} 

//print{

    public void printBoard()
    {
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");

        for (int row = 0; row < 8; row++)
	    {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++)
	        {
	            if(Character.isUpperCase(_board[row][col]) || _board[row][col] == '!')
                {
                    System.out.print(WhiteColor);
                }
                if(Character.isLowerCase(_board[row][col]) || _board[row][col] == '?')
                {
                    System.out.print(BlackColor);
                }
	            if((row + col) % 2 == 1)
	            {
	                System.out.print(DarkBG);
	            }
	            else
	            {
	                System.out.print(LightBG);
	            }
	            System.out.print(pieceTOsymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " + (8 - row));
	    }
	    System.out.println("  A B C D E F G H\n");
    }
    
    public void showMoves(int r, int c)
    {
        char p = _board[r][c];
        
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");
        for (int row = 0; row < 8; row++)
	    {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++)
	        {
	            if(isLegal(r, c, row, col) == true && outOfCheck(r, c, row, col) == true)
	            {
	                if((row + col) % 2 == 1)
    	            {
    	                System.out.print(DarkHL);
    	            }
    	            else
    	            {
    	                System.out.print(LightHL);
    	            }
	            }
	            else
	            { 
	                if((row + col) % 2 == 1)
    	            {
    	                System.out.print(DarkBG);
    	            }
    	            else
    	            {
    	                System.out.print(LightBG);
    	            }
	            }
	            if(Character.isUpperCase(_board[row][col]) || _board[row][col] == '!')
                {
                    System.out.print(WhiteColor);
                }
                if(Character.isLowerCase(_board[row][col]) || _board[row][col] == '?')
                {
                    System.out.print(BlackColor);
                }
	            System.out.print(pieceTOsymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " +(8 - row) + " ");
	    }
	    System.out.println("  A B C D E F G H\n");
    }

    public void showPlay(int pr, int pc, int nr, int nc)
    {
        char p = _board[pr][pc];
        
        System.out.println(RESET);
        System.out.println("  A B C D E F G H");
        for (int row = 0; row < 8; row++)
	    {
	        System.out.print((8 - row) + " ");
	        for (int col = 0; col < 8; col++)
	        {
	            if(row == pr && col == pc || row == nr && col == nc)
	            {
	                if((row + col) % 2 == 1)
    	            {
    	                System.out.print(DarkHL);
    	            }
    	            else
    	            {
    	                System.out.print(LightHL);
    	            }
	            }
	            else
	            { 
	                if((row + col) % 2 == 1)
    	            {
    	                System.out.print(DarkBG);
    	            }
    	            else
    	            {
    	                System.out.print(LightBG);
    	            }
	            }
	            if(Character.isUpperCase(_board[row][col]) || _board[row][col] == '!')
                {
                    System.out.print(WhiteColor);
                }
                if(Character.isLowerCase(_board[row][col]) || _board[row][col] == '?')
                {
                    System.out.print(BlackColor);
                }
	            System.out.print(pieceTOsymbol(row, col) + " " + RESET);
	        }
	        System.out.println(" " +(8 - row) + " ");
	    }
	    System.out.println("  A B C D E F G H\n");
    }
//} 

//basic comp{

    public int abcTO123(char c)
    {
        int pos = c - 'a' + 1;
        return pos;
    }
    
    public char numTOabc(int i) 
    {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        return alphabet[i];
    }
    
    public String pieceTOname(int r, int c)
    {
        char p = Character.toLowerCase(_board[r][c]);
        
        if(p =='p')
        return "pawn";
        
        if(p =='r')
        return "rook";
        
        if(p =='n')
        return "knight";
        
        if(p =='b')
        return "bishop";
        
        if(p =='q')
        return "queen";
        
        if(p =='k')
        return "king";
        
        return "what";
    }
    
    public char pieceTOsymbol(int r, int c)
    {
        char p = Character.toLowerCase(_board[r][c]);
        
        if(sym == false)
        return p;
        
        if(p =='p')
        return '♙';
        
        if(p =='r')
        return '♖';
        
        if(p =='n')
        return '♘';
        
        if(p =='b')
        return '♗';
        
        if(p =='q')
        return '♕';
        
        if(p =='k')
        return '♔';
        
        return ' ';
    }
    
    public boolean canWpromote(int r, int c)
    {
        return (_board[r][c] == 'P' && r == 0);
    }
    
    public boolean canBpromote(int r, int c)
    {
        return (_board[r][c] == 'p' && r == 7);
    }
    
    public boolean isUpper(int r, int c)
    {
        return Character.isUpperCase(_board[r][c]);
    }
    
    public boolean isLower(int r, int c)
    {
        return Character.isLowerCase(_board[r][c]);
    }
    
//}
    
//win{

    public boolean noWin()
    {
        int W = 0, B = 0;
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
	            if(_board[row][col] == 'N' || _board[row][col] == 'B')
	                W++;
                if(_board[row][col] == 'n' || _board[row][col] == 'b')
                    B++;
	       
	            if(_board[row][col] == 'P' || _board[row][col] == 'p' || 
	               _board[row][col] == 'R' || _board[row][col] == 'r' || 
	               _board[row][col] == 'Q' || _board[row][col] == 'q')
	            {
	                return false;
	            }
	            if(W >= 2 || B >= 2)
	            {
	                return false;
	            }
	        }
	    }
	    return true;
    }

    public int winVSwhite()
    {
        boolean tf = false;
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
                if(Character.isUpperCase(_board[row][col]))
                {
                    for (int r = 0; r < 8; r++)
            	    {
            	        for (int c = 0; c < 8; c++)
            	        {
                            if(isLegal(row, col, r, c) == true && outOfCheck(row, col, r, c) == true)
                            {
                                tf = true;
                            }
            	        }
            	    }
                }
	        }   
	    }
	    if(tf == false && whiteInCheck(wkRow, wkCol) == true)
	        return 1;
	        
	    if(tf == false && whiteInCheck(wkRow, wkCol) == false)
	        return 2;
	   
	    return 0;
    }
    
    public int winVSblack()
    {
        boolean tf = false;
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
                if(Character.isLowerCase(_board[row][col]))
                {
                    for (int r = 0; r < 8; r++)
            	    {
            	        for (int c = 0; c < 8; c++)
            	        {
                            if(isLegal(row, col, r, c) == true && outOfCheck(row, col, r, c) == true)
                            {
                                tf = true;
                            }
            	        }
            	    }
                }
	        }   
	    }
	    if(tf == false && blackInCheck(bkRow, bkCol) == true)
	        return 1;
	        
	    if(tf == false && blackInCheck(bkRow, bkCol) == false)
	        return 2;
	   
	    return 0;
    }

//}  

//complex comp{

    public String notationInfo(int pRow, int pCol, int nRow, int nCol)
    {
        char p = _board[pRow][pCol];
        String ret = "";
        
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
	            if(_board[row][col] == p && (row != pRow || col != pCol) && 
	               isLegal(row, col, nRow, nCol) == true && outOfCheck(row, col, nRow, nCol) == true)
	            {
                    if(row == pRow)
	                {
	                    ret += Character.toLowerCase(numTOabc(pCol));
	                }
	                else if(col == pCol)
	                {
	                    ret += (8 - pRow);
	                }
	                else if(row != pRow && col != pCol)
	                {
	                    ret += Character.toLowerCase(numTOabc(pCol));
	                }
	            }
	        }
	    }
        return ret;
    }
    
    public boolean outOfCheck(int pRow, int pCol, int nRow, int nCol)
    {
        char p = _board[pRow][pCol];
        boolean tf = true;
        int wtRow = wkRow, wtCol = wkCol, btRow = bkRow, btCol = bkCol;
        
        char temp = _board[nRow][nCol];
        _board[pRow][pCol] = ' ';
        _board[nRow][nCol] = p;
        
        if(p == 'K')
        {
            wtRow = nRow;
            wtCol = nCol;
        }
        if(p == 'k')
        {
            btRow = nRow;
            btCol = nCol;
        }
        
        if(Character.isUpperCase(p))
        {
            tf = !whiteInCheck(wtRow, wtCol);
        }
        if(Character.isLowerCase(p))
        {
            tf = !blackInCheck(btRow, btCol);
        }

        _board[pRow][pCol] = p;
        _board[nRow][nCol] = temp;
        return tf;
    }

    public boolean whiteInCheck(int r, int c)
    {
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
	            if(Character.isLowerCase(_board[row][col]) && isLegal(row, col, r, c) == true)
	            {
	                return true;
	            }
	        }
	    }
        return false;
    }
    
    public boolean blackInCheck(int r, int c)
    {
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
	            if(Character.isUpperCase(_board[row][col]) && isLegal(row, col, r, c) == true)
	            {
	                return true;
	            }
	        }
	    }
        return false;
    }
    
    public boolean isPossible(int pRow, int pCol)
    {
        for (int row = 0; row < 8; row++)
	    {
	        for (int col = 0; col < 8; col++)
	        {
	            if(isLegal(pRow, pCol, row, col) == true && outOfCheck(pRow, pCol, row, col) == true)
	            {
	                return true;
	            }
	        }
	    }
	    return false;
    }
    
  //public boolean isLegal(int pRow, int pCol, int nRow, intnCol) {

    public boolean isLegal(int pRow, int pCol, int nRow, int nCol)
    {
        char p = _board[pRow][pCol];

        if(pRow == nRow && pCol == nCol)
        {
            return false;
        }
        else if (p == 'p')
        {
            return isLegal_p(pRow, pCol, nRow, nCol);
        }
        else if (p == 'r')
        {
            return isLegal_r(pRow, pCol, nRow, nCol);
        }
        else if (p == 'n')
        {
            return isLegal_n(pRow, pCol, nRow, nCol);
        }
        else if (p == 'b')
        {
            return isLegal_b(pRow, pCol, nRow, nCol);
        }
        else if (p == 'q')
        {
            return isLegal_q(pRow, pCol, nRow, nCol);
        }
        else if (p == 'k')
        {
            return isLegal_k(pRow, pCol, nRow, nCol);
        }
        else if (p == 'P')
        {
            return isLegal_P(pRow, pCol, nRow, nCol);
        }
        else if (p == 'R')
        {
            return isLegal_R(pRow, pCol, nRow, nCol);
        }
        else if (p == 'N')
        {
            return isLegal_N(pRow, pCol, nRow, nCol);
        }
        else if (p == 'B')
        {
            return isLegal_B(pRow, pCol, nRow, nCol);
        }
        else if (p == 'Q')
        {
            return isLegal_Q(pRow, pCol, nRow, nCol);
        }
        else if (p == 'K')
        {
            return isLegal_K(pRow, pCol, nRow, nCol);
        }
        else
        {
            return false;
        }
    }
    
    public boolean isLegal_p(int pRow, int pCol, int nRow, int nCol)
    {
        if(pCol == nCol && (nRow - pRow == 1) && _board[nRow][nCol] == ' ')
            return true;
            
        if(pCol == nCol && pRow == 1 && nRow == 3 && _board[2][pCol] == ' ' && _board[3][pCol] == ' ')
        {
            return true;
        }
            
        if((nCol - pCol == 1) && (nRow - pRow == 1) && (Character.isUpperCase(_board[nRow][nCol]) || (nRow == 5 && whiteEP[nCol] == 1)))
            return true;
            
        if((nCol - pCol == -1) && (nRow - pRow == 1) && (Character.isUpperCase(_board[nRow][nCol]) || (nRow == 5 && whiteEP[nCol] == 1)))
            return true;
            
        return false;
    }   
    
    public boolean isLegal_r(int pRow, int pCol, int nRow, int nCol)
    {
        if (pRow == nRow || pCol == nCol)
        {
            if(pRow == nRow && nCol > pCol)
            {
                for(int i = pCol + 1; i < nCol; i++)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            if(pRow == nRow && nCol < pCol)
            {
                for(int i = pCol - 1; i > nCol; i--)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            
            if(pCol == nCol && nRow > pRow)
            {
                for(int i = pRow + 1; i < nRow; i++)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            if(pCol == nCol && nRow < pRow)
            {
                for(int i = pRow - 1; i > nRow; i--)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            
            if(_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol]))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegal_n(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nRow - pRow) == 2 && Math.abs(nCol - pCol) == 1 && (_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol])))
            return true;
            
        if(Math.abs(nRow - pRow) == 1 && Math.abs(nCol - pCol) == 2 && (_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol])))
            return true;
            
        return false;
    }
    
    public boolean isLegal_b(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol-pCol) == Math.abs(nRow - pRow))
        {
            if(pRow - nRow == pCol - nCol && nCol > pCol) //bottom right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow+i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == pCol - nCol && nCol < pCol) //top left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow-i][pCol-i] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == nCol - pCol && nCol > pCol) //top right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow-i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == nCol - pCol && nCol < pCol) //bottom left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow+i][pCol-i] != ' ')
                        return false;
                }
            }
            if(_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_q(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol-pCol) == Math.abs(nRow - pRow) || pRow == nRow || pCol == nCol)
        {
            
            if(pRow == nRow && nCol > pCol)
            {
                for(int i = pCol + 1; i < nCol; i++)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            if(pRow == nRow && nCol < pCol)
            {
                for(int i = pCol - 1; i > nCol; i--)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            
            if(pCol == nCol && nRow > pRow)
            {
                for(int i = pRow + 1; i < nRow; i++)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            if(pCol == nCol && nRow < pRow)
            {
                for(int i = pRow - 1; i > nRow; i--)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == pCol - nCol && nCol > pCol) //bottom right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow+i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == pCol - nCol && nCol < pCol) //top left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow-i][pCol-i] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == nCol - pCol && nCol > pCol) //top right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow-i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == nCol - pCol && nCol < pCol) //bottom left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow+i][pCol-i] != ' ')
                        return false;
                }
            }
            
            if(_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_k(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol - pCol) <= 1 && Math.abs(nRow - pRow) <= 1)
        {
            if(_board[nRow][nCol] == ' ' || Character.isUpperCase(_board[nRow][nCol]))
            {
                return true;
            }
        }
        
        if(kkCastle == true && nCol == 6 && nRow == 0 && _board[0][5] == ' ' && _board[0][6] == ' ' && 
           blackInCheck(0, 4) == false && blackInCheck(0, 5) == false && blackInCheck(0, 6) == false)
        {
            return true;   
        }
        if(kqCastle == true && nCol == 2 && nRow == 0 && _board[0][3] == ' ' && _board[0][2] == ' ' && _board[0][1] == ' ' &&
           blackInCheck(0, 4) == false && blackInCheck(0, 3) == false && blackInCheck(0, 2) == false)        
        {
            return true;
        }

        return false;
    }
    
    public boolean isLegal_P(int pRow, int pCol, int nRow, int nCol)
    {
        if(pCol == nCol && pRow - nRow == 1 && _board[nRow][nCol] == ' ')
        {
            return true;
        }
            
        if(pCol == nCol && pRow == 6 && nRow == 4 && _board[5][pCol] == ' ' && _board[4][pCol] == ' ')
        {
            return true;
        }
            
        if((nCol - pCol == 1) && (pRow - nRow == 1) && (Character.isLowerCase(_board[nRow][nCol]) || (nRow == 2 && blackEP[nCol] == 1)))
        {
            return true;
        }
            
        if((nCol - pCol == -1) && (pRow - nRow == 1) && (Character.isLowerCase(_board[nRow][nCol]) || (nRow == 2 && blackEP[nCol] == 1)))
        {
            return true;
        }
            
        return false;
    }   
    
    public boolean isLegal_R(int pRow, int pCol, int nRow, int nCol)
    {
        if (pRow == nRow || pCol == nCol)
        {
            if(pRow == nRow && nCol > pCol)
            {
                for(int i = pCol + 1; i < nCol; i++)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            if(pRow == nRow && nCol < pCol)
            {
                for(int i = pCol - 1; i > nCol; i--)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            
            if(pCol == nCol && nRow > pRow)
            {
                for(int i = pRow + 1; i < nRow; i++)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            if(pCol == nCol && nRow < pRow)
            {
                for(int i = pRow - 1; i > nRow; i--)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            
            if(_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol]))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegal_N(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nRow - pRow) == 2 && Math.abs(nCol - pCol) == 1 && (_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol])))
            return true;
            
        if(Math.abs(nRow - pRow) == 1 && Math.abs(nCol - pCol) == 2 && (_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol])))
            return true;
            
        return false;
    }
    
    public boolean isLegal_B(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol-pCol) == Math.abs(nRow - pRow))
        {
            if(pRow - nRow == pCol - nCol && nCol > pCol) //bottom right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow+i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == pCol - nCol && nCol < pCol) //top left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow-i][pCol-i] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == nCol - pCol && nCol > pCol) //top right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow-i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == nCol - pCol && nCol < pCol) //bottom left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow+i][pCol-i] != ' ')
                        return false;
                }
            }
            if(_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol]))
                    return true;
        }
        return false;
    }
    
    public boolean isLegal_Q(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol-pCol) == Math.abs(nRow - pRow) || pRow == nRow || pCol == nCol)
        {
            if(pRow == nRow && nCol > pCol)
            {
                for(int i = pCol + 1; i < nCol; i++)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            if(pRow == nRow && nCol < pCol)
            {
                for(int i = pCol - 1; i > nCol; i--)
                {
                    if(_board[pRow][i] != ' ')
                        return false;
                }
            }
            if(pCol == nCol && nRow > pRow)
            {
                for(int i = pRow + 1; i < nRow; i++)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            if(pCol == nCol && nRow < pRow)
            {
                for(int i = pRow - 1; i > nRow; i--)
                {
                    if(_board[i][pCol] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == pCol - nCol && nCol > pCol) //bottom right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow+i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == pCol - nCol && nCol < pCol) //top left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow-i][pCol-i] != ' ')
                        return false;
                }
            }
            
            if(pRow - nRow == nCol - pCol && nCol > pCol) //top right
            {
                for(int i = 1; i < (nCol - pCol); i++)
                {
                    if(_board[pRow-i][pCol+i] != ' ')
                        return false;
                }
            }
            if(pRow - nRow == nCol - pCol && nCol < pCol) //bottom left
            {
                for(int i = 1; i < (pCol - nCol); i++)
                {
                    if(_board[pRow+i][pCol-i] != ' ')
                        return false;
                }
            }
            if(_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol]))
                return true;
        }
        return false;
    }
    
    public boolean isLegal_K(int pRow, int pCol, int nRow, int nCol)
    {
        if(Math.abs(nCol - pCol) <= 1 && Math.abs(nRow - pRow) <= 1)
        {
            if(_board[nRow][nCol] == ' ' || Character.isLowerCase(_board[nRow][nCol]))
            {
                return true;
            }
        }
        
        if(KKCastle == true && nCol == 6 && nRow == 7 && _board[7][5] == ' ' && _board[7][6] == ' ' &&
           whiteInCheck(7, 4) == false && whiteInCheck(7, 5) == false && whiteInCheck(7, 6) == false)
        {
            return true;   
        }
        if(KQCastle == true && nCol == 2 && nRow == 7 && _board[7][3] == ' ' && _board[7][2] == ' ' && _board[7][1] == ' ' &&
           whiteInCheck(7, 4) == false && whiteInCheck(7, 3) == false && whiteInCheck(7, 2) == false)        
        {
            return true;
        }
        
        return false;
    }
    
//}

//}

}
