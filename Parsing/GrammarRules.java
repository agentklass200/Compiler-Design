package Parsing;

import LexicalAnalyzer.TokenVariable;

public class GrammarRules {
	private String name;
	private int ruleNo;
	private int childNo;
	private TokenVariable var;
	private String info;
	
	public GrammarRules(String name, String info, int ruleNo, int childNo){
		this.name = name;
		this.ruleNo = ruleNo;
		this.childNo = childNo;
		this.var = new TokenVariable(name);
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public int getRuleNo() {
		return ruleNo;
	}

	public int getChildNo() {
		return childNo;
	}

	public TokenVariable getVar() {
		return var;
	}

	public String getInfo() {
		return info;
	}
	
}
