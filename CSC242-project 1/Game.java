package CSC242project1;

import java.util.*;

class Node {

	ArrayList<Node> children  = new ArrayList<Node>();
	Node parent;
	int numPieces;
	Board board;
	Agent player;
	int utility;
	
	public Node(Board board, Node parent) { //children constructor
		this.parent = parent;
		this.board = board;
//		System.out.println("This is the root board");
//		board.printBoard();
		player = parent.player.getOpposite();
		if(player.getColor().equals("x")) {
			numPieces = board.numPieces()[0];
		} else {
			numPieces = board.numPieces()[1];				
		}
	}
	
//	public Node(Move move, Board board, Agent player, boolean isFirstTurn) { //root node constructor when player moves first
//		this.move = move;
//		this.board = board;
//		this.player = player;
//		if(player.getColor().equals("x")) {
//			numPieces = board.numPieces()[0];
//		} else {
//			numPieces = board.numPieces()[1];				
//		}
//		
//	}
	
	public Node(Board board, Agent player) { //root node constructor when player moves first
		this.player = player;
		this.board = board;
		if(player.getColor().equals("x")) {
			numPieces = board.numPieces()[0];
		} else {
			numPieces = board.numPieces()[1];				
		}
		
	}
	
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
	
}

public class Game {
	
	Node root;
	
	public Game(int boardSize) {
	}
	
	public Game(Node n) {
		root = n;
//		System.out.println("board from the last state");
//		root.board.printBoard();
	}
	
	public int Utility2(Board board,Agent computer) {
		if(computer.getColor().equals("x")) {
			if(board.numPieces()[0]>board.numPieces()[1]) {
				return board.numPieces()[0];
			}
			else if(board.numPieces()[0]<board.numPieces()[1]) {
				return board.numPieces()[0]-board.numPieces()[1];
			}
			else {
				return 0;
			}
		}
		else {
			if(board.numPieces()[1]>board.numPieces()[0]) {
				return board.numPieces()[1];
			}
			else if(board.numPieces()[1]<board.numPieces()[0]) {
				return board.numPieces()[1]-board.numPieces()[0];
			}
			else {
				return 0;
			}
		}
	}
	
