package LexicalAnalyzer;

import java.io.*;
import java.util.*;
import LexicalAnalyzer.*;

public class LexiScan {
	int posNo, lineNo, tracker, lookahead;
	char current;
	ArrayList<Integer> stream = new ArrayList<Integer>();
	ArrayList<Token> tokens = new ArrayList<Token>();
	ArrayList<Token> idList = new ArrayList<Token>();
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
			posNo = 1;
			lineNo = 1;
		
			
			while((val = bReader.read()) != -1){
				stream.add(val);
			}
			
			stream.add(-1);
			
			tracker = 0;
			
			
			while(stream.get(tracker) != -1 && isError == false){
				getToken();
				tracker = lookahead;
				tokenBuilder.clear();
			}
			System.out.println("List of Identifiers");
			for(int i = 0; i < idList.size(); i++){
				System.out.print(idList.get(i));
			}
			System.out.println();
			System.out.println("List of Tokens");
			for(int i = 0; i < tokens.size(); i++){
				System.out.print(tokens.get(i));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			file.close();
			bReader.close();
		}
			
	}
	
	public void getToken(){
		String newToken;
		switch((char)stream.get(tracker).intValue()){
			case ' ':
			case '\t':
			case '\n':
			case '\r': // WhiteSpace
				lookahead++;
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
				}
				else{
					isError = true;
					System.out.println();
					System.out.println("Invalid Token: Identifier name not found.");
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
						if(tokens.get(tokens.size() - 1).getName() == "Increment"){
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
						tokens.add(symbol.getTable().get(newToken));
				}
				break;
			case '/':
				lookahead++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				newToken = buildToken(tokenBuilder);
				tokens.add(symbol.getTable().get(newToken));
				break;
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
