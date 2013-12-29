package com.easyfun.eclipse.performance.other.memcached.model;

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

import com.easyfun.eclipse.component.kv.MenuUtils;
import com.easyfun.eclipse.util.ui.ColumnViewerSorter;

/**
 * MemModel TableViewer
 * 
 * @author linzhaoming
 *
 * 2011-5-15
 *
 */
public class MemTableViewer extends TableViewer {
	
	private String[] colNames = new String[] { "Key", "Value", "Desc" };
	
	private int[] widths = new int[] { 150, 200, 400 };
	
	public MemTableViewer(Composite parent){
		super(parent, SWT.FULL_SELECTION);
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
				if(element instanceof MemModel){
					return ((MemModel) element).getKey();
				}else{
					return element.toString();
				}
				
			}
		});
		ColumnViewerSorter cSorter = new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemModel && e2 instanceof MemModel){
					MemModel p1 = (MemModel) e1;
					MemModel p2 = (MemModel) e2;
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
				if(element instanceof MemModel){
					return ((MemModel) element).getValue();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[2]);
		if(widths != null){
			column.getColumn().setWidth(widths[2]);
		}else{
			column.getColumn().setWidth(100);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof MemModel){
					return ((MemModel) element).getDesc();
				}else{
					return element.toString();
				}
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				MemModel p1 = (MemModel) e1;
				MemModel p2 = (MemModel) e2;
				return p1.getDesc().compareTo(p2.getDesc());
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
				IStructuredSelection selection = (IStructuredSelection)MemTableViewer.this.getSelection();
				Object selected = selection.getFirstElement();
				if(selected instanceof MemModel){
					final MemModel kv = (MemModel)selected;
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
		
		MemTableViewer.this.getTable().setMenu(parenMenu);
		
		cSorter.setSorter(cSorter, ColumnViewerSorter.ASC);
	}
	
}
