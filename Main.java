import java.util.*;
import java.lang.Math.*;
import java.util.Random;

/**
 * Chess in Terminal
 *
 * A very bare bones and industry nonstandard chess game that I made my junior year of high school.
 * Select 2 player or 1 player mode (you play against random moves), and select moves with chess coordinates.
 * Have fun!
 *
 * @author Max Luo
 * @version May 2022
 */

public class Main
{
    public static void main(String[] args) 
    {
	    Chess chs = new Chess();
		Scanner sc = new Scanner(System.in);
	    
		final String RESET = "\u001B[0m";
		final String[] color = {"\u001B[34m", "\u001B[31m"};

		int win = 0, mode = 0, player = 0, plays = 0;
    	int pRow, pCol, nRow, nCol;
		String add = "", notation = "";

		String input = "xx", prom = "x";
        String[] name = {"blue", "red"};
        String[] piecesOf = {"P R N B Q K", "p r n b q k"};
        
        System.out.println("Welcome to Chess:");
        while (true)
        {
            System.out.print("Input mode: 1 player | 2 player: ");
            mode = sc.nextInt();
            
            if(mode == 1 || mode == 2 || mode == 999)
                break;
                
            System.out.println("Invalid: input 1 or 2\n");
        }
        
        input = sc.nextLine();
        
        chs.Chess();
        chs.printBoard();
        
        while(true && win == 0) //game while loop
        {
            if(plays % 2 == 0)
                player = 0;
            if(plays % 2 == 1)
                player = 1;
            
            if((mode == 2 || player == 0) && mode != 999) //player
            {
                System.out.print(color[player]);
                System.out.println(name[player] + "'s turn (" + piecesOf[player] + ")");
                
                while(true) //p
                {
                    while(true)
                    {
						System.out.print("Enter location of piece (a-h)(1-8): ");
						input = sc.nextLine();
			
						if(input.length() != 2)
						{
			 			    System.out.println(RESET + "\nInvalid: incorrect # of inputs\n" + color[player]);
						}
						else
						{
							pRow = 8 - ((int)(input.charAt(1)) - 48);
						    pCol = chs.abcTO123(input.charAt(0)) - 1;
						    if(pRow > 8 || pRow < 0 || pCol > 8 || pCol < 0)
							{
							    System.out.println(RESET + "\nInvalid: out of bounds\n" + color[player]);
							}
							else
							{
							    break;
							}
						}
                    }
                	
				    if(player == 0 && chs.isUpper(pRow, pCol) == false || player == 1 && chs.isLower(pRow, pCol) == false)
				    {
						System.out.println(RESET + "\nInvalid: no " + name[player] + " piece here\n" + color[player]);
				    }
				    else if(chs.isPossible(pRow, pCol) == false)
				    {
						System.out.println(RESET + "\nInvalid: " + chs.pieceTOname(pRow, pCol) + " does not have a move\n" + color[player]);
				    }
				    else 
			        {
						break;
				    }
                }
                
                chs.showMoves(pRow, pCol);
             
                while(true) //n
                {
            		System.out.print(color[player] + "Move " + chs.pieceTOname(pRow, pCol) + " to (a-h)(1-8): ");
            		input = sc.nextLine();
            		
            		if(input.length() != 2)
            		{
            		    System.out.println(RESET + "\nInvalid: incorrect # of inputs\n" + color[player]);
            		}
            		else
            		{
    		            nRow = 8 - ((int)(input.charAt(1)) - 48);
            		    nCol = chs.abcTO123(input.charAt(0)) - 1;
            		    if(nRow > 7 || nRow < 0 || nCol > 7 || nCol < 0)
                		{
                		    System.out.println(RESET + "Invalid: out of bounds\n" + color[player]);
                		}
                		else
                		{
                		    break;
                		}
            		}
                }
            
            	if(chs.isLegal(pRow, pCol, nRow, nCol) == false)
                {
                    System.out.println(RESET + "\nInvalid: not a legal move\n " + color[player]);
                }
                else if(chs.outOfCheck(pRow, pCol, nRow, nCol) == false)
                {
                    System.out.println(RESET + "\nInvalid: puts you in check\n " + color[player]);
                }
                else
            	{
            	    plays++;
            	    
        	    //notation{    
                    add = RESET; //reset
                    if(player == 0)
                        add += ((plays+1)/2) + ". "; //move#
                    add += color[player];
                    
                    int castle = chs.doCastle(pRow, pCol, nCol);
                    if(castle == 0)
                    {
                        if(Character.toLowerCase(chs.getPiece(pRow, pCol)) != 'p') //if non pawn
                        {
                            add += Character.toUpperCase(chs.getPiece(pRow, pCol)); //piece
                            add += chs.notationInfo(pRow, pCol, nRow, nCol); //piece(ambiguous)
                        }

                        if(player == 0 && Character.isLowerCase(chs.getPiece(nRow, nCol)) ||
                           player == 1 && Character.isUpperCase(chs.getPiece(nRow, nCol))) //if takes
                        {
                            if(Character.toLowerCase(chs.getPiece(pRow, pCol)) == 'p')
                            {
                                add += Character.toLowerCase(chs.numTOabc(pCol)); //pawn file
                            }
                            add +=  "x"; //takes
                        }
                        
                        add += Character.toLowerCase(chs.numTOabc(nCol));   //new move file
                        add += (8 - nRow);                                  //new move rank
                    }
                    if(castle == 1)
                    {
                        add += "0-0";
                    }
                    if(castle == 2)
                    {
                        add += "0-0-0";
                    }
                //}
                
            	    chs.doEP(pRow, pCol, nRow, nCol);
                    chs.doMove(pRow, pCol, nRow, nCol);            	

            	    if(player == 0)
            	        chs.removeEPB();
            	    if(player == 1)
            	        chs.removeEPW();
    
            	    if(chs.canWpromote(nRow, nCol) == true)
            	    {
            	        while(true)
                        {
                    		System.out.print("\nPromote pawn (R N B Q): ");
                    		prom = sc.nextLine();
                    		
                    		if(prom.equals("R") || prom.equals("N") || prom.equals("B") || prom.equals("Q"))
                    		{
                    		    chs.setPiece(nRow, nCol, prom.charAt(0));
                    		    add += "=" + prom; //promote
                    		    break;
                    		}
                    		System.out.println(RESET + "Invalid: chooese one piece\n" + color[player]);
                        }
            	    }
            	    if(chs.canBpromote(nRow, nCol) == true)
            	    {
            	        while(true)
                        {
                    		System.out.print("\nPromote pawn (r n b q): ");
                    		prom = sc.nextLine();
                    		
                    		if(prom.equals("r") || prom.equals("n") || prom.equals("b") || prom.equals("q"))
                    		{
                    		    chs.setPiece(nRow, nCol, prom.charAt(0));
                    		    add += "=" + prom; //promote
                    		    break;
                    		}
                    		System.out.println(RESET + "Invalid: chooese one piece\n" + color[player]);
                        }
            	    }
            	    
                    chs.showPlay(pRow, pCol, nRow, nCol);
            	
            	//win{
            	    if(chs.noWin() == true)
                    {
                        System.out.println("Tie in " + plays + " plays! Drawn position");
                        win = 2;
                    }
                    if(player == 1 && chs.winVSwhite() == 2)
                    {
                        System.out.println("Tie in " + plays + " plays! Blue in stalemate");
                        win = 2;
                    }
                    if(player == 0 && chs.winVSblack() == 2)
                    {
                        System.out.println("Tie in " + plays + " plays! Red in stalemate");
                        win = 2;
                    }
                    if(chs.winVSwhite() == 1)
                    {
                        System.out.println("Red has won in " + plays + " plays!");
                        win = 1;
                    }
                    if(chs.winVSblack() == 1)
                    {
                        System.out.println("Blue has won in " + plays + " plays!");
                        win = 1;
                    }
                    if(win == 1)
                    {
                        notation += add + "#";
                        System.out.println(notation + "\n");
                    }
                    if(win == 2)
                    {
                        notation += add + " 1/2-1/2";
                        System.out.println(notation + "\n");
                    }
            	//}
            	
                    if(win == 0 && chs.whiteInCheck(chs.wkRow, chs.wkCol) == true)
            	    {
            	        System.out.println("Blue is in check\n");
            	        add += "+";
            	    }
            	    if(win == 0 && chs.blackInCheck(chs.bkRow, chs.bkCol) == true)
            	    {
            	        System.out.println("Red is in check\n");
            	        add += "+";
            	    }
            	    
            	add += " ";
            	notation += add;
            	 
            	}
            } //player
    
            if(mode == 1 && player == 1 || mode == 999) //computer
            {
                while(true)
                {
                	pRow = (int)(Math.random()*8);
                	pCol = (int)(Math.random()*8);
    
                	if((player == 0 && chs.isUpper(pRow, pCol) == true ||
                	    player == 1 && chs.isLower(pRow, pCol) == true) && 
                	    chs.isPossible(pRow, pCol) == true)
                	{
                	    break;
                	}
                }
                
                while(true)
                {
            		nRow = (int)(Math.random()*8);
                	nCol = (int)(Math.random()*8);
    
                	if(chs.isLegal(pRow, pCol, nRow, nCol) == true &&
                	   chs.outOfCheck(pRow, pCol, nRow, nCol) == true)
                    {
                        plays++;
                        if(mode != 999)
                        {
                            System.out.print(color[player]);
                            System.out.println("Computer plays " + chs.numTOabc(pCol) + (8 - pRow) + " " + chs.pieceTOname(pRow, pCol) + " to " + chs.numTOabc(nCol) + (8 - nRow));
                            if(chs.isUpper(nRow, nCol) == true)
                            {
                                System.out.println("Captures " + color[0] + chs.pieceTOname(nRow, nCol));
                            }
                        }
                        
                    //notation{    
                        add = RESET; //reset
                        if(player == 0)
                            add += ((plays+1)/2) + ". "; //move#
                        add += color[player];
                        
                        int castle = chs.doCastle(pRow, pCol, nCol);
                        if(castle == 0)
                        {
                            if(Character.toLowerCase(chs.getPiece(pRow, pCol)) != 'p') //if non pawn
                            {
                                add += Character.toUpperCase(chs.getPiece(pRow, pCol)); //piece
                                add += chs.notationInfo(pRow, pCol, nRow, nCol); //piece(ambiguous)
                            }
    
                            if(player == 0 && Character.isLowerCase(chs.getPiece(nRow, nCol)) ||
                               player == 1 && Character.isUpperCase(chs.getPiece(nRow, nCol))) //if takes
                            {
                                if(Character.toLowerCase(chs.getPiece(pRow, pCol)) == 'p')
                                {
                                    add += Character.toLowerCase(chs.numTOabc(pCol)); //pawn file
                                }
                                add +=  "x"; //takes
                            }
                            
                            add += Character.toLowerCase(chs.numTOabc(nCol)); //new move file
                            add += (8 - nRow);                                               //new move rank
                        }
                        if(castle == 1)
                        {
                            add += "0-0";
                        }
                        if(castle == 2)
                        {
                            add += "0-0-0";
                        }
                    //}
                        
                        chs.doEP(pRow, pCol, nRow, nCol);
                        chs.doMove(pRow, pCol, nRow, nCol);

                        if(player == 0)
                	        chs.removeEPB();
                	    if(player == 1)
                	        chs.removeEPW();
                	    
                	    if(chs.canWpromote(nRow, nCol) == true)
                	    {
                	        chs.setPiece(nRow, nCol, 'Q');
                            add += "=" + "Q"; //promote
                	    }
                	    if(chs.canBpromote(nRow, nCol) == true)
                	    {
                	        chs.setPiece(nRow, nCol, 'q');
                            add += "=" + "q"; //promote
                	    }
                	    
                	    if(mode != 999)
                            chs.showPlay(pRow, pCol, nRow, nCol);
                    
                    //win{
                	    if(chs.noWin() == true)
                        {
                            System.out.println("Tie in " + plays + " plays! Drawn position");
                            win = 2;
                        }
                        if(player == 1 && chs.winVSwhite() == 2)
                        {
                            System.out.println("Tie in " + plays + " plays! Blue in stalemate");
                            win = 2;
                        }
                        if(player == 0 && chs.winVSblack() == 2)
                        {
                            System.out.println("Tie in " + plays + " plays! Red in stalemate");
                            win = 2;
                        }
                        if(chs.winVSwhite() == 1)
                        {
                            System.out.println("Red has won in " + plays + " plays!");
                            win = 1;
                        }
                        if(chs.winVSblack() == 1)
                        {
                            System.out.println("Blue has won in " + plays + " plays!");
                            win = 1;
                        }
                        if(win == 1)
                        {
                            if(mode == 999)
                                chs.showPlay(pRow, pCol, nRow, nCol);
                                
                            notation += add + "#";
                            System.out.println(notation + RESET + "\n");
                        }
                        if(win == 2)
                        {
                            if(mode == 999)
                                chs.showPlay(pRow, pCol, nRow, nCol);
                                
                            notation += add + " 1/2-1/2";
                            System.out.println(notation + RESET + "\n");
                        }
                	//}
                	
                	    if(mode != 999 && win == 0 && chs.whiteInCheck(chs.wkRow, chs.wkCol) == true)
                	    {
                	        System.out.println("Blue is in check\n");
                	        add += "+";
                	    }
                        if(mode != 999 && win == 0 && chs.blackInCheck(chs.wkRow, chs.wkCol) == true)
                	    {
                	        System.out.println("Red is in check\n");
                	        add += "+";
                	    }
                	    
                	    add += " ";
                	    notation += add;
                	    
                    	break;
                	}
            	}
            } //computer
            
            if(win != 0)
            {
                chs.Chess();
                notation = "";
                plays = 0;
                player = 0;
                
                while(true)
                {
                    System.out.print(RESET + "New Game? (y/n): ");
                    String yn = sc.nextLine();
                    if(yn.equals("y"))
                    {
                        win = 0;
                        break;
                    }
                    if(yn.equals("n"))
                        break;
                        
                    System.out.println("\nEnter y or n\n");
                }
            }
            
        } //game while loop
	}
}
