package LexicalAnalyzer;

public class Token {
	protected String name;
	protected String value;
	protected String type;
	protected boolean isIgnored;
	protected boolean isError;
	public int colNo;
	public int lineNo;
	
	public Token(String name, String value, String type, int lineNo, int colNo){
		this.name = name;
		this.value = value;
        this.type = type;
        this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
	}
	
	public Token(String name, String value, String type){
		this.name = name;
		this.value = value;
        this.type = type;
        this.isIgnored = false;
        this.isError = false;
        this.lineNo = 0;
        this.colNo = 0;
	}
	
	protected Token(String name, int lineNo, int colNo){
		this.name = name;
		this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
	}
	
	protected Token(String name, String type, int lineNo, int colNo){
		this.name = name;
		this.type = type;
		this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
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

	@Override
	public String toString() {
//		return "[["+name+"]" + this.lineNo + ":" + this.colNo + "]";
		return "[" + name + "]";
	}

	public boolean isIgnored() {
		return isIgnored;
	}

	public boolean isError() {
		return isError;
	}

	public int getColNo() {
		return colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
        
	
	
	
}


