package CSC242project1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Board {

	int boardSize;
	String[][] board;
	String[] colNames = {"a", "b", "c", "d", "e", "f", "g", "h"};
	Hashtable<String, Integer> colKey = new Hashtable<String, Integer>();
	
	public Board(int boardSize) {
		this.boardSize = boardSize;
		board = new String[boardSize][boardSize];
		
		switch(boardSize) {
		case 4:
			colNames = Arrays.copyOfRange(colNames, 0, 4);
			for(int i = 0; i < colNames.length; i++)
				colKey.put(colNames[i], i);
			break;
		case 6:
			colNames = Arrays.copyOfRange(colNames, 0, 6);
			for(int i = 0; i < colNames.length; i++)
				colKey.put(colNames[i], i);
			break;
		case 8:
			colNames = Arrays.copyOfRange(colNames, 0, 8);
			for(int i = 0; i < colNames.length; i++)
				colKey.put(colNames[i], i);
			break;
		}
	}
	
	public Board() {
		boardSize = 4;
		board = new String[boardSize][boardSize];
		colNames = Arrays.copyOfRange(colNames, 0, 4);
	}
	
	public Board(Board fullBoard) {
		String[][] fullBoardArr = fullBoard.getBoard();
		boardSize = fullBoard.getBoardSize();
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = fullBoardArr[i][j];
			}
		}
		colNames = Arrays.copyOfRange(colNames, 0, boardSize);
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public String[][] getBoard(){
		return board;
	}
	
