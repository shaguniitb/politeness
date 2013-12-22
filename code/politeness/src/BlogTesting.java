import java.io.File;

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
import weka.core.converters.ArffSaver;
import weka.core.neighboursearch.LinearNNSearch;


public class BlogTesting {	

	public static void writeBlogOut(String className, Classifier cls, Instances test_data) throws Exception{
		System.out.println(className);
		String fileName = className + ".txt";
		int numTestInstances = test_data.numInstances();
		System.out.printf("There are %d test instances\n", numTestInstances);
		String output = null;
		output = "blog #\tpolite\timpolite";
		CrossValidating.WriteOutput(fileName, output, true);
		for (int i=0; i<numTestInstances; i++){
			double predictionIndex = cls.classifyInstance(test_data.instance(i));
			double [] predictionDistribution = cls.distributionForInstance(test_data.instance(i));
			output = "blog " + String.valueOf(i+6);
		       for (int predictionDistributionIndex = 0; 
		               predictionDistributionIndex < predictionDistribution.length; 
		               predictionDistributionIndex++)
		          {
		              // Get this distribution index's class label.
		              String predictionDistributionIndexAsClassLabel = 
		                  test_data.classAttribute().value(
		                      predictionDistributionIndex);

		              // Get the probability.
		              double predictionProbability = 
		                  predictionDistribution[predictionDistributionIndex];
		              predictionProbability = (double)Math.round(predictionProbability*1000)/1000;
		              
		              System.out.printf("[%10s : %6.3f]", 
                              predictionDistributionIndexAsClassLabel, 
                              predictionProbability );

		               output += "\t" + predictionDistributionIndexAsClassLabel + "\t" + predictionProbability;		              
		          }
		      CrossValidating.WriteOutput(fileName, output, i==0?false:true);
		}
		System.out.println("\n");		
	}
	
	public static void TestingNaiveBayes(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new NaiveBayes();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);		
		writeBlogOut(folder + "/naive_bayes", cls, test_data);		
	}	
	
	public static void TestingNaiveBayesMultinomial(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new NaiveBayesMultinomial();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		writeBlogOut(folder + "/naive_bayes_multinomial", cls, test_data);		
	}
	
	public static void TestingJ48(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new J48();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		writeBlogOut(folder + "/J48", cls, test_data);		
	}
	
	public static void TestingRandomForest(Instances training_data, Instances test_data, String folder, int numTrees) throws Exception{
		RandomForest rf = new RandomForest();
		rf.setNumTrees(numTrees);
		Classifier cls = rf;
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		String filename = "random_forest_" + String.valueOf(numTrees) + "_trees";
		writeBlogOut(folder + "/" + filename, cls, test_data);		
	}
	
	public static void TestingIbk(Instances training_data, Instances test_data, String folder,  int KNN, LinearNNSearch search) throws Exception{
		IBk ibk = new IBk();
		ibk.setKNN(KNN);
		ibk.setNearestNeighbourSearchAlgorithm(search);
		Classifier cls = ibk;
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		DistanceFunction distanceFunction = search.getDistanceFunction();		
		String distance= distanceFunction.getClass().getSimpleName();
		String filename = "ibk_" + String.valueOf(KNN) + "_KNN_" + distance;		
		writeBlogOut(folder + "/" + filename, cls, test_data);		
	}	
	
	public static void TestingSMO(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new SMO();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		writeBlogOut(folder + "/SMO", cls, test_data);
	}
	
	public static void runTests(Instances data, Instances test_data, String folder) throws Exception{
		CrossValidating.CreateFolder(folder);
		TestingNaiveBayes(data, test_data, folder);
		TestingNaiveBayesMultinomial(data, test_data, folder);
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
		String test_arff = "../../weka-3-6-10/kblogs.arff";
		
		String main_folder = "results/kblogs_stack_Ling";
		File data = new File(main_folder);
		String folder = null;
		data.mkdir();
		
		/* Preprocessing using Alphabetic Tokenizer and Attribute Selection */
		Instances [] alpha_ret = PreProcessing.preAlphaTestProcess(input_arff, test_arff);
		Instances [] alpha_attr_ret = PreProcessing.TestAttributeSelecting(alpha_ret);
		Instances pre_alpha_data = alpha_attr_ret[0]; 
		Instances pre_alpha_test_data = alpha_attr_ret[1];		
		
		folder = main_folder + "/pre_alpha_with_attribute_selection";
		runTests(pre_alpha_data, pre_alpha_test_data, folder);

	}

}
