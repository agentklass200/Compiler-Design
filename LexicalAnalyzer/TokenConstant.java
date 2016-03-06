package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenConstant extends Token {
	private String key;
	
	public TokenConstant(String name, String key, String type){
		super(name, key, type);
		this.setKey(key);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
	
}
