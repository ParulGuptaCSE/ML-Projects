import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifier implements Classifier {
	Map<String, Integer> myPosWordCountMap = new HashMap<String, Integer>();
	Map<String, Integer> myNegWordCountMap = new HashMap<String, Integer>();
	Integer myPosWordCount = 0;
	Integer myNegWordCount = 0;
	Map<Label, Integer> myDocCountPerLabel;
	Map<Label, Integer> myWordCountPerLabel;
	Integer myVocabSize = 0;

	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(List<Instance> trainData, int v) {
		// TODO : Implement
		// Hint: First, calculate the documents and words counts per label and store
		// them.
		// Then, for all the words in the documents of each label, count the number of
		// occurrences of each word.
		// Save these information as you will need them to calculate the log
		// probabilities later.
		//
		// e.g.
		// Assume m_map is the map that stores the occurrences per word for positive
		// documents
		// m_map.get("catch") should return the number of "catch" es, in the documents
		// labeled positive
		// m_map.get("asdasd") would return null, when the word has not appeared before.
		// Use m_map.put(word,1) to put the first count in.
		// Use m_map.replace(word, count+1) to update the value
		myVocabSize = v;
		myWordCountPerLabel = getWordsCountPerLabel(trainData);
		myDocCountPerLabel = getDocumentsCountPerLabel(trainData);
		

		for (Instance inst : trainData) {
			if (inst.label == Label.POSITIVE) {
				for (String word : inst.words) {
					myPosWordCountMap.put(word, myPosWordCountMap.getOrDefault(word, 0) + 1);
					myPosWordCount++;
				}
			} else {
				for (String word : inst.words) {
					myNegWordCountMap.put(word, myNegWordCountMap.getOrDefault(word, 0) + 1);
					myNegWordCount++;
				}
			}
		}

	}

	/*
	 * Counts the number of words for each label
	 */
	@Override
	public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
		Map<Label, Integer> map = new HashMap<Label, Integer>();
		for (Instance inst : trainData) {
			if (inst.label == Label.NEGATIVE) {
				map.put(Label.NEGATIVE, map.getOrDefault(inst.label, 0) + inst.words.size());
			} else {
				map.put(Label.POSITIVE, map.getOrDefault(inst.label, 0) + inst.words.size());
			}

		}

		return map;
	}

	/*
	 * Counts the total number of documents for each label
	 */
	@Override
	public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
		Map<Label, Integer> map = new HashMap<Label, Integer>();

		for (Instance inst : trainData) {
			if (inst.label == Label.NEGATIVE) {
				map.put(Label.NEGATIVE, map.getOrDefault(inst.label, 0) + 1);
			} else {
				map.put(Label.POSITIVE, map.getOrDefault(inst.label, 0) + 1);
			}
		}
		return map;
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or
	 * P(NEGATIVE)
	 */
	private double p_l(Label label) {
		// TODO : Implement
		// Calculate the probability for the label. No smoothing here.
		// Just the number of label counts divided by the number of documents.
		double denominator = myDocCountPerLabel.get(label.POSITIVE) + myDocCountPerLabel.get(label.NEGATIVE);
		return (double)myDocCountPerLabel.get(label)/ (double)denominator;		
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|POSITIVE) or P(word|NEGATIVE)
	 */
	private double p_w_given_l(String word, Label label) 
	{		
		// Calculate the probability with Laplace smoothing for word in class(label)

		
		if(label == label.POSITIVE)
		{
			return (double)(myPosWordCountMap.getOrDefault(word, 0)+1) / (myPosWordCount + myVocabSize);
		}
		else
		{
			return (double)(myNegWordCountMap.getOrDefault(word, 0)+1 )/ (myNegWordCount + myVocabSize);
		}		
	}  

	/**
	 * Classifies an array of words as either POSITIVE or NEGATIVE.
	 */
	@Override
	public ClassifyResult classify(List<String> words) {
		// TODO : Implement
		// Sum up the log probabilities for each word in the input data, and the
		// probability of the label
		// Set the label to the class with larger log probability
		ClassifyResult res = new ClassifyResult();
		Map<Label, Double> logProbPerLabel = new HashMap<Label, Double>();
		double posProb = 0.0;
		for(String word : words) {
			posProb +=Math.log(p_w_given_l(word, Label.POSITIVE));
		}		
		posProb += Math.log(p_l(Label.POSITIVE));
		logProbPerLabel.put(Label.POSITIVE, posProb);
		double negProb = 0.0;
		for(String word : words) {
			negProb +=Math.log(p_w_given_l(word, Label.NEGATIVE));
		}
		negProb += Math.log(p_l(Label.NEGATIVE));
		logProbPerLabel.put(Label.NEGATIVE, negProb);
		res.logProbPerLabel = logProbPerLabel;
		if(posProb >= negProb)
		{
			res.label = Label.POSITIVE;
		}
		else
		{
			res.label = Label.NEGATIVE;
		}
		return res;
	}

}
