import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String args[]) throws FileNotFoundException, Exception{
		new Test();
	}
	public Test() throws FileNotFoundException, Exception{
		List<String> list = new ArrayList<>();
		list = core.util.CSVParser.parseLine(new FileReader(new File("test.csv")));
		for(int i = list.size() - 1; i >= 0; i--){
			String s = list.get(i);
			System.out.println("["+i+"] "+s);
		}
	}
}