	public int Utility1(Board board, Agent computer) {
		if(computer.getColor().equals("x")) {
			if(board.numPieces()[0]>board.numPieces()[1]) {
				return 1;
			}
			else if(board.numPieces()[0]<board.numPieces()[1]) {
				return -1;
			}
			else {
				return 0;
			}
		}
		else {
			if(board.numPieces()[1]>board.numPieces()[0]) {
				return 1;
			}
			else if(board.numPieces()[1]<board.numPieces()[0]) {
				return -1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	public Board getMinimaxBoard(Node n, int val) {
		Board board = new Board(n.board.getBoardSize());
		
		val = -2;
		for(Node child : n.getChildren()) {
			if(child.utility > val) {
				board = child.board;
			} 
		}
		return board;
	}
	
	public Board getRandBoard(Board b, Agent player) {
		Board curBoard = Board.copyBoard(b);
		ArrayList<Move> validMoves = curBoard.generateValidMoves(player);
		Random rnd = new Random();
		
		curBoard.makeMove(curBoard, validMoves.get(rnd.nextInt(validMoves.size())), player);
		
		return curBoard;
	}
	
	public int hminipruning_fixlength (Node n, int alpha, int beta, int depth, Agent player){
		if(depth==0|| n.getBoard().isTerminalState(false)==true) {
			return Utility2(n.getBoard(),player);
		}
		if(player.isComputer==true) {
			int maxEval = -64;
			ArrayList<Integer>utility =new ArrayList<Integer>();
			ArrayList<Integer>ab =new ArrayList<Integer>();
			ab.add(alpha);
			utility.add(maxEval);
			ArrayList<Move>validMoves = n.getBoard().generateValidMoves(player);
			for(Move m : validMoves) {
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				childBoard =childBoard.makeMove(childBoard,m, player);
				Node childNode = new Node(childBoard, player.getOpposite());
				n.children.add(childNode);
				int eval = Minipruning(childNode,depth-1,alpha,beta,player.getOpposite());
				childNode.utility = eval;
				if(eval>maxEval) {
					maxEval = eval;
				}
				if(eval>alpha) {
					alpha = eval;
				}
				if(beta<=alpha) {
					break;
				}
			}
			
			return maxEval;
			
		}
		else {
			int minEval = 64;
			ArrayList<Integer>utility = new ArrayList<Integer>();
			ArrayList<Integer>ab =new ArrayList<Integer>();
			ab.add(beta);
			utility.add(minEval);
			ArrayList <Move>validMoves = n.getBoard().generateValidMoves(player);
			for(Move m : validMoves) {
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				childBoard =childBoard.makeMove(childBoard,m, player);
				Node childNode = new Node(childBoard, player.getOpposite());
				n.children.add(childNode);
				int eval = Minipruning(childNode,depth-1,alpha,beta,player.getOpposite());
				childNode.utility = eval;
				if(eval<minEval) {
					minEval = eval;
				}
				if(eval<beta) {
					beta = eval;
				}
				if(beta<=alpha) {
					break;
				}
			}
			return minEval;
			
			
		}
		
	}
	
	
	
	
	public int Minipruning(Node n, int alpha, int beta, int depth, Agent player) {
		
		
		if(depth ==0|| n.getBoard().isTerminalState(false)==true) {
			return Utility1(n.getBoard(),player);
			
		}
		if(player.isComputer==true) {
			int maxEval = -2;
			ArrayList<Integer>utility =new ArrayList<Integer>();
			ArrayList<Integer>ab =new ArrayList<Integer>();
			ab.add(alpha);
			utility.add(maxEval);
			ArrayList<Move>validMoves = n.getBoard().generateValidMoves(player);
			for(Move m : validMoves) {
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				childBoard =childBoard.makeMove(childBoard,m, player);
				Node childNode = new Node(childBoard, player.getOpposite());
				n.children.add(childNode);
				int eval = Minipruning(childNode,depth-1,alpha,beta,player.getOpposite());
				childNode.utility = eval;
				if(eval>maxEval) {
					maxEval = eval;
				}
				if(eval>alpha) {
					alpha = eval;
				}
				if(beta<=alpha) {
					break;
				}
			}
			
			return maxEval;
			
		}
		else {
			int minEval = 2;
			ArrayList<Integer>utility = new ArrayList<Integer>();
			ArrayList<Integer>ab =new ArrayList<Integer>();
			ab.add(beta);
			utility.add(minEval);
			ArrayList <Move>validMoves = n.getBoard().generateValidMoves(player);
			for(Move m : validMoves) {
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				childBoard =childBoard.makeMove(childBoard,m, player);
				Node childNode = new Node(childBoard, player.getOpposite());
				n.children.add(childNode);
				int eval = Minipruning(childNode,depth-1,alpha,beta,player.getOpposite());
				childNode.utility = eval;
				if(eval<minEval) {
					minEval = eval;
				}
				if(eval<beta) {
					beta = eval;
				}
				if(beta<=alpha) {
					break;
				}
			}
			return minEval;
			
		}
		
	}

	
	public int MiniMax(Node n, int depth, Agent player) {
		if (depth ==0||n.getBoard().isTerminalState(false)==true) {
//			System.out.println(" i am in the base case");
//			System.out.println("the utility for "+player.getColor()+" is "+ Utility(n.getBoard(),player));
			return Utility1(n.getBoard(),player);
			
			
		}
		if(player.isComputer==true) {
//			System.out.println("the depth is "+depth);
//			System.out.println("we are in computer");
			int maxEval = -2;
			ArrayList<Integer> utility = new ArrayList<Integer>();
			utility.add(maxEval);
			ArrayList<Move> validMoves = n.getBoard().generateValidMoves(player);
//			System.out.print("Valid moves: ");
//			for(int i  = 0; i < validMoves.size(); i++) {
//				System.out.print(validMoves.get(i).getMoveStr()+ " ");
//				
//			}
			//System.out.println();
		   // n.getBoard().printBoard();
			for(Move m : validMoves) {
				//System.out.println("generating children board for computer turn");
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				//System.out.println("unmodified child board");
				//childBoard.printBoard();
				childBoard =childBoard.makeMove(childBoard,m, player);
				//System.out.println("modified child board");
				//childBoard.printBoard();
				Node childNode = new Node(childBoard, player.getOpposite());
				//System.out.println("finish making the opponent node");
				n.children.add(childNode);
				int eval = MiniMax(childNode,depth-1,player.getOpposite());
					childNode.utility = eval;
					//System.out.println("the utility is" + eval);
				if(eval>maxEval) {
					maxEval = eval;
				}
			}
//			for(int i = 0; i < n.getChildren().size(); i++) {
//				if(n.getChildren().get(i).utility > maxEval){
//					maxEval = n.getChildren().get(i).utility;
//				}
//			}
			
			return maxEval;	
			}
		else {
			int minEval = 2;
			
			ArrayList<Integer> utility = new ArrayList<Integer>();
			utility.add(minEval);
			//System.out.println("we are in human ");
			//System.out.println("the color is "+ player.getColor());
			ArrayList<Move> validMoves = n.getBoard().generateValidMoves(player);
			//System.out.print("Valid moves: ");
			//for(int i  = 0; i < validMoves.size(); i++) {
				//System.out.print(validMoves.get(i).getMoveStr()+ " ");
			//}
			//System.out.println();
			for(Move m : validMoves) {
				Board childBoard = new Board(n.getBoard().getBoardSize());
				childBoard = Board.copyBoard(n.board);
				//childBoard.printBoard();
				childBoard=childBoard.makeMove(childBoard,m, player);
				//System.out.println("modified child board");
				//childBoard.printBoard();
				Node childNode = new Node(childBoard, player.getOpposite());
				n.children.add(childNode);
				int eval = MiniMax(childNode,depth-1,player.getOpposite());
					childNode.utility = eval;
					if(eval<minEval) {
						minEval = eval;
					}
					
			}
//			for(Node child : n.getChildren()) {
//				if(child.utility < minEval) {
//					minEval = child.utility;
//				}
//			}
			return minEval;
		}
			
			
		}
	
	}
