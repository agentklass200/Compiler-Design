package Parsing;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.supercsv.io.ICsvListReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class TableReader {

	public static String[][] CreateActionTable(String Filename) throws IOException{
		ICsvListReader actionCSV = null;
		try{
			actionCSV = new CsvListReader(new FileReader(Filename), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			List<String> actionListRow;
			List<String[]> actionListCol = new ArrayList<String[]>();
			String[] actionRow;
			while((actionListRow = actionCSV.read()) !=  null){
				actionRow = actionListRow.toArray(new String[actionListRow.size()]);
				for(int i = 0; i < actionRow.length; i++){
					if(actionRow[i] == null){
						actionRow[i] = "";
					}
				}
				actionListCol.add(actionRow);
			}
			String[][] actionTable = new String[actionListCol.size()][];
			for(int i = 0; i < actionTable.length; i++){
				actionTable[i] = actionListCol.get(i);
			}
			
			return actionTable;
		}
		finally{
			if(actionCSV != null){
				actionCSV.close();
			}
		}
	}
	
	public static int[][] CreateGoToTable(String Filename) throws IOException{
		ICsvListReader gotoCSV = null;
		try{
			gotoCSV = new CsvListReader(new FileReader(Filename), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			List<String> gotoListRow;
			List<int[]> gotoListCol = new ArrayList<int[]>();
			int[] gotoRow;
			while((gotoListRow = gotoCSV.read()) !=  null){
				gotoRow = new int[36];
				for(int i = 0; i < gotoListRow.size(); i++){
					if(gotoListRow.get(i) == null){
						gotoListRow.set(i, "-1");
					}
					gotoRow[i] = Integer.parseInt(gotoListRow.get(i));
				}
				gotoListCol.add(gotoRow);
			}
			int[][] gotoTable = new int[gotoListCol.size()][];
			for(int i = 0; i < gotoTable.length; i++){
				gotoTable[i] = gotoListCol.get(i);
			}
			
			return gotoTable;
		}
		finally{
			if(gotoCSV != null){
				gotoCSV.close();
			}
		}
	}
}
