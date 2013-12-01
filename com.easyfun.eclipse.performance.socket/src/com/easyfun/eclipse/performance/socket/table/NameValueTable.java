package com.easyfun.eclipse.performance.socket.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * @author zhaoming
 */
public class NameValueTable extends Composite {
	private static final String NAME = "Name";
	private static final String VALUE = "Value";

	private static final String DEFAULT_INDEX = "defaultIndex"; 
	private static final String DEFAULT_VALUE = "value"; 
	private static final String DEFAULT_NAME = "name"; 

	private Button addButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;
	
	private TableViewer tableViewer;
	private Label tableLabel;
	
	private ButtonsHandler buttonsHandler;
	
	private boolean buttonsEnabled=true;
	
	public static class TableNameValueHolder {
		private String name;
		private String value;

		public TableNameValueHolder(String name, String value) {
			if(name==null){
				this.name="'";
			}else{
				this.name = name;
			}
			if(value==null){
				this.value="";
			}else{
				this.value = value;
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String toString() {
			return "[" + name + value + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static class ButtonsHandler{
	protected TableViewer tableViewer;
		
		public ButtonsHandler(TableViewer tableViewer){
			this.tableViewer=tableViewer;
		}
		
		public void handleAddButtonClicked(){
			TableNameValueHolder eventParameter = getDefaultParameter();
			List model=(List)tableViewer.getInput();
			if(model==null){
				return;
			}
			model.add(eventParameter);
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(eventParameter), true);
			tableViewer.getTable().select(model.indexOf(eventParameter));
		}
		
		public void handleRemoveButtonClicked(){
			ISelection selection = tableViewer.getSelection();
			if (selection == null || !(selection instanceof IStructuredSelection))
				return;

			Iterator<?> iter = ((IStructuredSelection) selection).iterator();
			List model=(List)tableViewer.getInput();
			if(model==null){
				return;
			}
			while (iter.hasNext()) {
				model.remove(iter.next());

			}
			tableViewer.refresh();
			tableViewer.setSelection(null, true);
			tableViewer.getTable().deselectAll();
		}
		
		public void handleUpButtonClicked(){	
			ISelection selection = tableViewer.getSelection();

			if (selection != null && selection instanceof IStructuredSelection) {
				Object parameter = ((IStructuredSelection) selection).getFirstElement();

				List model=(List)tableViewer.getInput();
				if(model==null){
					return;
				}
				
				int index = model.indexOf(parameter);
				if (index != -1 && index > 0) {
					model.remove(index);
					model.add(index - 1, (TableNameValueHolder)parameter);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(parameter), true);
					tableViewer.getTable().select(model.indexOf(parameter));
				}
			}
		}
		
		public void handleDownButtonClicked(){
			ISelection selection = tableViewer.getSelection();

			if (selection != null && selection instanceof IStructuredSelection) {
				Object parameter = ((IStructuredSelection) selection)
						.getFirstElement();
				List model=(List)tableViewer.getInput();
				int index = model.indexOf(parameter);

				if (index != -1 && index < model.size() - 1) {
					model.remove(index);
					model.add(index + 1, (TableNameValueHolder)parameter);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(parameter),true);
					tableViewer.getTable().select(model.indexOf(parameter));
				}
			}
		}
		
		public void handleTableEdited(){
			//do nothing here
		}
		
		private TableNameValueHolder getDefaultParameter() {
			Object obj = tableViewer.getData(DEFAULT_INDEX);
			String defaultName = DEFAULT_NAME;
			String defaultValue = DEFAULT_VALUE;
			if (obj != null) {
				int i = ((Integer) obj).intValue();
				defaultName += i;
				defaultValue += i;
				tableViewer.setData(DEFAULT_INDEX, new Integer(++i));
			} else {
				tableViewer.setData(DEFAULT_INDEX, new Integer(0));
			}
			return new TableNameValueHolder(defaultName, defaultValue);
		}
	}
	
	public NameValueTable(Composite parent, int style) {
		super(parent, style);
		Composite parameterComposite=new Composite(parent, SWT.NULL);
		parameterComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,2,1));
		GridLayout layout=new GridLayout(2,false);
		layout.marginWidth=0;
		parameterComposite.setLayout(layout);
		
