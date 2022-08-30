package edu.utexas.cs.cs378;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class MapToDataFile {
	
	
	static private Pattern pattern = Pattern.compile("[^a-zA-Z]");

	/**
	 * 
	 * @param file
	 * @param batchSize
	 * @param outputTempFile
	 */
	static void mapIt(String file, int batchSize, String outputTempFile) {

		try {
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fin);

			// Here we uncompress .bz2 file
			CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			// Initialize a bunch of variables

			StringBuilder batch = new StringBuilder("");

		

			mapToFile(batchSize, outputTempFile, br, batch);



			fin.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CompressorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param batchSize
	 * @param outputTempFile
	 * @param br
	 * @param batch
	 * @param lineCounter
	 * @throws IOException
	 */
	static void mapToFile(int batchSize, String outputTempFile, BufferedReader br, StringBuilder batch)
			throws IOException {
		String line;
		Map<String, Long> wordCountTmp = new HashMap<String, Long>(batchSize);
		
		Long lineCounter = 0l;
		// Start reading the file line by line.
		while ((line = br.readLine()) != null) {

			lineCounter += 1;

			// add the current text line to the data batch that we want to process.
			batch.append(line);

			if (lineCounter % batchSize == 0) {
				wordCountTmp = MapToDataFile.processLine(batch.toString());

				System.out.println(lineCounter + "  Pages processed!");

				// We can write the map into disk and read it back if it is too big.
				// System.out.println(lineCounter + " Pages processed! ");
				MapToDataFile.appendToTempFile(wordCountTmp, outputTempFile);

				// reset the batch to empty string and restart.
				batch = new StringBuilder("");

			}
		}
	}

	/**
	 * 
	 * @param input
	 * @return
	 */

	public static Map<String, Long> processLine(String input) {

		String[] lines = input.split("\\R");
		
		Map<String, Long> wordCount = Arrays.stream(lines).flatMap(line -> Arrays.stream(line.trim().split(" "))) // split
																													// by
																													// space
				.filter(word -> !pattern.matcher(word).find()) //
				.map(word -> word.toLowerCase().trim()) // Drop all words with special chars
														// and convert it to lower case.
				.filter(word -> !word.isEmpty()) // Drop all empty words
				.map(word -> new SimpleEntry<>(word, 1))
				.collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.counting()));

		return wordCount;

	}

	public static void appendToTempFile(Map<String, Long> data, String outputFile) {

		// new file object
		File file = new File(outputFile);
		BufferedWriter bf = null;

		try {

			// create new BufferedWriter for the output file
			// true means append
			bf = new BufferedWriter(new FileWriter(file, true));

			// iterate map entries
			for (Map.Entry<String, Long> entry : data.entrySet()) {

				// put key and value separated by a comma
				// better use a string builder.
				// better use a data serializiation library

				bf.write(entry.getKey() + "," + entry.getValue());

				// new line
				bf.newLine();
			}

			bf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				// always close the writer
				bf.close();
			} catch (Exception e) {
			}
		}
	}

}
