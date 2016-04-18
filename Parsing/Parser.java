package Parsing;

import java.io.IOException;
import java.util.HashMap;
import LexicalAnalyzer.*;
import Parsing.TableReader;
import Parsing.StateProp;
import Parsing.GrammarRules;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	private LexiScan scanner;
	private HashMap<String, Token> idMaps = new HashMap<String, Token>();
	private String[][] actTab = TableReader.CreateActionTable("src/action.csv");
	private int[][] gotoTab = TableReader.CreateGoToTable("src/GOTO.csv");
	private Token newToken = null;
	private Token prevToken = null;
	private HashMap<Integer, GrammarRules> rules;
	private Stack<Token> tokenStack = new Stack<Token>();
	private Stack<Integer> stateStack = new Stack<Integer>();
	public boolean recoveryMode = false;
	
	private Token parsingTreeRoot;
	
	public Parser(String fileName) throws IOException{
		
		
        displayTokens(fileName);
        initGrammar();
        
		System.out.println();
		System.out.println(idMaps.toString());
		// Testing
		scanner = new LexiScan(fileName);
//		displayActionTable();
//		System.out.println();
//		displayGOTOTable();
		System.out.println();
		
		// End Testing
		
		// Parsing Begins!
		stateStack.push(0);
		tokenStack.push(new Token("EOF", "$", "EndOfStack"));
		System.out.println();
		System.out.println("Parsing");
		System.out.println("========================");
		boolean isEOF = false;
		
		int stepCounter = 1;
		
		
		while(!isEOF){
			// Get Token
			if(scanner.stream.get(scanner.getTracker()) == -1){
				if(newToken != null){
					if(!newToken.isIgnored() && !newToken.isError()){
						prevToken = newToken;
					}
				}
				newToken = new Token("EOF", "$", "EndOfStack");
				isEOF = true;
			}
			else{
				if(newToken != null){
					if(!newToken.isIgnored() && !newToken.isError()){
						prevToken = newToken;
					}
				}
				newToken = scanner.getToken(this);
				scanner.setTracker(scanner.getLookahead());
				scanner.tokenBuilder.clear();
			}
			if(!newToken.isIgnored()&& !newToken.isError()){
				Token lookaheadToken = newToken;
				boolean needToken = false;
				
				System.out.println("\nLookahead: "+lookaheadToken.getName());
				StateProp state = new StateProp(actTab[stateStack.peek()][getTokenVal(lookaheadToken.getName())]);
				while(!needToken){
					if(recoveryMode){
						if(state.getAction() != StateProp.ERROR){
							if(state.getAction() == StateProp.SHIFT){
								stateStack.push(state.getStateNum());
								tokenStack.push(lookaheadToken);
								System.out.println(stepCounter++ + ". Shift: " + lookaheadToken.getName());
								needToken = true;
								recoveryMode = false;
								System.out.println("=======================================");
							}
							else if(state.getAction() == StateProp.REDUCE){
								GrammarRules rule = getRules(state.getStateNum());
								List<Token> tokenNodes = new ArrayList<Token>();
								Stack<Token> tempStack = new Stack<Token>();
								for(int i = 0; i < rule.getChildNo(); i++){
									stateStack.pop();
									tempStack.push(tokenStack.pop());
								}
								for(int i = 0; i < rule.getChildNo(); i++){
									tokenNodes.add(tempStack.pop());
								}
								if(rule.getChildNo() == 0){
									tokenStack.push(new TokenVariable(rule.getName()));
								}
								else{
									tokenStack.push(new TokenVariable(rule.getName(), tokenNodes));
								}
								System.out.println(stepCounter++ + ". Reduce by Rule #"+ rule.getRuleNo() + ": " + rule.getInfo());
								System.out.println("=======================================");
//								System.out.println(tokenStack);
								if(rule.getRuleNo() != 0){
									stateStack.push(gotoTab[stateStack.peek()][getVarVal(tokenStack.peek().getName())]);
									state = new StateProp(actTab[stateStack.peek()][getTokenVal(lookaheadToken.getName())]);
									System.out.println("\nLookahead: "+lookaheadToken.getName());
									
									
//									System.out.println(stateStack);
								}
								else{
									System.out.println("\nACCEPTED!!!\n" + tokenStack);
									needToken = true;
								}
								recoveryMode = false;
								
							}
						}
						else{
							needToken = true;
							System.out.println("SKIP!");
						}
					}
					else{
						if(state.getAction() == StateProp.SHIFT){
							stateStack.push(state.getStateNum());
							tokenStack.push(lookaheadToken);
							System.out.println(stepCounter++ + ". Shift: " + lookaheadToken.getName());
//							System.out.println(stateStack);
//							System.out.println(tokenStack);
							needToken = true;
						}
						else if(state.getAction() == StateProp.REDUCE){
							GrammarRules rule = getRules(state.getStateNum());
							List<Token> tokenNodes = new ArrayList<Token>();
							Stack<Token> tempStack = new Stack<Token>();
							for(int i = 0; i < rule.getChildNo(); i++){
								stateStack.pop();
								tempStack.push(tokenStack.pop());
							}
							for(int i = 0; i < rule.getChildNo(); i++){
								tokenNodes.add(tempStack.pop());
								tokenNodes.get(i).nodeOrder = i;
							}
							if(rule.getChildNo() == 0){
								tokenStack.push(new TokenVariable(rule.getName()));
							}
							else{
								tokenStack.push(new TokenVariable(rule.getName(), tokenNodes));
							}
							System.out.println(stepCounter++ + ". Reduce by Rule #"+ rule.getRuleNo() + ": " + rule.getInfo());
//							System.out.println(tokenStack);
							if(rule.getRuleNo() != 0){
								stateStack.push(gotoTab[stateStack.peek()][getVarVal(tokenStack.peek().getName())]);
								state = new StateProp(actTab[stateStack.peek()][getTokenVal(lookaheadToken.getName())]);
								System.out.println("\nLookahead: "+lookaheadToken.getName());
								
//								System.out.println(stateStack);
							}
							else{
								System.out.println("\nACCEPTED!!!\n" + tokenStack);
								needToken = true;
							}
						}
						else{
							int errorLineNumber;
							if(!tokenStack.peek().isNonTerminal){
								errorLineNumber = tokenStack.peek().lineNo;
							}
							else{
								errorLineNumber = lookaheadToken.lineNo;
							}
							System.out.println("=======================================");
							System.out.println("SYNTAX ERROR! Panic Mode Recovery");
							System.out.println("Line Number: " + errorLineNumber);
							System.out.println("=======================================");
							System.out.println("\n** Original Stack **");
							System.out.println(stateStack);
							System.out.println(tokenStack);
							System.out.println("** !Original Stack **\n");
							
							boolean isStatement = false;
							
							
							while(!isStatement && tokenStack.peek().getName() != "EOF"){
								if(gotoTab[stateStack.peek()][getVarVal("STMT")] != -1){
									isStatement = true;
								}
								else{
									stateStack.pop();
									tokenStack.pop();
								}
							}
							System.out.println("** New Stack **");
							System.out.println(stateStack);
							System.out.println(tokenStack);
							System.out.println("** !New Stack **");
							recoveryMode = true;
							System.out.println("\nLookahead: "+lookaheadToken.getName());
							state = new StateProp(actTab[stateStack.peek()][getTokenVal(lookaheadToken.getName())]);
						}
					}
					
				}
			}
		}
		
		this.parsingTreeRoot = tokenStack.peek();
		
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

	public void displayTokens(String fileName) throws IOException {
		try {
			scanner = new LexiScan(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("List of Tokens");
		System.out.println("========================");
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
		newToken = null;
		prevToken = null;
		
     }
	
	public HashMap<Integer, GrammarRules> initGrammar(){
		rules = new HashMap<>();
		rules.put(0, new GrammarRules("BPRIME","BPRIME -> B", 0, 1));
		rules.put(1, new GrammarRules("B","B -> initiate SCOPE terminate", 1, 3));
		rules.put(2, new GrammarRules("SCOPE","SCOPE -> start STMTS end", 2, 3));
		rules.put(3, new GrammarRules("STMTS","STMTS -> STMT STMTS", 3, 2));
		rules.put(4, new GrammarRules("STMTS","STMTS -> empty", 4, 0));
		rules.put(5, new GrammarRules("STMT","STMT -> DECLARATIONSTMT", 5, 1));
		rules.put(6, new GrammarRules("STMT","STMT -> ITERATSTMT", 6, 1));
		rules.put(7, new GrammarRules("STMT","STMT -> INPUTSTMT", 7, 1));
		rules.put(8, new GrammarRules("STMT","STMT -> OUTPUTSTMT", 8, 1));
		rules.put(9, new GrammarRules("STMT","STMT -> ASSIGNMENTSTMT delimiter", 9, 2));
		rules.put(10, new GrammarRules("STMT","STMT -> CONVERTSTMT", 10, 1));
		rules.put(11, new GrammarRules("STMT","STMT -> CONDITIONALSTMT", 11, 1));
		rules.put(12, new GrammarRules("DECLARATIONSTMT","DECLARATIONSTMT -> DATATYPE INITIALIZATION delimiter", 12, 3));
		rules.put(13, new GrammarRules("ITERATSTMT","ITERATSTMT -> WHILELOOP", 13, 1));
		rules.put(14, new GrammarRules("ITERATSTMT","ITERATSTMT -> FORLOOP", 14, 1));
		rules.put(15, new GrammarRules("INPUTSTMT","INPUTSTMT -> input colon id delimiter", 15, 4));
		rules.put(16, new GrammarRules("OUTPUTSTMT","OUTPUTSTMT -> output colon EXPR delimiter", 16, 4));
		rules.put(17, new GrammarRules("ASSIGNMENTSTMT","ASSIGNMENTSTMT -> ASSIGNMENT", 17, 1));
		rules.put(18, new GrammarRules("ASSIGNMENTSTMT","ASSIGNMENTSTMT -> POSTEXPR", 18, 1));
		rules.put(19, new GrammarRules("CONVERTSTMT","CONVERTSTMT -> id convert DATATYPE delimiter", 19, 4));
		rules.put(20, new GrammarRules("CONDITIONALSTMT","CONDITIONALSTMT -> if colon EXPR SCOPE ELSESTMT", 20, 5));
		rules.put(21, new GrammarRules("ELSESTMT","ELSESTMT -> elseIf colon EXPR SCOPE ELSESTMT", 21, 5));
		rules.put(22, new GrammarRules("ELSESTMT","ELSESTMT -> else SCOPE endIf", 22, 3));
		rules.put(23, new GrammarRules("ELSESTMT","ELSESTMT -> endIf", 23, 1));
		rules.put(24, new GrammarRules("INITIALIZATION","INITIALIZATION -> ASSIGNMENT INITIALIZATIONPRIME", 24, 2));
		rules.put(25, new GrammarRules("INITIALIZATION","INITIALIZATION -> id INITIALIZATIONPRIME", 25, 2));
		rules.put(26, new GrammarRules("INITIALIZATIONPRIME","INITIALIZATIONPRIME -> comma INITIALIZATION", 26, 2));
		rules.put(27, new GrammarRules("INITIALIZATIONPRIME","INITIALIZATIONPRIME -> empty", 27, 0));
		rules.put(28, new GrammarRules("WHILELOOP","WHILELOOP -> whileLoop colon EXPR SCOPE", 28, 4));
		rules.put(29, new GrammarRules("FORLOOP","FORLOOP -> forLoop colon ASSIGNMENTSTMT conditionDelimiter EXPR conditionDelimiter ASSIGNMENTSTMT SCOPE", 29, 8));
		rules.put(30, new GrammarRules("ASSIGNMENT","ASSIGNMENT -> id assignment EXPR", 30, 3));
		rules.put(31, new GrammarRules("DATATYPE","DATATYPE -> string", 31, 1));
		rules.put(32, new GrammarRules("DATATYPE","DATATYPE -> character", 32, 1));
		rules.put(33, new GrammarRules("DATATYPE","DATATYPE -> integer", 33, 1));
		rules.put(34, new GrammarRules("DATATYPE","DATATYPE -> boolean", 34, 1));
		rules.put(35, new GrammarRules("DATATYPE","DATATYPE -> float", 35, 1));
		rules.put(36, new GrammarRules("EXPR","EXPR -> LOGEXPR", 36, 1));
		rules.put(37, new GrammarRules("LOGEXPR","LOGEXPR -> LOGEXPR LOGOPER EQEXPR", 37, 3));
		rules.put(38, new GrammarRules("LOGEXPR","LOGEXPR -> EQEXPR", 38, 1));
		rules.put(39, new GrammarRules("LOGOPER","LOGOPER -> and", 39, 1));
		rules.put(40, new GrammarRules("LOGOPER","LOGOPER -> or", 40, 1));
		rules.put(41, new GrammarRules("EQEXPR","EQEXPR -> EQEXPR EQOPER RELEXPR", 41, 3));
		rules.put(42, new GrammarRules("EQEXPR","EQEXPR -> RELEXPR", 42, 1));
		rules.put(43, new GrammarRules("EQOPER","EQOPER -> equal", 43, 1));
		rules.put(44, new GrammarRules("EQOPER","EQOPER -> notEqual", 44, 1));
		rules.put(45, new GrammarRules("RELEXPR","RELEXPR -> RELEXPR RELOPER TERMEXPR", 45, 3));
		rules.put(46, new GrammarRules("RELEXPR","RELEXPR -> TERMEXPR", 46, 1));
		rules.put(47, new GrammarRules("RELOPER","RELOPER -> lessThan", 47, 1));
		rules.put(48, new GrammarRules("RELOPER","RELOPER -> lessThanOrEqual", 48, 1));
		rules.put(49, new GrammarRules("RELOPER","RELOPER -> greaterThan", 49, 1));
		rules.put(50, new GrammarRules("RELOPER","RELOPER -> greaterThanOrEqual", 50, 1));
		rules.put(51, new GrammarRules("TERMEXPR","TERMEXPR -> TERMEXPR TERMOPER FACTEXPR", 51, 3));
		rules.put(52, new GrammarRules("TERMEXPR","TERMEXPR -> FACTEXPR", 52, 1));
		rules.put(53, new GrammarRules("TERMOPER","TERMOPER -> plus", 53, 1));
		rules.put(54, new GrammarRules("TERMOPER","TERMOPER -> minus", 54, 1));
		rules.put(55, new GrammarRules("TERMOPER","TERMOPER -> concat", 55, 1));
		rules.put(56, new GrammarRules("FACTEXPR","FACTEXPR -> FACTEXPR FACTOPER UNARYEXPR", 56, 3));
		rules.put(57, new GrammarRules("FACTEXPR","FACTEXPR -> UNARYEXPR", 57, 1));
		rules.put(58, new GrammarRules("FACTOPER","FACTOPER -> times", 58, 1));
		rules.put(59, new GrammarRules("FACTOPER","FACTOPER -> divide", 59, 1));
		rules.put(60, new GrammarRules("FACTOPER","FACTOPER -> modulo", 60, 1));
		rules.put(61, new GrammarRules("UNARYEXPR","UNARYEXPR -> UNARYOPER UNARYEXPR", 61, 2));
		rules.put(62, new GrammarRules("UNARYEXPR","UNARYEXPR -> POSTEXPR", 62, 1));
		rules.put(63, new GrammarRules("UNARYOPER","UNARYOPER -> negative", 63, 1));
		rules.put(64, new GrammarRules("UNARYOPER","UNARYOPER -> not", 64, 1));
		rules.put(65, new GrammarRules("POSTEXPR","POSTEXPR -> POSTEXPR POSTOPER", 65, 2));
		rules.put(66, new GrammarRules("POSTEXPR","POSTEXPR -> VALUE", 66, 1));
		rules.put(67, new GrammarRules("POSTOPER","POSTOPER -> increment", 67, 1));
		rules.put(68, new GrammarRules("POSTOPER","POSTOPER -> decrement", 68, 1));
		rules.put(69, new GrammarRules("VALUE","VALUE -> id", 69, 1));
		rules.put(70, new GrammarRules("VALUE","VALUE -> CONST", 70, 1));
		rules.put(71, new GrammarRules("VALUE","VALUE -> openParen EXPR closeParen", 71, 3));
		rules.put(72, new GrammarRules("CONST","CONST -> stringConstant", 72, 1));
		rules.put(73, new GrammarRules("CONST","CONST -> charConstant", 73, 1));
		rules.put(74, new GrammarRules("CONST","CONST -> BOOLCONST", 74, 1));
		rules.put(75, new GrammarRules("CONST","CONST -> floatConstant", 75, 1));
		rules.put(76, new GrammarRules("CONST","CONST -> integerConstant", 76, 1));
		rules.put(77, new GrammarRules("BOOLCONST","BOOLCONST -> true", 77, 1));
		rules.put(78, new GrammarRules("BOOLCONST","BOOLCONST -> false", 78, 1));
		
		return rules;
	}
	
	public GrammarRules getRules(int ruleNo){
		return rules.get(ruleNo);
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
			case "initiate":
				return 0;
			case "terminate":
				return 1;
			case "start":
				return 2;
			case "end":
				return 3;
			case "delimiter":
				return 4;
			case "input":
				return 5;
			case "colon":
				return 6;
			case "identifier":
				return 7;
			case "assignment":
				return 8;
			case "output":
				return 9;
			case "convert":
				return 10;
			case "if":
				return 11;
			case "elseIf":
				return 12;
			case "else":
				return 13;
			case "endIf":
				return 14;
			case "comma":
				return 15;
			case "whileLoop":
				return 16;
			case "forLoop":
				return 17;
			case "conditionDelimiter":
				return 18;
			case "string":
				return 19;
			case "character":
				return 20;
			case "integer":
				return 21;
			case "boolean":
				return 22;
			case "float":
				return 23;
			case "stringConstant":
				return 24;
			case "charConstant":
				return 25;
			case "floatConstant":
				return 26;
			case "integerConstant":
				return 27;
			case "true":
				return 28;
			case "false":
				return 29;
			case "openParen":
				return 30;
			case "closeParen":
				return 31;
			case "and":
				return 32;
			case "or":
				return 33;
			case "equal":
				return 34;
			case "notEqual":
				return 35;
			case "lessThan":
				return 36;
			case "lessThanOrEqual":
				return 37;
			case "greaterThan":
				return 38;
			case "greaterThanOrEqual":
				return 39;
			case "plus":
				return 40;
			case "minus":
				return 41;
			case "concat":
				return 42;
			case "times":
				return 43;
			case "divide":
				return 44;
			case "modulo":
				return 45;
			case "negative":
				return 46;
			case "not":
				return 47;
			case "increment":
				return 48;
			case "decrement":
				return 49;
			case "EOF":
				return 50;
			default:
				return -1;
			
		}
	}





	public Token getParsingTreeRoot() {
		return parsingTreeRoot;
	}





	public void setParsingTreeRoot(Token parsingTreeRoot) {
		this.parsingTreeRoot = parsingTreeRoot;
	}
       


	
	
}
