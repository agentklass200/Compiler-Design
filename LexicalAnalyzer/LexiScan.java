package LexicalAnalyzer;

import java.io.*;
import java.util.*;

public class LexiScan {
	int posNo, lineNo;
	char current;
	char[] codeStream;
	
	public LexiScan(String fileName) throws IOException{
		
		InputStream file = null;
		BufferedReader bReader = null;
		
		ArrayList<Integer> stream = new ArrayList<Integer>();
		
		try{
			file = new FileInputStream(fileName);
			bReader = new BufferedReader(new InputStreamReader(file));
			int val = 0;
			posNo = 1;
			lineNo = 1;
			while((val = bReader.read()) != -1){
				stream.add(val);
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
					posNo = 0;
				}
				else{
					if(val != 13){
						symb = "["+ current +", "+ lineNo +":"+ posNo +"]";
						System.out.print(symb);
					}
				}
				posNo++;
				
			}
			System.out.println();
			for(int i = 0; i < stream.size(); i++){
				
				System.out.print((char)stream.get(i).intValue());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			file.close();
			bReader.close();
		}
	}
}
