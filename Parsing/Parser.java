package Parsing;

import java.io.IOException;
import java.util.ArrayList;

import LexicalAnalyzer.*;

public class Parser {
	private LexiScan scanner;
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private ArrayList<TokenID> idList = new ArrayList<TokenID>();
	
	
	public Parser(String fileName) throws IOException{
		scanner = new LexiScan(fileName);
		tokens.add(new Token("filler", "", "filler"));
		while(scanner.stream.get(scanner.getTracker()) != -1 && scanner.isError() == false){
			scanner.getToken(this);
			scanner.setTracker(scanner.getLookahead());;
			scanner.tokenBuilder.clear();
		}
		
		if(scanner.isError() == false){
			tokens.remove(0);
			System.out.println("Sample Program: Source Code");
			System.out.println("========================");
			for(int i = 0; i < scanner.stream.size() -1; i++){
				System.out.print((char)scanner.stream.get(i).intValue());
			}
			System.out.println();
			System.out.println();
			System.out.println("List of Identifiers");
			System.out.println("========================");
			for(int i = 0; i < idList.size(); i++){
				System.out.print(idList.get(i).formalString());
				
			}
			System.out.println();
			System.out.println("List of Tokens");
			System.out.println("========================");
			for(int i = 0; i < tokens.size(); i++){
				System.out.print(tokens.get(i));
				if(i%9 == 0 && i != 0){
					System.out.println();
				}
			}
		}
	}


	public ArrayList<Token> getTokens() {
		return tokens;
	}


	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}


	public ArrayList<TokenID> getIdList() {
		return idList;
	}


	public void setIdList(ArrayList<TokenID> idList) {
		this.idList = idList;
	}
	
	
}