//	public void buildTestBoard() {
//		for(int i = 0; i < boardSize; i++) {
//			for(int j = 0; j < boardSize; j++) {
//				board[i][j] = " ";
//			}
//		}
//		
//		board[(boardSize/2) - 1][boardSize/2] = "o";
//		board[boardSize/2][(boardSize/2) - 1] = "o";
//		board[(boardSize/2) - 1][(boardSize/2) - 1] = "x";
//		board[boardSize/2][boardSize/2] = "x";
//		board[1][3] = "x";
//		board[2][3] = "x";
//		
////		printBoard();
//	}
	public static Board copyBoard ( Board root) {
		Board child = new Board( root.boardSize);
		for(int i = 0; i < root.boardSize; i++) {
			for(int j = 0; j < root.boardSize; j++) {
				child.board[i][j] = root.board[i][j];
			}
			
		}
		return child;
		
	}
	public void buildBoard() {
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = " ";
			}
		}
		
		board[(boardSize/2) - 1][boardSize/2] = "x";
		board[boardSize/2][(boardSize/2) - 1] = "x";
		board[(boardSize/2) - 1][(boardSize/2) - 1] = "o";
		board[boardSize/2][boardSize/2] = "o";		
	}
	
	public void printBoard() {
		
		System.out.print("  ");
		for(String s : colNames) {
			System.out.printf("%s ", s);
		}
		System.out.println();
		for(int i = 0; i < boardSize; i++) {
			System.out.printf("%d ", i+1);
			for(int j = 0; j < boardSize; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.printf("%d \n", i+1);
		}
		System.out.print("  ");
		for(String s : colNames) {
			System.out.printf("%s ", s);
		}
		
		System.out.println();
	}
	
	public String getBoardValue(int row, int col) {
		return board[row][col];
	}
	
	public ArrayList<Move> getValidMoves(Agent player){
		ArrayList<Move> validMoves = generateValidMoves(player);
		
		return validMoves;
	}
	
	public ArrayList<Move> generateValidMoves(Agent player) {

		ArrayList<Move> validMoves = new ArrayList<Move>();
		Move moveTest = new Move("");
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				moveTest = Move.indicesToMove(i, j);
				if(isValidMove(moveTest, player)==true) {
					validMoves.add(Move.indicesToMove(i, j));
				} 
			}
		}
		
		return validMoves;
	}
	
	public boolean isValidMove(Move move, Agent player) {
		
		int row = move.moveToIndices()[0];
		int col = move.moveToIndices()[1];
		
		if(!board[row][col].equals(" ")) {
			return false;
		}
		
		int sum = 0;
		
		if(row == 0) {
			if(col == 0) {
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("se", row, col, player) ? 1 : 0;
								
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else if(col == boardSize - 1) {
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("sw", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else {
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("se", row, col, player) ? 1 : 0;
				sum += moveSearch("sw", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			}
		} else if (row == boardSize - 1) {
			if(col == 0) {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("ne", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else if (col == boardSize - 1) {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("nw", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("ne", row, col, player) ? 1 : 0;
				sum += moveSearch("nw", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			if(col == 0) {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("ne", row, col, player) ? 1 : 0;
				sum += moveSearch("se", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else if(col == boardSize - 1) {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("nw", row, col, player) ? 1 : 0;
				sum += moveSearch("sw", row, col, player) ? 1 : 0;
				
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			} else {
				sum += moveSearch("n", row, col, player) ? 1 : 0;
				sum += moveSearch("e", row, col, player) ? 1 : 0;
				sum += moveSearch("s", row, col, player) ? 1 : 0;
				sum += moveSearch("w", row, col, player) ? 1 : 0;
				sum += moveSearch("nw", row, col, player) ? 1 : 0;
				sum += moveSearch("ne", row, col, player) ? 1 : 0;
				sum += moveSearch("sw", row, col, player) ? 1 : 0;
				sum += moveSearch("se", row, col, player) ? 1 : 0;
	
				if(sum == 0) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	public boolean moveSearch(String dir, int row, int col, Agent p) {
		
		String opponent = "";
		String player =p.getColor();
		if(p.getColor().equals("x")){
			opponent = "o";
		} else {
			opponent = "x";
		}
		
		switch(dir) {
		case "n":
			if(board[row-1][col].equals(opponent)) {
				while(board[row-1][col].equals(opponent)) {
					row--;
					if(row - 1 < 0) {
						return false;
					}
					
				}
				if(board[row-1][col].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "ne":
			if(board[row-1][col+1].equals(opponent)) {
				while(board[row-1][col+1].equals(opponent)) {
					row--;
					col++;
					if(row-1 < 0 || col+1 > boardSize - 1) {
						return false;
					}
				}
				if(board[row-1][col+1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "e":
			if(board[row][col+1].equals(opponent)) {
				while(board[row][col+1].equals(opponent)) {
					col++;
					if(col+1 > boardSize - 1) {
						return false;
					}
				}
				if(board[row][col+1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "se":
			if(board[row+1][col+1].equals(opponent)) {
				while(board[row+1][col+1].equals(opponent)) {
					row++;
					col++;
					if(row+1 > boardSize - 1 || col+1 > boardSize - 1) {
						return false;
					}
				}
				if(board[row+1][col+1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "s":
			if(board[row+1][col].equals(opponent)) {
				while(board[row+1][col].equals(opponent)) {
					row++;
					if(row+1 > boardSize - 1) {
						return false;
					}
				}
				if(board[row+1][col].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "sw":
			if(board[row+1][col-1].equals(opponent)) {
				while(board[row+1][col-1].equals(opponent)) {
					row++;
					col--;
					if(row+1 > boardSize - 1 || col-1 < 0) {
						return false;
					}
				}
				if(board[row+1][col-1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "w":
			if(board[row][col-1].equals(opponent)) {
				while(board[row][col-1].equals(opponent)) {
					col--;
					if(col-1 < 0) {
						return false;
					}
				}
				if(board[row][col-1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		case "nw":
			if(board[row-1][col-1].equals(opponent)) {
				while(board[row-1][col-1].equals(opponent)) {
					row--;
					col--;
					if(row - 1 < 0 || col - 1 < 0) {
						return false;
					}
				}
				if(board[row-1][col-1].equals(player)) {
					return true;
				} 
			} else {
				return false;
			}
			break;
		}
		return false;
	}
	
	public boolean isTerminalState(boolean isPrint) {
		
		int sum = 0;
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				if(board[i][j].equals(" ")) {
					sum++;
				}
			}
		}
		Agent x = new Agent("x",false);
		Agent y = new Agent("o",false);
 		ArrayList<Move> validMovesX = generateValidMoves(x);
		ArrayList<Move> validMovesO = generateValidMoves(y);
		
		int[] results = numPieces();
		
		if (sum != 0) {
			
			if(validMovesX.size() == 0 &&
					validMovesO.size() == 0) {
				
				if(results[0] > results[1]) {
					if(isPrint) {
						System.out.println("X wins!");
						System.out.println("X: " + results[0] + " O: " + results[1]);
					}
				} else if (results[0] < results[1]) {
					if(isPrint) {
						System.out.println("O wins!");
						System.out.println("O: " + results[1] + " X: " + results[0]);
					}
				} else {
					if(isPrint) {
						System.out.println("It's a draw!");
						System.out.println("X: " + results[0] + " O: " + results[1]);
					}
				}
				return true;
			} else {	
				
				return false;
			}
		} 
		else {
			if(results[0] > results[1]) {
				if(isPrint) {
					System.out.println("X wins!");
					System.out.println("X: " + results[0] + " O: " + results[1]);
				}
			} else if (results[0] < results[1]) {
				if(isPrint) {
					System.out.println("O wins!");
					System.out.println("O: " + results[1] + " X: " + results[0]);
				}
			} else {
				if(isPrint) {
					System.out.println("It's a draw!");
					System.out.println("X: " + results[0] + " O: " + results[1]);
				}
			}
			return true;
		}
	}
	
	public Board makeMove(Board currentboard, Move move, Agent player) {
		Board newboard = new Board(currentboard.boardSize);
		int row = move.moveToIndices()[0];
		int col = move.moveToIndices()[1];
		String playerpiece = player.color;
		ArrayList<Move> validMoves = generateValidMoves(player);
		ArrayList<String> validMovesStr = new ArrayList<String>();
		
		for(Move m : validMoves) {
			validMovesStr.add(m.move);
		}
		
		if(validMovesStr.contains(Move.indicesToMove(row, col).getMoveStr())) {
			currentboard.board[row][col] = playerpiece;
			flipPieces(row, col, player);
			newboard =currentboard;
			return newboard;
		} else {
			System.out.println("Invalid move for " + playerpiece + "\nPlease try again.");
			return currentboard;
		}
		
	}
	
	public void flipPieces(int row, int col, Agent player) {
		int x=row;
		int y=col;
		String opponent =player.getOpponent();
		String playerpiece =player.getColor();
		
		ArrayList<String> dir = new ArrayList<String>();
		
		if(row == 0) {
			if(col == 0) {
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
				dir.add(moveSearch("se", row, col, player) ? "se" : " ");
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
			} else if (col == boardSize - 1) {
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
				dir.add(moveSearch("sw", row, col, player) ? "sw" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
			} else {
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
				dir.add(moveSearch("se", row, col, player) ? "se" : " ");
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
				dir.add(moveSearch("sw", row, col, player) ? "sw" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
			}
		} else if (row == boardSize - 1) {
			if(col == 0) {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("ne", row, col, player) ? "ne" : " ");
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
			} else if (col == boardSize - 1) {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
				dir.add(moveSearch("nw", row, col, player) ? "nw" : " ");
			} else {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("ne", row, col, player) ? "ne" : " ");
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
				dir.add(moveSearch("nw", row, col, player) ? "nw" : " ");
			}
		} else {
			if (col == 0) {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("ne", row, col, player) ? "ne" : " ");
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
				dir.add(moveSearch("se", row, col, player) ? "se" : " ");
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
			} else if (col == boardSize - 1) {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
				dir.add(moveSearch("sw", row, col, player) ? "sw" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
				dir.add(moveSearch("nw", row, col, player) ? "nw" : " ");
			} else {
				dir.add(moveSearch("n", row, col, player) ? "n" : " ");
				dir.add(moveSearch("ne", row, col, player) ? "ne" : " ");
				dir.add(moveSearch("e", row, col, player) ? "e" : " ");
				dir.add(moveSearch("se", row, col, player) ? "se" : " ");
				dir.add(moveSearch("s", row, col, player) ? "s" : " ");
				dir.add(moveSearch("sw", row, col, player) ? "sw" : " ");
				dir.add(moveSearch("w", row, col, player) ? "w" : " ");
				dir.add(moveSearch("nw", row, col, player) ? "nw" : " ");
			}
		}
		
		for(String s : dir) {
			
			switch(s) {
			case "n":
				if(board[x-1][y].equals(opponent)) {
					while(board[x-1][y].equals(opponent)) {
						board[x-1][y] = playerpiece;
						x--;
						if(x-1 < 0) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x-1][y].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "ne":
				if(board[x-1][y+1].equals(opponent)) {
					while(board[x-1][y+1].equals(opponent)) {
						board[x-1][y+1] = playerpiece;
						x--;
						y++;
						if(x-1 < 0 || y+1 > boardSize - 1) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x-1][y+1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "e":
				if(board[x][y+1].equals(opponent)) {
					while(board[x][y+1].equals(opponent)) {
						board[x][y+1] = playerpiece;
						y++;
						if(y+1 > boardSize - 1) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x][y+1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "se":
				if(board[x+1][y+1].equals(opponent)) {
					while(board[x+1][y+1].equals(opponent)) {
						board[x+1][y+1] = playerpiece;
						x++;
						y++;
						if(y+1 > boardSize - 1 ||
								x+1 > boardSize - 1) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x+1][y+1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "s":
				if(board[x+1][y].equals(opponent)) {
					while(board[x+1][y].equals(opponent)) {
						board[x+1][y] = playerpiece;
						x++;
						if(x+1 > boardSize - 1) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x+1][y].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "sw":
				if(board[x+1][y-1].equals(opponent)) {
					while(board[x+1][y-1].equals(opponent)) {
						board[x+1][y-1] = playerpiece;
						x++;
						y--;
						if(x+1 > boardSize - 1 || y-1 < 0) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x+1][y-1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "w":
				if(board[x][y-1].equals(opponent)) {
					while(board[x][y-1].equals(opponent)) {
						board[x][y-1] = playerpiece;
						y--;
						if(y-1 < 0) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x][y-1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}
			case "nw":
				if(board[x-1][y-1].equals(opponent)) {
					while(board[x-1][y-1].equals(opponent)) {
						board[x-1][y-1] =playerpiece;
						x--;
						y--;
						if(x-1 < 0 || y-1 < 0) {
							x=row;
							y=col;
							break;
						}
					}
					if(board[x-1][y-1].equals(playerpiece)) {
						x=row;
						y=col;
						break;
					} 
				} else {
					break;
				}

			}
			
		}
	}
	
	public int[] numPieces() {
		int numX = 0;
		int numO = 0;
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				if(board[i][j].equals("x")) {
					numX++;
				} else if(board[i][j].equals("o")) {
					numO++;
				}
			}
		}
		
		int[] results = {numX, numO};
		
		return results;
	}
	
}
 