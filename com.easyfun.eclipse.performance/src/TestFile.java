import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class TestFile {
	public static void main(String[] args) throws Exception{
		File file = new File("d:\\test.txt");
		BufferedReader br  = new BufferedReader(new FileReader(file));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
		List lineList = new ArrayList();
		while (br.ready()) {
			String line = br.readLine();
			lineList.add(line);
		}
		br.close();
		System.out.println(file.delete());
	}
}
