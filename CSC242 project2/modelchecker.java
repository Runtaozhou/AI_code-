package project2;

import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.HashMap;
import java.util.Map;
public class modelchecker {
	public static void main(String [] args) {
	problem2c(); 
		
	}
	
	static ArrayList<clause> readclausesfromtext(String filename) {
		ArrayList<clause> clauseset = new ArrayList<>();
		try {
			File myObj = new File(filename);
		    Scanner scnr = new Scanner(myObj);
		    String[] problemline = null;
		    while (scnr.hasNextLine()) {
		    	String line = scnr.nextLine();
		    	if (line.charAt(0) == 'p') {
		    		problemline = line.split(" ");
		    		break;
		    	}
		      }
		    modelchecker thisone = new modelchecker();
		    for(int i = 0; i < Integer.parseInt(problemline[3]); i++) {
		    	clause curclause = thisone.new clause();
		    	int element;
		    	do{
		    		element = scnr.nextInt();
		    		if(element != 0) {
		    			curclause.add(element);
		    		}
		    	}while(element != 0);
		    	clauseset.add(curclause);

		    }
		      scnr.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return clauseset;
		  
	
	}
	public class clause{
		HashMap<Integer, Boolean> elements; // Create an ArrayList object
		
		public clause() {
			elements = new HashMap<>(); 
		}
		
		public void add(int i){
			elements.put(i, null);
		}
		
		public void addarray(int[] arr) {
			for (int i = 0; i < arr.length; i++) {
				elements.put(arr[i], null);
			}
		}
		public void set(int i, boolean b) {
			if (elements.containsKey(i)) {
				elements.put(i, b);
			}
		}
		public boolean evaluate() {
			for(Map.Entry<Integer, Boolean> e: elements.entrySet()) {
				if(e.getKey() < 0) {
					 if(!(e.getValue())) {
						 return true;
					 }
				}
				else {
					 if((e.getValue())) {
						 return true;
					 }
				}
				
			}	
			return false;
		}
		public String toString() {
			String s = "";
			for(Map.Entry<Integer, Boolean> e: elements.entrySet()) {
				s = s + " " + e.getKey().toString(); 
			}
			return s;
		}
	}
	public class model{
		HashMap<Integer, Boolean> variables;
		ArrayList<clause> setofclauses;
		
		public  model(){
			setofclauses = new ArrayList<>();
			variables = new HashMap<>();
		}
		
		public void addclause(clause c){
			setofclauses.add(c);
			for(Map.Entry<Integer, Boolean> e: c.elements.entrySet()) {
				variables.put(Math.abs(e.getKey()), null);
			}
		}
		
		public void createclause(int[] z) {
			clause curclause = new clause();
			curclause.addarray(z);
			setofclauses.add(curclause);
			for(Map.Entry<Integer, Boolean> e: curclause.elements.entrySet()) {
				variables.put(Math.abs(e.getKey()), null);
			}
		}
		
		public void setvariable(int i, boolean b) {
			if (variables.containsKey(i)) {
				variables.put(i, b);
			}
		}
		public boolean evaluate() {
			for (clause c : setofclauses) {
				for(Map.Entry<Integer, Boolean> e: variables.entrySet()) {
					c.set(e.getKey(), e.getValue());
					c.set(-e.getKey(), e.getValue());

				}
				if (!c.evaluate()) {
					return false;
				}
			}		
			return true;
		}
		
		public boolean entails(model b) {
			int k = variables.size();
			for (int i = 0; i < (int)Math.pow(2, k); i++){
				String binarystring = Integer.toBinaryString(i);
				while(binarystring.length() < k) {
					binarystring = "0" + binarystring;
				}
				int j = 0;
				for(Map.Entry<Integer, Boolean> e: variables.entrySet()) {
					if(binarystring.length() < k || binarystring.charAt(j)== '0') {
						setvariable(e.getKey(), false);
						b.setvariable(e.getKey(), false);
					}
					else {
						setvariable(e.getKey(), true);
						b.setvariable(e.getKey(), true);
					}
					
					j++;
				}
				
				if(evaluate() && !(b.evaluate()) ) {
					return false;
				}
			}
			return true;
			
		}
		
	}
	

	static void problem2a() {
		// p =1 , ==2
		modelchecker thismodelchecker = new modelchecker();
		
		model modela = thismodelchecker.new model();
		model modelb = thismodelchecker.new model();
		
		int[] r1 = {1};
		int[] r2 = {-1, 2};
		modela.createclause(r1);
		modela.createclause(r2);
		
		int[] x = {2};
		modelb.createclause(x);
		System.out.println(modela.entails(modelb));
	}
	static void problem2b() {
		
		// 1,2, 3,4,5, 6 = p11 p12 p21 p22 p31 p13
		// 7, 8,9 = B11 , B12, B21
		
		modelchecker thismodelchecker = new modelchecker();
		model modela = thismodelchecker.new model();
		model modelb = thismodelchecker.new model();
		
		int[] r1 ={ -1};
		modela.createclause(r1);
		
		int[] r2a ={ -7,2,3};
		int[] r2b ={ 7,-2};
		int[] r2c ={ 7, -3 };

		modela.createclause(r2a);
		modela.createclause(r2b);
		modela.createclause(r2c);

		int[] r3a ={ -9,1,4,5};
		int[] r3b ={ 9,-1};
		int[] r3c ={ 9 , -4 };
		int[] r3d ={ 9, -5};
		
		modela.createclause(r3a);
		modela.createclause(r3b);
		modela.createclause(r3c);
		modela.createclause(r3d);
	
		
		int[] r7a ={ -8,1,4,6};
		int[] r7b ={ 8,-1};
		int[] r7c ={ 8 , -4 };
		int[] r7d ={ 8, -6};
		
		modela.createclause(r7a);
		modela.createclause(r7b);
		modela.createclause(r7c);
		modela.createclause(r7d);
		
		int[] r4 = {-7};
		
		modela.createclause(r4);
		
		int[] x = {4};
		modelb.createclause(x);
		
		System.out.println(modela.entails(modelb));
	}
	static void problem2c() {
		// 1 => mythical 2 => immortal 3=> mammal 4=> horned  5=> magical 
		
	modelchecker thismodelchecker = new modelchecker();
	model modela = thismodelchecker.new model();
	model modelb = thismodelchecker.new model();
	model modelc = thismodelchecker.new model();
	model modeld = thismodelchecker.new model();
	int[]p1 = {-1,2};
	modela.createclause(p1);
	int[]p2 = {1,-2};
	int[]p3 = {1,3};
	modela.createclause(p2);
	modela.createclause(p3);
	int[]p4 = {-2,4};
	int[]p5 = {-3,4};
	modela.createclause(p4);
	modela.createclause(p5);
	int[]p6 = {-4,5};
	modela.createclause(p6);
	int[]q1 = {1};
	modelb.createclause(q1);
	int[]q2 = {5};
	modelc.createclause(q2);
	int[]q3 = {4};
	modeld.createclause(q3);
	System.out.println("unicorn is mythical is "+modela.entails(modelb));
	System.out.println("unicorn is magical is "+modela.entails(modelc));
	System.out.println("unicorn is horned is "+modela.entails(modeld));
	}
}

