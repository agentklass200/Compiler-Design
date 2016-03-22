package LexicalAnalyzer;

import java.io.*;
import java.util.*;
import LexicalAnalyzer.TokenID;
import LexicalAnalyzer.TokenConstant;


import Parsing.*;

public class LexiScan {
	private int lineNo, tracker, lookahead, colNo, tokCol, tokLine;
	public ArrayList<Integer> stream = new ArrayList<Integer>();
	public ArrayList<Character> tokenBuilder = new ArrayList<Character>();
	private Token tempToken;
	private SymbolTable symbol = new SymbolTable();
	
	
	
	public LexiScan(String fileName) throws IOException{
		
		InputStream file = null;
		BufferedReader bReader = null;
		try{
			file = new FileInputStream(fileName);
			bReader = new BufferedReader(new InputStreamReader(file));
			int val = 0;
			lineNo = 1;
			colNo = 1;
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
	
	public Token getToken(Parser parse){
		String newToken, newConstant;
		switch((char)stream.get(tracker).intValue()){
			case ' ':
				lookahead++;
				colNo++;
				return new TokenWhitespace(TokenWhitespace.SPACE_WHITESPACE);
			case '\t':
				lookahead++;
				colNo = colNo + 4;
				return new TokenWhitespace(TokenWhitespace.TAB_WHITESPACE);
			case '\n':
				lookahead++;
				return new TokenWhitespace(TokenWhitespace.NEWLINE_WHITESPACE);
			case '\r': // WhiteSpace
				lookahead++;
				lineNo++;
				colNo = 0;
				return new TokenWhitespace(TokenWhitespace.RETURN_WHITESPACE);
			case '_':  // Identifier
				tokenBuilder.add((char)stream.get(tracker).intValue());
				lookahead++;
				colNo++;
				tokCol = colNo;
				tokLine = lineNo;
				if(Character.isLetter((char)stream.get(lookahead).intValue())){
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
					colNo++;
					while(Character.isLetter((char)stream.get(lookahead).intValue()) || Character.isDigit((char)stream.get(lookahead).intValue())){
                        tokenBuilder.add((char)stream.get(lookahead).intValue());
						lookahead++;
						colNo++;
					}
					
					String newID = buildToken(tokenBuilder);
					
					if(parse.getIdMaps().isEmpty()){
						parse.getIdMaps().put(newID, new TokenID(newID));
					}
					else{
						if(!parse.getIdMaps().containsKey(newID)){
							parse.getIdMaps().put(newID, new TokenID(newID));
						}
					}
					
					return new TokenID(newID, tokLine, tokCol);
				}
				else if(Character.isDigit((char)stream.get(lookahead).intValue())){
					return new TokenError(TokenError.SCANNER_ERROR, 0, lineNo, colNo);
				}
				else{
					return new TokenError(TokenError.SCANNER_ERROR, 1, lineNo, colNo);
				}
			case '*':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case ':':  /* *: Hello :hello :* */
						lookahead++;
						colNo++;
						while(((char)stream.get(lookahead).intValue() != ':' || (char)stream.get(lookahead + 1).intValue() != '*')){
							 if((char)stream.get(lookahead).intValue() == '\r'){
                                 lineNo++;
                                 colNo = 0;
                             }
							 else if((char)stream.get(lookahead).intValue() == '\t'){
								 colNo = colNo + 4;
							 }
							 else{
								 colNo++;
							 }
                             if(stream.get(lookahead).intValue() == -1){
                                 return new TokenError(TokenError.SCANNER_ERROR, 2, lineNo, --colNo);
                             }
                             lookahead++;
						}
						lookahead =  lookahead + 2;
						colNo = colNo + 2;
						return new TokenWhitespace(TokenWhitespace.COMMENT_WHITESPACE);
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case '+':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '+':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						return symbol.getTable().get(newToken);
					default:
						newToken = buildToken(tokenBuilder);
						if(parse.getPrevToken().getName() == "StringConstant"){
							tempToken = symbol.getTable().get(newToken + "con");
							tempToken.setLineNo(tokLine);
							tempToken.setColNo(tokCol);
							return tempToken;
						}
						else{
							tempToken = symbol.getTable().get(newToken + "add");
							tempToken.setLineNo(tokLine);
							tempToken.setColNo(tokCol);
							return tempToken;
						}
				}
			case '-':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '-':
						if(parse.getPrevToken().getName() !=  "Decrement"){
							tokenBuilder.add((char)stream.get(lookahead).intValue());
							newToken = buildToken(tokenBuilder);
							lookahead++;
							colNo++;
							tempToken = symbol.getTable().get(newToken);
							tempToken.setLineNo(tokLine);
							tempToken.setColNo(tokCol);
							return tempToken;
						}
					default:
						newToken = buildToken(tokenBuilder);
						if(parse.getPrevToken().getName() == "IntegerConstant" || 
							parse.getPrevToken().getName() == "FloatingConstant" || 
							parse.getPrevToken().getName() == "Identifier" || 
							parse.getPrevToken().getName() == "Decrement"){
							tempToken = symbol.getTable().get(newToken + "sub");
							tempToken.setLineNo(tokLine);
							tempToken.setColNo(tokCol);
							return tempToken;
						}
						else{
							tempToken = symbol.getTable().get(newToken + "neg");
							tempToken.setLineNo(tokLine);
							tempToken.setColNo(tokCol);
							return tempToken;
						}
				}
			case '/':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case '%':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case '=':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case '~':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case '&':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '&':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case '|':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '|':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						return new TokenError(TokenError.SCANNER_ERROR, 3, lineNo, colNo);
				}
			case '>':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					case '>':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case '<':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				switch((char)stream.get(lookahead).intValue()){
					case '=':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					case '<':
						tokenBuilder.add((char)stream.get(lookahead).intValue());
						newToken = buildToken(tokenBuilder);
						lookahead++;
						colNo++;
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
					default:
						newToken = buildToken(tokenBuilder);
						tempToken = symbol.getTable().get(newToken);
						tempToken.setLineNo(tokLine);
						tempToken.setColNo(tokCol);
						return tempToken;
				}
			case ';':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case ':':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case ',':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case '(':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case ')':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				newToken = buildToken(tokenBuilder);
				tempToken = symbol.getTable().get(newToken);
				tempToken.setLineNo(tokLine);
				tempToken.setColNo(tokCol);
				return tempToken;
			case '"': 
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				while((char)stream.get(lookahead).intValue() != '"'){
					if((char)stream.get(lookahead).intValue() == '\r' ){
						lineNo++;
						colNo = 0;
                    }
					else if((char)stream.get(lookahead).intValue() == '\t'){
						colNo = colNo + 4;
					}
					else{
						colNo++;
					}
					
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					if(stream.get(lookahead).intValue() == -1 ){
                        return new TokenError(TokenError.SCANNER_ERROR, 4, lineNo, --colNo);
                    }
                    if((char)stream.get(lookahead).intValue() == '\\'){
						if((char)stream.get(lookahead + 1).intValue() == '\"'){
							lookahead++;
							colNo++;
							tokenBuilder.add((char)stream.get(lookahead).intValue());	
						}
					}                    
					lookahead++;
				}
				tokenBuilder.add((char)stream.get(lookahead).intValue());
				newConstant = buildToken(tokenBuilder);
				lookahead++;
				colNo++;
				return new TokenConstant(TokenConstant.STRING_CONSTANT, newConstant, "Constant", lineNo, colNo);
			case '\'':
				lookahead++;
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue()); // '
				tokCol = colNo;
				tokLine = lineNo;
				if((char)stream.get(lookahead).intValue() != '\''){
					tokenBuilder.add((char)stream.get(lookahead).intValue()); // 'a
					if((char)stream.get(lookahead).intValue() == '\\'){ // '\ or 'a
						lookahead++;
						colNo++;
						if((char)stream.get(lookahead).intValue() =='\''){ // '\'
							tokenBuilder.add((char)stream.get(lookahead).intValue()); 
							lookahead++;
							colNo++;
							if((char)stream.get(lookahead).intValue() == '\''){
								tokenBuilder.add((char)stream.get(lookahead).intValue());
								newConstant = buildToken(tokenBuilder);
								lookahead++;
								colNo++;
								return new TokenConstant(TokenConstant.CHAR_CONSTANT, newConstant, "Constant", lineNo, colNo);
							}
							else{
								return new TokenError(TokenError.SCANNER_ERROR, 5, lineNo, colNo);
							}
							
						}
						else{
							tokenBuilder.add((char)stream.get(lookahead).intValue()); // '\n
							lookahead++;
							colNo++;
							if((char)stream.get(lookahead).intValue() != '\''){
								return new TokenError(TokenError.SCANNER_ERROR, 5, lineNo, colNo);
							}
							else{
								tokenBuilder.add((char)stream.get(lookahead).intValue()); // '\n'
								newConstant = buildToken(tokenBuilder);
								lookahead++;
								colNo++;
								return new TokenConstant(TokenConstant.CHAR_CONSTANT, newConstant, "Constant", lineNo, colNo);
							}
						}
					}
					else{
						lookahead++;
						colNo++;
						if((char)stream.get(lookahead).intValue() != '\''){ //  'ab
							return new TokenError(TokenError.SCANNER_ERROR, 5, lineNo, colNo);
						}
						else{
							tokenBuilder.add((char)stream.get(lookahead).intValue()); // 'a'
							newConstant = buildToken(tokenBuilder);
							lookahead++;
							colNo++;
							return new TokenConstant(TokenConstant.CHAR_CONSTANT, newConstant, "Constant", lineNo, colNo);
						}
					}
				}
				else{ // ''
					return new TokenError(TokenError.SCANNER_ERROR, 5, lineNo, colNo);
				}
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
				colNo++;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				tokCol = colNo;
				tokLine = lineNo;
				while(Character.isDigit((char)stream.get(lookahead).intValue()) || ((char)stream.get(lookahead).intValue() == '.' && isFloat == false) ){
					if((char)stream.get(lookahead).intValue() == '.'){
						isFloat = true;
					}
					
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
					colNo++;
				}
				newConstant = buildToken(tokenBuilder);
				if(isFloat){
					return new TokenConstant(TokenConstant.FLOATING_CONSTANT, newConstant, "Constant", lineNo, colNo);
				}
				else{
					return new TokenConstant(TokenConstant.INTEGER_CONSTANT, newConstant, "Constant", lineNo, colNo);
				}
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
				colNo++;
				tokCol = colNo;
				tokLine = lineNo;
				tokenBuilder.add((char)stream.get(tracker).intValue());
				while(Character.isLetter((char)stream.get(lookahead).intValue())){
					tokenBuilder.add((char)stream.get(lookahead).intValue());
					lookahead++;
					colNo++;
				}
				newToken = buildToken(tokenBuilder);
				if(symbol.getTable().containsKey(newToken)){
					tempToken = symbol.getTable().get(newToken);
					tempToken.setLineNo(tokLine);
					tempToken.setColNo(tokCol);
					return tempToken;
				}
				else{
					return new TokenError(TokenError.SCANNER_ERROR, 7, lineNo, colNo, newToken);
				}
			default:
				return new TokenError(TokenError.SCANNER_ERROR, 7, lineNo, colNo, (char)stream.get(tracker).intValue());
		}
		
	}
	
	public String buildToken(ArrayList<Character> cA){
		StringBuilder b = new StringBuilder(cA.size());
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

	public void setTracker(int tracker) {
		this.tracker = tracker;
	}
}
