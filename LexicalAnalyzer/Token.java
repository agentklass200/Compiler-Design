package LexicalAnalyzer;

public class Token {
	protected String name;
	protected String value;
	protected String lexeme;
	protected String type;
	
	public Token(String name, String value, String type){
		this.name = name;
		this.value = value;
        this.type = type;
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

        public String getType() {
		return value;
	}

	public void setType(String type) {
		this.value = value;
	}
        
	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	
	
	
	
}


