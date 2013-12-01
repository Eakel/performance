//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.eclipse.jface.viewers.DoubleClickEvent;
//import org.eclipse.jface.viewers.IDoubleClickListener;
//import org.eclipse.jface.viewers.ISelectionChangedListener;
//import org.eclipse.jface.viewers.SelectionChangedEvent;
//import org.eclipse.jface.viewers.TreeSelection;
//import org.eclipse.jface.viewers.TreeViewer;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//
//import com.easyfun.eclipse.common.tree.TreeContentProvider;
//import com.easyfun.eclipse.component.trace.builder.TraceBuilder;
//import com.easyfun.eclipse.component.trace.model.AppTrace;
//import com.easyfun.eclipse.component.trace.model.ITrace;
//import com.easyfun.eclipse.component.trace.ui.ITraceComposite;
//import com.easyfun.eclipse.component.trace.ui.TraceDialog;
//import com.easyfun.eclipse.component.trace.ui.TraceTreeLabelProvider;
//import com.easyfun.eclipse.component.trace.ui.TraceUIFactory;
//import com.easyfun.eclipse.utils.ui.SWTUtil;
//
//
//public class TraceTest {
//	
//	public static void main(String[] args) throws Exception{
//	    final Display display = new Display();
//	    final Shell shell = new Shell(display);
//	    shell.setLayout(new FillLayout());
//	    
//	    Button createSqlButton = new Button(shell, SWT.NULL);
//		createSqlButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
//		createSqlButton.setText("Éú³Ésql");
//		
//	    
//	    createSqlButton.addSelectionListener(new SelectionAdapter(){
//	    	public void widgetSelected(SelectionEvent e) {
//	    		try {
//					TraceDialog dis = new TraceDialog(shell, new FileInputStream("example.xml"));
//					dis.open();
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//	    	}
//	    });
//
//			    
//	    shell.open();
//	    while (!shell.isDisposed()) {
//	      if (!display.readAndDispatch())
//	        display.sleep();
//	    }
//	    display.dispose();
//	}
//
//}
