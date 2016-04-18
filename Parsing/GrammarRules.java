package Parsing;


public class GrammarRules {
	private String name;
	private int ruleNo;
	private int childNo;
	private String info;
	
	public GrammarRules(String name, String info, int ruleNo, int childNo){
		this.name = name;
		this.ruleNo = ruleNo;
		this.childNo = childNo;
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

	public String getInfo() {
		return info;
	}
	
}
