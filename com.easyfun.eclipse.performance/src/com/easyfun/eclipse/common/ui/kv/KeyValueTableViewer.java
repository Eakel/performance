package com.easyfun.eclipse.common.ui.kv;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.easyfun.eclipse.common.ui.ColumnViewerSorter;

/**
 * KeyValue TableViewer
 * 
 * @author linzhaoming
 *
 * 2011-5-15
 *
 */
public class KeyValueTableViewer extends TableViewer {
	
	private String[] colNames = null;
	
	private int[] widths = null;
	
	public KeyValueTableViewer(Composite parent, String[] colNames){
		super(parent, SWT.FULL_SELECTION);
		Assert.isTrue(colNames != null && colNames.length == 2);
		this.colNames = colNames;
		init(this);
	}
	
	public KeyValueTableViewer(Composite parent, String[] colNames, int[] widths){
		super(parent, SWT.FULL_SELECTION);
		Assert.isTrue(colNames != null && colNames.length == 2);
		Assert.isTrue(widths != null && widths.length == 2);
		this.colNames = colNames;
		this.widths = widths;
		init(this);
	}
	
	private void init(TableViewer tableViewer){
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[0]);
		if(widths != null){
			column.getColumn().setWidth(widths[0]);
		}else{
			column.getColumn().setWidth(300);
		}
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof KeyValue){
					return ((KeyValue) element).getKey();
				}else{
					return element.toString();
				}
				
			}
		});
		ColumnViewerSorter cSorter = new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof KeyValue && e2 instanceof KeyValue){
					KeyValue p1 = (KeyValue) e1;
					KeyValue p2 = (KeyValue) e2;
					return p1.getKey().compareToIgnoreCase(p2.getKey());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}

		};

		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[1]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(100);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof KeyValue){
					return ((KeyValue) element).getValue();
				}else{
					return element.toString();
				}
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				KeyValue p1 = (KeyValue) e1;
				KeyValue p2 = (KeyValue) e2;
				Integer vlaue1 = Integer.valueOf(0);
				try {
					vlaue1 = Integer.valueOf(p1.getValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					vlaue1 = Integer.valueOf(0);
				}
				Integer value2 = Integer.valueOf(0);
				try {
					value2 = Integer.valueOf(p2.getValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					vlaue1 = Integer.valueOf(0);
				}
				return vlaue1.compareTo(value2);
			}
		};
		
		Menu parenMenu = new Menu(tableViewer.getTable());
		
		Menu menu = new Menu(tableViewer.getTable().getShell(), SWT.DROP_DOWN);
		MenuItem filterMenuItem = new MenuItem(parenMenu, SWT.CASCADE);
		filterMenuItem.setMenu(menu);
		filterMenuItem.setText("&µ¼³ö");
		
		MenuUtils.addExportTxt(tableViewer, menu);
		MenuUtils.addExportExcel(tableViewer, menu);
		MenuUtils.addExportHtml(tableViewer, menu);
		MenuUtils.addExportPDF(tableViewer, menu);
		
		final Menu copyMenu = new Menu(tableViewer.getTable().getShell(), SWT.DROP_DOWN);
		MenuItem copyMenuItem = new MenuItem(parenMenu, SWT.CASCADE);
		copyMenuItem.setMenu(copyMenu);
		copyMenuItem.setText("&Copy");	
		
		parenMenu.addMenuListener(new MenuAdapter(){
			public void menuShown(MenuEvent e) {
				MenuItem[] items = copyMenu.getItems();
				for (MenuItem item : items) {
					item.dispose();
				}
				IStructuredSelection selection = (IStructuredSelection)KeyValueTableViewer.this.getSelection();
				Object selected = selection.getFirstElement();
				if(selected instanceof KeyValue){
					final KeyValue kv = (KeyValue)selected;
					MenuItem menuItem = new MenuItem(copyMenu, SWT.PUSH);		
					menuItem.setText(kv.getKey());
					menuItem.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							final Clipboard cb = new Clipboard(copyMenu.getShell().getDisplay()); 
							String rtfData = "{\\rtf1 \\b\\i " + kv.getKey() + "}"; 
							TextTransfer textTransfer = TextTransfer.getInstance(); 
							RTFTransfer rtfTransfer = RTFTransfer.getInstance(); 
							Transfer[] types = new Transfer[] { textTransfer, rtfTransfer }; 
							cb.setContents(new Object[] { kv.getKey(),rtfData}, types); 
						}
					});
					
					menuItem = new MenuItem(copyMenu, SWT.PUSH);		
					menuItem.setText(kv.getKey() + "\t" + kv.getValue());
					menuItem.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							final Clipboard cb = new Clipboard(copyMenu.getShell().getDisplay()); 
							String rtfData = "{\\rtf1 \\b\\i " + kv.getKey() + "\t" + kv.getValue() + "}"; 
							TextTransfer textTransfer = TextTransfer.getInstance(); 
							RTFTransfer rtfTransfer = RTFTransfer.getInstance(); 
							Transfer[] types = new Transfer[] { textTransfer, rtfTransfer }; 
							cb.setContents(new Object[] { kv.getKey() + "\t" + kv.getValue(),rtfData}, types); 
						}
					});
				}
			}
		});
		
		KeyValueTableViewer.this.getTable().setMenu(parenMenu);
		
		cSorter.setSorter(cSorter, ColumnViewerSorter.ASC);
	}
	
}
