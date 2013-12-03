import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	
	public static ArrayList <scoreMap> getScoreMapList(HashMap<String, ArrayList<scoreMap>> map, String key){
		if (map.containsKey(key)){
			return map.get(key);
		}
		else {
			ArrayList <scoreMap> sm = new ArrayList <scoreMap> ();
			map.put(key, sm);
			return sm;
		}
	}
	
	public static void createScoreMaps(String input_file, ArrayList <scoreMap> scoreMaps) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(input_file));
		String line = null;
		reader.readLine();
		while ((line = reader.readLine()) != null){
			String [] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//			String community = parts[0];
			String request_id = parts[1];
			String request = parts[2];
			String norm_score_string = parts[13];
			Float norm_score = Float.parseFloat(norm_score_string);
			String clean_request = request.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();	
			scoreMap newScore = new scoreMap(request_id, clean_request, norm_score);
			scoreMaps.add(newScore);
		}		
		reader.close();		
	}
	
	public static void buildModel(HashMap<String, ArrayList<scoreMap>> map, String input_file) throws IOException{		
		ArrayList <scoreMap> scoreMaps = new ArrayList<scoreMap> (); 		
		createScoreMaps(input_file, scoreMaps);
		Collections.sort(scoreMaps, new scoreMapScoreComparator());
		int totalScores = scoreMaps.size();
		System.out.println(totalScores);
		int count = 0;
		ArrayList <scoreMap> sm = new ArrayList <scoreMap> ();		
		for (scoreMap s: scoreMaps){
			count++;					
			if (count <= totalScores/4 ) { //it's an impolite request				
				sm = getScoreMapList(map, "impolite");				
				sm.add(s);
			}
			else if ((count > totalScores*3/4) && (count < totalScores)) { //it's a polite request
				sm = getScoreMapList(map, "polite");
				sm.add(s);
			}			
		}
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
		
		HashMap<String, ArrayList<scoreMap>> h = new HashMap<String, ArrayList<scoreMap>>();		
	
		buildModel(h, input_file);
//		writeModelFile(h, model_file);
	}

}
