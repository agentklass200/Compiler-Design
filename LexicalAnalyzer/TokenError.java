package LexicalAnalyzer;

import LexicalAnalyzer.Token;

public class TokenError extends Token {
	private String key;
	private String errorMessage;
	private int errorID;
	private String identifierError;
	
	public static final String SCANNER_ERROR = "ScannerErrorToken";
	public static final String PARSING_ERROR = "ParsingErrorToken";
	
	
	public TokenError(String key, int errorID, int lineNo, int colNo){
		super("ErrorToken", lineNo, colNo);
		this.key = key;
		super.isError = true;
		this.errorID = errorID;
		this.identifierError = "";
		this.val = this.key;
	}
	
	public TokenError(String key, int errorID, int lineNo, int colNo, String indentifierError){
		super("ErrorToken", lineNo, colNo);
		this.key = key;
		super.isError = true;
		this.errorID = errorID;
		this.identifierError = indentifierError;
		this.val = this.key;
	}
	
	public TokenError(String key, int errorID, int lineNo, int colNo, char c){
		super("ErrorToken", lineNo, colNo);
		this.key = key;
		super.isError = true;
		this.errorID = errorID;
		this.identifierError = "" + c;
		this.val = this.key;
	}
	
	public String genErrorMessage(){
		switch(this.key){
			case SCANNER_ERROR:
				switch(this.errorID){
					case 0: return "Invalid Token: Identifier name must not start with a digit.";
					case 1: return "Invalid Token: Identifier name not found.";
					case 2: return "Error: Comment not closed with \":*\".";
					case 3: return "Invalid Token! The token \"|\" is not valid.";
					case 4: return "Error: String Constant not closed with '\"'.";
					case 5: return "Invalid Token! Invalid Character Constant.";
					case 6: return "Invalid Token! Empty char.";
					case 7: return "Invalid Token! The token \""+ this.identifierError +"\" is not valid";
				}
			default:
				return "Error: 999";
		
		}
	}

	@Override
	public String toString() {
		return "\n\n==================================\n" + this.genErrorMessage() + "\n" + "Error at line no: " + this.getLineNo() + ", Col no: " + this.getColNo() + ".\n==================================\n\n";
	}

	public String getKey() {
		return key;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
