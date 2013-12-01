

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * StyledText������
 * 
 * @author
 * 
 */
public class TextStyledText2 extends ApplicationWindow {

	private StyledText styledText;
	private Action actionRun;
	private SourceViewer sourceViewer_source;
	private List<String> optionList = new ArrayList<String>();

	/**
	 * Create the application window
	 */
	public TextStyledText2() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		final SashForm sashForm = new SashForm(container, SWT.BORDER);
		sashForm.setOrientation(SWT.VERTICAL);

		sourceViewer_source = new SourceViewer(sashForm, null, SWT.V_SCROLL | SWT.BORDER);

		sourceViewer_source.setDocument(new Document(""));
		sourceViewer_source.setEditable(true);

		final CTabFolder tabFolder = new CTabFolder(sashForm, SWT.NONE);

		final CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("���");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		tabItem.setControl(composite);

		final TextViewer textViewer_trage = new TextViewer(composite,
				SWT.BORDER);
		styledText = textViewer_trage.getTextWidget();
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sashForm.setWeights(new int[] { 1, 1 });
		//

		initData();
		initListener();

		return container;
	}

	private void initData() {
		String[] options = new String[] { "From", "To", "Table", "order" };
		Arrays.sort(options);// ����һ��
		for (String option : options) {
			optionList.add(option.toLowerCase());
		}
		/*
		 * �Զ���ɹ���һ�����������������µ���һ��С�������û���ʾ��ǰ�ɹ�ѡ���ѡ�һ���û�����ָ������ϼ�ʱ�������û��������ض����ַ�ʱ��
		 * SourceViewer֧�������ִ�����ʽ���ڳ�����ʹ��SourceViewer��ʹ��һ��ؼ�û�кܴ�ķֱ�ֻ��SourceViewer
		 * ��StyledText�İ�װ������һЩ����Ҫͨ��getTextWidget()��ɣ�������ʾ��
		 */
		// Configure source viewer, add content assistant support
		sourceViewer_source.configure(new SourceViewerConfiguration() {
			public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
				ContentAssistant assistant = new ContentAssistant();
				IContentAssistProcessor cap = new MyContentAssistProcessor();
				assistant.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
				assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
				assistant.enableAutoActivation(true);
				return assistant;
			}
		});

	}

	private void initListener() {
		sourceViewer_source.appendVerifyKeyListener(new VerifyKeyListener() {
			public void verifyKey(VerifyEvent event) {
				// Check for Alt+/
				if (event.stateMask == SWT.ALT && event.character == '/') {
					// Check if source viewer is able to perform operation
					if (sourceViewer_source.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS))
						// Perform operation
						sourceViewer_source.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
					// Veto this key press to avoid further processing
					event.doit = false;
				} else if (event.character == ' ') {// ����ո����ɫ
					String text = sourceViewer_source.getTextWidget().getText();
					if (text.length() > 0) {
						// text = text.substring(text.length() - 1);
						int p = text.lastIndexOf(" ");
						String rengText = text;
						if (p != -1) {
							rengText = text.substring(p + 1);
							p = p + 1;
						} else {
							p = 0;
						}
						if (rengText.length() > 0
								&& optionList.contains(rengText.toLowerCase())) {
							Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
							StyleRange sr = new StyleRange(p, rengText.length(), red, null);
							sourceViewer_source.getTextWidget().setStyleRange(sr);
						}
					}
				}

			}
		});
		Menu menu = new Menu(sourceViewer_source.getTextWidget());
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("ִ��");
		menuItem.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runAction();
			}

		});

		sourceViewer_source.getTextWidget().setMenu(menu);
	}

	private void runAction() {
		MessageDialog.openInformation(getShell(), "��Ϣ", "��û�����أ����������Լ��Ĺ���");
	}

	/**
	 * Create the actions
	 */
	private void createActions() {
		actionRun = new Action("ִ��") {
			public void run() {
				runAction();
			}
		};
	}

	/**
	 * Create the toolbar manager
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);

		toolBarManager.add(actionRun);
		return toolBarManager;
	}

	/**
	 * Create the status line manager
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage(null, "");
		return statusLineManager;
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			TextStyledText2 window = new TextStyledText2();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Text StyledText ");
	}

	/**
	 * Return the initial size of the window
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}

	/**
	 * ������齫��Ϊ������ʾ�������ѡ��
	 * 
	 * @author ���ֺ�
	 * 
	 */
	class MyContentAssistProcessor implements IContentAssistProcessor {

		public ICompletionProposal[] computeCompletionProposals(
				ITextViewer viewer, int offset) {

			try {
				String text = viewer.getTextWidget().getText();
				String[] options = optionList.toArray(new String[optionList
						.size()]);

				if (text != null && text.length() > 0) {
					int p = text.lastIndexOf(" ");
					if (p != -1) {
						text = text.substring(p + 1);
					}
					List<String> s = new ArrayList<String>();
					HashMap<String, String> h = new HashMap<String, String>();
					Vector<String> v = new Vector<String>(1);
					for (String option : options) {
						if (text.length() < option.length()
								&& option.substring(0, text.length())
										.equalsIgnoreCase(text)) {
							// v.add(option.substring(text.length()));
							v.add(option);
						}
					}
					if (v.size() > 0) {
						options = v.toArray(new String[v.size()]);
					} else {
						return null;
					}
				}

				// Dynamically generate proposal
				ArrayList result = new ArrayList();
				for (int i = 0; i < options.length; i++) {
					// int len = options[i].length() - text.length();
					CompletionProposal proposal = new CompletionProposal(
							options[i], offset - text.length(), text.length(),
							options[i].length());
					result.add(proposal);
				}
				return (ICompletionProposal[]) result
						.toArray(new ICompletionProposal[result.size()]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public char[] getCompletionProposalAutoActivationCharacters() {
			return new char[] { 46 };// ��"."
		}

		public IContextInformation[] computeContextInformation(
				ITextViewer viewer, int offset) {
			return null;
		}

		public char[] getContextInformationAutoActivationCharacters() {
			return null;
		}

		public IContextInformationValidator getContextInformationValidator() {
			return null;
		}

		public String getErrorMessage() {
			return null;
		}

	}
}