import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
//			String clean_request = request.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();	
			scoreMap newScore = new scoreMap(request_id, request, norm_score);
			scoreMaps.add(newScore);
		}		
		reader.close();		
	}
	
	public static void sortClassifyScoreMaps(HashMap<String, ArrayList<scoreMap>> map, ArrayList <scoreMap> scoreMaps){
		Collections.sort(scoreMaps, new scoreMapScoreComparator());
		int totalScores = scoreMaps.size();		
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
	
	public static void buildModel(HashMap<String, ArrayList<scoreMap>> map, String input_file) throws IOException{		
		ArrayList <scoreMap> scoreMaps = new ArrayList<scoreMap> (); 		
		createScoreMaps(input_file, scoreMaps);
		sortClassifyScoreMaps(map, scoreMaps);

//		if (map.containsKey("polite")){
//			System.out.println(map.get("polite").size());
//		}
//		if (map.containsKey("impolite")){
//			System.out.println(map.get("impolite").size());
//		}
	}

	public static void writeModelFile(HashMap<String, ArrayList<scoreMap>> map) throws IOException{
		File data = new File("data");
		data.mkdir();
		for (String folder: map.keySet()){
			File dir = new File("data/" + folder);
			dir.mkdir();
			for (scoreMap s: map.get(folder)){
				String fileName = dir + "/" + s.getId() + ".csv";
				FileWriter writer = new FileWriter(fileName);
				BufferedWriter wr = new BufferedWriter(writer);
				wr.write(s.request);
				wr.close();
				writer.close();
			}
		}		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String input_file = "/home/shagun/politeness/politeness_corpus/wikipedia.annotated.csv";		
		HashMap<String, ArrayList<scoreMap>> h = new HashMap<String, ArrayList<scoreMap>>();	
		buildModel(h, input_file);
		writeModelFile(h);
	}

}
