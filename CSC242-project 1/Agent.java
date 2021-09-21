package CSC242project1;

public class Agent {

	public String color;
	public boolean isComputer;
	public String position;
	
	public Agent(String color, boolean isComputer) {
		this.color = color;
		this.isComputer = isComputer;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getOpponent() {
		if(color.equals("x")) {
			return "o";
		} else {
			return "x";
		}
	}
	public boolean isComputer(Agent player) {
		if(player.isComputer=true) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public Agent getOpposite() {
		Agent opp;
		String col;
		boolean isComp;
		
		if(color.equals("x")) {
			col = "o";
		} else {
			col = "x";
		}
		
		if(isComputer == true) {
			isComp = false;
		} else {
			isComp = true;
		}
		
		opp = new Agent(col, isComp);
		return opp;
	}

	
	 
}
