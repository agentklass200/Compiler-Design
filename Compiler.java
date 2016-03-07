import java.io.IOException;

import Parsing.Parser;


// Main Class
public class Compiler {
	public static void main(String[] args) throws IOException {
		String fileName = "src/sample.txt";
		Parser read = new Parser(fileName);		
	}
}
