package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Scheme | Host | Path | Query | Username | Password | Port | Fragment
 * @author linzhaoming
 *
 */
public class HttpURLControl extends Composite {	
	private static final String SCHEME_PROP = "scheme"; 
	private static final String USERNAME_PROP = "username"; 
	private static final String PASSWORD_PROP = "password"; 
	private static final String HOST_PROP = "host"; 
	private static final String PORT_PROP = "port"; 
	private static final String PATH_PROP = "path"; 
	private static final String QUERY_PROP = "query"; 
	private static final String FRAGEMENT_PROP = "fragement"; 
	private static final String HTTP_SCHEME = "HTTP";
	private static final String HTTPS_SCHEME = "HTTPS"; 

	/** 当前使用的HttpURL */
	private HttpURL currentURL = null; 
		
	private HttpURL httpURL = null;
	private HttpsURL httpsURL = null;
	
	private Combo schemeCombo = null;
	private Text usernameText = null;
	private Text passwordText = null;
	private Text hostText = null;
	private Text portText = null;
	private Text pathText = null;
	private Text queryText = null;
	private Text fragmentText = null;

	public HttpURLControl(Composite parent, int style) {
		super(parent, style);
		try {
			httpURL = new HttpURL("http://localhost");
			httpsURL = new HttpsURL("https://localhost");
		} catch (URIException e) {
			e.printStackTrace(); // should never happen.
		}
		initialize();
		setScheme(HTTP_SCHEME);
	}
	
