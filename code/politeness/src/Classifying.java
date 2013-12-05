import weka.core.Instances;
import weka.classifiers.bayes.NaiveBayes;

public class Classifying {
	
	public static void usingNaiveBayes(Instances data) throws Exception{
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(data);		
	}

	public static void main(String[] args) throws Exception{
		String input_arff = "../../weka-3-6-10/wiki_train.arff";
		Instances data = PreProcessing.preAlphaProcess(input_arff);
		usingNaiveBayes(data);
	}
}
