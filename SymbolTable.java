
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
