import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;


public class Training {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static float calculateAverage(ArrayList<Float> list) {
		Float sum = (float) 0;
		for (Float member : list) {
			sum = sum + member;
		}
		Float count = (float) list.size();
		Float average = sum / count;
		return average;
	}
	
	public static void buildModel(HashMap<String, ArrayList<Float>> map, String input_file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(input_file));
		String line = null;
		reader.readLine();
		
		while ((line = reader.readLine()) != null){
			String [] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			String community = parts[0];
			String request = parts[2];
			String norm_score_string = parts[13];
			Float norm_score = Float.parseFloat(norm_score_string);
			String clean_request = request.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();			
			String [] terms = clean_request.split(" ");			
			for (String term: terms){				
				ArrayList<Float> term_list = new ArrayList<Float>();
				if (map.containsKey(term)){
					term_list = map.get(term);					
				}
				term_list.add(norm_score);
				map.put(term, term_list);				
			}			
		}
		reader.close();
	}

	public static void writeModelFile(HashMap<String, ArrayList<Float>> map, String model_file) throws IOException{
		FileWriter writer = new FileWriter(model_file);
		BufferedWriter wr = new BufferedWriter(writer);
		for (String key: map.keySet()){
			wr.write(key);
			ArrayList<Float> key_list = map.get(key);
			Float average = calculateAverage(key_list);
			wr.write("\t" + average.toString());			
			wr.write("\n");
		}
		wr.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String input_file = "/home/shagun/politeness/politeness_corpus/wikipedia.annotated.csv";
		String model_file = "model_file.txt";
		
		HashMap<String, ArrayList<Float>> h = new HashMap<String, ArrayList<Float>>();		
	
		buildModel(h, input_file);
		writeModelFile(h, model_file);
	}

}
