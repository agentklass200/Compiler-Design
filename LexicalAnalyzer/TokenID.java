package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenID extends Token {
	
	public TokenID(String key, int lineNo, int colNo){
		super("identifier", lineNo, colNo);
		this.setKey(key);

	}
	
	public TokenID(String key){
		super("identifier", 0,0);
		this.setKey(key);
	}

	public String formalString(){
		return "ID[key = " + this.getKey() + " Details = [ Lexeme = " + this.getKey() + ", Type = ID] ]\n";
	}
	
	@Override
	public String toString() {
//		return "[" + key  + " " + this.getLineNo() + ":" + this.getColNo() + "]";
		return this.getKey();
	}



}
