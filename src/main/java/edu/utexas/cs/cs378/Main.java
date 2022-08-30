package edu.utexas.cs.cs378;

import java.util.Map;

public class Main {

	/**
	 * A main method to run examples.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		// pass the file name as the first argument. 
		// We can also accept a .bz2 file
		
		// This line is just for Kia :) 
		// You should pass the file name and path as first argument of this main method. 
		String file = "/Users/kiat/Downloads/WikipediaPagesOneDocPerLine.txt.bz2";
		
		if(args.length>0)
			file=args[0];
		
		int batchSize = 4000;
		
		if(args.length>1)
			batchSize = Integer.parseInt(args[1]);
		
		String outputTempFile  = "temp.txt";
		
		
		MapToDataFile.mapIt(file, batchSize, outputTempFile);
		
		System.out.println("Now, we start reading the temp data and reducing it.");
		
		Map<String, Long> results = Reducer.reduceFromFile(outputTempFile);	
		
		MapToDataFile.appendToTempFile(results, "results.txt");
		

	}
	
}