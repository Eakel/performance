import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author linzhaoming
 *
 * 2013-3-26
 *
 */
public class TimePicker extends Dialog {

	protected Object result;
	protected Shell shell;

	public TimePicker(Shell parent, int style) {
		super(parent, style);
	}

	public TimePicker(Shell parent) {
		this(parent, SWT.NONE);
	}

	public TimePicker() {
		super(Display.getDefault().getActiveShell());
	}

	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(448, 201);
		shell.setText("Time Picker");
		shell.setLayout(new GridLayout(3, false));

		final DateTime calendar = new DateTime(shell, SWT.CALENDAR | SWT.BORDER);
		final DateTime date = new DateTime(shell, SWT.DATE | SWT.SHORT);
		final DateTime time = new DateTime(shell, SWT.TIME | SWT.SHORT);

		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText(" OK ");
		ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println(" Calendar date selected (MM/DD/YYYY) = "
						+ (calendar.getMonth() + 1) + " / " + calendar.getDay()
						+ " / " + calendar.getYear());
				System.out.println(" Date selected (MM/YYYY) = "
						+ (date.getMonth() + 1) + " / " + date.getYear());
				System.out.println(" Time selected (HH:MM) = "
						+ time.getHours() + " : " + time.getMinutes());

				System.out.println("(YYYY/MM/DD/HH:MM)" + calendar.getYear()
						+ "/" + (calendar.getMonth() + 1 )
						+ "/" + calendar.getDay() + "/" + time.getHours() + ":"
						+ time.getMinutes());
				shell.close();
			}
		});
		shell.setDefaultButton(ok);
	}

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Button open = new Button(shell, SWT.PUSH);
		open.setText(" Open Dialog ");
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final TimePicker dialog = new TimePicker();
				
				
				dialog.open();
			}
		});
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}