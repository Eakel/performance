package com.easyfun.eclipse.performance.http.views;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * Supper class for Http Header Table.
 * <p>Known subclasses are HttpRequest and HttpResponse.
 * 
 * @author linzhaoming
 *
 */
public abstract class HttpHeaderTable extends Composite {
	protected static String[] columnNames = new String[] { "name", "value" };

	protected TableViewer tableViewer = null;

	public HttpHeaderTable(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridLayout(3, false));
		final Table table = new Table(this, SWT.FULL_SELECTION|SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 2));
		table.setLinesVisible(true);
		
		TableColumn tableColumnName = new TableColumn(table, SWT.NONE);
		tableColumnName.setWidth(300);
		tableColumnName.setText("Header Name");
		
		TableColumn tableColumnValue = new TableColumn(table, SWT.NONE);
		tableColumnValue.setWidth(300);
		tableColumnValue.setText("Header Value");
		
		tableViewer = new TableViewer(table);	
		tableViewer.setLabelProvider(new HeaderLabelProvider());
		tableViewer.setColumnProperties(columnNames);
					
		hookTableViewer(tableViewer, this);
		
		final HttpSorter nameColumnSorter = new HttpSorter(columnNames[0]);
		table.getColumn(0).addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableViewer.getSorter() == nameColumnSorter) {
					// Same column selected for sort: reverse sort order;
					table.setSortDirection((table.getSortDirection() == SWT.UP) ? SWT.DOWN : SWT.UP);
					nameColumnSorter.toggle();
					tableViewer.refresh();
				} else {
					table.setSortColumn(table.getColumn(0));
					table.setSortDirection(SWT.UP);
					tableViewer.setSorter(nameColumnSorter);
				}
			}
		});

		final HttpSorter valueColumnSorter = new HttpSorter(columnNames[1]);
		table.getColumn(1).addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableViewer.getSorter() == valueColumnSorter) {
					// Same column selected for sort: reverse sort order;
					table.setSortDirection((table.getSortDirection() == SWT.UP) ? SWT.DOWN : SWT.UP);
					valueColumnSorter.toggle();
					tableViewer.refresh();
				} else {
					table.setSortColumn(table.getColumn(1));
					table.setSortDirection(SWT.UP);
					tableViewer.setSorter(valueColumnSorter);
				}
			}

		});
		table.setSortColumn(table.getColumn(0));
		table.setSortDirection(SWT.UP);
	}
	
	/**
	 * Subclass should hook the TableViewer through this method.
	 * @param tableViewer
	 * @param parent
	 */
	protected void hookTableViewer(TableViewer tableViewer, Composite parent){
		
	}

	public void setInput(IHttpMethod method) {
		tableViewer.setInput(method);
	}
	
	//-------------------  	Providers ------------------------------
	public class HttpCellModifier implements ICellModifier {
		public boolean canModify(Object arg0, String arg1) {
			return true;
		}

		public Object getValue(Object element, String property) {
			Object result;
			List<String> colNameList = Arrays.asList(columnNames);
			int columnIndex = colNameList.indexOf(property);
			Header header = (Header) element;
			switch (columnIndex) {
			case 0: // Header Name
				result = header.getName();
				break;
			case 1: // Header
				result = header.getValue();
				break;
			default:
				result = "";
			}
			return result;
		}

		public void modify(Object element, String property, Object value) {
			List<String> colNameList = Arrays.asList(columnNames);
			int columnIndex = colNameList.indexOf(property);
			TableItem item = (TableItem) element;
			Header header = (Header) item.getData();
			switch (columnIndex) {
			case 0: // Header Name
				header.setName(value.toString());
				break;
			case 1: // Header
				header.setValue(value.toString());
				break;
			default:
				break;
			}
			tableViewer.update(header, null);
		}

	}

	private static class HeaderLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object arg0, int arg1) {
			return null; // No Icons
		}

		public String getColumnText(Object element, int columnIndex) {
			Header header = (Header) element;
			switch (columnIndex) {
			case 0: // Header Name
				return  header.getName();
			case 1: // Header Value
				return  header.getValue();
			default:
				return "";
			}
		}

	}

	private static class HttpSorter extends ViewerSorter {
		private String property;
		private int direction = 1;

		public HttpSorter(String property) {
			this.property = property;
		}

		public int compare(Viewer viewer, Object element1, Object element2) {
			int result = 0;
			Header header1 = (Header) element1;
			Header header2 = (Header) element2;
			if (property.equals(columnNames[0])) {
				result = direction * getComparator().compare(header1.getName(), header2.getName());
			} else if (property.equals(columnNames[1])) {
				result = direction * getComparator().compare(header1.getValue(), header2.getValue());
			}
			return result;
		}

		public boolean isSorterProperty(Object element, String property) {
			return this.property.equals(property);
		}

		public void toggle() {
			direction = -direction;
		}
	}
}
