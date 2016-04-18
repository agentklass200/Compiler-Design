package LexicalAnalyzer;


import LexicalAnalyzer.Token;
import java.util.HashMap;

public class SymbolTable {
   
    private HashMap<String, Token> table = new HashMap<String, Token>();

    public SymbolTable(){
        table.put("+add",new Token("plus","+","Arithmetic Operator"));
        table.put("++",new Token("increment","++","Arithmetic Operator"));
        table.put("-sub",new Token("minus","-","Arithmetic Operator"));
        table.put("-neg",new Token("negative","-","Arithmetic Operator"));
        table.put("--",new Token("decrement","--","Arithmetic Operator"));
        table.put("*",new Token("times","*","Arithmetic Operator"));
        table.put("/",new Token("divide","/","Arithmetic Operator"));
        table.put("%",new Token("modulo","%","Arithmetic Operator"));

        table.put("=",new Token("assignment","=","Assignment Operator"));

        table.put("~",new Token("not","~","Relational Operator"));
        table.put("&&",new Token("and","&&","Relational Operator"));
        table.put("||",new Token("or","||","Relational Operator"));

        table.put("~=",new Token("notEqual","~=","Conditional Operator"));
        table.put("==",new Token("equal","==","Conditional Operator"));
        table.put(">",new Token("greaterThan",">","Conditional Operator"));
        table.put(">=",new Token("greaterThanOrEqual",">=","Conditional Operator"));
        table.put("<",new Token("lessThan","<","Conditional Operator"));
        table.put("<=",new Token("lessThanOrEqual","<=","Conditional Operator"));

        table.put(":",new Token("colon",":","Special Character"));
        table.put(",",new Token("comma",",","Special Character"));
        table.put(";",new Token("delimiter",";","Special Character"));
        table.put("(",new Token("openParen","(","Special Character"));//
        table.put(")",new Token("closeParen",")","Special Character"));
        table.put(">>",new Token("start",">>","Special Character"));
        table.put("<<",new Token("end","<<","Special Character"));
        table.put("&",new Token("conditionDelimiter","&","Special Character"));
        table.put("+con",new Token("concat","+","Special Character"));

        table.put("TRUE",new TokenConstant("true","TRUE","Constant"));
        table.put("FALSE",new TokenConstant("false","FALSE","Constant"));

        table.put("INT", new Token("integer", "INT", "Data Type"));
        table.put("BOOL", new Token("boolean", "BOOL", "Data Type"));
        table.put("STR", new Token("string", "STR", "Data Type"));
        table.put("CHAR", new Token("character", "CHAR", "Data Type"));
        table.put("FLOAT", new Token("float", "FLOAT", "Data Type"));

        table.put("OBTAIN", new Token("input", "OBTAIN", "Input and Output"));
        table.put("REVEAL", new Token("output", "REVEAL", "Input and Output"));

        table.put("IF", new Token("if", "IF", "Conditional Statements"));        
        table.put("ELSEIF", new Token("elseIf", "ELSEIF", "Conditional Statements"));
        table.put("ELSE", new Token("else", "ELSE", "Conditional Statements"));
        table.put("ENDIF", new Token("endIf", "ENDIF", "Conditional Statements"));   
        
        table.put("WHILE", new Token("whileLoop", "WHILE", "Loop"));   
        table.put("LAPSE", new Token("forLoop", "LAPSE", "Loop"));
        
        table.put("INITIATE", new Token("initiate", "INITIATE", "Division Statement"));   
        table.put("TERMINATE", new Token("terminate", "TERMINATE", "Division Statement"));  
        
        table.put("TRANSFORMTO", new Token("convert", "TRANSFORMTO", "Conversion Statement"));
    }

	public HashMap<String, Token> getTable() {
		return table;
	}
        
}
