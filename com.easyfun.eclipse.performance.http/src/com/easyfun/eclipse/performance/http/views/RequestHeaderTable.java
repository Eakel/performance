package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeListener;

import org.apache.commons.httpclient.Header;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * The table for Request.
 * 
 * @author linzhaoming
 *
 */
public class RequestHeaderTable extends HttpHeaderTable {
	private CellEditor[] cellEditors;

	private AddAction addAction;
	private RemoveAction removeAction;
	
	private Button buttonAdd = null;
	private Button buttonRemove = null;

	public RequestHeaderTable(Composite parent, int style) {
		super(parent, style);
		addAction = new AddAction();
		removeAction = new RemoveAction();		
	}

	public final void setInput(IHttpMethod method) {
		super.setInput(method);
		if(buttonAdd != null){
			buttonAdd.setEnabled(method != null);
		}
	}

	protected void hookTableViewer(final TableViewer tableViewer, Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		
		this.buttonAdd = new Button(composite, SWT.NONE);
		buttonAdd.setText("Add");
		buttonAdd.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_ADD_PATH).createImage());
//		buttonAdd.setEnabled(false);
		GridData gridData = new GridData(GridData.CENTER,GridData.CENTER,false, false);
		gridData.widthHint = 80;
		buttonAdd.setLayoutData(gridData);
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addAction.run();
			}
		});
		buttonRemove = new Button(composite, SWT.NONE);
		buttonRemove.setText("Remove");
		buttonRemove.setEnabled(false);
		buttonRemove.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_DELETE_PATH).createImage());
		gridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData.widthHint = 80;
		buttonRemove.setLayoutData(gridData);
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeAction.run();
			}
		});
		
		cellEditors = new CellEditor[columnNames.length];		
		cellEditors[0] = new TextCellEditor(tableViewer.getTable());	// Column 0 : Header Name		
		cellEditors[1] = new TextCellEditor(tableViewer.getTable());	// Column 1 : Header Value
		
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new HttpCellModifier());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if(RequestHeaderTable.this.buttonRemove!=null){
					RequestHeaderTable.this.buttonRemove.setEnabled(selection.size() == 1);
				}				
			}
		});
		
		MenuManager manager = new MenuManager();
		Menu menu = manager.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(addAction);
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.size() == 1) {
					manager.add(new Separator());
					manager.add(removeAction);
				}
			}
		});
		
		tableViewer.setContentProvider(new RequestContentProvider(tableViewer));
	}
	
	//Action Definition.
	/**
	 * Add Header Action popup
	 * @author linzhaoming
	 *
	 */
	public class AddAction extends Action {
		public AddAction() {
			this.setEnabled(true);
			this.setText("Add Header");
			this.setToolTipText("Add Header");
		}

		public void run() {
			IHttpMethod method = (IHttpMethod) tableViewer.getInput();
			method.addRequestHeader(new Header("NewHeader", ""));
		}

	}

	/**
	 * Remove Header Action popup
	 * @author linzhaoming
	 *
	 */
	public class RemoveAction extends Action {
		public RemoveAction() {
			this.setEnabled(true);
			this.setText("Remove Header");
			this.setToolTipText("Remove Header");
		}

		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			Header header = (Header) selection.getFirstElement();
			if (header != null) {
				IHttpMethod method = (IHttpMethod) tableViewer.getInput();
				method.removeRequestHeader(header);
			}
		}

	}
	
	//------------- Providers --------------------------
	private static class RequestContentProvider implements IStructuredContentProvider, PropertyChangeListener {
		private TableViewer tableViewer;
		
		public RequestContentProvider(TableViewer tableViewer){
			this.tableViewer = tableViewer;
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return ((IHttpMethod) parent).getRequestHeaders();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				((IHttpMethod) oldInput).removePropertyChangeListener(IHttpMethod.REQUEST_HEADERS_PROP, this);
			}

			if (newInput != null) {
				((IHttpMethod) newInput).addPropertyChangeListener(IHttpMethod.REQUEST_HEADERS_PROP, this);
			}
		}

		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			tableViewer.refresh();
		}

	}
}
