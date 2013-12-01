import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class BrowserTest {
	public static void main(String[] args) {
	    final Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    
	    StringBuffer statData = new StringBuffer();
	    statData.append("<br>" +
                "<table border=0><tr bgcolor=\"#dddddd\"><td><font face=System " +
                ">Overall Thread Count</td><td width=\"150\"></td><td><b><font face=System>test</font></b>");
	    Browser browser = new Browser(shell, SWT.NULL);
	    browser.setText(statData.toString());
	    
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
}
