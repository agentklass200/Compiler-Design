import java.io.IOException;

import LexicalAnalyzer.LexiScan;


// Main Class
public class Compiler {
	public static void main(String[] args) throws IOException {
		String fileName = "src/sample.txt";
		LexiScan read = new LexiScan(fileName);		
	}
}
