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
   
    public HashMap<String, Token> table = new HashMap<String, Token>();

    public SymbolTable(){
       table.put("Add",new Token("Add","+","Arithmetic Operator"));
       table.put("Increment",new Token("Increment","++","Arithmetic Operator"));
       table.put("Subtract",new Token("Subtract","-","Arithmetic Operator"));
       table.put("Decrement",new Token("Decrement","--","Arithmetic Operator"));
       table.put("Multiply",new Token("Multiply","*","Arithmetic Operator"));
       table.put("Divide",new Token("Divide","/","Arithmetic Operator"));
       table.put("Modulo",new Token("Modulo","%","Arithmetic Operator"));
       
       table.put("Assignment",new Token("Assignment","=","Assignment Operator"));
       
       table.put("Not",new Token("Not","~","Relational Operator"));
       table.put("And",new Token("And","&&","Relational Operator"));
       table.put("Or",new Token("Or","||","Relational Operator"));
       
       table.put("NotEqual",new Token("NotEqual","~=","Conditional Operator"));
       table.put("Equal",new Token("Equal","==","Conditional Operator"));
       table.put("GreaterThan",new Token("GreaterThan",">","Conditional Operator"));
       table.put("GreaterThanOrEqual",new Token("GreaterThanOrEqual",">=","Conditional Operator"));
       table.put("LessThan",new Token("LessThan","<","Conditional Operator"));
       table.put("LessThanOrEqual",new Token("LessThanOrEqual","<=","Conditional Operator"));
      
       table.put("StartComment",new Token("StartComment","*:","Special Character"));
       table.put("EndComment",new Token("EndComment",":*","Special Character"));
       table.put("Delimiter",new Token("Delimiter",";","Special Character"));
       table.put("OpenParen",new Token("OpenParen","(","Special Character"));
       table.put("CloseParen",new Token("CloseParen",")","Special Character"));
       table.put("OpenCurl",new Token("OpenCurl","{","Special Character"));
       table.put("CloseCurl",new Token("CloseCurl","}","Special Character"));
       table.put("Start",new Token("Start",">>","Special Character"));
       table.put("End",new Token("End","<<","Special Character"));
       table.put("DouQuot",new Token("DouQuot","\"","Special Character"));
       table.put("SingQuot",new Token("SingQuot","'","Special Character"));
       table.put("Concat",new Token("Concat","+","Special Character"));
       
       table.put("TrueBoolean",new Token("TrueBoolean","TRUE","Constant"));
       table.put("FalseBoolean",new Token("FalseBoolean","FALSE","Constant"));
       
       table.put("Integer", new Token("Integer", "INT", "Data Type"));
        table.put("Boolean", new Token("Boolean", "BOOL", "Data Type"));
        table.put("String", new Token("String", "STR", "Data Type"));
        table.put("Character", new Token("Character", "CHAR", "Data Type"));
        table.put("Float", new Token("Float", "FLOAT", "Data Type"));
        
        table.put("Input", new Token("Input", "OBTAIN", "Input and Output"));
        table.put("Output", new Token("Output", "REVEAL", "Input and Output"));
        
        table.put("If", new Token("If", "IF", "Conditional Statements"));        
        table.put("ElseIf", new Token("ElseIf", "ELSEIF", "Conditional Statements"));
        table.put("Else", new Token("Else", "ELSE", "Conditional Statements"));
        table.put("EndIf", new Token("EndIf", "ENDIF", "Conditional Statements"));                                                
       
    }
        
}
