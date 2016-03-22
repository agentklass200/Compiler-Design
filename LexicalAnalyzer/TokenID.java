package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenID extends Token {
	private String key;
	private String idType;
	private String idValue;
	
	public TokenID(String key, int lineNo, int colNo){
		super("Identifier", lineNo, colNo);
		this.setKey(key);
		this.setIdType(null);
		setIdValue(null);
	}
	
	public TokenID(String key){
		super("Identifier", 0,0);
		this.setKey(key);
		this.setIdType(null);
		setIdValue(null);
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
//		return "[" + key  + " " + this.getLineNo() + ":" + this.getColNo() + "]";
		return key;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

}
