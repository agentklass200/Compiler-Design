package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenConstant extends Token {
	private String key;
	
	public static final String STRING_CONSTANT = "StringConstant";
	public static final String INTEGER_CONSTANT = "IntegerConstant";
	public static final String FLOATING_CONSTANT = "FloatingConstant";
	public static final String CHAR_CONSTANT = "CharConstant";
	
	public TokenConstant(String name, String key, String type ,int lineNo, int colNo){
		super(name, key, type, lineNo, colNo);
		this.setKey(key);
	}
	
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
