import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class ReadCSV {

	public static void parseCSV(String input_file, String output_file) throws IOException{		
		CSVReader reader = new CSVReader(new FileReader(input_file));
		CSVWriter writer = new CSVWriter(new FileWriter(output_file), ',');		
		String [] nextLine, writeLine;	
		reader.readNext();
		
		String line = null;
		String id = null;		
		String request = null;
		String score = null;
		writeLine = new String [2];
		while ((nextLine = reader.readNext()) != null) {	
			
			request = nextLine[0];
			score = nextLine[1];
									
//			request = request.replaceAll("\"", "&quote;");
//			request = request.replaceAll("\'", "&quote;");
//			request = request.replaceAll(",", "");
//			request = request.replaceAll("\n", "");			
//			request = "\'" + request + "\'";
			writeLine[0] = request;
			writeLine[1] = score;
						
			writer.writeNext(writeLine);
			
		}
		reader.close();
	}
	
	public static void usingLoader(String input_file, String output_file) throws IOException{
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(input_file));
//		loader.setFieldSeparator(",");
//		loader.setStringAttributes(input_file);
		Instances data = loader.getDataSet();
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(output_file));
		saver.setDestination(new File(output_file));
		saver.writeBatch();
		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String input_file = "/home/shagun/output.csv";
		String output_file = "/home/shagun/alt_output.csv";
		String parse_input_file = "/home/shagun/alt_output.csv";
		String parse_output_file = "/home/shagun/final_arff.arff";
//		parseCSV(input_file, output_file);
		usingLoader(input_file, parse_output_file);

	}

}
