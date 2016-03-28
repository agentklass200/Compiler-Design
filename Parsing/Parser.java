package Parsing;

import java.io.IOException;
import java.util.HashMap;

import LexicalAnalyzer.*;

public class Parser {
	private LexiScan scanner;
	private HashMap<String, Token> idMaps = new HashMap<String, Token>();
	
	private Token newToken = null;
	private Token prevToken = null;
	
	
	public Parser(String fileName) throws IOException{
		scanner = new LexiScan(fileName);
		
		System.out.println("Sample Program: Source Code");
		System.out.println("========================");
		for(int i = 0; i < scanner.stream.size() -1; i++){
			System.out.print((char)scanner.stream.get(i).intValue());
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("List of Tokens");
		System.out.println("========================");
		
                DisplayTokens();
		
		System.out.println();
		System.out.println(idMaps.toString());
	}
	



	public HashMap<String, Token> getIdMaps() {
		return idMaps;
	}


	public void setIdMaps(HashMap<String, Token> idMaps) {
		this.idMaps = idMaps;
	}



	public Token getNewToken() {
		return newToken;
	}

	public Token getPrevToken() {
		return prevToken;
	}


        public void DisplayTokens() {
            boolean isPLined = false;
		while(scanner.stream.get(scanner.getTracker()) != -1){
			if(newToken != null){
				if(!newToken.isIgnored()){
					prevToken = newToken;
				}
			}
			newToken = scanner.getToken(this);
			scanner.setTracker(scanner.getLookahead());
			scanner.tokenBuilder.clear();
			
			//Print
			if(!newToken.isIgnored()){
				System.out.print(newToken);
				isPLined = false;
			}
			else{
				if(newToken.toString() == TokenWhitespace.RETURN_WHITESPACE && !isPLined){
					System.out.println();
					isPLined = true;
				}
			}
		}
        }


	
	
}
