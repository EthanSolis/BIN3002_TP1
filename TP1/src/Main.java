import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	
	public static ArrayList<String> parse(String path) throws FileNotFoundException{
		ArrayList<String> chrs = new ArrayList<String>();
		String currentChr = "";
		try {
		File fasta = new File(path);
		Scanner in = new Scanner(fasta);
		
		while(in.hasNextLine()) {
			String line = in.nextLine();
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
		in.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found dummy, try again");
		}
		
		return chrs;
 	}
}
