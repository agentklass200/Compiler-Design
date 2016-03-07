package LexicalAnalyzer;

import java.io.*;
import java.util.*;
import LexicalAnalyzer.*;

public class LexiScan {
	int lineNo, tracker, lookahead;
	char current;
	ArrayList<Integer> stream = new ArrayList<Integer>();
	ArrayList<Token> tokens = new ArrayList<Token>();
	ArrayList<TokenID> idList = new ArrayList<TokenID>();
	ArrayList<Character> tokenBuilder = new ArrayList<Character>();
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
			
			tokens.add(new Token("filler", "", "filler"));
			
			
			while(stream.get(tracker) != -1 && isError == false){
				getToken();
				tracker = lookahead;
				tokenBuilder.clear();
			}
			
			if(isError == false){
				tokens.remove(0);
				System.out.println("Sample Program");
				System.out.println("========================");
				for(int i = 0; i < stream.size() -1; i++){
					System.out.print((char)stream.get(i).intValue());
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
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			file.close();
			bReader.close();
		}
			
	}
	
	public void getToken(){
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
					
					tokens.add(new TokenID(newID));
					if(idList.isEmpty()){
						idList.add(new TokenID(newID));
					}
					else{
						if(!checkID(newID)){
							idList.add(new TokenID(newID));
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
						while((char)stream.get(lookahead).intValue() != ':' || (char)stream.get(lookahead + 1).intValue() != '*'){
							lookahead++;
						}
						lookahead =  lookahead + 2;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '+':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '+':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						if(tokens.get(tokens.size() - 1).getName() == "StringConstant"){
							tokens.add(symbol.getTable().get(newToken + 2));
						}
						else{
							tokens.add(symbol.getTable().get(newToken + 1));
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
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						if(tokens.get(tokens.size() - 1).getName() == "IntegerConstant" || tokens.get(tokens.size() - 1).getName() == "FloatingConstant" || tokens.get(tokens.size() - 1).getName() == "Identifier"){
							tokens.add(symbol.getTable().get(newToken + "sub"));
						}
						else{
							tokens.add(symbol.getTable().get(newToken + "neg"));
						}
				}
				break;
			case '/':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case '%':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case '=':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '~':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '&':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '&':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '|':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '|':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
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
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					case '>':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '<':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					case '<':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
						lookahead++;
						break;
					default:
						newToken = buildToken(tokenBuilder);
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case ';':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case ':':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case ',':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case '(':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case ')':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
			case '"': 
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				while((char)stream.get(lookahead).intValue() != '"'){
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
				}
				tokenBuilder.add((char)stream.get(lookahead).intValue());
				newConstant = buildToken(tokenBuilder);
				tokens.add(new TokenConstant("StringConstant", newConstant, "Constant"));
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
								tokens.add(new TokenConstant("CharConstant", newConstant, "Constant"));
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
							tokens.add(new TokenConstant("CharConstant", newConstant, "Constant"));
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
					tokens.add(new TokenConstant("FloatingConstant", newConstant, "Constant"));
				}
				else{
					tokens.add(new TokenConstant("IntegerConstant", newConstant, "Constant"));
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
					tokens.add(symbol.getTable().get(newToken));
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
	
	public boolean checkID(String newID){
		TokenID[] tokID = idList.toArray(new TokenID[idList.size()]);
		for(TokenID id: tokID){
			if(id.getKey().equals(newID)){
				return true;
			}
		}
		return false;
	}
	
	public String buildToken(ArrayList<Character> cA){
		StringBuilder b = new StringBuilder(cA.size());
		for(Character c: cA){
			b.append(c);
		}
		return b.toString();
	}
}
