package edu.utexas.cs.cs378;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

// Useful article about getting 3 billion items in Java Map with 16 GB RAM
// http://kotek.net/blog/3G_map

public class Reducer {

	private static BufferedReader br;

	public static  Map<String, Long> reduceFromFile(String outputTempFile) {
		
		
		String line;
		Map<String, Long> results = new HashMap<String, Long>(5500000);
//		TreeMap<String, Long> results = new TreeMap<String, Long>();
		
		FileInputStream fin;
		try {
			fin = new FileInputStream(outputTempFile);
			BufferedInputStream bis = new BufferedInputStream(fin);
			br = new BufferedReader(new InputStreamReader(bis));
			
			Long lineCounter = 0l;
			
			// Start reading the file line by line.
			while ((line = br.readLine()) != null) {
				lineCounter += 1;
				
				String[] data= line.split(",");
				
				long count = Long.parseLong(data[1]); 
				
				if(results.containsKey(data[0])) {
					results.put(data[0], count + results.get(data[0]));
					
			    } else{
			    	results.put(data[0], count);
			    }
				
				// We print every 1m lines
			if (lineCounter % 1000000 == 0) 
		   // Print out some results to keep track of the running process.
		   System.out.println(lineCounter + " lines processed! Count of \"the\" word is: "+ results.get("the") +" dictionary size is: " +results.size());
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO: We can do the sorting in a separate process if we want. 
		// The following can cause be larger than heap size. 
		
//		// LinkedHashMap preserve the ordering of elements in which they are inserted
//		LinkedHashMap<String, Long> resultsSorted = new LinkedHashMap<>();
//		
//		// Let us sort the final result and then print it out.
//		results.entrySet().stream().sorted(Map.Entry.comparingByValue())
//				.forEachOrdered(x -> resultsSorted.put(x.getKey(), x.getValue()));
//
//		System.out.println("Sorted Results : " + resultsSorted);

		
		
		return results;

	}

}
