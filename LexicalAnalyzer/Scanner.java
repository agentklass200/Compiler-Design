package LexicalAnalyzer;

import java.io.*;

public class Scanner {
	int posNo, lineNo;
	char current;
	
	public Scanner(String fileName) throws IOException{
		
		InputStream file = null;
		BufferedReader bReader = null;
		
		try{
			file = new FileInputStream(fileName);
			bReader = new BufferedReader(new InputStreamReader(file));
			int val = 0;
			posNo = 1;
			lineNo = 1;
			while((val = bReader.read()) != -1){
				current = (char)val;
				String symb;
				if (current == " ".charAt(0)){
					symb = "[WHITESPACE, "+ lineNo +":"+ posNo +"]";
					System.out.print(symb);
				}
				else if(current == '\t'){
					symb = "[TAB, "+ lineNo +":"+ posNo +"]";
					System.out.print(symb);
				}
				else if(current == '\n'){
					System.out.println();
					lineNo++;
					posNo = 1;
				}
				else{
					if(val != 13){
						symb = "["+ current +", "+ lineNo +":"+ posNo +"]";
						System.out.print(symb);
					}
				}
				posNo++;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			file.close();
			bReader.close();
		}
	}
}
