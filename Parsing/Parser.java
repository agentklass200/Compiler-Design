package Parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import LexicalAnalyzer.*;

public class Parser {
	private LexiScan scanner;
//	private ArrayList<Token> tokens = new ArrayList<Token>();
	private HashMap<String, Token> idMaps = new HashMap<String, Token>();
	
	private Token newToken = null;
	private Token prevToken = null;
	
	
	public Parser(String fileName) throws IOException{
		boolean ignoreToken = false;
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
		
		
		while(scanner.stream.get(scanner.getTracker()) != -1 && scanner.isError() == false){
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
			}
			else{
				if(newToken.toString() == TokenWhitespace.RETURN_WHITESPACE){
					System.out.println();
				}
			}
		}
		
//		if(scanner.isError() == false){
//			tokens.remove(0);
//			System.out.println("Sample Program: Source Code");
//			System.out.println("========================");
//			for(int i = 0; i < scanner.stream.size() -1; i++){
//				System.out.print((char)scanner.stream.get(i).intValue());
//			}
//			System.out.println();
//			System.out.println();
//			System.out.println("List of Identifiers");
//			System.out.println("========================");
//			for(int i = 0; i < idList.size(); i++){
//				System.out.print(idList.get(i).formalString());
//				
//			}
//			System.out.println();
//			System.out.println("List of Tokens");
//			System.out.println("========================");
//			for(int i = 0; i < tokens.size(); i++){
//				System.out.print(tokens.get(i));
//				if(i%9 == 0 && i != 0){
//					System.out.println();
//				}
//			}
//		}
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





	
	
}
