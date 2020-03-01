# Machine-Learning-Projects
Machine Learning Algorithms 

ML Project consists of 3 mini projects which are described below:

* # Decision Tree for Breast Cancer Classification
Here, I have implemented a program that builds a binary decision tree for numerical attributes and binary classification tasks. Java programming is used for building a tree from a training set and classifying instances of both the training set and the testing set with the learned decision tree
The dataset used comes from the Wisconsin Breast Cancer dataset and can be found at the UCI machine learning repository: https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Original%29

Accuracy for the classifer is 92.5%

* # Sentiment Analysis

One application of Naïve Bayes classifiers is sentiment analysis, which is a sub-field of AI that extracts affective states and subjective information from text. One common use of sentiment analysis is to determine if a text document expresses negative or positive feelings. In this project I have implemented a Naïve Bayes classifier for categorizing movie reviews as either POSITIVE or NEGATIVE. The dataset provided consists of online movie reviews derived from an IMDb dataset: https://ai.stanford.edu/~amaas/data/sentiment/ that have been labeled based on the review scores. A negative review has a score ≤ 4 out of 10, and a positive review has a score ≥ 7 out of 10. We have done some preprocessing on the original dataset to remove some noisy features. Each row in the training set and test set files contains one review, where the first word in each line is the class label (1 represents POSITIVE and 0 represents NEGATIVE) and the remainder of the line is the review text.

Accuracy for the classifer is 93.4%

* # Handwritten Digit Recognition

Implemented a feed-forward neural network and the back-propagation algorithm using Java programming language. The neural network handles a multi-class classification problem for recognizing images of handwritten digits. All nodes in the hidden layer (except for the bias node) use the ReLU activation function, while all the nodes in the output layer use the Softmax activation function.

The dataset used is called Semeion (https://archive.ics.uci.edu/ml/datasets/Semeion+Handwritten+Digit). It contains 1,593 binary images of size 16 x 16 that each contain one handwritten digit.

Accuracy for the classifer is 97.4%
