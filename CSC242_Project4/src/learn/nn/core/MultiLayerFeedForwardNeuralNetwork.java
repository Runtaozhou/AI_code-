package learn.nn.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import learn.nn.core.Example;

/**
 * A MultiLayerFeedForwardNeuralNetwork is a FeedForwardNeuralNetwork with at
 * least one ``hidden'' layer of units between the inputs and the outputs
 * (AIMA Section 18.7.3).
 */
 public class MultiLayerFeedForwardNeuralNetwork extends FeedForwardNeuralNetwork {
	
	/**
	 * Construct and return a new MultiLayerFeedForwardNeuralNetwork with the given
	 * layers of Units (InputUnits in the first layer, LogisticUnits in other layers).
	 * It's up to you to arrange the feed-forward connections between the Units
	 * properly.
	 */
	public MultiLayerFeedForwardNeuralNetwork(Unit[][] layers) {
		super(layers);
	}
	
	/**
	 * Construct and return a new MultiLayerFeedForwardNeuralNetwork with the given
	 * number of input units, hidden units (array of lengths, one for each layer),
	 * and the given number of output units, where each layer is fully connected to the next.
	 */
	public MultiLayerFeedForwardNeuralNetwork(int ninputs, int[] nhiddens, int noutputs) {
		this(new Unit[nhiddens.length+2][]);
		// Input layer
		this.layers[0] = new InputUnit[ninputs];
		for (int i=0; i < ninputs; i++) {
			this.layers[0][i] = new InputUnit();
		}
		// Hidden layers: each connected to all previous layer units
		for (int l=1; l <= nhiddens.length; l++) {
			int n = nhiddens[l-1];
			this.layers[l] = new LogisticUnit[n];
			for (int j=0; j < n; j++) {
				this.layers[l][j] = new LogisticUnit();
				for (int i=0; i < layers[l-1].length; i++) {
					new Connection(this.layers[l-1][i], this.layers[l][j]);
				}
			}
		}
		// Output layer: each connected to all last-hidden-layer units
		int l = this.layers.length - 1;
		this.layers[l] = new LogisticUnit[noutputs];
		for (int j=0; j < noutputs; j++) {
			this.layers[l][j] = new LogisticUnit();
			for (int i=0; i < layers[l-1].length; i++) {
				new Connection(this.layers[l-1][i], this.layers[l][j]);
			}
		}
	}
	
	/**
	 * Construct and return a new MultiLayerFeedForwardNeuralNetwork with the given
	 * number of input units, a single hidden layer of the given length,
	 * and the given number of output units, where each layer is fully connected to the next.
	 */
	public MultiLayerFeedForwardNeuralNetwork(int ninputs, int nhiddens, int noutputs) {
		this(ninputs, new int[]{ nhiddens }, noutputs);
	}
	


	/**
	 * Print this MultiLayerFeedForwardNeuralNetwork to stdout.
	 * All we print the weights of each unit in each non-input layer in
	 * tab-separated format: LAYERNUM UNITNUM w_0 w_1 ... w_n.
	 */
	public void dump() {
		System.out.println("LAYER\tUNIT\tw_0 ...");
		for (int l=1; l < this.layers.length; l++) {
			NeuronUnit units[] = (NeuronUnit[])this.layers[l];
			for (int i=0; i < units.length; i++) {
				NeuronUnit unit = units[i];
				System.out.format("%d\t%d", l, i);
				for (Connection conn : unit.incomingConnections) {
					System.out.format("\t%.2f", conn.weight);
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Output of a MultiLayerFeedForwardNeuralNetwork is the index of output unit
	 * (i.e., class label) with the maximum activation.
	 * You could override this in a subclass if you wanted something different.
	 */
	public int getOutputValue() {
		double max = Double.NEGATIVE_INFINITY;
		int answer = -1;
		for (int i=0; i < this.getOutputUnits().length; i++) {
			Unit unit = this.getOutputUnits()[i];
			if (unit.getOutput() > max) {
				answer = i;
				max = unit.getOutput();
			}
		}
		return answer;
	}

	/**
	 * AIMA BACK-PROP-LEARNING algorithm (Fig 18.24).
	 */
	public ArrayList<String> train(List<Example> examples, int epochs, double alpha, boolean error) {
		initializeWeights();
		// ``until some stopping criterion is specified'' (we use a fixed number of epochs)
		//System.out.println(epochs);
		ArrayList<String> output = new ArrayList<String>();
		for (int epoch=1; epoch <= epochs; epoch++) {
			notifyTrainingEpochStarted(epoch);
			for (Example example : examples) {
				train(example, alpha);
			}
			if(error == false)
				output.add(epoch + "," + test(examples));
			else
				output.add(epoch + "," + (1-test(examples)));
			notifyTrainingEpochCompleted(epoch);
		}
		return output;
	}
	
	/**
	 * AIMA Fig 18.24 says weights should each be initialized to
	 * ``a small random number.'' WTF? It also has that step inside
	 * the learning loop, which strikes me as very wrong.
	 * For now I'm setting all non-input unit weights to w in range [-0.05,0.05].
	 */
	public void initializeWeights() {
		for (int l=1; l < this.layers.length; l++) {
			NeuronUnit units[] = (NeuronUnit[])this.layers[l];
			for (int i=0; i < units.length; i++) {
				NeuronUnit unit = units[i];
				for (Connection conn : unit.incomingConnections) {
					double w = random.nextDouble() * 0.1 - 0.05;
					conn.weight = w;
				}
			}
		}
	}
	
	protected Random random = new Random();

	/**
	 * AIMA Fig 18.14 body of loop looks like three steps, but it's really four:
	 * (1) ``Propagate the inputs forward to compute the outputs''
	 * (2) Computing the error vector delta
	 * (3) ``Propagating deltas backward from output layer to input layer''
	 * (4) ``Update every weight in network using deltas''
	 */
	public void train(Example example, double alpha) {
		propagate(example);
		backprop(example, alpha);
		
	}
	
	/**
	 * AIMA Fig 18.14: Body of main loop, step 1:
	 * ``Propagate the inputs forward to compute the outputs.''
	 */
	public void propagate(Example example) {
		// for each node i in the input layer do
		InputUnit[] inputs = this.getInputUnits();
		for (int i=0; i < inputs.length; i++) {
			// a_i <- x_i
			inputs[i].setOutput(example.inputs[i]);
		}
		// for l from 2 to L do
		for (int l=1; l < this.layers.length; l++) {
			// for each node j in layer l do
			for (int j=0; j < this.layers[l].length; j++) {
				Unit unit = this.layers[l][j];
				// in_j <- \sum_i w_i,j a_i; a_j <- g(in_j)
				unit.run();
			}
		}
	}
		
	/**
	 * AIMA Fig 18.14 body of loop after propagating inputs forward:
	 * (2) Computing the error vector delta
	 * (3) ``Propagating deltas backward from output layer to input layer''
	 * (4) ``Update every weight in network using deltas''
	 */
	
	public void backprop(Example example, double alpha) {

		// This must be implemented by you

		// for each node j in the output layer do
		//     Delta[j] <- g'(in_j) \times (y_j - a_j)
		int L = this.layers.length - 1; 
		//NeuronUnit[] outputlayer = (NeuronUnit[])this.layers[l];
		for(int j = 0; j < this.layers[L].length; j++) {
			LogisticUnit curunit = (LogisticUnit)this.layers[L][j];
			curunit.delta = curunit.activationPrime(curunit.getInputSum()) * (example.outputs[j] - curunit.getOutput());
		}
		// for l = L-1 to 1 do
		for(int l = L-1; l > 0; l--) {
//		     for each node i in layer l do 
			for(LogisticUnit curunit : (LogisticUnit[])this.layers[l] ) {
				//         Delta[i] <- g'(in_i) * \sum_j w_ij Delta[j]
				curunit.delta =  curunit.activationPrime(curunit.getInputSum()) * curunit.outputdeltasum();
			}
		}
		// for each weight w_ij in network do
		//     w_ij <- w_ij + alpha * a_i * delta_j
		for (int l=1; l < this.layers.length; l++) {
			for (int j=0; j < this.layers[l].length; j++) {
				NeuronUnit unit = (NeuronUnit)this.layers[l][j];
				unit.update(alpha);
			}
		}
		
	}
	
	/**
	 * Return true if this MultiLayerFeedForwardNeuralNetwork gets the right answer
	 * on the given Example.
	 * ``Getting the right answer'' depends on how the problem is represented in the
	 * network. This default implementation assumes that there is one output unit per
	 * class label (that is, a vector output). In the Example, only one of these will
	 * be 1.0 and the others will be 0.0. This method tests that the index of the output
	 * unit with the highest activation (the network's ``output'') is the index of the
	 * 1.0 in the Example's outputs.
	 * Other things are possible, in which case subclasses can override
	 * this implementation. 
	 */
	@Override
	public boolean test(Example example) {

		// Propagate the example's inputs forward through the network
		propagate(example);
		// Determine which output unit has highest activation
		int prediction = getOutputValue();
		//System.out.println(prediction);
		// Return true if that's the one that's supposed to be on according to the example
		boolean result = (example.outputs[prediction] == 1.0);
		return result;
	}

	/**
	 * Run a k-fold cross-validation experiment on this MultiLayerFeedForwardNeuralNetwork using
	 * the given Examples and return the average accuracy over the k trials.
	 * For this type of NeuralNetwork, we use a closure to pass the number of epochs and learning
	 * rate alpha into the training procedure.
	 */
	public double kFoldCrossValidate(List<Example> examples, int k, int epochs, double alpha) {
		NeuralNetwork.Trainer trainer = new NeuralNetwork.Trainer() {
			public void train(NeuralNetwork network, List<Example> examples) {
				((MultiLayerFeedForwardNeuralNetwork)network).train(examples, epochs, alpha, false);
			}
		};
		NeuralNetwork.Tester tester = new NeuralNetwork.Tester() {
			public double test(NeuralNetwork network, List<Example> examples) {
				return ((MultiLayerFeedForwardNeuralNetwork)network).test(examples);
			}
		};
		return super.kFoldCrossValidate(examples, k, trainer, tester);
	}

	public List<Example> readfileMNIST(String filename) throws FileNotFoundException{
		List<Example> examples= new ArrayList<Example> ();
		File myobj= new File( filename);
	    Scanner scnr = new Scanner(myobj);
	    String[] lines;
	    double[] inputs;
	    for(int j = 0; j < 500; j++) {
	    	
	    	String line = scnr.nextLine();
	    	lines = line.split(",");
		    double[] outputs = new double[10];
		    outputs[Integer.parseInt(lines[0])] = 1;
	    	inputs = new double[785];
	    	for(int i=0;i<lines.length-1;i++) {
	    		inputs[i]= Double.parseDouble(lines[i+1]);
	    	}
	    	inputs[784] = 1.0;
	    	Example example = new Example(inputs,outputs);
	    	examples.add(example);
	      }
	    
	    scnr.close();
	    return examples;
	}
	public List<Example> readfileIRIS(String filename) throws FileNotFoundException{
		List<Example> examples= new ArrayList<Example> ();
		File myobj= new File( filename);
	    Scanner scnr = new Scanner(myobj);
	    String[] lines;
	    double[] inputs;
	    while (scnr.hasNextLine()) {
	    	
	    	String line = scnr.nextLine();
	    	lines = line.split(",");
		    double[] outputs = new double[3];
		    if(lines[lines.length-1].equals("Iris-setosa")) {
		    	outputs[0] = 1;
		    }
		    else if(lines[lines.length-1].equals("Iris-versicolor")) {
		    	outputs[1] = 1;
		    }
		    else if(lines[lines.length-1].equals("Iris-virginica")) {
		    	outputs[2] = 1;
		    }
	    	inputs = new double[5];
	    	for(int i=0;i<lines.length-1;i++) {
	    		inputs[i]= Double.parseDouble(lines[i]);
	    	}
	    	
	    	inputs[4] = 1.0;
	    	Example example = new Example(inputs,outputs);
	    	examples.add(example);
	    //	System.out.println(example);
	      }
	    
	    scnr.close();

	    Collections.shuffle(examples);
//    	System.out.println(examples);

	    return examples;
	}
	public static void MNISTrun(int epochs, double learnRate, int numHidden, int k) throws FileNotFoundException {
		MultiLayerFeedForwardNeuralNetwork network1 = new MultiLayerFeedForwardNeuralNetwork(785, numHidden, 10);
		List<Example> examples = network1.readfileMNIST("src/learn/nn/examples/MNIST_train.txt");
		ArrayList<String> output = network1.train(examples, epochs, learnRate, false);
		System.out.println("Overall Accuracy (after "+epochs+ " epochs): " + network1.test(examples));
		System.out.println();
		System.out.println("K-Fold Cross Validation");
		System.out.println("-----------------------");
		network1.export(output, "NN_data.txt");
		network1.test(examples);
		network1.kFoldCrossValidate(examples, k, epochs, learnRate);
	}
	
	public void export(ArrayList<String> input, String filename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filename);
        for (int i = 0; i < input.size(); i++) {
        	out.println(input.get(i));
        }
        out.close();
	}
	
	public static void IRISrun(int epochs, double learnRate, int numHidden, int k) throws FileNotFoundException
	{
		MultiLayerFeedForwardNeuralNetwork network1 = new MultiLayerFeedForwardNeuralNetwork(5, numHidden, 3 );
		List<Example> examples = network1.readfileIRIS("src/learn/nn/examples/iris.data.txt");
		ArrayList<String> output = network1.train(examples, epochs, learnRate, false);
		System.out.println("Overall Accuracy (after "+epochs+ " epochs): " + network1.test(examples));
		System.out.println();
		System.out.println("K-Fold Cross Validation");
		System.out.println("-----------------------");
		network1.export(output, "NN_data.txt");
		output = network1.train(examples, epochs, learnRate, true);
		network1.export(output, "NN_data_error.txt");
		network1.kFoldCrossValidate(examples, k, epochs, learnRate);
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		if(args[0].equals("IRIS")) {
			IRISrun(Integer.parseInt(args[1]), Double.parseDouble(args[2]), 
					Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		} else if (args[0].equals("MNIST")){
			MNISTrun(Integer.parseInt(args[1]), Double.parseDouble(args[2]), 
					Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		}

	}
}
