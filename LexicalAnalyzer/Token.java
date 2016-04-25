package LexicalAnalyzer;

import java.util.List;


public class Token {
	protected String name;
	public String value;
	public String type;
	public Object val = null;
	public String other;
	protected boolean isIgnored;
	protected boolean isError;
	public int colNo;
	public int lineNo;
	protected Token parent;
	protected List<Token> children;
	public boolean isNonTerminal;
	public int nodeOrder;
	public int dataType = -1;
	public int ruleNo = 0;
	private String key;
	public boolean isIdentifier = false;
	
	public static final int STRING = 0;
	public static final int DOUBLE = 1;
	public static final int INTEGER = 2;
	public static final int CHARACTER = 3;
	public static final int FLOAT = 4;
	public static final int BOOLEAN = 5;
	
	public Token(String name, String value, String type, int lineNo, int colNo){
		this.name = name;
		this.value = value;
        this.type = type;
        this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.isNonTerminal = false;
	}
	
	public Token(String name, String value, String type){
		this.name = name;
		this.value = value;
        this.type = type;
        this.isIgnored = false;
        this.isError = false;
        this.lineNo = 0;
        this.colNo = 0;
        this.isNonTerminal = false;
	}
	
	protected Token(String name, int lineNo, int colNo){
		this.name = name;
		this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.isNonTerminal = false;
	}
	
	protected Token(String name, String type, int lineNo, int colNo){
		this.name = name;
		this.type = type;
		this.isIgnored = false;
        this.isError = false;
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.isNonTerminal = false;
	}
	
	public int noOfChild(){
		if(children != null){
			return children.size();
		}
		return 0;
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
    
    public String printTree(){
    	if(this.noOfChild() == 0){
//			return "("+ this.getName() +")";
			return "["+ this.getName() +"]";
		}
		else{
//			return "("+ this.getName() + printChild() + ")";
			return "["+ this.getName() + printChild() + "]";
		}
    }
    
    private String printChild(){
		String s = new String();
		for(int i = 0; i < children.size(); i++){
			s = s + children.get(i).printTree();
		}
		return s;
	}
    
    public void printPostTraversal(){
    	for(int i = 0; i < this.noOfChild(); i++){
    		this.getChildren().get(i).printPostTraversal();
    	}
    	System.out.print("| " + this.name + " |");
    }
    
//    public void growNode(){
//    	int oldNodeOrder = this.nodeOrder;
//    	int parentNodeOrder = this.getParent().nodeOrder;
//    	List<Token> children  = this.getChildren();
//    	List<Token> siblings = this.getParent().getChildren();
//    	siblings.remove(oldNodeOrder);
//    	System.out.println(this.getParent());
//    	this.setParent(this.getParent().getParent());
//    	System.out.println(this.getParent());
//    	this.nodeOrder = parentNodeOrder;
//    	this.getParent().children.set(this.nodeOrder, this);
//    	this.nodeOrder = parentNodeOrder;
//    	if(this.noOfChild != 0){
//    		for(int i = 0; i < siblings.size(); i++){
//        		if(i == oldNodeOrder){
//        			for(int j = 0; j < children.size(); j++){
//        				siblings.add(i+j, children.get(j));
//        			}
//        		}
//        	}
//    	}
//    	
//    	for(int i = 0; i <siblings.size(); i++){
//			siblings.get(i).setParent(this);
//			siblings.get(i).nodeOrder = i;
//		}
//    	this.children = siblings;
//    	this.noOfChild = this.children.size();
//    }
    

	@Override
	public String toString() {
		return "["+ this.getName() +"]";
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

	public Token getParent() {
		return parent;
	}

	public void setParent(Token parent) {
		this.parent = parent;
	}

	public List<Token> getChildren() {
		return children;
	}

	public void setChildren(List<Token> children) {
		this.children = children;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	
	
}


