package CSC242project1;

import java.util.ArrayList;
import java.util.Hashtable;

public class Move {

	String move;
	static String[] colNames = {"a", "b", "c", "d", "e", "f", "g", "h"};
	static Hashtable<String, Integer> colKey = new Hashtable<String, Integer>();
	
	public Move(String move) {
		this.move = move;
		for(int i = 0; i < colNames.length; i++)
			colKey.put(colNames[i], i);
	}
	
	public String getMoveStr() {
		return move;
	}
	
	public int[] moveToIndices() {
		
		String colStr = move.substring(0,1);
		
		int col = colKey.get(colStr); 
		int row = Integer.parseInt(move.substring(1)) - 1;
		
		int[] position = {row, col};
		
		return position;
	}
	
	public static Move indicesToMove(int row, int col) {
		
		row++;
		String colStr = colNames[col];
		
		String move = colStr + row;
		
		Move moveObj = new Move(move);
		
		return moveObj;
	}
	
}