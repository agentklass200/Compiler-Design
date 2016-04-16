package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenVariable extends Token {
	
	public TokenVariable(String name){
		super(name, 0, 0);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
