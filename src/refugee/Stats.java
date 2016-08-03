package refugee;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.csv4j.CSVReader;
public class Stats {

	public static void main(String[] args) {
		HashMap<String, Double> actualpop;
		String actual_file = "parameter_sweeps/actual.csv";
		readInActual(actual_file);
		
	}
	
	private static void readInActual(String actual_file){
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
}
