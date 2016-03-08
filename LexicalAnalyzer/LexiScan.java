package LexicalAnalyzer;

import java.io.*;
import java.util.*;
import LexicalAnalyzer.TokenID;
import LexicalAnalyzer.TokenConstant;


import Parsing.*;

public class LexiScan {
	private int lineNo, tracker, lookahead;
	public ArrayList<Integer> stream = new ArrayList<Integer>();
	public ArrayList<Character> tokenBuilder = new ArrayList<Character>();
	boolean isError = false;
	SymbolTable symbol = new SymbolTable();
	
	public LexiScan(String fileName) throws IOException{
		
		InputStream file = null;
		BufferedReader bReader = null;
		try{
			file = new FileInputStream(fileName);
			bReader = new BufferedReader(new InputStreamReader(file));
			int val = 0;
			lineNo = 1;
			while((val = bReader.read()) != -1){
				stream.add(val);
			}
			stream.add(-1);
			
			tracker = 0;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			file.close();
			bReader.close();
		}
			
	}
	
	public void getToken(Parser parse){
		String newToken, newConstant;
		switch((char)stream.get(tracker).intValue()){
			case ' ':
				lookahead++;
				break;
			case '\t':
				lookahead++;
				break;
			case '\n':
				lookahead++;
				break;
			case '\r': // WhiteSpace
				lookahead++;
				lineNo++;
				break;
			case '_':  // Identifier
				tokenBuilder.add((char)stream.get(tracker).intValue());
				lookahead++;
				if(Character.isLetter((char)stream.get(lookahead).intValue())){
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
					while(Character.isLetter((char)stream.get(lookahead).intValue()) || Character.isDigit((char)stream.get(lookahead).intValue())){
                        tokenBuilder.add((char)stream.get(lookahead).intValue());
						lookahead++;
					}
					
					String newID = buildToken(tokenBuilder);
					
					parse.getTokens().add(new TokenID(newID));
					
					if(parse.getIdList().isEmpty()){
						parse.getIdList().add(new TokenID(newID));
					}
					else{
						if(!checkID(newID, parse)){
							parse.getIdList().add(new TokenID(newID));
						}
					}		
				}
				else if(Character.isDigit((char)stream.get(lookahead).intValue())){
					isError = true;
					System.out.println();
					System.out.println("Invalid Token: Identifier name must not start with a digit.");
					System.out.println("At line no: "+ lineNo);
				}
				else{
					isError = true;
					System.out.println();
					System.out.println("Invalid Token: Identifier name not found.");
					System.out.println("At line no: "+ lineNo);
				}
				break;
			case '*':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case ':':  /* *: Hello :hello :* */
						lookahead++;
						while(((char)stream.get(lookahead).intValue() != ':' || (char)stream.get(lookahead + 1).intValue() != '*') && isError == false){
							 if((char)stream.get(lookahead).intValue() == '\r'){
                                 lineNo++;
                             }
                             if(stream.get(lookahead).intValue() == -1){
                                 isError = true;
                                 System.out.println();
                                 System.out.println("Error: Comment not closed with \":*\".");
                                 System.out.println("Error in Line Number: " + lineNo);
                                 lookahead = lookahead - 3;
                             }
                             lookahead++;                                  
						}
						lookahead =  lookahead + 2;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case '+':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '+':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						if(parse.getTokens().get(parse.getTokens().size() - 1).getName() == "StringConstant"){
							parse.getTokens().add(symbol.getTable().get(newToken + "con"));
						}
						else{
							parse.getTokens().add(symbol.getTable().get(newToken + "add"));
						}
						
				}
				break;
			case '-':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '-':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						if(parse.getTokens().get(parse.getTokens().size() - 1).getName() == "IntegerConstant" || 
								parse.getTokens().get(parse.getTokens().size() - 1).getName() == "FloatingConstant" || 
								parse.getTokens().get(parse.getTokens().size() - 1).getName() == "Identifier"){
							parse.getTokens().add(symbol.getTable().get(newToken + "sub"));
						}
						else{
							parse.getTokens().add(symbol.getTable().get(newToken + "neg"));
						}
				}
				break;
			case '/':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case '%':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case '=':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case '~':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case '&':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '&':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case '|':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '|':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						isError = true;
						System.out.println();
						System.out.println("Invalid Token! The token \"|\" is not valid");
						System.out.println("At line no: "+ lineNo);
				}
				break;
			case '>':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					case '>':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case '<':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					case '<':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						parse.getTokens().add(symbol.getTable().get(newToken));
				}
				break;
			case ';':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case ':':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case ',':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case '(':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case ')':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				parse.getTokens().add(symbol.getTable().get(newToken));
				break;
			case '"': 
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				while((char)stream.get(lookahead).intValue() != '"' && isError ==  false){
					if((char)stream.get(lookahead).intValue() == '\r' ){
                      lineNo++;
                    }
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					if(stream.get(lookahead).intValue() == -1 ){
                        isError = true;
                        System.out.println();
                        System.out.println("Error: String Constant not closed with '\"'.");
                        System.out.println("Error in Line Number: " + lineNo);
                        lookahead = lookahead - 3;
                    }                    
					lookahead++;
				}
				tokenBuilder.add((char)stream.get(lookahead).intValue());
				newConstant = buildToken(tokenBuilder);
				parse.getTokens().add(new TokenConstant("StringConstant", newConstant, "Constant"));
				lookahead++;
				break;
			case '\'':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue()); // '
				if((char)stream.get(lookahead).intValue() != '\''){
					tokenBuilder.add((char)stream.get(lookahead).intValue()); // 'a
					if((char)stream.get(lookahead).intValue() == '\\'){ // '\ or 'a
						lookahead++;
						if((char)stream.get(lookahead).intValue() =='\''){ // '\'
							isError = true;
							System.out.println();
							System.out.println("Invalid Token! Invalid Character Constant");
							System.out.println("At line no: "+ lineNo);
						}
						else{
							tokenBuilder.add((char)stream.get(lookahead).intValue()); // '\n
							lookahead++;
							if((char)stream.get(lookahead).intValue() != '\''){
								isError = true;
								System.out.println();
								System.out.println("Invalid Token! Invalid Character Constant");
								System.out.println("At line no: "+ lineNo);
							}
							else{
								tokenBuilder.add((char)stream.get(lookahead).intValue()); // '\n'
								newConstant = buildToken(tokenBuilder);
								parse.getTokens().add(new TokenConstant("CharConstant", newConstant, "Constant"));
								lookahead++;
							}
						}
					}
					else{
						lookahead++;
						if((char)stream.get(lookahead).intValue() != '\''){ //  'ab
							isError = true;
							System.out.println();
							System.out.println("Invalid Token! Invalid Character Constant");
							System.out.println("At line no: "+ lineNo);
						}
						else{
							tokenBuilder.add((char)stream.get(lookahead).intValue()); // 'a'
							newConstant = buildToken(tokenBuilder);
							parse.getTokens().add(new TokenConstant("CharConstant", newConstant, "Constant"));
							lookahead++;
						}
					}
				}
				else{ // ''
					isError = true;
					System.out.println();
					System.out.println("Invalid Token! Empty char");
					System.out.println("At line no: "+ lineNo);
				}
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				boolean isFloat = false;
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				while(Character.isDigit((char)stream.get(lookahead).intValue()) || ((char)stream.get(lookahead).intValue() == '.' && isFloat == false) ){
					if((char)stream.get(lookahead).intValue() == '.'){
						isFloat = true;
					}
					
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
				}
				newConstant = buildToken(tokenBuilder);
				if(isFloat){
					parse.getTokens().add(new TokenConstant("FloatingConstant", newConstant, "Constant"));
				}
				else{
					parse.getTokens().add(new TokenConstant("IntegerConstant", newConstant, "Constant"));
				}
				break;
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':	
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				while(Character.isLetter((char)stream.get(lookahead).intValue())){
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
				}
				newToken = buildToken(tokenBuilder);
				if(symbol.getTable().containsKey(newToken)){
					parse.getTokens().add(symbol.getTable().get(newToken));
				}
				else{
					isError = true;
					System.out.println();
					System.out.println("Invalid Token! The token \""+ newToken +"\" is not valid");
					System.out.println("At line no: "+ lineNo);
				}
				break;
			default:
				isError = true;
				System.out.println();
				System.out.println("Invalid Token! The token \""+ (char)stream.get(tracker).intValue() +"\" is not valid");
				System.out.println("At line no: "+ lineNo);
		}
		
	}
	
	public boolean checkID(String newID, Parser parse){
		TokenID[] tokID = parse.getIdList().toArray(new TokenID[parse.getIdList().size()]);
		for(TokenID id: tokID){
			if(id.getKey().equals(newID)){
				return true;
			}
		}
		return false;
	}
	
	public String buildToken(ArrayList<Character> cA){
		StringBuilder b = new StringBuilder(cA.size());
		String s;
		if(cA.get(0) == '\"' || cA.get(0) == '\''){
			for(int i = 0; i < cA.size(); i++){
				if(cA.get(i) == '\\'){
					if(cA.get(i + 1) == 'n'){
						cA.remove(i);
						cA.remove(i);
						cA.add(i, '\n');
					}
					else if(cA.get(i + 1) == 'r'){
						cA.remove(i);
						cA.remove(i);
						cA.add(i, '\r');
					}
					else if(cA.get(i + 1) == 't'){
						cA.remove(i);
						cA.remove(i);
						cA.add(i, '\t');
					}
					else{
						cA.remove(i);
					}
				}
			}
		}
		for(Character c: cA){
			b.append(c);
		}
		return b.toString();
	}

	public int getTracker() {
		return tracker;
	}

	public int getLookahead() {
		return lookahead;
	}

	public boolean isError() {
		return isError;
	}

	public void setTracker(int tracker) {
		this.tracker = tracker;
	}
}
