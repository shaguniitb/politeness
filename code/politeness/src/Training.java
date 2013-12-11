
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
//import weka.core.converters.TextDirectoryLoader;

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
	
	public static int checkInList(List<String> list, String request){
		request = request.toLowerCase();
		int count = 0;
		for (String listItem: list){
			listItem = listItem.toLowerCase();
			if (request.contains(listItem)){
				count++;
			}
		}
		return count;
	}
	
	public static List <String> lexiconList(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;		
		List<String> outputList = new ArrayList<String>();
		while ((line = reader.readLine()) != null){
			line = line.trim();
			outputList.add(line);
		}
		reader.close();		
		return outputList;
	}
	
	public static void createScoreMaps(String input_file, ArrayList <scoreMap> scoreMaps) throws IOException{
		List<String> gratitudeList = Arrays.asList("appreciate", "thankful", "grateful", "recognize", "indebted");
		List<String> deferenceList = Arrays.asList("Nice work", "respect", "polite");
		List<String> greetingList = Arrays.asList("Hey", "Hi", "Hello", "take care", "tc", "bye", "Good morning", "Good afternoon", "Good evening", "Good night", "gn", "Dear", "howdy", "ciao", "what's up", "wassup", "yo", "whassup", "welcome", "hail");
		List<String> positiveList = lexiconList("data/positive-words.txt");
		List<String> negativeList = lexiconList("data/negative-words.txt");
		
		List<String> apologizingList = Arrays.asList("sorry", "pardon", "regret", "apologize", "ashamed", "regretful", "penitent");
		
		BufferedReader reader = new BufferedReader(new FileReader(input_file));
		String line = null;
		reader.readLine();
		while ((line = reader.readLine()) != null){
			System.out.println(line);
			String [] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//			String community = parts[0];
			String request_id = parts[0];
			String request = parts[1];
			String norm_score_string = parts[2];
			Float norm_score = Float.parseFloat(norm_score_string);
//			String clean_request = request.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();	
			scoreMap newScore = new scoreMap(request_id, request, norm_score);			
			
			newScore.numGratitude = checkInList(gratitudeList, request); 			
			newScore.numDeference = checkInList(deferenceList, request);				
			newScore.numGreeting = checkInList(greetingList, request);	
			newScore.inPosLexicon = checkInList(positiveList, request);
			newScore.inNegLexicon = checkInList(negativeList, request);
			
			newScore.numApologize = checkInList(apologizingList, request);
			newScore.pleaseCount = checkInList(Arrays.asList("please"), request);
			newScore.pleaseStartCount = pleaseStartCountCheck(request);
			newScore.numIndirect = checkInList(Arrays.asList("by the way", "btw"), request);
			
			newScore.numDirectQuestion = directQuestionCheck(request);
			newScore.numDirectStart = directStartCheck(request);
			newScore.numCounterFactual = counterFactualCheck(request);
			newScore.numIndicative = indicativeCheck(request);
			
			scoreMaps.add(newScore);
		}		
		reader.close();		
	}
	
	public static int counterFactualCheck(String request){
		String[] lines = request.split("\\r?\\n");
		int count = 0;
		for (String line: lines){
			line = line.trim();
			line = line.toLowerCase();
			if (!line.startsWith("could") || line.startsWith("would")){
				count++;
			}
		}
		return count;
	}
	
	public static int indicativeCheck(String request){
		String[] lines = request.split("\\r?\\n");
		int count = 0;
		for (String line: lines){
			line = line.trim();
			line = line.toLowerCase();
			if (!line.startsWith("can") || line.startsWith("will")){
				count++;
			}
		}
		return count;
	}
	

	public static int pleaseStartCountCheck(String request){
		String[] lines = request.split("\\r?\\n");
		int count = 0;
		for (String line: lines){
			line = line.trim();
			line = line.toLowerCase();
			if (line.startsWith("please")){
				count++;
			}
		}
		return count;
	}
	
	public static int directQuestionCheck(String request){
		String[] lines = request.split("\\r?\\n");
		int count = 0;
		for (String line: lines){
			line = line.trim();
			line = line.toLowerCase();
			if (line.startsWith("wh") && line.endsWith("?")){
				count++;
			}
		}
		return count;
	}
	
	public static int directStartCheck(String request){
		String[] lines = request.split("\\r?\\n");
		int count = 0;
		for (String line: lines){
			line = line.trim();
			line = line.toLowerCase();
			if (!line.startsWith("wh") && line.endsWith("?")){
				count++;
			}
		}
		return count;
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
	
	public static void writeNext(BufferedWriter wr, String string) throws IOException{
		wr.write(",");
		wr.write(string);
	}
	
	public static void writeAdditionalFeatures(BufferedWriter wr, scoreMap s) throws IOException{
		writeNext(wr, String.valueOf(s.numGratitude));
		writeNext(wr, String.valueOf(s.numDeference));
		writeNext(wr, String.valueOf(s.numGreeting));
		writeNext(wr, String.valueOf(s.inPosLexicon));
		writeNext(wr, String.valueOf(s.inNegLexicon));
		
		writeNext(wr, String.valueOf(s.numApologize));
		writeNext(wr, String.valueOf(s.pleaseCount));
		writeNext(wr, String.valueOf(s.pleaseStartCount));
		writeNext(wr, String.valueOf(s.numIndirect));
		
		writeNext(wr, String.valueOf(s.numDirectQuestion));
		writeNext(wr, String.valueOf(s.numDirectStart));
		writeNext(wr, String.valueOf(s.numCounterFactual));
		writeNext(wr, String.valueOf(s.numIndicative));
	}
	
	public static void InitializeArff(BufferedWriter wr) throws IOException{
		wr.write("@relation politeness\n\n");
		
		wr.write("@attribute request string\n");
		
		wr.write("@attribute numGratitude numeric\n");
		wr.write("@attribute numDeference numeric\n");
		wr.write("@attribute numGreeting numeric\n");		
		wr.write("@attribute inPosLexicon numeric\n");
		wr.write("@attribute inNegLexicon numeric\n");
		
		wr.write("@attribute numApologize numeric\n");
		wr.write("@attribute pleaseCount numeric\n");
		wr.write("@attribute pleaseStartCount numeric\n");
		wr.write("@attribute numIndirect numeric\n");
		
		wr.write("@attribute numDirectQuestion numeric\n");
		wr.write("@attribute numDirectStart numeric\n");
		wr.write("@attribute numCounterFactual numeric\n");		
		wr.write("@attribute numIndicative numeric\n");
		
		wr.write("@attribute class {polite,impolite}\n");
		wr.write("\n");
		
		wr.write("@data\n");
		
	}

	public static void writeModelFile(HashMap<String, ArrayList<scoreMap>> map, String output_file) throws IOException{
		File data = new File("data");
		data.mkdir();		
		String request;	
		FileWriter writer = new FileWriter(output_file);
		BufferedWriter wr = new BufferedWriter(writer);	
		InitializeArff(wr);
		for (String label: map.keySet()){
			for (scoreMap s: map.get(label)){
				request = s.request;
				request = request.replaceAll("\"", "\\\\\"");
				request = request.replaceAll("\'", "\\\\\'");
				request = "\'" + request + "\'";
				wr.write(request);
				writeAdditionalFeatures(wr, s);
				wr.write(",");
				wr.write(label);
				wr.write("\n");
			}
						
		}
		wr.close();
		writer.close();
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String input_file = "/home/shagun/output.csv";	
		String output_file = "/home/shagun/stack_arff.arff";
		HashMap<String, ArrayList<scoreMap>> h = new HashMap<String, ArrayList<scoreMap>>();	
		buildModel(h, input_file);
		writeModelFile(h, output_file);		
	}

}