	private void initialize() {
		this.setLayout(new GridLayout(6, false));
		
		//Scheme
		Label label = new Label(this, SWT.LEFT);
		label.setText("Scheme");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));		
		schemeCombo = new Combo(this, SWT.READ_ONLY);
		schemeCombo.add(HTTP_SCHEME);
		schemeCombo.add(HTTPS_SCHEME);
		schemeCombo.select(0);
		schemeCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,true, false));
		schemeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setScheme(schemeCombo.getText());
				firePropertyChange(SCHEME_PROP, null, schemeCombo.getText());
			}
		});

		//Username
		label = new Label(this, SWT.NONE);
		label.setText("Username");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		usernameText = new Text(this, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		usernameText.addFocusListener(new FocusAdapter() {
			String usernameOnFocus = null;
			public void focusLost(FocusEvent e) {
				try {
					String username = usernameText.getText();
					if (username.length() != 0) {
						currentURL.setUser(username);
					}
					firePropertyChange(USERNAME_PROP, usernameOnFocus, username);
				} catch (Exception ex) {
					usernameText.setText(usernameOnFocus);
				}
			}

			public void focusGained(FocusEvent e) {
				usernameOnFocus = usernameText.getText();
			}
		});		
		
		//Password
		label = new Label(this, SWT.NONE);
		label.setText("Password");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		passwordText = new Text(this, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		passwordText.addFocusListener(new FocusAdapter() {
			String passwordOnFocus = null;
			public void focusLost(FocusEvent e) {
				try {
					String password = passwordText.getText();
					if (password.length() != 0) {
						currentURL.setPassword(password);
					}
					firePropertyChange(PASSWORD_PROP, passwordOnFocus, password);
				} catch (Exception e1) {
					passwordText.setText(passwordOnFocus);
				}
			}

			public void focusGained(FocusEvent e) {
				passwordOnFocus = passwordText.getText();
			}
		});
		
		//Host
		label = new Label(this, SWT.NONE);
		label.setText("Host");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		hostText = new Text(this, SWT.BORDER);
		hostText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
		hostText.addFocusListener(new FocusAdapter() {
			String hostOnFocus = null;

			public void focusLost(FocusEvent e) {
				try {
					currentURL.setEscapedAuthority(hostText.getText() + ":" + portText.getText());
					firePropertyChange(HOST_PROP, hostOnFocus, hostText.getText());
				} catch (Exception e1) {
					hostText.setText(hostOnFocus);
				}
			}

			public void focusGained(FocusEvent e) {
				hostOnFocus = hostText.getText();
			}
		});
		
		//Port
		label = new Label(this, SWT.NONE);
		label.setText("Port");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		portText = new Text(this, SWT.BORDER);
		portText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		portText.addFocusListener(new FocusAdapter() {
			String portOnFocus = null;
			public void focusLost(FocusEvent e) {
				try {
					currentURL.setEscapedAuthority(hostText.getText() + ":" + portText.getText());
					firePropertyChange(PORT_PROP, portOnFocus, portText.getText());
				} catch (URIException e1) {
					portText.setText(portOnFocus);
				}
			}

			public void focusGained(FocusEvent e) {
				portOnFocus = portText.getText();
			}
		});
		
		//Path
		label = new Label(this, SWT.NONE);
		label.setText("Path");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		pathText = new Text(this, SWT.BORDER);
		pathText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));
		pathText.addFocusListener(new FocusAdapter() {
			String pathOnFocus = null;

			public void focusGained(FocusEvent e) {
				pathOnFocus = pathText.getText();
			}

			public void focusLost(FocusEvent e) {
				try {
					if (pathText.getText().length() == 0) {
						pathText.setText("/");
					} else if (pathText.getText().charAt(0) != '/') {
						pathText.setText("/" + pathText.getText());
					}
					currentURL.setPath(pathText.getText());
					firePropertyChange(PATH_PROP, pathOnFocus, pathText.getText());
				} catch (URIException e1) {
					pathText.setText(pathOnFocus);
				}
			}
		});
		
		//Query
		label = new Label(this, SWT.NONE);
		label.setText("Query");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		queryText = new Text(this, SWT.BORDER);
		queryText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
		queryText.addFocusListener(new FocusAdapter() {
			String queryOnFocus = null;

			public void focusGained(FocusEvent e) {
				queryOnFocus = queryText.getText();
			}

			public void focusLost(FocusEvent e) {
				try {
					currentURL.setQuery(queryText.getText());
					firePropertyChange(QUERY_PROP, queryOnFocus, queryText.getText());
				} catch (URIException e1) {
					queryText.setText(queryOnFocus);
				}
			}
		});
		
		//Fragment
		label = new Label(this, SWT.NONE);
		label.setText("Fragment");
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		fragmentText = new Text(this, SWT.BORDER);
		fragmentText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		fragmentText.addFocusListener(new FocusAdapter() {
			String fragmentOnFocus = null;

			public void focusGained(FocusEvent e) {
				fragmentOnFocus = fragmentText.getText();
			}

			public void focusLost(FocusEvent e) {
				try {
					currentURL.setFragment(fragmentText.getText());
					firePropertyChange(FRAGEMENT_PROP, fragmentOnFocus, fragmentText.getText());
				} catch (URIException e1) {
					fragmentText.setText(fragmentOnFocus);
				}
			}
		});
	}
	
	public void setInput(Object object) {
		if (object instanceof HttpsURL) {
			httpsURL = (HttpsURL)object;
			setScheme(HTTPS_SCHEME);
		} else if (object instanceof HttpURL) {
			httpURL = (HttpURL)object;
			setScheme(HTTP_SCHEME);
		}
	}

	public HttpURL getURL() {
		return currentURL;
	}

	private void setScheme(String scheme) {
		if (scheme.equals(HTTP_SCHEME)) {
			currentURL = httpURL;
		} else { // scheme == HTTPS_SCHEME
			currentURL = httpsURL;
		}
		try {
			if (currentURL.getUser() != null) {
				usernameText.setText(currentURL.getUser());
			}
			if (currentURL.getPassword() != null) {
				passwordText.setText(currentURL.getPassword());
			}
			if (currentURL.getHost() != null) {
				hostText.setText(currentURL.getHost());
			}
			portText.setText(Integer.toString(currentURL.getPort()));
			if (currentURL.getPath() != null) {
				pathText.setText(currentURL.getPath());
			}
			if (currentURL.getQuery() != null) {
				queryText.setText(currentURL.getQuery());
			}
			if (currentURL.getFragment() != null) {
				fragmentText.setText(currentURL.getFragment());
			}
		} catch (URIException e) {
			e.printStackTrace();
		}
	}
	// --- PropertyChangeSupport
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this); 
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
}
