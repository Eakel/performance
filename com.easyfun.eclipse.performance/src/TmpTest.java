import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class TmpTest {
	public static void main(String[] args) throws Exception{
		File file = new File("D:\\1.gz");
		GZIPInputStream stream = new GZIPInputStream(new FileInputStream(file));
		System.out.println(stream);
		List<String> readLines = IOUtils.readLines(stream);
		StringBuffer sb = new StringBuffer();
		for (String str : readLines) {
			sb.append(str);
		}
		// System.out.println(readLines.get(0));

		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
	    
//	    StringBuffer statData = new StringBuffer();
//	    statData.append("<br>" +
//                "<table border=0><tr bgcolor=\"#dddddd\"><td><font face=System " +
//                ">Overall Thread Count</td><td width=\"150\"></td><td><b><font face=System>test</font></b>");
		Browser browser = new Browser(shell, SWT.NULL);
		browser.setText(sb.toString());

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
			stream.close();
		}
		display.dispose();
	}
}
