package refugee;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.csv4j.CSVReader;
public class Stats {

	public static void main(String[] args) {
		HashMap<String, Double> actualpop = new HashMap<String, Double>();
		String actual_file = "parameter_sweeps/actual.csv";
		readInActual(actual_file, actualpop);
		
		String compareFile = "parameter_sweeps/1/output_pass";
		compareToFile(compareFile, actualpop);
		
	}
	
	private static void readInActual(String actual_file, HashMap<String, Double> actualpop){
		try {
			// buffer reader for age distribution data
			CSVReader csvReader = new CSVReader(new FileReader(new File(actual_file)));
			List<String> line = csvReader.readLine();
			while (!line.isEmpty()) {
				// read in the county ids
				String name = line.get(0);
				// relevant info is from 5 - 21
				double percOfPop = NumberFormat.getNumberInstance(java.util.Locale.US).parse(line.get(2)).doubleValue();
				// System.out.println("sum = " + sum);
				// System.out.println();

				// now add it to the hashmap
				actualpop.put(name,  percOfPop);
				line = csvReader.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static void compareToFile(String filename, HashMap<String, Double> actualpop){
		try {
			String filenameout = filename + "_compare.csv";
			File outfile = new File(filenameout);
			CSVReader csvReader = new CSVReader(new FileReader(new File(filename + ".csv")));
			FileWriter fw = new FileWriter(outfile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			List<String> names = csvReader.readLine(); 
			List<String> values = csvReader.readLine();
			System.out.println(values);
			List<Double> newvalues;
			
			while (!values.isEmpty()){
			newvalues = new ArrayList<Double>(values.size());
			//System.out.println("Size: " + newvalues.size());
			int trialNumber = NumberFormat.getNumberInstance(java.util.Locale.US).parse(values.get(0)).intValue();
			//double sum = 0;
			for (int i = 1; i < values.size(); i++){
				double value = NumberFormat.getNumberInstance(java.util.Locale.US).parse(values.get(i)).doubleValue();
				//sum += value;
				newvalues.add(i-1, value);
			}
			double min = Collections.min(newvalues);
			double max = Collections.max(newvalues);
			double difference = 0;
			for (int i = 0; i < newvalues.size(); i++){
				double percent = (newvalues.get(i) - min)/(max - min);
				double actualPercent = actualpop.get(names.get(i-1));
				difference += Math.abs(percent - actualPercent);
			}
			writer.println(trialNumber + "," + difference);
			values = csvReader.readLine();
			}
			writer.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	}