		tableLabel = new Label(parameterComposite, SWT.NULL);
		tableLabel.setText("Config the parameters for the request");
		tableLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2,1));
		tableLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		createParameterControl(parameterComposite);
		createButtonPanel(parameterComposite);
	}

	public void setInput(List<TableNameValueHolder> model){
		tableViewer.setInput(model);
	}
	
	public void setTableLabel(String label){
		tableLabel.setText(label);
	}
	
	public TableViewer getTableViewer(){
		return tableViewer;
	}
	
	public void setButtonsHandler(ButtonsHandler buttonsHandler){
		this.buttonsHandler=buttonsHandler;
	}
	
	public void setNameReadOnly(boolean readOnly){
		((TableCellModifier)tableViewer.getCellModifier()).setNameReadOnly(readOnly);
	}
	
	public void setButtonsStatus(boolean enabled){
		this.buttonsEnabled=enabled;
		updateButtons(null);
	}	
	
	private void createParameterControl(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		
		TableLayout layout = new TableLayout();
		table.setLayout(layout);

		TableColumn col = new TableColumn(table, SWT.NULL, 0);
		col.setResizable(true);
		col.setText(NAME);
		layout.addColumnData(new ColumnWeightData(30));	

		col = new TableColumn(table, SWT.NULL, 1);
		col.setText(VALUE);
		col.setResizable(true);
		layout.addColumnData(new ColumnWeightData(70));	

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		// enable or disable the related buttons regards of selection
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						updateButtons(event);
					}
				});

		tableViewer.setCellModifier(new TableCellModifier());

		tableViewer.setCellEditors(new CellEditor[] {new TextCellEditor(table), new TextCellEditor(table) });
		tableViewer.setColumnProperties(new String[] { NAME, VALUE });
	}
	
	private void createButtonPanel(Composite buttonsGroup) {
		addButton = new Button(buttonsGroup, SWT.NULL);
		addButton.setText("  Add  ");
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buttonsHandler.handleAddButtonClicked();
			}
		});

		removeButton = new Button(buttonsGroup, SWT.NULL);
		removeButton.setText("  Remove  ");
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,true));
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buttonsHandler.handleRemoveButtonClicked();
			}
		});

		upButton = new Button(buttonsGroup, SWT.NULL);
		upButton.setText("  Up  ");
		upButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buttonsHandler.handleUpButtonClicked();
			}
		});

		downButton = new Button(buttonsGroup, SWT.NULL);
		downButton.setText("  Down  ");
		downButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buttonsHandler.handleDownButtonClicked();
			}
		});
	}

	private void updateButtons(SelectionChangedEvent event) {
		if(buttonsEnabled==false){
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			return;
		}
		ISelection selection = event.getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			removeButton.setEnabled(true);
			Object parameter = ((IStructuredSelection) selection).getFirstElement();
			List model=(List)tableViewer.getInput();
			if(model==null){
				return;
			}
			int index = model.indexOf(parameter);

			if (index > 0) {
				upButton.setEnabled(true);
			} else {
				upButton.setEnabled(false);
			}

			if (index < model.size() - 1) {
				downButton.setEnabled(true);
			} else {
				downButton.setEnabled(false);
			}
		} else {
			removeButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		}
	}

	
	private class TableCellModifier implements ICellModifier{

		private boolean nameReadOnly=false;
		
		public boolean canModify(Object element, String property) {
			if(nameReadOnly && NAME.equals(property)){
				return false;
			}
			return true;
		}
		public Object getValue(Object element, String property) {
			Assert.isTrue(element instanceof TableNameValueHolder);
			if (NAME.equals(property))
				return ((TableNameValueHolder) element).getName();
			else
				return ((TableNameValueHolder) element).getValue();
		}

		public void modify(Object element, String property, Object value) {
			Assert.isTrue(element instanceof TableItem);
			TableItem tableItem = (TableItem) element;
			Object data = tableItem.getData();
			if (data instanceof TableNameValueHolder) {
				List model=(List)tableViewer.getInput();
				int index = model.indexOf(data);
				if (index != -1) {
					TableNameValueHolder parameter = (TableNameValueHolder) model.get(index);
					if (parameter != null) {
						if (NAME.equals(property) && value.toString().length() != 0) {
							parameter.setName(value.toString());
						} else if (VALUE.equals(property)) {
							parameter.setValue(value.toString());
						}
						tableViewer.refresh();
						buttonsHandler.handleTableEdited();
					}
				}
			}
		}
		
		public void setNameReadOnly(boolean nameReadOnly){
			this.nameReadOnly=nameReadOnly;
		}
	}
	
	private class TableLabelProvider implements ITableLabelProvider {
		protected List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();

		public Image getColumnImage(Object element, int columnIndex) {
			return (null);
		}

		public String getColumnText(Object element, int columnIndex) {
			String value = null;
			switch (columnIndex) {
			case 0:
				if (element != null) {
					value = ((TableNameValueHolder) element).getName();
				}
				return (value);
			case 1:
				if (element != null) {
					value = (((TableNameValueHolder) element).getValue());
				}
				return value;
			default:
				break;
			}
			return (null);
		}

		public void dispose() {
			listeners.clear();
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void addListener(ILabelProviderListener listener) {
			listeners.add(listener);
		}

		public void removeListener(ILabelProviderListener listener) {
			listeners.remove(listener);
		}

	}
}
