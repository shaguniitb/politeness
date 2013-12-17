import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.converters.ArffSaver;
import weka.core.neighboursearch.LinearNNSearch;


public class BlogTesting {

	public static void testResults(Classifier cls, Instances test_data) throws Exception{
		int numTestInstances = test_data.numInstances();
		System.out.printf("There are %d test instances\n", numTestInstances);
		
		for (int i=0; i<numTestInstances; i++){
			double predictionIndex = cls.classifyInstance(test_data.instance(i));
			double [] predictionDistribution = cls.distributionForInstance(test_data.instance(i));
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

		              System.out.printf("[%10s : %6.3f]", 
		                                predictionDistributionIndexAsClassLabel, 
		                                predictionProbability );
		          }
		}
		System.out.println("\n");		
	}
	
	public static void TestingNaiveBayes(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new NaiveBayes();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		System.out.println("Naive Bayes");
		testResults(cls, test_data);
	}
	
	public static void TestingNaiveBayesMultinomial(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new NaiveBayesMultinomial();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		System.out.println("Naive Bayes Multinomial");
		testResults(cls, test_data);
	}
	
	public static void TestingJ48(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new J48();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		System.out.println("J48");
		testResults(cls, test_data);
	}
	
	public static void TestingSMO(Instances training_data, Instances test_data, String folder) throws Exception{
		Classifier cls = new SMO();
		cls.buildClassifier(training_data);
		Evaluation eval = new Evaluation(training_data);
		eval.evaluateModel(cls, test_data);
		System.out.println("SMO");
		testResults(cls, test_data);
	}
	
	public static void runTests(Instances data, Instances test_data, String folder) throws Exception{
		CrossValidating.CreateFolder(folder);
		TestingNaiveBayes(data, test_data, folder);
		TestingNaiveBayesMultinomial(data, test_data, folder);
		TestingJ48(data, test_data, folder);
		TestingSMO(data, test_data, folder);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String input_arff = "../../weka-3-6-10/wiki_BOW.arff";
		String test_arff = "../../weka-3-6-10/blogs.arff";
		
		String main_folder = "results/blogs_wiki_BOW";
		File data = new File(main_folder);
		String folder = null;
		data.mkdir();
		
		/* Preprocessing using Alphabetic Tokenizer*/
		Instances [] alpha_ret = PreProcessing.preAlphaTestProcess(input_arff, test_arff);
		Instances pre_alpha_data = alpha_ret[0]; 
		Instances pre_alpha_test_data = alpha_ret[1];		
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(pre_alpha_data);
		saver.setFile(new File("../../weka-3-6-10/new_wiki_BOW.arff"));
//		saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		saver.writeBatch();
				
		ArffSaver tsaver = new ArffSaver();
		tsaver.setInstances(pre_alpha_test_data);
		tsaver.setFile(new File("../../weka-3-6-10/new_blogs.arff"));
//		saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		tsaver.writeBatch();
		
		folder = main_folder + "/pre_alpha";
		runTests(pre_alpha_data, pre_alpha_test_data, folder);

	}

}
