package LexicalAnalyzer;

import LexicalAnalyzer.Token;
import java.util.List;

public class TokenVariable extends Token {
	
	
	
	public TokenVariable(String name){
		super(name, 0, 0);
		this.children = null;
		this.isNonTerminal = true;
	}
	
	public TokenVariable(String name,List<Token> children){
		super(name, 0, 0);
		this.children = children;
		for(int i = 0; i < this.noOfChild(); i++){
			children.get(i).setParent(this);
		}
		this.isNonTerminal = true;
	}
	
	public String printChild(){
		String s = new String();
		for(int i = 0; i < children.size(); i++){
			s = s + children.get(i).printTree();
		}
		return s;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	

	public List<Token> getChildren() {
		return children;
	}

	public void setChildren(List<Token> children) {
		this.children = children;
	}

}
