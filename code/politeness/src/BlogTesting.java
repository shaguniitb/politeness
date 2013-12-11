import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;


public class BlogTesting {

	public static void TestingNaiveBayes(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new NaiveBayes();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/naive_bayes", eval);
		for (int i=0; i<test_data.numInstances(); i++){
			System.out.println("instance " + String.valueOf(i));
			test_data.instance(i).setClassMissing();
			double [] values = cls.distributionForInstance(test_data.instance(i));
			for (double value: values){
				System.out.println(value);
			}
			
		}
	}
	
	public static void TestingSMO(Instances training_data, Instances test_data, String folder) throws Exception{
		Evaluation eval = new Evaluation(training_data);		
		Classifier cls = new SMO();
		cls.buildClassifier(training_data);
		eval.evaluateModel(cls, test_data);		
		CrossValidating.writeOut(folder + "/SMO", eval);
		for (int i=0; i<test_data.numInstances(); i++){
			System.out.println("instance " + String.valueOf(i));
			test_data.instance(i).setClassMissing();
			double clsi = cls.classifyInstance(test_data.instance(i));
			System.out.println(clsi);
			double [] values = cls.distributionForInstance(test_data.instance(i));
			for (double value: values){
				System.out.println(value);
			}
			
		}
	}
	
	/**
	 * @param args
	 */
	
	public static void runTests(Instances data, Instances test_data, String folder) throws Exception{
		CrossValidating.CreateFolder(folder);
//		TestingNaiveBayes(data, test_data, folder);
		TestingSMO(data, test_data, folder);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String input_arff = "../../weka-3-6-10/wiki_BOW.arff";
		String test_arff = "/home/shagun/blogs.arff";
		
		String main_folder = "results/blogs_wiki_BOW";
		File data = new File(main_folder);
		String folder = null;
		data.mkdir();
		
		/* Preprocessing using Alphabetic Tokenizer*/
		Instances [] alpha_ret = PreProcessing.preAlphaTestProcess(input_arff, test_arff);
		Instances pre_alpha_data = alpha_ret[0]; 
		Instances pre_alpha_test_data = alpha_ret[1];		
		folder = main_folder + "/pre_alpha";
		runTests(pre_alpha_data, pre_alpha_test_data, folder);

	}

}
