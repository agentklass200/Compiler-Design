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

	@Override
	public String toString() {
		return "TokenID [key=" + key + " details=[lexeme="+ key +" type=id]]\n";
	}

}
