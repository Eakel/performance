package com.easyfun.eclipse.performance.appframe.views;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.performance.appframe.util.appf.AppFEncrypt;
import com.easyfun.eclipse.performance.appframe.util.appf.K;
import com.easyfun.eclipse.performance.appframe.util.appf.MD5;
import com.easyfun.eclipse.performance.appframe.util.appf.RC2;
import com.easyfun.eclipse.utils.ui.SWTUtil;

/**
 * WebTools Views.
 * 
 * @author linzhaoming
 * Create Date: 2010-6-19
 */
public class WebToolsView extends ViewPart {
	private Text original52Text;
	private Button dec52Button;
	private Button enc52Button;
	private Text appf52ResultText;
	private Button enc55DBButton;
	private Button dec55DBButton;
	private Text original55DBText;
	private Text appf55DBResultText;
	private Button enc55OPButton;
	private Button dec55OPButton;
	private Text original55OPText;
	private Text appf55OPResultText;
	private Text md5ResultText;
	private Text md5PlaiText;
	

	public WebToolsView() {
	}

	public void createPartControl(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());

		Composite utfComposite = createUTF8Control(tabFolder);
		Composite colorComposite = createColorControl(tabFolder);
		Composite timeComposite = createTimeControl(tabFolder);
		Composite appfComposite = createAppFControl(tabFolder);
		Composite base64Composite = createBase64Control(tabFolder);
		
		Composite sshRC2Composite = createSSHRC2Control(tabFolder);
		

		TabItem utf8Item = new TabItem(tabFolder, SWT.NULL);
		utf8Item.setText("UTF8");
		utf8Item.setControl(utfComposite);

		TabItem colorItem = new TabItem(tabFolder, SWT.NULL);
		colorItem.setText("��ɫ");
		colorItem.setControl(colorComposite);
		
		TabItem timeItem = new TabItem(tabFolder, SWT.NULL);
		timeItem.setText("ʱ��");
		timeItem.setControl(timeComposite);
		
		TabItem appfItem = new TabItem(tabFolder, SWT.NULL);
		appfItem.setText("AppFrame");
		appfItem.setControl(appfComposite);
		
		TabItem base64Item = new TabItem(tabFolder, SWT.NULL);
		base64Item.setText("Base64");
		base64Item.setControl(base64Composite);
		
