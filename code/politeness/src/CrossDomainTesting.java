import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;


public class CrossDomainTesting {
	
	public static void TestingNaiveBayes(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new NaiveBayes();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/naive_bayes", eval);
	}

	public static void TestingNaiveBayesMultiNomial(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new NaiveBayesMultinomial();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/naive_bayes_multinomial", eval);
	}

	public static void TestingJ48(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new J48();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/J48", eval);
	}
	
	public static void TestingRandomForest(Instances training_data, Instances test_data, String folder, int numTrees) throws Exception{
		Evaluation eval = new Evaluation(training_data);
		RandomForest rf = new RandomForest();
		rf.setNumTrees(numTrees);
		Classifier cls = rf;
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/random_forest_"  + String.valueOf(numTrees) + "_trees", eval);
	}
	
	public static void TestingIbk(Instances training_data, Instances test_data, String folder, int KNN, LinearNNSearch search) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		IBk ibk = new IBk();
		ibk.setKNN(KNN);
		ibk.setNearestNeighbourSearchAlgorithm(search);
		Classifier cls = ibk;
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		DistanceFunction distanceFunction = search.getDistanceFunction();	
		String distance= distanceFunction.getClass().getSimpleName();
		String filename = "/ibk_" + String.valueOf(KNN) + "_KNN_" + distance;
		CrossValidating.writeOut(folder + filename, eval);
	}

	public static void TestingSMO(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new SMO();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/SMO", eval);
	}
	
	public static void runTests(Instances data, Instances test_data, String folder) throws Exception{
		CrossValidating.CreateFolder(folder);
		TestingNaiveBayes(data, test_data, folder);
		TestingNaiveBayesMultiNomial(data, test_data, folder);
		TestingJ48(data, test_data, folder);
		
		TestingRandomForest(data, test_data, folder, 10);
		TestingRandomForest(data, test_data, folder, 100);
		
		LinearNNSearch euc_search = new LinearNNSearch();		
		euc_search.setDistanceFunction(new EuclideanDistance());
		TestingIbk(data, test_data, folder, 1, euc_search);
		TestingIbk(data, test_data, folder, 10, euc_search);
		
		LinearNNSearch manhattan_search = new LinearNNSearch();		
		manhattan_search.setDistanceFunction(new ManhattanDistance());
		TestingIbk(data, test_data, folder, 1, manhattan_search);
		TestingIbk(data, test_data, folder, 10, manhattan_search);
		
		TestingSMO(data, test_data, folder);
	
		
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String input_arff = "../../weka-3-6-10/stack_Ling.arff";
		String test_arff = "../../weka-3-6-10/wiki_Ling.arff";
		
		String main_folder = "results/cross-domain_stack_Ling";
		File data = new File(main_folder);
		String folder = null;
		data.mkdir();
		
		/* Preprocessing using Alphabetic Tokenizer*/
		Instances [] alpha_ret = PreProcessing.preAlphaTestProcess(input_arff, test_arff);
		Instances pre_alpha_data = alpha_ret[0]; 
		Instances pre_alpha_test_data = alpha_ret[1];		
		folder = main_folder + "/pre_alpha";
		runTests(pre_alpha_data, pre_alpha_test_data, folder);
		
		/* Preprocessing using Word Tokenizer*/
		Instances [] word_ret = PreProcessing.preWordTestProcess(input_arff, test_arff);
		Instances pre_word_data = word_ret[0]; 
		Instances pre_word_test_data = word_ret[1];		
		folder = main_folder + "/pre_word";
		runTests(pre_word_data, pre_word_test_data, folder);
		
		/* Preprocessing using Alphabetic Tokenizer and Attribute Selection */
		Instances [] alpha_attr_select_ret = PreProcessing.TestAttributeSelecting(alpha_ret);
		Instances pre_alpha_attr_select = alpha_attr_select_ret[0]; 
		Instances pre_alpha_attr_select_test = alpha_attr_select_ret[1];		
		folder = main_folder + "/pre_alpha_with_attribute_selection";
		runTests(pre_alpha_attr_select, pre_alpha_attr_select_test, folder);
		
		/* Preprocessing using Word Tokenizer and Attribute Selection */
		Instances [] word_attr_select_ret = PreProcessing.TestAttributeSelecting(word_ret);
		Instances pre_word_attr_select = word_attr_select_ret[0]; 
		Instances pre_word_attr_select_test = word_attr_select_ret[1];		
		folder = main_folder + "/pre_word_with_attribute_selection";
		runTests(pre_word_attr_select, pre_word_attr_select_test, folder);
		
		
		

	}

}
