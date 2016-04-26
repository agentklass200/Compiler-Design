import java.io.IOException;
import java.text.DecimalFormat;

import Parsing.Parser;
import LexicalAnalyzer.*;
import java.util.*;


// Main Class
public class Compiler {
	static Parser parse = null;
	static boolean isError = false;
	static boolean pass = false;
	static boolean loop = false;
	public static void main(String[] args) throws IOException {
		String fileName = "src/simpleSample.txt";
		parse = new Parser(fileName);
		if(parse.recoveryMode){
			System.out.println();
			System.out.println("No More Valid Tokens or Variables to Continue!");
		}
		else{
			System.out.println();
			System.out.println("Parsing Tree");
			System.out.println("========================");
			System.out.println(parse.getParsingTreeRoot().printTree());
			System.out.println();
			System.out.println("Program!!");
			System.out.println("========================");
			evaluateTree(parse.getParsingTreeRoot(), parse);
		}
		
		
	}
	
	public static void  evaluateTree(Token node, Parser parse){

		if(!isError){
			switch(node.ruleNo){
				case 20: //CONDITIONAL
					evaluateTree(node.getChildren().get(2), parse); //EVALUATE EXPR
					if((boolean)node.getChildren().get(2).val){
						evaluateTree(node.getChildren().get(3), parse); //SCOPE
					}
					else{
						evaluateTree(node.getChildren().get(4), parse); //ELSESTMT
					}
					pass = true;
					break;
				case 21:
					evaluateTree(node.getChildren().get(2), parse); //EVALUATE EXPR
					if((boolean)node.getChildren().get(2).val){
						evaluateTree(node.getChildren().get(3), parse); //SCOPE
					}
					else{
						evaluateTree(node.getChildren().get(4), parse); //ELSESTMT
					}
					pass = true;
					break;
				case 22:
					evaluateTree(node.getChildren().get(1), parse); //SCOPE
					pass = true;
					break;
				case 28:
					evaluateTree(node.getChildren().get(2), parse); //EVALUATE EXPR
					if((boolean)node.getChildren().get(2).val){
						evaluateTree(node.getChildren().get(3), parse); //SCOPE
						evaluateTree(node, parse); //Loop
					}
					pass = true;
					break;
				case 29:
					if(!loop){
						evaluateTree(node.getChildren().get(2), parse); //EVALUATE ASSIGNMENT
					}
					evaluateTree(node.getChildren().get(4), parse); // CONDITION
					if((boolean)node.getChildren().get(4).val){
						evaluateTree(node.getChildren().get(7), parse); //SCOPE
						evaluateTree(node.getChildren().get(6), parse); //SCOPE
						loop = true;
						evaluateTree(node, parse); //Loop
					}
					loop = false;
					pass = true;
					break;
					
			}
		}
		if(!pass){
			for(int i = 0; i < node.noOfChild(); i++){
				evaluateTree(node.getChildren().get(i), parse);	
			}
		}
		else{
			pass = false;
		}
		
//		System.out.println(node.ruleNo + " - " + node.getName());
		//EVALUATE
		if(node.noOfChild() > 0 && !isError){
			switch(node.ruleNo){
				case 78:
					node.val = false;
					node.dataType = Token.BOOLEAN;
					break;
				case 77:
					node.val = true;
					node.dataType = Token.BOOLEAN;
					break;
				case 76:
					node.val = Float.parseFloat(node.getChildren().get(0).value);
					node.dataType = Token.INTEGER;
					break;
				case 75:
					node.val = Float.parseFloat(node.getChildren().get(0).value);
					node.dataType = Token.FLOAT;
					break;
				case 74:
				case 70:
				case 66:
				case 62:
				case 57:
				case 52:
				case 46:
				case 42:
				case 38:
					node.val = node.getChildren().get(0).val;
					node.dataType = node.getChildren().get(0).dataType;
					node.isIdentifier = node.getChildren().get(0).isIdentifier;
					node.setKey(node.getChildren().get(0).getKey());
					break;
				case 36:
					switch(node.getChildren().get(0).dataType){
					case Token.INTEGER:
						node.val = Float.parseFloat(node.getChildren().get(0).val.toString());
						break;
					case Token.BOOLEAN:
						node.val = (boolean)node.getChildren().get(0).val;
						break;
					case Token.CHARACTER:
						node.val = (char)node.getChildren().get(0).val;
						break;
					case Token.FLOAT:
						node.val = Float.parseFloat(node.getChildren().get(0).val.toString());
						break;
					case Token.STRING:
						node.val = node.getChildren().get(0).val.toString();
						break;
					default:
						node.val = node.getChildren().get(0).val;
					}
					node.dataType = node.getChildren().get(0).dataType;
					node.isIdentifier = node.getChildren().get(0).isIdentifier;
					break;
				case 73:
					node.val = getCharVal(node.getChildren().get(0).value);
					node.dataType = Token.CHARACTER;
					break;
				case 72:
					node.val = getStringVal(node.getChildren().get(0).value);
					node.dataType = Token.STRING;
					break;
				case 71:
					node.val = node.getChildren().get(1).val;
					node.dataType = node.getChildren().get(1).dataType;
					break;
				case 69:
					if(parse.getIdMaps().get(node.getChildren().get(0).getKey()).val == null || parse.getIdMaps().get(node.getChildren().get(0).getKey()).dataType == -1){
						System.out.println("The ID " + parse.getIdMaps().get(node.getChildren().get(0).getKey()).getKey() + " is not defined!" );
						isError = true;
					}
					else{
						if(parse.getIdMaps().get(node.getChildren().get(0).getKey()).dataType == Token.INTEGER){
							node.val = Float.parseFloat(parse.getIdMaps().get(node.getChildren().get(0).getKey()).val.toString());
						}
						else{
							node.val = parse.getIdMaps().get(node.getChildren().get(0).getKey()).val;
						}
						node.setKey(node.getChildren().get(0).getKey());
						node.dataType = parse.getIdMaps().get(node.getChildren().get(0).getKey()).dataType;
						node.isIdentifier = true;
					}
					break;
				case 68:
				case 67:
				case 64:
				case 63:
				case 60:
				case 59:
				case 58:
				case 55:
				case 54:
				case 53:
				case 50:
				case 49:
				case 48:
				case 47:
				case 44:
				case 43:
				case 40:
				case 39:
					node.other = node.getChildren().get(0).getName();
					break;
				case 65:
					switch(node.getChildren().get(1).other){
						case "increment":
							if(node.getChildren().get(0).isIdentifier){
								if(node.getChildren().get(0).dataType == Token.INTEGER){
									node.val = parse.getIdMaps().get(node.getChildren().get(0).getKey()).val;
									parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = Float.parseFloat(parse.getIdMaps().get(node.getChildren().get(0).getKey()).val.toString()) +  1;
									node.dataType = node.getChildren().get(0).dataType;
								}
								else if(node.getChildren().get(0).dataType == Token.FLOAT){
									node.val = parse.getIdMaps().get(node.getChildren().get(0).getKey()).val;
									parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = (float)parse.getIdMaps().get(node.getChildren().get(0).getKey()).val +  1;
									node.dataType = node.getChildren().get(0).dataType;
								}
							}
							break;
						case "decrement":
							if(node.getChildren().get(0).isIdentifier){
								if(node.getChildren().get(0).dataType == Token.INTEGER){
									node.val = parse.getIdMaps().get(node.getChildren().get(0).getKey()).val;
									parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = (float)parse.getIdMaps().get(node.getChildren().get(0).getKey()).val -  1;
									node.dataType = node.getChildren().get(0).dataType;
								}
								else if(node.getChildren().get(0).dataType == Token.FLOAT){
									node.val = parse.getIdMaps().get(node.getChildren().get(0).getKey()).val;
									parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = (float)parse.getIdMaps().get(node.getChildren().get(0).getKey()).val -  1;
									node.dataType = node.getChildren().get(0).dataType;
								}
							}
							break;
						default:
							System.out.println("ERROR! Invalid operator");
							System.out.println(node.ruleNo + " " + node.getChildren().get(1).other);
						
					}
					break;
				case 61:
					switch(node.getChildren().get(0).other){
						case "negative":
							if(node.getChildren().get(1).dataType == Token.INTEGER){
								node.val = -(int)node.getChildren().get(1).val;
								node.dataType = node.getChildren().get(1).dataType;
							}
							else if(node.getChildren().get(1).dataType == Token.FLOAT){
								node.val = -(float)node.getChildren().get(1).val;
								node.dataType = node.getChildren().get(1).dataType;
							}
							else{
								System.out.println("ERROR! The type is not a number type.");
								isError = true;
							}
							break;
						case "not":
							if(node.getChildren().get(1).dataType == Token.BOOLEAN){
								node.val = !(boolean)node.getChildren().get(1).val;
								node.dataType = node.getChildren().get(1).dataType;
							}
							else{
								System.out.println("ERROR! The type is not a boolean type.");
							}
						default:
							System.out.println("ERROR! Invalid operator");
							System.out.println(node.ruleNo);
							
					}
					break;
				case 56:
					switch(node.getChildren().get(1).other){
					case "multiply":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) * (float)getVal(node.getChildren().get(2));
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "divide":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) / (float)getVal(node.getChildren().get(2));
							node.dataType = Token.FLOAT;
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "modulo":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) % (float)getVal(node.getChildren().get(2));
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
					default:
						System.out.println("ERROR! Invalid operator");
						System.out.println(node.ruleNo + " " + node.getChildren().get(1).other);
						
					}
					break;
				case 51:
					switch(node.getChildren().get(1).other){
					case "add":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) + (float)getVal(node.getChildren().get(2));
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else if((node.getChildren().get(0).dataType == Token.STRING || node.getChildren().get(0).dataType == Token.CHARACTER) || (node.getChildren().get(2).dataType == Token.STRING ||  node.getChildren().get(2).dataType == Token.CHARACTER)){
							node.val = getVal(node.getChildren().get(0)).toString() + getVal(node.getChildren().get(2)).toString();
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else{
							System.out.println("ERROR! The type is not a number or string type.");
							isError = true;
						}
						break;
					case "subtract":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) - (float)getVal(node.getChildren().get(2));
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "concat":
						if((node.getChildren().get(0).dataType == Token.STRING || node.getChildren().get(0).dataType == Token.CHARACTER) || (node.getChildren().get(2).dataType == Token.STRING || node.getChildren().get(0).dataType == Token.CHARACTER)){
							node.val = getVal(node.getChildren().get(0)).toString() + getVal(node.getChildren().get(2)).toString();
							node.dataType = getGreaterDataType(node.getChildren().get(0), node.getChildren().get(2));
						}
						else{
							System.out.println("ERROR! The type is not a string type.");
							isError = true;
						}
						break;
					default:
						System.out.println("ERROR! Invalid operator");
						System.out.println(node.ruleNo + " " + node.getChildren().get(1).other);
						
					}
					break;
				case 45:
					switch(node.getChildren().get(1).other){
					case "lessThan":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) < (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "lessThanOrEqual":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) <= (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "greaterThan":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) < (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					case "greaterThanOrEqual":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) <= (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number type.");
							isError = true;
						}
						break;
					default:
						System.out.println("ERROR! Invalid operator");
						System.out.println(node.ruleNo);
						
					}
					break;
				case 41:
					switch(node.getChildren().get(1).other){
					case "equal":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) == (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else if((node.getChildren().get(0).dataType == Token.STRING) || (node.getChildren().get(2).dataType == Token.STRING)){
							node.val = getVal(node.getChildren().get(0)).toString() == getVal(node.getChildren().get(2)).toString();
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number or string type.");
							isError = true;
						}
						break;
					case "notEqual":
						if((node.getChildren().get(0).dataType == Token.INTEGER || node.getChildren().get(0).dataType == Token.FLOAT)
								&& (node.getChildren().get(2).dataType == Token.INTEGER || node.getChildren().get(2).dataType == Token.FLOAT)
								){
							node.val = (float)getVal(node.getChildren().get(0)) != (float)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else if((node.getChildren().get(0).dataType == Token.STRING) || (node.getChildren().get(2).dataType == Token.STRING)){
							node.val = getVal(node.getChildren().get(0)).toString() != getVal(node.getChildren().get(2)).toString();
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a number or string type.");
							isError = true;
						}
						break;
					default:
						System.out.println("ERROR! Invalid operator");
						System.out.println(node.ruleNo);
					}
					break;
				case 37:
					switch(node.getChildren().get(1).other){
					case "and":
						if((node.getChildren().get(0).dataType == Token.BOOLEAN) || (node.getChildren().get(2).dataType == Token.BOOLEAN)){
							node.val = (boolean)getVal(node.getChildren().get(0)) && (boolean)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a boolean type.");
							isError = true;
						}
						break;
					case "or":
						if((node.getChildren().get(0).dataType == Token.BOOLEAN) || (node.getChildren().get(2).dataType == Token.BOOLEAN)){
							node.val = (boolean)getVal(node.getChildren().get(0)) || (boolean)getVal(node.getChildren().get(2));
							node.dataType = Token.BOOLEAN;
						}
						else{
							System.out.println("ERROR! The type is not a boolean type.");
							isError = true;
						}
						break;
					default:
						System.out.println("ERROR! Invalid operator");
						System.out.println(node.ruleNo);
					}
					break;
				case 16:
					System.out.print(node.getChildren().get(2).val);
					break;
				case 30:
					if(parse.getIdMaps().get(node.getChildren().get(0).getKey()).dataType == -1){
						parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = node.getChildren().get(2).val;
						node.dataType = node.getChildren().get(2).dataType;
						node.setKey(node.getChildren().get(0).getKey());
					}
					else{
						if(isDataTypeSame(parse.getIdMaps().get(node.getChildren().get(0).getKey()), node.getChildren().get(2) )){
							parse.getIdMaps().get(node.getChildren().get(0).getKey()).val = node.getChildren().get(2).val;
						}
						else{
							System.out.println("ERROR: Type Mismatched!");
							isError = true;
						}
					}
					break;
				case 24:
					node.dataType = node.getChildren().get(0).dataType;
					node.setKey(node.getChildren().get(0).getKey());
					break;
				case 26:
					if(node.dataType != -1){
						if(node.getChildren().get(1).ruleNo == 24){
							if(node.dataType != node.getChildren().get(1).dataType){
								if(node.dataType == Token.STRING || node.dataType == Token.CHARACTER){
									parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType = node.dataType;
									node.getChildren().get(1).getChildren().get(1).dataType = node.dataType;
									evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
								}
								else if(node.dataType == Token.FLOAT && node.dataType == Token.INTEGER){
									parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType =  node.dataType;
									node.getChildren().get(1).getChildren().get(1).dataType = node.dataType;
									evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
								}
								else{
									System.out.println("ERROR! The declared id value is type mismatched.");
									isError = true;
								}
								
							}
							else{
								parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType =  node.dataType;
								node.getChildren().get(1).getChildren().get(1).dataType = node.dataType;
								evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
							}
						}
						else{
							parse.getIdMaps().get(node.getChildren().get(1).getChildren().get(0).getKey()).dataType = node.dataType;
							node.getChildren().get(1).getChildren().get(1).dataType = node.dataType;
							evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
							
						}
					}
					break;
				case 12:
					if(node.getChildren().get(1).ruleNo == 24){
						if(node.getChildren().get(0).dataType != node.getChildren().get(1).dataType){
							if(node.getChildren().get(0).dataType == Token.STRING || node.getChildren().get(0).dataType == Token.CHARACTER){
								parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType =  node.getChildren().get(0).dataType;
								node.getChildren().get(1).getChildren().get(1).dataType = node.getChildren().get(0).dataType;
								evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
							}
							else if(node.getChildren().get(0).dataType == Token.FLOAT && node.getChildren().get(1).dataType == Token.INTEGER){
								parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType =  node.getChildren().get(0).dataType;
								node.getChildren().get(1).getChildren().get(1).dataType = node.getChildren().get(0).dataType;
								evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
							}
							else{
								System.out.println("ERROR! The declared id value is type mismatched.");
								isError = true;
							}
							
						}
						else{
							parse.getIdMaps().get(node.getChildren().get(1).getKey()).dataType =  node.getChildren().get(0).dataType;
							node.getChildren().get(1).getChildren().get(1).dataType = node.getChildren().get(0).dataType;
							evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
						}
					}
					else{
						parse.getIdMaps().get(node.getChildren().get(1).getChildren().get(0).getKey()).dataType = node.getChildren().get(0).dataType;
						node.getChildren().get(1).getChildren().get(1).dataType = node.getChildren().get(0).dataType;
						evaluateTree(node.getChildren().get(1).getChildren().get(1), parse);
						
					}
					break;
				case 31:
				case 32:
				case 33:
				case 34:
				case 35:
					node.dataType = convertDT(node.getChildren().get(0).getName());
					break;
				case 15:
					Scanner scan = new Scanner(System.in);
					Object o = scan.nextLine();
					
					if(parse.getIdMaps().get(node.getChildren().get(2).getKey()).dataType != -1){
						switch(parse.getIdMaps().get(node.getChildren().get(2).getKey()).dataType){
						case Token.INTEGER:
							if(isNumeric(o.toString())){
								parse.getIdMaps().get(node.getChildren().get(2).getKey()).val = Integer.parseInt(o.toString());
							}
							else{
								System.out.println("ERROR! the input is non-numeric!");
							}
							break;
						case Token.FLOAT:
							if(isNumeric(o.toString())){
								parse.getIdMaps().get(node.getChildren().get(2).getKey()).val = Float.parseFloat(o.toString());
							}
							else{
								System.out.println("ERROR! the input is non-numeric!");
							}
							break;
						case Token.STRING:
							parse.getIdMaps().get(node.getChildren().get(2).getKey()).val = o.toString();
							break;
						default:
							System.out.println("ERROR! Invalid datatype for input.");
						}
						
					}
					else{
						System.out.println("ERROR! the identifier is not yet defined!");
						isError = true;
					}
					break;
				case 19:
					parse.getIdMaps().get(node.getChildren().get(0).getKey()).dataType = convertDT(node.getChildren().get(2).getName());
					break;
				default:
				
			}
		}
	}
	
	public static boolean isDataTypeSame(Token t1, Token t2){
		return t1.dataType == t2.dataType;
	}
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static int getGreaterDataType(Token t1, Token t2){
		if(t1.dataType == Token.BOOLEAN || t2.dataType == Token.BOOLEAN){
			return Token.BOOLEAN;
		}
		else if(t1.dataType == Token.STRING || t2.dataType == Token.STRING){
			return Token.STRING ;
		}
		else if(t1.dataType == Token.FLOAT || t2.dataType == Token.FLOAT){
			return Token.FLOAT ;
		}
		return Token.INTEGER;
	}
	
	public static int convertDT(String s){
		if(s == "string"){
			return Token.STRING;
		}
		else if(s == "integer"){
			return Token.INTEGER;
		}
		else if(s == "float"){
			return Token.FLOAT;
		}
		else if(s == "boolean"){
			return Token.BOOLEAN;
		}
		else if(s == "character"){
			return Token.CHARACTER;
		}
		else{
			return -1;
		}
	}
	
	public static Object getVal(Token t){
		return t.val;
	}
	
	public static String getStringVal(String s){
		return s.substring(1, s.length()-1);
	}
	
	public static char getCharVal(String s){
		char[] cA = s.toCharArray();
		return cA[1];
	}
}
