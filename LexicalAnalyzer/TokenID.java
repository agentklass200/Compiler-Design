package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenID extends Token {
	private String idName;
	private String idType;
	private String idValue;
	
	public TokenID(String idName, String idType){
		super("Identifier");
		this.idName = idName;
		this.idType = idType;
		idValue = null;
	}

}
