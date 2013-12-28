package com.easyfun.eclipse.performance.awr.model;

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
import com.easyfun.eclipse.common.ui.kv.MenuUtils;

/**
 * AWRTableViewer TableViewer
 * 
 * @author linzhaoming
 *
 * 2011-5-15
 *
 */
public class AWRTableViewer extends TableViewer {
	
	private String[] colNames = null;
	
	private int[] widths = null;
	
	public AWRTableViewer(Composite parent, String[] colNames){
		super(parent, SWT.FULL_SELECTION|SWT.MULTI);
		this.colNames = colNames;
		init(this);
	}
	
	public AWRTableViewer(Composite parent, String[] colNames, int[] widths){
		super(parent, SWT.FULL_SELECTION|SWT.MULTI);
		this.colNames = colNames;
		this.widths = widths;
		init(this);
	}
	
	private void init(TableViewer tableViewer){
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		//1 SNAP_ID
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[0]);
		if(widths != null){
			column.getColumn().setWidth(widths[0]);
		}else{
			column.getColumn().setWidth(100);
		}
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getSnapId() + "";
			}
		});
		ColumnViewerSorter cSorter = new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				return Long.valueOf(p1.getSnapId()).compareTo(Long.valueOf(p2.getSnapId()));
			}
		};

		//2 BEGIN_DATE
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
				return ((SnapShot) element).getBeginDate();
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				Long vlaue1 = Long.valueOf(p1.getBeginDate());
				Long value2 = Long.valueOf(p2.getBeginDate());
				return vlaue1.compareTo(value2);
			}
		};
		
		//3 END_DATE
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
				return ((SnapShot) element).getEndDate();
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				Long vlaue1 = Long.valueOf(p1.getEndDate());
				Long value2 = Long.valueOf(p2.getEndDate());
				return vlaue1.compareTo(value2);
			}
		};
		
		//4 DBID
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[3]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(200);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getDbId() + "";
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				Long vlaue1 = Long.valueOf(p1.getDbId());
				Long value2 = Long.valueOf(p2.getDbId());
				return vlaue1.compareTo(value2);
			}
		};
		
		//5 INSTANCE_NUMBER
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[4]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(150);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getInstanceNumber() + "";
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				Long vlaue1 = Long.valueOf(p1.getInstanceNumber());
				Long value2 = Long.valueOf(p2.getInstanceNumber());
				return vlaue1.compareTo(value2);
			}
		};
		
		//6 INSTANCE_NAME
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[5]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(150);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getInstanceName();
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				return p1.getInstanceName().compareTo(p2.getInstanceName());
			}
		};
		
		//7 VERSION
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[6]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(100);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getVersion();
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				return p1.getVersion().compareTo(p2.getVersion());
			}
		};
		
		//8 GET_HOST_NAME
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText(colNames[7]);
		if(widths != null){
			column.getColumn().setWidth(widths[1]);
		}else{
			column.getColumn().setWidth(200);
		}
		column.getColumn().setMoveable(false);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SnapShot) element).getHostname();
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				SnapShot p1 = (SnapShot) e1;
				SnapShot p2 = (SnapShot) e2;
				return p1.getHostname().compareTo(p2.getHostname());
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
				IStructuredSelection selection = (IStructuredSelection)AWRTableViewer.this.getSelection();
				Object selected = selection.getFirstElement();
				if(selected instanceof SnapShot){
					final SnapShot snapShot = (SnapShot)selected;
					final String text = snapShot.toString();
					
					MenuItem menuItem = new MenuItem(copyMenu, SWT.PUSH);		
					menuItem.setText(text);
					menuItem.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							final Clipboard cb = new Clipboard(copyMenu.getShell().getDisplay()); 
							String rtfData = "{\\rtf1 \\b\\i " + text + "}"; 
							TextTransfer textTransfer = TextTransfer.getInstance(); 
							RTFTransfer rtfTransfer = RTFTransfer.getInstance(); 
							Transfer[] types = new Transfer[] { textTransfer, rtfTransfer }; 
							cb.setContents(new Object[] { text,rtfData}, types); 
						}
					});
				}
			}
		});
		
		AWRTableViewer.this.getTable().setMenu(parenMenu);
		
		cSorter.setSorter(cSorter, ColumnViewerSorter.DESC);
	}
	
}
