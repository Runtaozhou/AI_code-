package CSC242project1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Reversi {

	public static void main(String[] args) {
		initGame();
	}
	
	public static void initGame() {
		Scanner scan = new Scanner(System.in);
		int boardChoice = 0;
		int oppChoice = 0;
		String playerChoice = "";
		Board board = new Board();  
		
		System.out.println("Reversi by Goutham Swaminathan");
		System.out.println("Choose your game: ");
		System.out.println("1. Small 4x4 Reversi");
		System.out.println("2. Medium  6x6 Reversi");
		System.out.println("3. Standard 8x8 Reversi");
		System.out.println();
		System.out.print("Your choice: ");

		boardChoice = scan.nextInt();
		System.out.println();
		
		while(boardChoice != 1 &&
			  boardChoice != 2 &&
		      boardChoice != 3) {
			System.out.println("Invalid choice. Please try again.");
			System.out.print("Your choice: ");
			boardChoice = scan.nextInt();
		}
		
		scan.reset();
		
		switch(boardChoice) {
		case 1:
			board = new Board(4);
			break;
		case 2:
			board = new Board(6);
			break;
		case 3:
			board = new Board(8);
			break;
		}
		
		System.out.println("Choose your opponent: ");
		if(boardChoice == 1) {
			System.out.println("1. An agent that plays randomly");
			System.out.println("2. An agent that uses MINIMAX");
			System.out.println("3. An agent that uses MINIMAX with alpha-beta pruning");
			System.out.println("4. An agent that uses H-MINIMAX with a fixed depth cutoff and a-b pruning");
			
			System.out.println();
			System.out.print("Your choice: ");
			
			oppChoice = scan.nextInt();
			System.out.println();
			
			while(oppChoice != 1 &&
				  oppChoice != 2 &&
			      oppChoice != 3 &&
			      oppChoice != 4) {
				System.out.println("Choice: " + oppChoice);
				System.out.println("Invalid choice. Please try again.");
				System.out.print("Your choice: ");
				oppChoice = scan.nextInt();
			}
			
		} else if(boardChoice == 2 || boardChoice == 3) {
			System.out.println("1. An agent that plays randomly");
			System.out.println("4. An agent that uses H-MINIMAX with a fixed depth cutoff and a-b pruning");
			
			System.out.println();
			System.out.print("Your choice: ");
			
			oppChoice = scan.nextInt();
			System.out.println();
			
			while(oppChoice != 1 &&
				  oppChoice != 2) {
				System.out.println("Choice: " + oppChoice);
				System.out.println("Invalid choice. Please try again.");
				System.out.print("Your choice: ");
				oppChoice = scan.nextInt();
			}
		}
		
		scan.reset();
		
		System.out.println("Do you want to play DARK (x) or LIGHT (o)?");
		System.out.println();
		System.out.print("Your choice: ");
		
		playerChoice = scan.next().toLowerCase();
		System.out.println();
		
		while(!playerChoice.equals("x") &&
			  !playerChoice.equals("o")) {
			System.out.println("Invalid choice. Please try again.");
			System.out.print("Your choice: ");
			playerChoice = scan.next().toLowerCase();
		}
		
		Agent human = new Agent(playerChoice, false);
		Agent computer;
		boolean isHumanFirst;
		
		if(human.getColor().equals("x")) {
			isHumanFirst = true;
			computer = new Agent("o", true);
		} else {
			isHumanFirst = false;
			computer = new Agent("x", true);
		}
		
		board.buildBoard();
		board.printBoard();
		System.out.println();
		
		runGame(board, human, computer, oppChoice);
	}
	
	public Agent whichX(Agent human, Agent computer) {
		if(human.getColor().equals("x")) {
			return human;
		} else {
			return computer;
		}
	}
	
	public static void runGame(Board board, Agent human, Agent computer, int oppChoice) {
		Board curBoard = board;
		int turnCounter = 0;
		String nextToPlay = "";
		String currentPlayer = "";
		Game g;

		String moveStr = "";
		Scanner scan = new Scanner(System.in);
		Move move;
		
		while(!curBoard.isTerminalState(true)) {
			if(turnCounter % 2 == 0) {
				System.out.println("Next to play: DARK");
				System.out.println();
			} else {
				System.out.println("Next to play: LIGHT");
				System.out.println();
			}
			
			moveStr = "";
			scan = new Scanner(System.in);
			
			if(turnCounter % 2 == 0) {
				if(human.getColor().equals("x")) {
				if(board.generateValidMoves(human).size() == 0) {
					System.out.println("You have no valid moves. Pass.");
					turnCounter++;
				} else {
					System.out.print("Your move (? for help): ");
					moveStr = scan.next().toLowerCase();
					move = new Move(moveStr);
					if(board.isValidMove(move, human)) {
						turnCounter++;
					} 
					curBoard=board.makeMove(curBoard,move, human);
					curBoard.printBoard();
					ArrayList<Move> validMoves = new ArrayList<Move>();
					validMoves = curBoard.generateValidMoves(human.getOpposite());
				}
			}
				else if(computer.getColor().equals("x"));{
					{
						if(board.generateValidMoves(computer).size() == 0) {
							System.out.println("You have no valid moves. Pass.");
							turnCounter++;
						}
						else {
							
							if(oppChoice == 2) {
								Node n = new Node(curBoard,computer);
								g = new Game(n);
								int bestVal = g.MiniMax(n, 10, computer);
								Board bestBoard;
								bestBoard = g.getMinimaxBoard(n, bestVal);
								
								System.out.println("I am making my move..");
								
								curBoard = bestBoard;
								turnCounter++;
								board = bestBoard;
							} else if(oppChoice == 1) {
								Node n = new Node(curBoard,computer);
								g = new Game(n);
								curBoard = g.getRandBoard(curBoard, computer);
								board = curBoard;
								turnCounter++;
							}
							else if(oppChoice ==3) {
								Node n =new Node(curBoard,computer);
								g = new Game(n);
								int bestVal = g.Minipruning(n, -2, 2, 10, computer);
								Board bestBoard;
								bestBoard =g.getMinimaxBoard(n, bestVal);
								System.out.println("I am making my move..");
								
								curBoard = bestBoard;
								turnCounter++;
								board = bestBoard;
								
							}
							else if(oppChoice ==4) {
								Node n =new Node(curBoard,computer);
								g = new Game(n);
								int bestVal = g.hminipruning_fixlength (n, -64, 64, 10, computer);
								Board bestBoard;
								bestBoard =g.getMinimaxBoard(n, bestVal);
								System.out.println("I am making my move..");
								
								curBoard = bestBoard;
								turnCounter++;
								board = bestBoard;
							}

							System.out.println();

							board.printBoard();
							
						}
					}

				}
			}
			 else {
				 if(human.getColor().equals("o")) {
					 if(board.generateValidMoves(human).size() == 0) {
							System.out.println("You have no valid moves. Pass.");
							turnCounter++;
						} 
					 else {
							System.out.print("Your move (? for help): ");
							moveStr = scan.next().toLowerCase();
							move = new Move(moveStr);
							if(board.isValidMove(move, human)) {
								turnCounter++;
							} 
							curBoard=board.makeMove(curBoard,move, human);
							curBoard.printBoard();
							ArrayList<Move> validmoves = new ArrayList<Move>();
							validmoves = curBoard.generateValidMoves(human.getOpposite());
					 }

					 
				 }
				 else if (computer.getColor().equals("o")) {
					if(board.generateValidMoves(computer).size() == 0) {
						System.out.println("You have no valid moves. Pass.");
						turnCounter++;
					} else {
						if(oppChoice == 2) {
							Node n = new Node(curBoard,computer);
							g = new Game(n);
							int bestVal = g.MiniMax(n, 12, computer);
							Board bestBoard;
							bestBoard = g.getMinimaxBoard(n, bestVal);
							
							System.out.println("I am making my move..");
							
							curBoard = bestBoard;
							turnCounter++;
							board = bestBoard;
						} else if(oppChoice == 1) {
							Node n = new Node(curBoard,computer);
							g = new Game(n);
							curBoard = g.getRandBoard(curBoard, computer);
							board = curBoard;
							turnCounter++;
						}
						else if(oppChoice ==3) {
							Node n =new Node(curBoard,computer);
							g = new Game(n);
							int bestVal = g.Minipruning(n, -2, 2, 12, computer);
							Board bestBoard;
							bestBoard =g.getMinimaxBoard(n, bestVal);
							System.out.println("I am making my move..");
							
							curBoard = bestBoard;
							turnCounter++;
							board = bestBoard;
							
						}
						else if(oppChoice ==4) {
							Node n =new Node(curBoard,computer);
							g = new Game(n);
							int bestVal = g.hminipruning_fixlength (n, -64, 64, 10, computer);
							Board bestBoard;
							bestBoard =g.getMinimaxBoard(n, bestVal);
							System.out.println("I am making my move..");
							
							curBoard = bestBoard;
							turnCounter++;
							board = bestBoard;
						}

						System.out.println();

						board.printBoard();

					}
				 }
		}
			
		}
	}
	
}
