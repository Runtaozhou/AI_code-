package learn.lc.core;

// Import this class to handle errors
import java.io.*;  // Import the File class

import learn.math.util.VectorOps;
import java.util.*;

public class PerceptronClassifier extends LinearClassifier {
	
	public PerceptronClassifier(double[] weights) {
		super(weights);
	}
	
	public PerceptronClassifier(int ninputs) {
		super(ninputs);
	}
	
	/**
	 * A PerceptronClassifier uses the perceptron learning rule
	 * (AIMA Eq. 18.7): w_i \leftarrow w_i+\alpha(y-h_w(x)) \times x_i 
	 */
	public void update(double[] x, double y, double alpha) {
		double hw = this.eval(x);
		for (int i=0; i<this.weights.length;i++) {
			this.weights[i]=this.weights[i]+alpha*(y-hw)*x[i];
		}
	}
	
	/**
	 * A PerceptronClassifier uses a hard 0/1 threshold.
	 */
	public double threshold(double z) {
		double threshold = 0;
		if(z>=0) {
			threshold=1;
		}
		else {
			threshold=0;
		}
		return threshold;
			
	}
	public void export(double[]input) throws FileNotFoundException {
		PrintWriter out = new PrintWriter("Perceptron_data.txt");
        for (int i=0; i<input.length;i++) {
        	out.println(i+1+","+input[i]);
        }
        out.close();
	}
	public List<Example>  readfile(String filename) throws FileNotFoundException{
		List<Example> examples= new ArrayList<Example> ();
		File myobj= new File(filename);
	    Scanner scnr = new Scanner(myobj);
	    String[] lines;
	    while (scnr.hasNextLine()) {
	    	String line = scnr.nextLine();
	    	lines = line.split(",");
	    	double[] inputs = new double[lines.length-1];
	    	for(int i=0;i<lines.length-1;i++) {
	    		double temp = Double.parseDouble(lines[i]);
	    		inputs[i]=temp;
	    	}
	    	double outputs = Double.parseDouble(lines[lines.length-1]);
	    	Example example = new Example(inputs,outputs);
	    	examples.add(example);
	      }
	    
	    scnr.close();
	    return examples;
	}
	public static int numWeights(String filename) throws FileNotFoundException {
		int numWeights;
		File myobj= new File(filename);
	    Scanner scnr = new Scanner(myobj);
	    String[] lines;
	    String line = scnr.nextLine();
	    lines = line.split(",");
	    numWeights = lines.length-1;
		return numWeights;
	}
	public static void main (String[]args) throws FileNotFoundException {
		String filename ="src/learn/lc/examples/"+ args[0];
		int weightNum = numWeights(filename);
		if(args[2].equals("decay")) {
			System.out.println("decaying learning rate schedule ");
			PerceptronClassifier q = new PerceptronClassifier(weightNum);
			List<Example> example = q.readfile(filename);
			q.trainDecay(example, Integer.parseInt(args[1]));
			q.export(q.Accuracy);
		}
		else {
			System.out.println("fixed learning rate schedule ");
			PerceptronClassifier p = new PerceptronClassifier(weightNum);
			List<Example> example = p.readfile(filename);
			p.train(example, Integer.parseInt(args[1]), Double.parseDouble(args[2]));
			p.export(p.Accuracy);
		}
	}
	
}
