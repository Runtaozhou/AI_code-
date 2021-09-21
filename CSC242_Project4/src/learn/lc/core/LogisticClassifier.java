package learn.lc.core;

import java.io.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import learn.math.util.VectorOps;

public class LogisticClassifier extends LinearClassifier {
	
	
	public LogisticClassifier(double[] weights) {
		super(weights);
	}
	
	public LogisticClassifier(int ninputs) {
		super(ninputs);
	}
	
	/**
	 * A LogisticClassifier uses the logistic update rule
	 * (AIMA Eq. 18.8): w_i \leftarrow w_i+\alpha(y-h_w(x)) \times h_w(x)(1-h_w(x)) \times x_i 
	 */
	public void update(double[] x, double y, double alpha) {
		double hw = this.eval(x);
		
		for(int i=0; i<this.weights.length;i++) {
			this.weights[i]=this.weights[i]+alpha*(y-hw)*(hw*(1-hw))*x[i];
		}
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
	public void export(double[]input) throws FileNotFoundException {
		PrintWriter out = new PrintWriter("Logistic_data.txt");
        for (int i=0;i<input.length;i++) {
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
	public void trainlo(List<Example> examples, int nsteps, LearningRateSchedule schedule) {
		double[]acc=new double[nsteps];
		double[]se = new double[nsteps];
		Random random = new Random();
		int n = examples.size();
		System.out.println("samplesize: "+n);
		for (int i=1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			
			this.update(ex.inputs, ex.output, schedule.alpha(i));
			this.trainingReportlo(examples, i,  nsteps);
			acc[i-1]=accuracy(examples);
			se[i-1]=squaredErrorPerSample(examples);
			
		}
		Accuracy=acc;
		Square_Error=se;
		
		
	}
	public void trainDecaylo(List<Example> examples, int nsteps, DecayingLearningRateSchedule schedule) {
		
		double[]se = new double[nsteps];
		Random random = new Random();
		int n = examples.size();
		for (int i=1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			this.update(ex.inputs, ex.output, schedule.alpha(i));
			this.trainingReportlo(examples, i,  nsteps);
			se[i-1]=squaredErrorPerSample(examples);
			
		}
		
		Square_Error=se;
		
	}
	public void trainDecaylo(List<Example> examples, int nsteps) {
		trainDecaylo(examples, nsteps, new DecayingLearningRateSchedule() {
			public double alpha(int t) { return 1000.0/(1000.0+t); }
		});
	}
	


	/**
	 * Train this LinearClassifier on the given Examples for the
	 * given number of steps, using given constant learning rate.
	 */
	public void trainlo(List<Example> examples, int nsteps, double constant_alpha) {
		trainlo(examples, nsteps, new LearningRateSchedule() {
			public double alpha(int t) { return constant_alpha; }
		});
	}
	
	
	/**
	 * A LogisticClassifier uses a 0/1 sigmoid threshold at z=0.
	 */
	public double threshold(double z) {
		double threshold = 0;
		double e = java.lang.Math.E;
		threshold = 1/(1+Math.pow(e, -z));
		return threshold;
		// This must be implemented by you
	}
	public static void main (String[]args) throws FileNotFoundException {
		String filename ="src/learn/lc/examples/"+ args[0];
		int weightNum = numWeights(filename);
		
		
		if(args[2].equals("decay")) {
			System.out.println("decaying learning rate schedule ");
			LogisticClassifier q = new LogisticClassifier(weightNum);
			List<Example> example = q.readfile(filename);
			q.trainDecaylo(example, Integer.parseInt(args[1]));
			q.export(q.Square_Error);
		}
		else {
			System.out.println("fixed learning rate schedule ");
			LogisticClassifier p = new LogisticClassifier(weightNum);
			List<Example> example = p.readfile(filename);
			p.trainlo(example, Integer.parseInt(args[1]), Double.parseDouble(args[2]));
			p.export(p.Square_Error);
		}
		
		
		
		
		
		
	}

}
