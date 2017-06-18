package core.util;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	public static void writeLine(Writer w, List<String> values) throws Exception{
		boolean firstVal = true;
		for (String val : values){
			if(!firstVal){
				w.write(",");
			}
			w.write("\"");
			for (int i = 0; i < val.length(); i++){
				char ch = val.charAt(i);
				if(ch == '\"'){
					w.write("\""); //Double quotes
				}
				w.write(ch);
			}
			w.write("\"");
			firstVal = false;
		}
		w.write("\n");
	}
	
	public static List<String> parseLine(Reader r) throws Exception{
		int ch = r.read();
		while (ch == '\r') {
			ch = r.read();
		}
		
		if (ch < 0) {
			return null;
		}
		ArrayList<String> store = new ArrayList<String>();
		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean started = false;
		while (ch >= 0){
			if (inQuotes){
				started = true;
				if (ch == '\"') {
					inQuotes = false;
				} else {
					curVal.append((char) ch);
				}
			} else { 
				if (ch == '\"'){
					inQuotes = true;
					if(started) {
						curVal.append('\"');
					}
				} else if (ch == ','){
					store.add(curVal.toString());
					curVal = new StringBuffer();
					started = false;
				} else if (ch == '\r'){
					//Ignore
				} else if (ch == '\n'){
					//EOL, break
					break;
				} else {
					curVal.append((char) ch);
				}
			}
			ch = r.read();
		}
		store.add(curVal.toString());
		return store;
	}
}
