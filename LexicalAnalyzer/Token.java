package LexicalAnalyzer;

public class Token {
	protected String name;
	protected String value;
	protected String lexeme;
	
	public Token(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public Token(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	
	
	
	
}


