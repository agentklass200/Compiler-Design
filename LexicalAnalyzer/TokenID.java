package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenID extends Token {
	private String key;
	private String idType;
	private String idValue;
	
	public TokenID(String key){
		super("Identifier");
		this.setKey(key);
		this.idType = null;
		idValue = null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	public String formalString(){
		return "ID[key = " + key + " Details = [ Lexeme = " + key + ", Type = ID] ]\n";
	}
	
	@Override
	public String toString() {
		return key;
	}

}
