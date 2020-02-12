import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;
	public int[] candidateSplits = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private int depth = 0;
	private int numOfNodeInstances = 0;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0)
			this.numAttr = trainDataSet.get(0).size() - 1;

		this.root = buildTree(trainDataSet, candidateSplits, getMajorityLabel(trainDataSet));
	}

	private DecTreeNode buildTree(List<List<Integer>> examples, int[] attributes, int defaultLabel) {
		// TODO: add code here
		/*
		 * tree.left = new DecTreeNode(getMajorityLabel(leftExamples), bestAttr,
		 * bestThresh); tree.right = new DecTreeNode(getMajorityLabel(leftExamples),
		 * bestAttr, bestThresh);
		 */
		List<Integer> labels = new ArrayList<Integer>();
		labels.add(0);
		labels.add(1);

		if (examples.isEmpty()) {
			return new DecTreeNode(defaultLabel);
		}

		int homolabel = IsHomogeneousList(examples);

		if (labels.contains(homolabel)) {
			return new DecTreeNode(homolabel);
		}

		numOfNodeInstances = examples.size();
		// if(attribute are empty) Is it possible ?
		double[] attributeAndThreshold = BestAttributeAndThreshold(examples);
		int bestAttr = (int) attributeAndThreshold[0];
		int bestThresh = (int) attributeAndThreshold[1];
		double informationGain = attributeAndThreshold[2];
		if (isLeafNode(numOfNodeInstances, depth, informationGain)) {
			return new DecTreeNode(getMajorityLabel(examples));
		}	
		

		DecTreeNode tree = new DecTreeNode(getMajorityLabel(examples), bestAttr, bestThresh);

		List<List<Integer>> leftExamples = new ArrayList<List<Integer>>();
		List<List<Integer>> rightExamples = new ArrayList<List<Integer>>();
		getLeftAndRightExamples(examples, bestAttr, bestThresh, leftExamples, rightExamples);
		depth++;
		tree.left = buildTree(leftExamples, candidateSplits, getMajorityLabel(leftExamples));
		tree.right = buildTree(rightExamples, candidateSplits, getMajorityLabel(rightExamples));
		depth--;
		 
		return tree;
	}

	private int IsHomogeneousList(List<List<Integer>> examples) {
		List<Integer> labels = new ArrayList<Integer>();
		examples.forEach(lis -> {
			labels.add(lis.get(9));
		});
		int countOf2 = 0;
		int countOf4 = 0;
		for (int label : labels) {
			if (label == 0) {
				countOf2++;
			} else {
				countOf4++;
			}
		}

		if (countOf2 == 0 ) {
			return 1;
		} else if (countOf4 == 0) {
			return 0;
		} else {
			return -1;
		}
	}

	private int getMajorityLabel(List<List<Integer>> examples) {
		List<Integer> labels = new ArrayList<Integer>();
		examples.forEach(lis -> {
			labels.add(lis.get(9));
		});
		int countOf2 = 0;
		int countOf4 = 0;
		for (int label : labels) {
			if (label == 0) {
				countOf2++;
			} else {
				countOf4++;
			}
		}
		if (countOf2 == countOf4) {
			return 1;
		}
		if (countOf2 > countOf4) {
			return 0;
		} else {
			return 1;
		}
	}

	private double[] getProbOfPosAndNegExamples(List<List<Integer>> examples) {

		double[] probs = new double[2];

		List<Integer> labels = new ArrayList<Integer>();
		examples.forEach(lis -> {
			labels.add(lis.get(9));
		});
		double countOf2 = 0;
		double countOf4 = 0;
		for (int label : labels) {
			if (label == 0) {
				countOf2++;
			} else {
				countOf4++;
			}
		}

		probs[0] = countOf2 / examples.size();
		probs[1] = countOf4 / examples.size();

		return probs;
	}

	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		
		return classifyRecusr(this.root, instance);
	}
	
	public int classifyRecusr(DecTreeNode root, List<Integer> instance) {
		if(root.isLeaf())
		{
			return root.classLabel;
		}
		else
		{
			if(instance.get(root.attribute) <= root.threshold)
			{
				return classifyRecusr(root.left, instance);
			}
			else
			{
				return classifyRecusr(root.right, instance);
			}
		}
	}

	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if (node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		} else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if (node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		} else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}

	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i++) {
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual * 100.0 / (double) numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}

	public boolean isLeafNode(int numOfNodeInstances, int depth, double informationGain) {
		if (numOfNodeInstances <= maxPerLeaf) {
			return true;
		}
		if (depth >= maxDepth) {
			return true;
		}
		if (informationGain == 0.0) {
			return true;
		}
		return false;
	}

	public double[] BestAttributeAndThreshold(List<List<Integer>> examples) {

		double[] attAndThresh = new double[3];
		double maxInfoGain = 0.0;
		int bestThreshold = 0;
		int bestAttribute = 0;
		for (int i = 0; i < candidateSplits.length; i++) {
			for (int j = 0; j < candidateSplits.length; j++) {
				
				double ig = FindInformationGain(examples, i, candidateSplits[j]);
				if (ig > maxInfoGain) {
					
					maxInfoGain = ig;
					bestThreshold = candidateSplits[j];
					bestAttribute = i;
				}
			}
		}
		attAndThresh[0] = bestAttribute;
		attAndThresh[1] = bestThreshold;
		attAndThresh[2] = maxInfoGain;
		return attAndThresh;
	}

	public double FindInformationGain(List<List<Integer>> examples, int attribute, int split) {
		double parentEntropy = FindEntropy(examples);
		List<List<Integer>> leftExamples = new ArrayList<List<Integer>>();
		List<List<Integer>> rightExamples = new ArrayList<List<Integer>>();
		getLeftAndRightExamples(examples, attribute, split, leftExamples, rightExamples);
		double probOfLeft = (double)leftExamples.size()/(double)examples.size();
		double probOfRight = (double)rightExamples.size()/(double)examples.size();
		double splitEntropy =  (FindEntropy(leftExamples) * probOfLeft)+ (FindEntropy(rightExamples) * probOfRight);
		double IG = parentEntropy - splitEntropy;
		return IG;
	}

	public void getLeftAndRightExamples(List<List<Integer>> examples, int attribute, int split,
			List<List<Integer>> left, List<List<Integer>> right) {
		for (List<Integer> lis : examples) {
			if (lis.get(attribute) <= split) {
				left.add(lis);
			} else {
				right.add(lis);
			}
		}
	}

	public double FindEntropy(List<List<Integer>> examples) {
		double[] values = getProbOfPosAndNegExamples(examples);
		double probOfPosInstances = values[0];
		double probOfNegInstances = values[1];
		//double entropy = -123456;

		if(probOfPosInstances == 0 && probOfNegInstances == 0)
		{
			return 0;
		}
		if(probOfNegInstances == 0)
		{
			return -(probOfPosInstances*log2(probOfPosInstances));
		}
		if(probOfPosInstances == 0)
		{
			return -(probOfNegInstances*log2(probOfNegInstances));
		}
		return -(probOfPosInstances*log2(probOfPosInstances)) - (probOfNegInstances*log2(probOfNegInstances));
		
	}

	public double log2(double x) {
		return (double) (Math.log(x) / Math.log(2));
	}
}
