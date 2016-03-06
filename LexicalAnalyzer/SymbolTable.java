package LexicalAnalyzer;


import LexicalAnalyzer.Token;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jplucido
 */
public class SymbolTable {
    //HI
   
    private HashMap<String, Token> table = new HashMap<String, Token>();

    public SymbolTable(){
        table.put("+1",new Token("Add","+","Arithmetic Operator"));
        table.put("++",new Token("Increment","++","Arithmetic Operator"));
        table.put("-",new Token("Subtract","-","Arithmetic Operator"));
        table.put("--",new Token("Decrement","--","Arithmetic Operator"));
        table.put("*",new Token("Multiply","*","Arithmetic Operator"));
        table.put("/",new Token("Divide","/","Arithmetic Operator"));
        table.put("%",new Token("Modulo","%","Arithmetic Operator"));

        table.put("=",new Token("Assignment","=","Assignment Operator"));

        table.put("~",new Token("Not","~","Relational Operator"));
        table.put("&&",new Token("And","&&","Relational Operator"));
        table.put("||",new Token("Or","||","Relational Operator"));

        table.put("~=",new Token("NotEqual","~=","Conditional Operator"));
        table.put("==",new Token("Equal","==","Conditional Operator"));
        table.put(">",new Token("GreaterThan",">","Conditional Operator"));
        table.put(">=",new Token("GreaterThanOrEqual",">=","Conditional Operator"));
        table.put("<",new Token("LessThan","<","Conditional Operator"));
        table.put("<=",new Token("LessThanOrEqual","<=","Conditional Operator"));

        table.put("*:",new Token("StartComment","*:","Special Character"));
        table.put(":*",new Token("EndComment",":*","Special Character"));
        table.put(";",new Token("Delimiter",";","Special Character"));
        table.put("(",new Token("OpenParen","(","Special Character"));
        table.put(")",new Token("CloseParen",")","Special Character"));
        table.put("{",new Token("OpenCurl","{","Special Character"));
        table.put("}",new Token("CloseCurl","}","Special Character"));
        table.put(">>",new Token("Start",">>","Special Character"));
        table.put("<<",new Token("End","<<","Special Character"));
        table.put("\"",new Token("DouQuot","\"","Special Character"));
        table.put("'",new Token("SingQuot","'","Special Character"));
        table.put("+2",new Token("Concat","+","Special Character"));

        table.put("TRUE",new Token("TrueBoolean","TRUE","Constant"));
        table.put("FALSE",new Token("FalseBoolean","FALSE","Constant"));

        table.put("INT", new Token("Integer", "INT", "Data Type"));
        table.put("BOOL", new Token("Boolean", "BOOL", "Data Type"));
        table.put("STR", new Token("String", "STR", "Data Type"));
        table.put("CHAR", new Token("Character", "CHAR", "Data Type"));
        table.put("FLOAT", new Token("Float", "FLOAT", "Data Type"));

        table.put("OBTAIN", new Token("Input", "OBTAIN", "Input and Output"));
        table.put("REVEAL", new Token("Output", "REVEAL", "Input and Output"));

        table.put("IF", new Token("If", "IF", "Conditional Statements"));        
        table.put("ELSEIF", new Token("ElseIf", "ELSEIF", "Conditional Statements"));
        table.put("ELSE", new Token("Else", "ELSE", "Conditional Statements"));
        table.put("ENDIF", new Token("EndIf", "ENDIF", "Conditional Statements"));   
        
        table.put("WHILE", new Token("WhileLoop", "WHILE", "Loop"));   
        table.put("LAPSE", new Token("ForLoop", "LAPSE", "Loop"));   
    }

	public HashMap<String, Token> getTable() {
		return table;
	}
        
}
