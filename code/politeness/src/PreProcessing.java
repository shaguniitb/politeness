import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.core.tokenizers.WordTokenizer;

public class PreProcessing {
	
	public static Instances preAlphaProcess(String input_arff) throws Exception{		
		DataSource source = new DataSource(input_arff);		
		Instances data = source.getDataSet();		
		if (data.classIndex() == -1){
			data.setClassIndex(data.numAttributes() - 1);
		}		
		data.setClassIndex(data.numAttributes() - 2);
		StringToWordVector swv = new StringToWordVector();
		AlphabeticTokenizer at = new AlphabeticTokenizer();
		swv.setTFTransform(true);
		swv.setIDFTransform(true);
		swv.setMinTermFreq(10);
		swv.setOutputWordCounts(true);
		swv.setTokenizer(at);
		swv.setInputFormat(data);
		swv.setAttributeNamePrefix("pre_");
		Instances newData = Filter.useFilter(data, swv);
		return newData;
	}
	
	public static Instances preWordProcess(String input_arff) throws Exception{		
		DataSource source = new DataSource(input_arff);		
		Instances data = source.getDataSet();		
		if (data.classIndex() == -1){
			data.setClassIndex(data.numAttributes() - 1);
		}
		data.setClassIndex(data.numAttributes() - 2);
		StringToWordVector swv = new StringToWordVector();		
		WordTokenizer wt = new WordTokenizer();
		swv.setTFTransform(true);
		swv.setIDFTransform(true);
		swv.setMinTermFreq(10);
		swv.setOutputWordCounts(true);
		swv.setTokenizer(wt);
		swv.setInputFormat(data);
		swv.setAttributeNamePrefix("pre_");
		Instances newData = Filter.useFilter(data, swv);
		return newData;
	}
	
	public static Instances AttributeSelecting(Instances data) throws Exception{		
		AttributeSelection as = new AttributeSelection();
		InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
		Ranker ranker = new Ranker();
		ranker.setThreshold(0);
		as.setEvaluator(evaluator);
		as.setSearch(ranker);
		as.setInputFormat(data);
		Instances newData = Filter.useFilter(data, as);
		return newData;
	}
	
	
}
