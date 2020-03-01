import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
    	
    
    	int foldSize = trainData.size()/k;
    	
    	List<List<Instance>> chunks = new ArrayList<List<Instance>>();
    	List<Double> accuracies = new ArrayList<Double>();
    	int startIndex = 0;
    	int endIndex = foldSize;
    	while(endIndex <= trainData.size())
    	{
    		List<Instance> lis = new ArrayList<Instance>();
    		for(int i = startIndex; i < endIndex; i++ )
    		{
    			lis.add(trainData.get(i));
    		}
    		chunks.add(lis);
    		startIndex += foldSize;
    		endIndex += foldSize;
    	}
    	
    	for(int i = 0; i < k; i++)
    	{
    	List<List<Instance>> temp = new ArrayList<List<Instance>>(chunks);
    	
    	List<Instance> test = temp.remove(i);
    	List<Instance> total = new ArrayList<Instance>();
    	for(List<Instance> inst : temp)
    	{
    		total.addAll(inst);
    	}
    	clf = new NaiveBayesClassifier();
    	clf.train(total, v);
    	int correctlyClassified = 0;
    	int count = 0;
    	for(Instance inst: test)
    	{
    		ClassifyResult res = clf.classify(inst.words);
    		if(res.label == inst.label)
    		{
    			correctlyClassified++;
    		}
    		count++;
    	}
    	accuracies.add((double)correctlyClassified/(double)count);
    	}
    	double accSum = 0.0;
    	for(Double accuracy : accuracies) {
    		accSum += accuracy;
    	}
    	
        return (double)accSum/k;
    }
}
