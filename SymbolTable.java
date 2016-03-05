
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
   }
    
    
    
}
