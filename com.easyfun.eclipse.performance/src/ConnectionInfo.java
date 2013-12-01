import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.Date;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

class ConnectionInfo {
  public String host;

  public int port;

  public String password;

  public String username;

}
class ConnectionDialog extends Dialog {
  private static final String DIALOG_SETTING_FILE = "ftp.connection.xml";

  private static final String KEY_HOST = "HOST";

  private static final String KEY_PORT = "PORT";

  private static final String KEY_USERNAME = "USER";

  private static final String KEY_PASSWORD = "PASSWORD";

  Text textHost;

  Text textPort;

  Text textUsername;

  Text textPassword;

  DialogSettings dialogSettings;

  ConnectionInfo connectionInfo;

  ConnectionDialog(FTPWindow window) {
    super(window.getShell());
    connectionInfo = null;

    dialogSettings = new DialogSettings("FTP");
    try {
      dialogSettings.load(DIALOG_SETTING_FILE);
    } catch (Exception e) {
      e.printStackTrace();
      // ignore.
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  protected Control createDialogArea(Composite parent) {
    getShell().setText("Connection Settings");

    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new GridLayout(2, false));

    new Label(composite, SWT.NULL).setText("Host: ");
    textHost = new Text(composite, SWT.BORDER);
    textHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    new Label(composite, SWT.NULL).setText("Port: ");
    textPort = new Text(composite, SWT.BORDER);
    textPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    new Label(composite, SWT.NULL).setText("Username: ");
    textUsername = new Text(composite, SWT.BORDER);
    textUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    new Label(composite, SWT.NULL).setText("Password: ");
    textPassword = new Text(composite, SWT.PASSWORD | SWT.BORDER);
    textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    // sets initial values.
    try {
      textHost.setText(dialogSettings.get(KEY_HOST));
      textPort.setText(dialogSettings.getInt(KEY_PORT) + "");
      textUsername.setText(dialogSettings.get(KEY_USERNAME));
      textPassword.setText(dialogSettings.get(KEY_PASSWORD));
    } catch (Exception e) {
      // ignore.
    }

    return composite;
  }

  /**
   * Returns a ConnectionInfo object containing connection information.
   * 
   * @return
   */
  public ConnectionInfo getConnectionInfo() {
    return connectionInfo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  protected void okPressed() {
    try {
      if (!new File(DIALOG_SETTING_FILE).exists()) {
        new File(DIALOG_SETTING_FILE).createNewFile();
      }
      dialogSettings.put(KEY_HOST, textHost.getText());
      dialogSettings.put(KEY_PORT, Integer.parseInt(textPort.getText()
          .trim()));
      dialogSettings.put(KEY_USERNAME, textUsername.getText());
      dialogSettings.put(KEY_PASSWORD, textPassword.getText());
      dialogSettings.save(DIALOG_SETTING_FILE);
    } catch (Exception e) {
      e.printStackTrace();
      // ignore
    }

    connectionInfo = new ConnectionInfo();
    connectionInfo.host = textHost.getText();
    connectionInfo.port = Integer.parseInt(textPort.getText().trim());
    connectionInfo.username = textUsername.getText();
    connectionInfo.password = textPassword.getText();

    super.okPressed();
  }

}