		TabItem sshRC2Item = new TabItem(tabFolder, SWT.NULL);
		sshRC2Item.setText("RC2(SSH)");
		sshRC2Item.setControl(sshRC2Composite);
	}

	/**
	 * UTF8 Composite
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createUTF8Control(Composite parent) {
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout());

		Label label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("Unicode");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text utfText = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		utfText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text cnText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		cnText.setLayoutData(new GridData(GridData.FILL_BOTH));

		utfText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					cnText.setText(CharsetUtil.loadConvert(utfText.getText().trim()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		return content;
	}
	
	/**
	 * ʱ��Tab
	 * @param tabFolder
	 * @return
	 */
	private Composite createTimeControl(TabFolder parent) {
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout());

		//ʱ���������ʱ��
		Label label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("ʱ���(long)");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text timeStampText = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		timeStampText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("��ʾʱ��");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text cnText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		cnText.setLayoutData(new GridData(GridData.FILL_BOTH));

		timeStampText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String str = timeStampText.getText();
					long time = Long.parseLong(str);
					Date date = new Date(time);
					DateFormat dateFormat = DateFormat.getDateTimeInstance();
					;
					cnText.setText(dateFormat.format(date));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		//����ʱ�䵽ʱ���
		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("����ʱ��(��ʽΪ2013-01-10 05:05:21)");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text timeText = new Text(content, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		timeText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("ʱ���");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text tsText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		tsText.setLayoutData(new GridData(GridData.FILL_BOTH));

		timeText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String strDate = timeText.getText().trim();
					 if (strDate == null) {
						return;
					}
					DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp ts = new Timestamp(dateformat.parse(strDate).getTime());
					tsText.setText(String.valueOf(ts.getTime()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		

		return content;
	}
	
	/**
	 * AppF Tab
	 * @param parent
	 * @return
	 */
	private Composite createAppFControl(Composite parent){
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout());
		
		TabFolder tabFolder = new TabFolder(content, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());
		
		TabItem item55 = new TabItem(tabFolder, SWT.NULL);
		item55.setText("V5.5�汾");
		
		TabItem item52 = new TabItem(tabFolder, SWT.NULL);
		item52.setText("V5.2�汾");

		Composite frame52Compsoite = createAppF52Compsoite(tabFolder);
		item52.setControl(frame52Compsoite);
		
		Composite frame55Compsoite = createAppF55Compsoite(tabFolder);
		item55.setControl(frame55Compsoite);

		return content;	
	}
	
	/**
	 * V5.2�汾
	 * @param tabFolder
	 * @return
	 */
	private Composite createAppF52Compsoite(TabFolder tabFolder){
		Composite panel = new Composite(tabFolder, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label = new Label(panel, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("���ݿ�ӽ���(V52)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		enc52Button = new Button(panel, SWT.RADIO);
		enc52Button.setLayoutData(new GridData());
		enc52Button.setText("����(V52)");
		enc52Button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleDBEnc52();
			}
		});
		
		dec52Button = new Button(panel, SWT.RADIO);
		dec52Button.setLayoutData(new GridData());
		dec52Button.setText("����(V52)");
		dec52Button.setSelection(true);
		dec52Button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				handleDBEnc52();
			}
		});

		original52Text = new Text(panel, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		original52Text.setLayoutData(gridData);

		label = new Label(panel, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("���ݿ�ӽ��ܽ��(V52)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		appf52ResultText = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		appf52ResultText.setLayoutData(gridData);

		original52Text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleDBEnc52();
			}
		});
		
		label = new Label(panel, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("V52�汾����Ա�ӽ��������ݿ���һ�µ�");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_RED));
		
		return panel;
	}
	
	/**
	 * V5.5�汾
	 * @param tabFolder
	 * @return
	 */
	private Composite createAppF55Compsoite(TabFolder tabFolder){
		Composite panel = new Composite(tabFolder, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label = new Label(panel, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("���ݿ�ӽ���(V55)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		enc55DBButton = new Button(panel, SWT.RADIO);
		enc55DBButton.setLayoutData(new GridData());
		enc55DBButton.setText("����(V55)");
		enc55DBButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleDBEnc55();
			}
		});
		
		dec55DBButton = new Button(panel, SWT.RADIO);
		dec55DBButton.setLayoutData(new GridData());
		dec55DBButton.setText("����(V55)");
		dec55DBButton.setSelection(true);
		dec55DBButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				handleDBEnc55();
			}
		});

		original55DBText = new Text(panel, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		original55DBText.setLayoutData(gridData);

		label = new Label(panel, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("���ݿ�ӽ��ܽ��(V55)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		appf55DBResultText = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		appf55DBResultText.setLayoutData(gridData);

		original55DBText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleDBEnc55();
			}
		});
	
		label = new Label(panel, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 30;
		label.setLayoutData(gridData);
		
		
		//����Ա
		label = new Label(panel, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("����Ա�ӽ���(V55)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		enc55OPButton = new Button(panel, SWT.RADIO);
		enc55OPButton.setLayoutData(new GridData());
		enc55OPButton.setText("����(V55)");
		enc55OPButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleOPEnc55();
			}
		});
		
		dec55OPButton = new Button(panel, SWT.RADIO);
		dec55OPButton.setLayoutData(new GridData());
		dec55OPButton.setText("����(V55)");
		dec55OPButton.setSelection(true);
		dec55OPButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				handleOPEnc55();
			}
		});

		original55OPText = new Text(panel, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		original55OPText.setLayoutData(gridData);

		label = new Label(panel, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("����Ա�ӽ��ܽ��(V55 MD5)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		appf55OPResultText = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		appf55OPResultText.setLayoutData(gridData);

		original55OPText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleOPEnc55();
			}
		});
		
		
		
		//MD5����
		label = new Label(panel, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("MD5����(HotPatch)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));		

		md5PlaiText = new Text(panel, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		md5PlaiText.setLayoutData(gridData);

		label = new Label(panel, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("MD5���ܽ��(HotPatch)");
		label.setForeground(panel.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		md5ResultText = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		md5ResultText.setLayoutData(gridData);

		md5PlaiText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleMD5();
			}
		});
		
		return panel;
	}
	
	/**
	 * Base64Tab
	 * @param tabFolder
	 * @return
	 */
	private Composite createBase64Control(TabFolder parent) {
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout());
		
		
		
		//����ʱ�䵽ʱ���
		Label label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("Base64");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text fromBase64Text = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		fromBase64Text.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text toPlainText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		toPlainText.setLayoutData(new GridData(GridData.FILL_BOTH));

		fromBase64Text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String strDate = fromBase64Text.getText().trim();
					if (strDate == null) {
						return;
					}

					byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(strDate);
					toPlainText.setText(new String(bytes));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		

		//ʱ���������ʱ��
		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text fromPlainText = new Text(content, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		fromPlainText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("Base64");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text toBase64Text = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		toBase64Text.setLayoutData(new GridData(GridData.FILL_BOTH));

		fromPlainText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String str = fromPlainText.getText();
					String result = new sun.misc.BASE64Encoder().encode(str.getBytes("GBK"));
					toBase64Text.setText(result);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		

		return content;
	}
	
	private Composite createSSHRC2Control(TabFolder parent) {
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout());
		
		
		
		//����ʱ�䵽ʱ���
		Label label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("[����]����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text fromPlainText = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		fromPlainText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("[����]����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text toEncryptText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		toEncryptText.setLayoutData(new GridData(GridData.FILL_BOTH));

		fromPlainText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String strDate = fromPlainText.getText().trim();
					if (strDate == null) {
						return;
					}

					RC2 rc2 = new RC2();
					String s = "{RC2}" + rc2.encrypt_rc2_array_base64(strDate.getBytes(), "ai_nj_crm".getBytes());
					if(s.endsWith("=")){
						s = s.substring(0, s.length() -1);
					}
					
					toEncryptText.setText(s);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		

		//ʱ���������ʱ��
		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("[����]����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text fromEncryptText = new Text(content, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		fromEncryptText.setLayoutData(gridData);

		label = new Label(content, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("[����]����");
		label.setForeground(content.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		final Text toPlainText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		toPlainText.setLayoutData(new GridData(GridData.FILL_BOTH));

		fromEncryptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					String str = fromEncryptText.getText();
					RC2 rc2 = new RC2();
					if(str.endsWith("=") == false){
						str = str + "=";
					}
					if (str.lastIndexOf("{RC2}") != -1) {
						str = str.substring(5);
					} 
					String s1 = rc2.decrypt_rc2_array_base64(str.getBytes(), "ai_nj_crm".getBytes());
					toPlainText.setText(s1);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		

		return content;
	}

	/**
	 * Color Display Composite
	 * 
	 * @param p
	 * @return
	 */
	private Composite createColorControl(Composite p) {
		Composite content = new Composite(p, SWT.NULL);
		content.setLayout(new GridLayout());

		final Text text = new Text(content, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button okButton = new Button(content, SWT.PUSH);
		okButton.setLayoutData(new GridData());
		okButton.setText("Show Color");

		final Label colorLabel = new Label(content, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		colorLabel.setLayoutData(gridData);

		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String str = text.getText();
				String colorStr = str;
				if (str.startsWith("#")) {
					colorStr = str.substring(1);
				}
				RGB rgb = null;
				
				if (colorStr.length() == 6) {
					try {
						int red = Integer.parseInt(colorStr.substring(0, 2), 16);
						int green = Integer.parseInt(colorStr.substring(2, 4), 16);
						int blue = Integer.parseInt(colorStr.substring(4, 6), 16);
						rgb = new RGB(red, green, blue);
					} catch (NumberFormatException ex) {
						SWTUtil.showMessage(getSite().getShell(), "Format is not allowed" + ex.getMessage());
					}
				} else if (colorStr.length() == 3) {
					try {
						int red = Integer.parseInt(colorStr.substring(0, 1), 16);
						int green = Integer.parseInt(colorStr.substring(1, 2), 16);
						int blue = Integer.parseInt(colorStr.substring(2, 3), 16);
						rgb = new RGB(red, green, blue);
					} catch (NumberFormatException ex) {
						SWTUtil.showMessage(getSite().getShell(), "Format is not allowed");
					}
				} else {
					SWTUtil.showMessage(getSite().getShell(), "Format is not allowed, number is not correct.");	
					return;
				}

				if (rgb != null) {
					Color c = (Color) colorLabel.getData();
					if (c != null) {
						c.dispose();
					}
					Color color = new Color(getSite().getShell().getDisplay(), rgb);
					colorLabel.setData(color);
					colorLabel.setBackground(color);
				}
			}
		});

		return content;
	}

	public void setFocus() {
		//
	}
	
	/**
	 * ����ӽ���V52
	 */
	private void handleDBEnc52(){
		String original = original52Text.getText().trim();
		String result = "";
		if(enc52Button.getSelection() && !dec52Button.getSelection()){	//����
			result = AppFEncrypt.DoEncrypt(original);
		}else if(!enc52Button.getSelection() && dec52Button.getSelection()){	//����
			result = AppFEncrypt.DoDecrypt(original);
		}else{
//			SWTUtil.showMessage(appf52ResultText.getShell(),  "��ѡ��ӽ�������(V52)");
			result = "";
		}
		appf52ResultText.setText(result);
	}
	
	/**
	 * ����ӽ���V55
	 */
	private void handleDBEnc55(){
		String original = original55DBText.getText().trim();
		String result = "";
		
		if(original.equals("")){
			appf55DBResultText.setText(result);
			return;
		}
		try {
			if(enc55DBButton.getSelection() && !dec55DBButton.getSelection()){	//����
				result = "{RC2}" + K.j(original);
			}else if(!enc55DBButton.getSelection() && dec55DBButton.getSelection()){	//����
				result = K.k_s(original);
			}else{
//				SWTUtil.showMessage(appf55DBResultText.getShell(),  "��ѡ��ӽ�������(V55)");
				result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ӽ���ʧ��";
		}
		appf55DBResultText.setText(result);
	}
	
	/**
	 * ����Ա����ӽ���
	 */
	private void handleOPEnc55(){
		String original = original55OPText.getText().trim();
		String result = "";
		
		if(original.equals("")){
			appf55OPResultText.setText(result);
			return;
		}
		try {
			if(enc55OPButton.getSelection() && !dec55OPButton.getSelection()){	//����
				MD5 md5 = new MD5();
				result = md5.toDigest(original);
			}else if(!enc55OPButton.getSelection() && dec55OPButton.getSelection()){	//����
				result = "δʵ��";
			}else{
				result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ӽ���ʧ��";
		}
		appf55OPResultText.setText(result);
	}
	
	/**
	 * HotPatch��MD5����
	 */
	private void handleMD5(){
		String original = md5PlaiText.getText().trim();
		String result = "";
		
		if(original.equals("")){
			md5ResultText.setText(result);
			return;
		}
		try {
			com.asiainfo.mon.util.MD5 md5 = new com.asiainfo.mon.util.MD5();
			result = md5.getMD5ofStr(original);
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ӽ���ʧ��";
		}
		md5ResultText.setText(result);
	}
}