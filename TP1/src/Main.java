import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static ArrayList<String> parse(String path) throws FileNotFoundException{
		ArrayList<String> chrs = new ArrayList<String>();
		String currentChr = "";
		try {
		File fasta = new File(path);
		Scanner in = new Scanner(fasta);
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.isEmpty()) {
				break;
			}
			if(line.charAt(0) == '>') {
				if(!chrs.isEmpty()) {
					chrs.add(currentChr);
					currentChr = "";
				}
			}
			else {
				currentChr += line;
			}
		}
		chrs.add(currentChr);
		in.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found dummy, try again");
		}
		
		return chrs;
 	}
	
	
	public static void main(String args[]) throws FileNotFoundException{
		//args: fasta location, assumed number of genes, no. training iterations, output name
		ArrayList<String> chromos = parse(args[0]);
		
		String chromo = chromos.get(0);
		int iter = Integer.parseInt(args[2]);
		
		Model model = new Model(chromo, Integer.parseInt(args[1]));
		
		int[] segments;
		do {
			segments = model.segmentation(chromo);
			model.apprentissage(segments, chromo);
			iter--;
		}
		while(iter > 0);
		segments = model.segmentation(chromo); //final segmentation
		ArrayList<int[]> seqs = new ArrayList<>();
		
		//finds all the segments longer than 50 nucs
		for(int i = 0; i<segments.length; i++) {
			if(segments[i] == 1) {
				for(int j = i; j<segments.length;  j++) {
					if(segments[j] == 0) {
						if(i-j >= 50) {
							int[] seq = {i, j, i-j, 0}; // begin, end, length, gcContent
							seqs.add(seq);
						}
						i = j;
						break;
					}
				}
			}
		}
		//calculate gc of each selected segment
		for(int x = 0; x < seqs.size(); x++) {
			int[] bounds = seqs.get(x);
			int gcCount = 0;
			for(int y = bounds[0]; y < bounds[1]; y++) {
				if(chromo.charAt(y) == 'C' || chromo.charAt(y) == 'G') {
					gcCount++;
				}
			}
			bounds[3] = gcCount / bounds[2] * 1000; //gc score
			seqs.set(x, bounds);
		}
		//creates output file
	    try {
	        File bed = new File(args[3]);
	        if (bed.createNewFile()) {
	          System.out.println("File created: " + bed.getName());
	        } else {
	          System.out.println("File already exists.");
	        }
	      } catch (IOException e) {
	        System.out.println("Couldn't create the output file");
	        e.printStackTrace();
	      }
	    //writes to output file
	    try {
	    	FileWriter output = new FileWriter(args[3]);
	    	for(int i = 0; i < seqs.size(); i++) {
	    		int[] gene = seqs.get(i);
	    		output.write("chr1\t" + String.valueOf(gene[0]+1) + "\t" + String.valueOf(gene[1]+1) + "\t" + "gene"+String.valueOf(i) + "\t" + String.valueOf(gene[3]));
	    	}
	    	output.close();
	    }
	    catch(Exception e) {
	        System.out.println("Couldn't write to output file");
	        e.printStackTrace();
	    }
		
	}
}
