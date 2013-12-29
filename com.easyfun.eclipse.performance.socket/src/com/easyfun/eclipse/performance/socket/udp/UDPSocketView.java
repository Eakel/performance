package com.easyfun.eclipse.performance.socket.udp;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
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

import com.easyfun.eclipse.performance.socket.common.PortDialog;
import com.easyfun.eclipse.performance.socket.common.SocketUtils;
import com.easyfun.eclipse.util.resource.FileUtil;
import com.easyfun.eclipse.util.ui.DialogUtils;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class UDPSocketView extends ViewPart {
	
	private final String NEW_LINE = "\r\n";

	private Text portField1;
	private Text portField2;

	private DatagramSocket server, client;
	private UdpServer udpServer;
	private Text ipField1;
	private Button connectButton;
	private StyledText messagesField;

	private Text sendField;

	private Text ipField2;

	private DatagramPacket pack;
	private byte buffer[];

	public UDPSocketView() {
	}

	public void createPartControl(Composite parent) {
		initial(parent);
	}
	
	private void initial(Composite p){
		p.setLayout(new GridLayout());
		
		createServerGroup(p);
		createConversationGroup(p);
		createClientGroup(p);
	}
	
	private Composite createServerGroup(Composite composite){
		Group parent = new Group(composite, SWT.NULL);
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parent.setLayout(new GridLayout(4, false));
		parent.setText("Server");
		
		Label label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("IP Address");
		
		ipField1 = new Text(parent, SWT.BORDER);
		ipField1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,3,1));
		ipField1.setText("0.0.0.0");
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Port");
		
		portField1 = new Text(parent, SWT.BORDER);
		portField1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		portField1.setText("21");
		
		Button portButton = new Button(parent, SWT.NULL);
		portButton.setLayoutData(new GridData());
		portButton.setText("&Port");
		portButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				PortDialog dialog = new PortDialog(getShell(), PortDialog.UDP);
				int result = dialog.open();
				if(result == InputDialog.OK){
					portField1.setText(dialog.getPort());
				}
			}
		});
		
		connectButton = new Button(parent, SWT.NULL);
		connectButton.setLayoutData(new GridData());
		connectButton.setText("&Start Listening");
		connectButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				listen();
			}
		});
		
		return parent;
	}
	
	private Composite createConversationGroup(Composite composite){
		Group parent = new Group(composite, SWT.NULL);
		parent.setText("Conversation");
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new GridLayout());
		
		messagesField = new StyledText(parent, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		messagesField.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		return parent;
	}
	
	private Composite createClientGroup(Composite composite){
		Composite c = new Composite(composite, SWT.NULL);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c.setLayout(new GridLayout(2, false));		
		
		Group parentGroup = new Group(c, SWT.NULL);
		parentGroup.setText("Client");
		parentGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		parentGroup.setLayout(new GridLayout(5, false));
		
		Composite operateGroup = new Composite(c, SWT.NULL);
		operateGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		operateGroup.setLayout(new GridLayout(1, true));
		
		Label label = new Label(parentGroup, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("IP Address");
		
		ipField2 = new Text(parentGroup, SWT.BORDER);
		ipField2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		ipField2.setText("127.0.0.1");
		
		label = new Label(parentGroup, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Port");
		
		portField2 = new Text(parentGroup, SWT.BORDER);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 30;
		portField2.setLayoutData(gridData);
		portField2.setText("21");
		
		Button portButton = new Button(parentGroup, SWT.NULL);
		portButton.setLayoutData(new GridData());
		portButton.setText(" &Port ");
		portButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				PortDialog dialog = new PortDialog(getShell(), PortDialog.UDP);
				int result = dialog.open();
				if(result == InputDialog.OK){
					portField2.setText(dialog.getPort());
				}
			}
		});
		
		label = new Label(parentGroup, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Message");
		
		sendField = new Text(parentGroup, SWT.BORDER);
		sendField.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		sendField.setText("");
		
		Button sendButton = new Button(parentGroup, SWT.NULL);
		sendButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		sendButton.setText(" Send ");
		sendButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
                String msg = sendField.getText();
                if(!msg.equals(""))
                    sendMessage(msg);
                else {
                	boolean result = DialogUtils.showConfirm(getShell(), "Send Blank Line ?", "");
                    if (result == true)
                        sendMessage(msg);
                }
			}
		});
		
		//operate
		
		Button saveButton = new Button(operateGroup, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalIndent = 10;
		saveButton.setLayoutData(gridData);
		saveButton.setText(" &Save ");
		saveButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
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
		
		Button cancelButton = new Button(operateGroup, SWT.NULL);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.setText(" &Clear ");
		cancelButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				 messagesField.setText("");
			}
		});
		
		return c;
	}
	
	private void listen(){
        if(server!=null) {
            stop();
            return;
        }
        String ip=ipField1.getText();
        String port=portField1.getText();
        if(ip==null || ip.equals("")) {
        	DialogUtils.showMsg(getShell(), "No IP Address. Please enter IP Address", "");
            ipField1.setFocus();
            ipField1.selectAll();
            return;
        }
        if(port==null || port.equals("")) {
        	DialogUtils.showMsg(getShell(), "No Port number. Please enter Port number", "");
            portField1.setFocus();
            portField1.selectAll();
            return;
        }
        if(!SocketUtils.checkHost(ip)) {
        	DialogUtils.showMsg(getShell(), "Bad IP Address", "");
            ipField1.setFocus();
            ipField1.selectAll();
            return;
        }
        int portNo = 0;
        try	{
            portNo=Integer.parseInt(port);
        } catch (Exception e) {
        	DialogUtils.showMsg(getShell(), "Bad Port number. Please enter Port number", "");
            portField1.setFocus();
            portField1.selectAll();
            return;
        }
        try {
            InetAddress bindAddr=null;
            if(!ip.equals("0.0.0.0")) {
                bindAddr = InetAddress.getByName(ip);
                server = new DatagramSocket(portNo, bindAddr);
            } else {
                bindAddr = null;
                server = new DatagramSocket(portNo);
            }
            
            ipField1.setEditable(false);
            portField1.setEditable(false);
            
            connectButton.setText("&Stop Listening");
            connectButton.setToolTipText("Stop Listening");
        } catch (Exception e) {
            error(e.getMessage(), "Starting Server at "+portNo);
            return;
        }
        messagesField.setText("> Server Started on Port : "+portNo+NEW_LINE);
        append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        udpServer = UdpServer.handle(this,server);
	}
	
    public synchronized void stop() {
        try {
            udpServer.setStop(true);
        } catch (Exception e) {}
        server=null;
        
        ipField1.setEditable(true);
        portField1.setEditable(true);
        
        connectButton.setText("&Start Listening");
        connectButton.setToolTipText("Start Listening");
        append("> Server stopped");
        append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public void sendMessage(String s) {
        try	{
            String ip=ipField2.getText();
            String port=portField2.getText();
            if(ip==null || ip.equals("")) {
            	DialogUtils.showMsg(getShell(), "No IP Address. Please enter IP Address", "");
                ipField2.setFocus();
                ipField2.selectAll();
                return;
            }
            if(port==null || port.equals("")) {
            	DialogUtils.showMsg(getShell(), "No Port number. Please enter Port number", "");
                portField2.setFocus();
                portField2.selectAll();
                return;
            }
            if(!SocketUtils.checkHost(ip)) {
            	DialogUtils.showMsg(getShell(), "Bad IP Address", "");
                ipField2.setFocus();
                ipField2.selectAll();
                return;
            }
            int portNo = 0;
            try	{
                portNo=Integer.parseInt(port);
            } catch (Exception e) {
            	DialogUtils.showMsg(getShell(), "Bad Port number. Please enter Port number", "");
                portField2.setFocus();
                portField2.selectAll();
                return;
            }
            
            InetAddress toAddr=null;
            toAddr = InetAddress.getByName(ip);
            
            if(client==null) {
                client = new DatagramSocket();
                UdpServer.handleClient(this,client); //listen for its response
            }
            buffer = s.getBytes();
            pack = new DatagramPacket(buffer, buffer.length, toAddr, portNo);
            append("S["+toAddr.getHostAddress()+":"+portNo+"]: "+s);
            client.send(pack);
            sendField.setText("");
        } catch (Exception e) {
        	DialogUtils.showMsg(getShell(), "Error Sending Message", "");
            client=null;
        }
    }
    
    public void error(String error) {
        if(error==null || error.equals(""))
            return;
        DialogUtils.showMsg(getShell(), error, "Error");
    }
    
    public void error(String error, String heading) {
        if(error==null || error.equals(""))
            return;
        DialogUtils.showMsg(getShell(), error, heading);
    }
    
    public void append(String msg) {
        messagesField.append(msg+NEW_LINE);
//        messagesField.setCaretPosition(messagesField.getText().length());
    }

	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}