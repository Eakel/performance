import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
 * StyledText测试类
 * 
 * @author
 * 
 */
public class TextStyledText extends ApplicationWindow {

	private StyledText styledText;
	private Action actionRun;
	private SourceViewer sourceViewer_source;
	private List<String> optionList = new ArrayList<String>();

	public TextStyledText() {
		super(null);
		actionRun = new Action("执行") {
			public void run() {
				MessageDialog.openInformation(getShell(), "信息", "还没有做呢，在这增加自己的功能");
			}
		};
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window
	 * 
	 * @param parent
	 */
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
		tabItem.setText("结果");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		tabItem.setControl(composite);

		final TextViewer trageTextViewer = new TextViewer(composite, SWT.BORDER);
		styledText = trageTextViewer.getTextWidget();
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sashForm.setWeights(new int[] { 1, 1 });

		initData();
		initListener();

		return container;
	}

	private void initData() {
		String[] options = new String[] { "From", "To", "Table", "order" };
		Arrays.sort(options);// 排序一下
		for (String option : options) {
			optionList.add(option.toLowerCase());
		}
		/*
		 * 自动完成功能一般在以下两种条件下弹出一个小窗口向用户提示当前可供选择的选项，一是用户按下指定的组合键时，二是用户输入了特定的字符时，
		 * SourceViewer支持这两种触发方式。在程序里使用SourceViewer和使用一般控件没有很大的分别，只是SourceViewer
		 * 是StyledText的包装，所以一些操作要通过getTextWidget()完成，如下所示：
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
				} else if (event.character == ' ') {// 输入空格后着色
					String text = sourceViewer_source.getTextWidget().getText();
					if (text.length() > 0) {
						int p = text.lastIndexOf(" ");
						String rengText = text;
						if (p != -1) {
							rengText = text.substring(p + 1);
							p = p + 1;
						} else {
							p = 0;
						}
						if (rengText.length() > 0 && optionList.contains(rengText.toLowerCase())) {
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
		menuItem.setText("执行");
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(getShell(), "信息", "还没有做呢，在这增加自己的功能");
			}
		});

		sourceViewer_source.getTextWidget().setMenu(menu);
	}

	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		toolBarManager.add(actionRun);
		return toolBarManager;
	}

	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage(null, "");
		return statusLineManager;
	}

	public static void main(String args[]) {
		try {
			TextStyledText window = new TextStyledText();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Text StyledText ");
	}

	protected Point getInitialSize() {
		return new Point(500, 375);
	}

	/**
	 * 结果数组将作为弹出提示窗口里的选项
	 */
	class MyContentAssistProcessor implements IContentAssistProcessor {
		public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
			try {
				String text = viewer.getTextWidget().getText();
				String[] options = optionList.toArray(new String[optionList.size()]);
				if (text != null && text.length() > 0) {
					int p = text.lastIndexOf(" ");
					if (p != -1) {
						text = text.substring(p + 1);
					}
					Vector<String> v = new Vector<String>(1);
					for (String option : options) {
						if (text.length() < option.length() && option.substring(0, text.length()).equalsIgnoreCase(text)) {
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
					CompletionProposal proposal = new CompletionProposal(options[i], offset - text.length(), text.length(), options[i].length());
					result.add(proposal);
				}
				return (ICompletionProposal[]) result.toArray(new ICompletionProposal[result.size()]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public char[] getCompletionProposalAutoActivationCharacters() {
			return new char[] { 46 };// 点"."
		}

		public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
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