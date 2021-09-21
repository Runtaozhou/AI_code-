README for lc.examples

AIMA earthquake
- AIMA Fig 18.5
- earthquake-clean.data.txt, earthquake-noisy.data.txt

house-votes
- http://archive.ics.uci.edu/ml/datasets/Congressional+Voting+Records
- house-votes-84.names.txt, house-votes-84.data.txt
- I created house-votes-84.data.num.txt with numeric attributes for use with linear
  classifiers: y=1, n=-1, ?=0, and class labels republican=0 and democrat=1.
  
You should write your own main methods to demonstrate your linear classifier implementation(s) and produce the data needed for your report.

Running a LinearClassifier should generally do the following:
- Read the data from a file
- Create the appropriate type of LinearClassifier with the appropriate number of inputs for the data
- Train the classifier on the data
  - Printing the information needed for graphs in your report after each step

Examples of what you might want to print (and then include in your report, usually as graphs):
- final accuracy on the training data, the testing data, or both (separately)
- per-step accuracy suitable for plotting in a learning curve

See AIMA for ideas and examples.
