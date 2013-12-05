import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.lazy.IBk;
import weka.classifiers.functions.SMO;
import weka.filters.supervised.attribute.AttributeSelection;

public class CrossValidating {

	public static void EvaluatingNaiveBayes(Instances data, String folder) throws Exception{
		Evaluation eval = new Evaluation(data);
		NaiveBayes nb = new NaiveBayes();
		eval.crossValidateModel(nb, data, 5, new Random());
		writeOut(folder + "/naive_bayes", eval);
	}
	
	public static void EvaluatingNaiveBayesMultiNomial(Instances data, String folder) throws Exception{
		Evaluation eval = new Evaluation(data);
		NaiveBayesMultinomial nbm = new NaiveBayesMultinomial();
		eval.crossValidateModel(nbm, data, 5, new Random());
		writeOut(folder + "/naive_bayes_multinomial", eval);
	}
	
	public static void EvaluatingJ48(Instances data, String folder) throws Exception{
		Evaluation eval = new Evaluation(data);
		J48 j = new J48();
		eval.crossValidateModel(j, data, 5, new Random());
		writeOut(folder + "/J48", eval);
	}
	
	public static void EvaluatingRandomForest(Instances data, String folder, int numTrees) throws Exception{
		Evaluation eval = new Evaluation(data);
		RandomForest rf = new RandomForest();
		rf.setNumTrees(numTrees);
		eval.crossValidateModel(rf, data, 5, new Random());
		String filename = "random_forest_" + String.valueOf(numTrees) + "_trees";
		writeOut(folder + "/" + filename, eval);
	}
	
	public static void EvaluatingIbk(Instances data, String folder, int KNN, LinearNNSearch search) throws Exception{
		Evaluation eval = new Evaluation(data);
		IBk ibk = new IBk();
		ibk.setKNN(KNN);
		ibk.setNearestNeighbourSearchAlgorithm(search);
		eval.crossValidateModel(ibk, data, 5, new Random());
		DistanceFunction distanceFunction = search.getDistanceFunction();		
		String distance= distanceFunction.getClass().getSimpleName();
		String filename = "ibk_" + String.valueOf(KNN) + "_KNN_" + distance;
		writeOut(folder + "/" + filename, eval);
	}
	
	public static void EvaluatingSMO(Instances data, String folder) throws Exception{
		Evaluation eval = new Evaluation(data);
		SMO smo = new SMO();		
		eval.crossValidateModel(smo, data, 5, new Random());
		writeOut(folder + "/SMO", eval);		
	}
	
	public static void writeOut(String className, Evaluation eval) throws Exception{
		String output_file = className + ".txt";
		String summary = eval.toSummaryString();
		WriteOutput(output_file, summary, false);		
		String detailed_accuracy = eval.toClassDetailsString();
		WriteOutput(output_file, detailed_accuracy, true);	
		String matrixString = eval.toMatrixString();
		WriteOutput(output_file, matrixString, true);
		Double errorRate = (double) Math.round(eval.errorRate()*100)/100;		
		System.out.println(output_file + "\t" + errorRate.toString());		
	}
	
	public static void WriteOutput(String fileName, String output, boolean append) throws IOException{
		FileWriter writer = new FileWriter(fileName, append);		
		BufferedWriter wr = new BufferedWriter(writer);
		if (append==true) wr.write('\n');
		wr.write(output);
		wr.close();
		writer.close();
	}
	
	public static void CreateFolder(String folder){
		File data = new File(folder);
		data.mkdir();
	}
	
	public static void runCrossValidations(Instances data, String folder) throws Exception{
		CreateFolder(folder);

		EvaluatingNaiveBayes(data, folder);
		EvaluatingNaiveBayesMultiNomial(data, folder);
		EvaluatingJ48(data, folder);
		
		EvaluatingRandomForest(data, folder, 10);
		EvaluatingRandomForest(data, folder, 100);
		
		LinearNNSearch euc_search = new LinearNNSearch();		
		euc_search.setDistanceFunction(new EuclideanDistance());
		EvaluatingIbk(data, folder, 1, euc_search);
		EvaluatingIbk(data, folder, 10, euc_search);
		
		LinearNNSearch manhattan_search = new LinearNNSearch();		
		manhattan_search.setDistanceFunction(new ManhattanDistance());
		EvaluatingIbk(data, folder, 1, manhattan_search);
		EvaluatingIbk(data, folder, 10, manhattan_search);
		
		EvaluatingSMO(data, folder);
		
	}
	
	public static void main(String[] args) throws Exception {
		String input_arff = "../../weka-3-6-10/wiki_train.arff";
		String folder;
		
		/* Preprocessing using Alphabetic Tokenizer*/
		Instances pre_alpha_data = PreProcessing.preAlphaProcess(input_arff);
		folder = "results/pre_alpha";
//		runCrossValidations(pre_alpha_data, folder);
		
		/* Preprocessing using Word Tokenizer*/
		Instances pre_word_data = PreProcessing.preWordProcess(input_arff);
		folder = "results/pre_word";
//		runCrossValidations(pre_word_data, folder);
		
		/* Preprocessing using Alphabetic Tokenizer and Attribute Selection */
		Instances pre_alpha_attr_select = PreProcessing.AttributeSelecting(pre_alpha_data);
		folder = "results/pre_alpha_with_attribute_selection";
//		runCrossValidations(pre_alpha_attr_select, folder);
		
		/* Preprocessing using Word Tokenizer and Attribute Selection */
		Instances pre_word_attr_select = PreProcessing.AttributeSelecting(pre_word_data);
		folder = "results/pre_word_with_attribute_selection";
//		runCrossValidations(pre_word_attr_select, folder);
	}

}
