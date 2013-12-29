package com.easyfun.eclipse.performance.socket.tcpclient;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.common.util.DialogUtils;
import com.easyfun.eclipse.common.util.resource.FileUtil;
import com.easyfun.eclipse.performance.socket.common.PortDialog;
import com.easyfun.eclipse.performance.socket.common.SocketUtils;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class TCPSocketClientView extends ViewPart {
	
	private final String NEW_LINE = "\r\n";

	private Text sendField;
	private StyledText messagesField;
	private Text portField;
	private Text ipField;

	private boolean isSecure = false;

	private static Socket socket;
	private PrintWriter out;
	private Button connectButton;
	private Button portButton;
	private Button secureButton;
	private Button saveButton;
	private Button clearwButton;

	private Button sendButton;
	private BufferedInputStream in;
	private boolean desonnected = false;

	private Group mainGroup;

	public TCPSocketClientView() {
	}

	public void createPartControl(Composite parent) {
		initial(parent);
	}
	
	private void initial(Composite p) {
		p.setLayout(new GridLayout());
		final Composite parent = new Composite(p, SWT.NULL);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group connectToGroup = new Group(parent, SWT.NONE);
		connectToGroup.setText("Connect To");
		final GridData gd_connectToGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		connectToGroup.setLayoutData(gd_connectToGroup);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		connectToGroup.setLayout(gridLayout);

		final Label ipAddressLabel = new Label(connectToGroup, SWT.NONE);
		ipAddressLabel.setText("IP Address");

		ipField = new Text(connectToGroup, SWT.BORDER);
		ipField.setText("127.0.0.1");
		final GridData gd_ipField = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		ipField.setLayoutData(gd_ipField);

		final Label portLabel = new Label(connectToGroup, SWT.NONE);
		portLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		portLabel.setText("Port");

		portField = new Text(connectToGroup, SWT.BORDER);
		portField.setText("21");
		final GridData gd_portField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		portField.setLayoutData(gd_portField);

		portButton = new Button(connectToGroup, SWT.NONE);
		portButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				PortDialog dia = new PortDialog(parent.getShell(), PortDialog.TCP);
				dia.open();
			}
		});
		portButton.setText("P&ort");

		connectButton = new Button(connectToGroup, SWT.NONE);
		connectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				connect();
			}
		});
		connectButton.setText("&Connect");

		secureButton = new Button(connectToGroup, SWT.CHECK);
		secureButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				isSecure = !isSecure;
			}
		});
		secureButton.setText("Secure");

		mainGroup = new Group(parent, SWT.NONE);
		mainGroup.setText("Connect To < NONE >");
		mainGroup.setLayout(new GridLayout());
		final GridData gd_mainGroup = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainGroup.setLayoutData(gd_mainGroup);

		final Label conversationLabel = new Label(mainGroup, SWT.NONE);
		final GridData gd_conversationLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
		conversationLabel.setLayoutData(gd_conversationLabel);
		conversationLabel.setText("Conversation with host");

		messagesField = new StyledText(mainGroup, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL);
		final GridData gd_messagesField = new GridData(SWT.FILL, SWT.FILL, true, true);
		messagesField.setLayoutData(gd_messagesField);

		final Composite bottomComposite = new Composite(parent, SWT.NONE);
		bottomComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		bottomComposite.setLayout(gridLayout_1);

		final Group sendGroup = new Group(bottomComposite, SWT.NONE);
		sendGroup.setText("Send");
		final GridData gd_sendGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		sendGroup.setLayoutData(gd_sendGroup);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 3;
		sendGroup.setLayout(gridLayout_2);

		final Label messageLabel = new Label(sendGroup, SWT.NONE);
		messageLabel.setText("Message");

		sendField = new Text(sendGroup, SWT.BORDER);
		final GridData gd_sendField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		sendField.setLayoutData(gd_sendField);

		sendButton = new Button(sendGroup, SWT.NONE);
		sendButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String msg = sendField.getText();
				if (!msg.equals(""))
					sendMessage(msg);
				else {
					boolean value = DialogUtils.showConfirm(getShell(), "Send Blank Line ?", "Send Data To Server");
					if (value == true)
						sendMessage(msg);
				}
			}
		});
		sendButton.setText("Send");

		final Composite composite = new Composite(bottomComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
		composite.setLayout(new GridLayout());

		saveButton = new Button(composite, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				File file = DialogUtils.openSaveDialog(getShell(), new String[]{"*.txt", "*.*"}, "socketServer.txt");
				if(file != null){
					try {
						FileUtil.writeTextFile(messagesField.getText(), file);
					} catch (IOException ex) {
						DialogUtils.showMsg(getShell(), "" + ex.getMessage(), "Error saving to file..");
					}
				}
			}
		});
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		saveButton.setText("Sa&ve");

		clearwButton = new Button(composite, SWT.NONE);
		clearwButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				messagesField.setText("");
			}
		});
		clearwButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		clearwButton.setText("&Clear");
	}

	private void connect() {
		if (socket != null) {
			disconnect();
			return;
		}
		final String ip = ipField.getText();
		final String port = portField.getText();
		if (ip == null || ip.equals("")) {
			DialogUtils.showMsg(getShell(), "No IP Address. Please enter IP Address", "Error connecting");
			ipField.setFocus();
			ipField.selectAll();
			return;
		}
		if (port == null || port.equals("")) {
			DialogUtils.showMsg(getShell(), "No Port number. Please enter Port number", "Error connecting");
			portField.setFocus();
			portField.selectAll();
			return;
		}
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				if (!SocketUtils.checkHost(ip)) {
					DialogUtils.showMsg(getShell(), "Bad IP Address", "Error connecting");
					ipField.setFocus();
					ipField.selectAll();
					return;
				}
				int portNo = 0;
				try {
					portNo = Integer.parseInt(port);
				} catch (Exception e) {
					DialogUtils.showMsg(getShell(), "Bad Port number. Please enter Port number", "Error connecting");
					portField.setFocus();
					portField.selectAll();
					return;
				}
				try {
					if (isSecure == false) {
						System.out.println("Connectig in normal mode : " + ip + ":" + portNo);
						socket = new Socket(ip, portNo);
					} else {
						System.out.println("Connectig in secure mode : " + ip + ":" + portNo);
						TrustManager[] tm = new TrustManager[] { new MyTrustManager(getShell()) };

						SSLContext context = SSLContext.getInstance("TLS");
						context.init(new KeyManager[0], tm, new SecureRandom());

						SSLSocketFactory factory = context.getSocketFactory();
						socket = factory.createSocket(ip, portNo);
					}

					ipField.setEditable(false);
					portField.setEditable(false);
					connectButton.setText("&Disconnect");
					connectButton.setToolTipText("Stop Connection");
					sendButton.setEnabled(true);
					sendField.setEditable(true);
				} catch (Exception e) {
					e.printStackTrace();
					error(e.getMessage(), "Opening connection");
					return;
				}
				changeBorder(" " + socket.getInetAddress().getHostName() + " [" + socket.getInetAddress().getHostAddress() + "] ");
				sendField.setText("");
				sendField.setFocus();
			}
		});

	}

	public synchronized void disconnect() {
		try {
			desonnected = true;
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showError(getShell(), "Error closing client : " + e.getMessage());
		}
		socket = null;
		out = null;
		changeBorder(null);
		ipField.setEditable(true);
		portField.setEditable(true);
		connectButton.setText("&Connect");
		connectButton.setToolTipText("Start Connection");
		sendButton.setEnabled(false);
		sendField.setEditable(false);
	}

	public void error(String error) {
		if (error == null || error.equals(""))
			return;
		DialogUtils.showMsg(getShell(), error, "Error");
	}

	public void error(String error, String heading) {
		if (error == null || error.equals(""))
			return;
		DialogUtils.showMsg(getShell(), error, heading);
	}

	public void append(String msg) {
		sendField.append(msg + NEW_LINE);
	}

	public void appendnoNewLine(String msg) {
		sendField.append(msg);
	}

	public void sendMessage(final String s) {
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				try {
					if (out == null) {
						out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					}
					append("S: " + s);
					out.print(s + NEW_LINE);
					out.flush();
					sendField.setText("");
				} catch (Exception e) {
					DialogUtils.showMsg(getShell(), e.getMessage(), "Error Sending Message");
					disconnect();
				}
			}
		});
	}

	private void changeBorder(String ip) {
		if (ip == null || ip.equals("")) {
			mainGroup.setText("Connected To < NONE >");
		} else {
			mainGroup.setText("Connected To < " + ip + " >");
		}
	}

	public void run() {
		InputStream is = null;
		try {
			is = socket.getInputStream();
			in = new BufferedInputStream(is);
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
				System.err.println("Socket not closed :" + e2);
			}
			error("Could not open socket : " + e.getMessage());
			disconnect();
			return;
		}

		while (!desonnected) {
			try {
				String got = readInputStream(in); // in.readLine();
				if (got == null) {
					disconnect();
					break;
				}
				appendnoNewLine(got);
			} catch (IOException e) {
				if (!desonnected) {
					error(e.getMessage(), "Connection lost");
					disconnect();
				}
				break;
			}
		}
		try {
			is.close();
			in.close();
		} catch (Exception err) {
		}
		socket = null;
	}

	private static String readInputStream(BufferedInputStream _in) throws IOException {
		String data = "";
		int s = _in.read();
		if (s == -1)
			return null;
		data += "" + (char) s;
		int len = _in.available();
		System.out.println("Len got : " + len);
		if (len > 0) {
			byte[] byteData = new byte[len];
			_in.read(byteData);
			data += new String(byteData);
		}
		return data;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}