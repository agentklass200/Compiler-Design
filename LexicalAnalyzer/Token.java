package LexicalAnalyzer;

public class Token {
	protected String name;
	protected String value;
	protected String type;
	protected boolean isIgnored;
	protected boolean isError;
	
	public Token(String name, String value, String type){
		this.name = name;
		this.value = value;
        this.type = type;
        this.isIgnored = false;
        this.isError = false;
	}
	
	protected Token(String name){
		this.name = name;
		this.isIgnored = false;
        this.isError = false;
	}
	
	protected Token(String name, String type){
		this.name = name;
		this.type = type;
		this.isIgnored = false;
        this.isError = false;
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

	@Override
	public String toString() {
		return "["+name+"]";
	}

	public boolean isIgnored() {
		return isIgnored;
	}

	public boolean isError() {
		return isError;
	}
        
	
	
	
}


