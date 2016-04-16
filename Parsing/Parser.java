package Parsing;

import java.io.IOException;
import java.util.HashMap;

import LexicalAnalyzer.*;
import Parsing.TableReader;

public class Parser {
	private LexiScan scanner;
	private HashMap<String, Token> idMaps = new HashMap<String, Token>();
	private String[][] actTab = TableReader.CreateActionTable("src/action.csv");
	private int[][] gotoTab = TableReader.CreateGoToTable("src/GOTO.csv");
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
		// Testing
		displayActionTable();
		System.out.println();
		displayGOTOTable();
		System.out.println();
		
		System.out.println(actTab[106][getTokenVal("StringConstant")]);
		// End Testing
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
	
	public void displayActionTable(){
		System.out.println("Action Table");
		System.out.println("========================");
		for(int i = 0; i < actTab.length; i++){
			System.out.print(i + ". [");
			for(int j = 0; j < actTab[i].length; j++){
				System.out.print("\""+ actTab[i][j] +"\",");
			}
			System.out.println("]");
		}
	}
	public void displayGOTOTable(){
		System.out.println("Go To Table");
		System.out.println("========================");
		for(int i = 0; i < gotoTab.length; i++){
			System.out.print(i + ". [");
			for(int j = 0; j < gotoTab[i].length; j++){
				System.out.print(gotoTab[i][j] +",");
			}
			System.out.println("]");
		}
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
	
	public int getVarVal(String var){
		switch(var){
			case "BPRIME":
				return 37;
			case "B":
				return 0;
			case "SCOPE":
				return 1;
			case "STMTS":
				return 2;
			case "STMT":
				return 3;
			case "DECLARATIONSTMT":
				return 4;
			case "ITERATSTMT":
				return 5;
			case "INPUTSTMT":
				return 6;
			case "OUTPUTSTMT":
				return 7;
			case "ASSIGNMENTSTMT":
				return 8;
			case "CONVERTSTMT":
				return 9;
			case "CONDITIONALSTMT":
				return 10;
			case "ELSESTMT":
				return 11;
			case "INITIALIZATION":
				return 12;
			case "INITIALIZATIONPRIME":
				return 13;
			case "WHILELOOP":
				return 14;
			case "FORLOOP":
				return 15;
			case "ASSIGNMENT":
				return 16;
			case "DATATYPE":
				return 17;
			case "EXPR":
				return 18;
			case "LOGEXPR":
				return 19;
			case "LOGOPER":
				return 20;
			case "EQEXPR":
				return 21;
			case "EQOPER":
				return 22;
			case "RELEXPR":
				return 23;
			case "RELOPER":
				return 24;
			case "TERMEXPR":
				return 25;
			case "TERMOPER":
				return 26;
			case "FACTEXPR":
				return 27;
			case "FACTOPER":
				return 28;
			case "UNARYEXPR":
				return 29;
			case "UNARYOPER":
				return 30;
			case "POSTEXPR":
				return 31;
			case "POSTOPER":
				return 32;
			case "VALUE":
				return 33;
			case "CONST":
				return 34;
			case "BOOLCONST":
				return 35;
			default:
				return -1;
				
		}
	}
	
	public int getTokenVal(String token){
		switch(token){
			case "Initiate":
				return 0;
			case "Terminate":
				return 1;
			case "Start":
				return 2;
			case "End":
				return 3;
			case "Delimiter":
				return 4;
			case "Input":
				return 5;
			case "Colon":
				return 6;
			case "Identifier":
				return 7;
			case "Assignment":
				return 8;
			case "Output":
				return 9;
			case "Convert":
				return 10;
			case "If":
				return 11;
			case "ElseIf":
				return 12;
			case "Else":
				return 13;
			case "EndIf":
				return 14;
			case "Comma":
				return 15;
			case "WhileLoop":
				return 16;
			case "ForLoop":
				return 17;
			case "ConditionDelimiter":
				return 18;
			case "String":
				return 19;
			case "Character":
				return 20;
			case "Integer":
				return 21;
			case "Boolean":
				return 22;
			case "Float":
				return 23;
			case "StringConstant":
				return 24;
			case "CharConstant":
				return 25;
			case "FloatConstant":
				return 26;
			case "IntegerConstant":
				return 27;
			case "True":
				return 28;
			case "False":
				return 29;
			case "OpenParen":
				return 30;
			case "CloseParen":
				return 31;
			case "And":
				return 32;
			case "Or":
				return 33;
			case "Equal":
				return 34;
			case "NotEqual":
				return 35;
			case "LessThan":
				return 36;
			case "LessThanOrEqual":
				return 38;
			case "GreaterThan":
				return 39;
			case "GreaterThanOrEqual":
				return 40;
			case "Plus":
				return 41;
			case "Minus":
				return 42;
			case "Concat":
				return 43;
			case "Times":
				return 44;
			case "Divide":
				return 45;
			case "Modulo":
				return 46;
			case "Negative":
				return 47;
			case "Not":
				return 48;
			case "Increment":
				return 49;
			case "Decrement":
				return 50;
			case "EOF":
				return 51;
			default:
				return -1;
			
		}
	}
       


	
	
}
