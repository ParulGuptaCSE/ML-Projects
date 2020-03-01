import java.util.*;

/**
 * The main class that handles the entire network Has multiple attributes each
 * with its own use
 */

public class NNImpl {
	private ArrayList<Node> inputNodes; // list of the output layer nodes.
	private ArrayList<Node> hiddenNodes; // list of the hidden layer nodes
	private ArrayList<Node> outputNodes; // list of the output layer nodes

	private ArrayList<Instance> trainingSet; // the training set

	private double learningRate; // variable to store the learning rate
	private int maxEpoch; // variable to store the maximum number of epochs
	private Random random; // random number generator to shuffle the training set

	/**
	 * This constructor creates the nodes necessary for the neural network Also
	 * connects the nodes of different layers After calling the constructor the last
	 * node of both inputNodes and hiddenNodes will be bias nodes.
	 */

	NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random,
			Double[][] hiddenWeights, Double[][] outputWeights) {
		this.trainingSet = trainingSet;
		this.learningRate = learningRate;
		this.maxEpoch = maxEpoch;
		this.random = random;

		// input layer nodes
		inputNodes = new ArrayList<>();
		int inputNodeCount = trainingSet.get(0).attributes.size();
		int outputNodeCount = trainingSet.get(0).classValues.size();
		for (int i = 0; i < inputNodeCount; i++) {
			Node node = new Node(0);
			inputNodes.add(node);
		}

		// bias node from input layer to hidden
		Node biasToHidden = new Node(1);
		inputNodes.add(biasToHidden);

		// hidden layer nodes
		hiddenNodes = new ArrayList<>();
		for (int i = 0; i < hiddenNodeCount; i++) {
			Node node = new Node(2);
			// Connecting hidden layer nodes with input layer nodes
			for (int j = 0; j < inputNodes.size(); j++) {
				NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
				node.parents.add(nwp);
			}
			hiddenNodes.add(node);
		}

		// bias node from hidden layer to output
		Node biasToOutput = new Node(3);
		hiddenNodes.add(biasToOutput);

		// Output node layer
		outputNodes = new ArrayList<>();
		for (int i = 0; i < outputNodeCount; i++) {
			Node node = new Node(4);
			// Connecting output layer nodes with hidden layer nodes
			for (int j = 0; j < hiddenNodes.size(); j++) {
				NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
				node.parents.add(nwp);
			}
			outputNodes.add(node);
		}
	}

	/**
	 * Get the prediction from the neural network for a single instance Return the
	 * idx with highest output values. For example if the outputs of the outputNodes
	 * are [0.1, 0.5, 0.2], it should return 1. The parameter is a single instance
	 */

	public int predict(Instance instance) {
		// TODO: add code here
		double maxValue = -1;
		int resIndex = -1;

		for (int i = 0; i < inputNodes.size() - 1; i++) {
			inputNodes.get(i).setInput(instance.attributes.get(i));
			// inputNodes.get(i).calculateOutput(null);
		}
		inputNodes.get(inputNodes.size() - 1).setInput(1);

		for (Node hiddenNode : hiddenNodes) {

			hiddenNode.calculateOutput(null);
		}
		// hiddenNodes.get(hiddenNodes.size() - 1).getOutput();

		for (Node outputNode : outputNodes) {
			outputNode.calculateOutput(outputNodes);
		}

		for (int i = 0; i < outputNodes.size(); i++) {
			if (outputNodes.get(i).getOutput() > maxValue) {
				maxValue = outputNodes.get(i).getOutput();
				resIndex = i;
			}
		}
		return resIndex;
	}

	/**
	 * Train the neural networks with the given parameters
	 * <p>
	 * The parameters are stored as attributes of this class
	 */

	public void train() {
		// TODO: add code here
		int currentEpoch = 0;
		
		while (currentEpoch < maxEpoch) {
			
			Collections.shuffle(trainingSet, random);
			for (Instance inst : trainingSet) {

				for (int i = 0; i < inputNodes.size() - 1; i++) {
					inputNodes.get(i).setInput(inst.attributes.get(i));
				}
				inputNodes.get(inputNodes.size() - 1).setInput(1);

				for (Node hiddenNode : hiddenNodes) {

					hiddenNode.calculateOutput(null);
				}
			
				for (Node outputNode : outputNodes) {
					outputNode.calculateOutput(outputNodes);
				}

				int count = 0;
				for (Node outputNode : outputNodes) {
					outputNode.calculateDelta(inst.classValues.get(count++), null);
				}

				for (Node hiddenNode : hiddenNodes) {
					hiddenNode.calculateDelta(-12345678, outputNodes);
				}
				for (Node outputNode : outputNodes) {
					outputNode.updateWeight(learningRate);
				}

				for (Node hiddenNode : hiddenNodes) {
					hiddenNode.updateWeight(learningRate);
				}
			} //end of one epoch
			List<Double> losses = new ArrayList<Double>();
			for (Instance inst : trainingSet)
			{
				for (int i = 0; i < inputNodes.size() - 1; i++) {
					inputNodes.get(i).setInput(inst.attributes.get(i));
					// inputNodes.get(i).calculateOutput(null);
				}
				inputNodes.get(inputNodes.size() - 1).setInput(1);
				for (Node hiddenNode : hiddenNodes) {
					hiddenNode.calculateOutput(null);
					}
				
				for (Node outputNode : outputNodes) {
					  outputNode.calculateOutput(outputNodes);
					  }
				losses.add(loss(inst));
			}
			double loss = cumulativeLoss(losses);

			// String s = String.format("%.3e", loss);
			// loss = Double.parseDouble(s);
			// System.out.print(value+" kg\n");
			System.out.format("Epoch: %d, Loss: %.3e", currentEpoch,loss);
			System.out.println();
			//System.out.println("Epoch: " + currentEpoch + ", Loss: " + loss);
			currentEpoch++;
		}

	}

	/**
	 * Calculate the cross entropy loss from the neural network for a single
	 * instance. The parameter is a single instance
	 */
	private double loss(Instance instance) {
		// TODO: add code here
		double loss = 0.0;
		ArrayList<Integer> targetClassValues = instance.classValues;

		for (int i = 0; i < targetClassValues.size(); i++) {
			// outputNodes.get(i).calculateOutput(outputNodes);
			loss += -targetClassValues.get(i) * java.lang.Math.log(outputNodes.get(i).getOutput());
		}

		return loss;
	}

	private double cumulativeLoss(List<Double> losses) {
		// TODO: add code here
		double sum = 0.0;
		for (Double loss : losses) {
			sum += loss;
		}
		// System.out.println(losses.size());
		// System.out.println(maxEpoch);
		return (double) sum / (double) losses.size();
	}

}
