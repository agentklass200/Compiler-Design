import java.io.IOException;

import Parsing.Parser;


// Main Class
public class Compiler {
	public static void main(String[] args) throws IOException {
		String fileName = "src/short1.txt";
		Parser parse = new Parser(fileName);
		if(parse.recoveryMode){
			System.out.println();
			System.out.println("No More Valid Tokens or Variables to Continue!");
		}
		else{
			System.out.println();
			System.out.println("Parsing Tree");
			System.out.println("========================");
			System.out.println(parse.getParsingTreeRoot().printTree());
			System.out.println();
			System.out.println("Post Traversal");
			System.out.println("========================");
			parse.getParsingTreeRoot().printPostTraversal();
			
		}
		
		
	}
}
