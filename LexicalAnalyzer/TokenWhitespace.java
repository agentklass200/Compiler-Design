package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenWhitespace extends Token {
	private String key;
	
	public static final String SPACE_WHITESPACE = "SpaceWhitespace";
	public static final String NEWLINE_WHITESPACE = "NewLineWhitespace";
	public static final String RETURN_WHITESPACE = "ReturnCarriageWhitespace";
	public static final String COMMENT_WHITESPACE = "CommentWhitespace";
	public static final String TAB_WHITESPACE = "TabWhitespace";
	
	public TokenWhitespace(String type){
		super("Whitespace", type);
		this.key = null;
		super.isIgnored = true;
	}

	@Override
	public String toString() {
		return super.type;
	}

	public String getKey() {
		return key;
	}


}
