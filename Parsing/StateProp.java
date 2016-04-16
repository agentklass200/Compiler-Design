package Parsing;

public class StateProp {
	private int stateNum;
	private int action;
	
	public static final int SHIFT = 0;
	public static final int REDUCE = 1;
	public static final int ERROR = -1;
	
	public StateProp(String s){
		if(s == ""){
			stateNum = -1;
			action = ERROR;
		}
		else{
			switch(s.substring(0, 1)){
				case "r":
					action = REDUCE;
					break;
				case "s":
					action = SHIFT;
					break;
				default:
					action = ERROR;
			}
			stateNum = Integer.parseInt(s.substring(1));
			
		}
		
	}

	public int getStateNum() {
		return stateNum;
	}

	public int getAction() {
		return action;
	}
}
