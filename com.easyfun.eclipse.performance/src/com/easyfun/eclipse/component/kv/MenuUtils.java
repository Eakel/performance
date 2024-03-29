package com.easyfun.eclipse.component.kv;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.rcp.RCPUtil;
import com.easyfun.eclipse.util.TimeUtil;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;

public class MenuUtils {
	
	public static void addExportTxt(final TableViewer tableViewer, Menu menu){
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("导出为文本...");
		menuItem.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_FILE_PATH).createImage());
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String fileName = "导出文件" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".txt";
				File file = RCPUtil.openSaveDialog(tableViewer.getTable().getShell(), new String[]{"*.txt", "*.*"}, fileName);
				if(file == null){
					return;
				}
				
				TableColumn[] columns = tableViewer.getTable().getColumns();
				List<String> content = new ArrayList<String>();
				StringBuffer header = new StringBuffer();
				for (TableColumn column : columns) {
					header.append(column).append("\t");
				}
				content.add(header.toString());
				
				List<KeyValue> keyValues = (List<KeyValue>)tableViewer.getInput();
				for (KeyValue kv : keyValues) {
					content.add(kv.getKey() + "\t" + kv.getValue());
				}
				
				try {
					FileOutputStream out = new FileOutputStream(file);
					IOUtils.writeLines(content, null, out);
					out.close();
				} catch (Exception e1) {
					RCPUtil.showError(tableViewer.getTable().getShell(), "表格导出异常");
				}
			}
		});
	}
	
	public static void addExportHtml(final TableViewer tableViewer, Menu menu){
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("导出为Html...");
		menuItem.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_HTML_PATH).createImage());
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String fileName = "文件" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".html";
				File file = RCPUtil.openSaveDialog(tableViewer.getTable().getShell(), new String[]{"*.html", "*.*"}, fileName);
				if(file == null){
					return;
				}
				
				try {
					Properties ps = new Properties();
					ps.put("resource.loader", "class");
					ps.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
					Velocity.init(ps);
					
					TableColumn[] columns = tableViewer.getTable().getColumns();
					List<String> headers = new ArrayList<String>();
					for(int i=0; i<columns.length; i++){
						TableColumn column = columns[i];
						headers.add(column.getText());
					}
					VelocityContext context = new VelocityContext();
					context.put("htmlTitile", "文件信息");
					context.put("headers", headers);
					context.put("kvs", tableViewer.getInput());
					
					Template aVmTemplate =  Velocity.getTemplate("vm/tableOutput.vm");
					FileWriter writer = new FileWriter(file);
					aVmTemplate.merge(context, writer);
					writer.flush();
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					RCPUtil.showError(tableViewer.getTable().getShell(), "导出html文件出出错\n" + ex.getMessage());
				}
			}
		});
	}
	
	public static void addExportPDF(final TableViewer tableViewer, Menu menu){
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("导出为PDF...");
		menuItem.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_PDF_PATH).createImage());
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String fileName = "导出文件" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".pdf";
				File file = RCPUtil.openSaveDialog(tableViewer.getTable().getShell(), new String[]{"*.pdf", "*.*"}, fileName);
				if(file == null){
					return;
				}
				
				try {
					Document document = new Document(PageSize.A4, 50, 50, 50, 50);
					document.open();
					
					BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
					Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, Color.BLACK);
					document.add(new Paragraph("文件信息", fontChinese));
					
					
					List<KeyValue> keyValues = (List<KeyValue>)tableViewer.getInput();
					
					com.lowagie.text.Table table = new com.lowagie.text.Table(2,keyValues.size() +1 );
					table.setBorderWidth(1);
					
					TableColumn[] columns = tableViewer.getTable().getColumns();
					for(int i=0; i<columns.length; i++){
						TableColumn column = columns[i];
						Cell headerCell = new Cell(new Paragraph(column.getText(), fontChinese));
						headerCell.setHeader(true);
						table.addCell(headerCell);
					}
					table.endHeaders();
					
					for (KeyValue kv : keyValues) {
						Cell c1 = new Cell(kv.getKey());
						table.addCell(c1);
						c1 = new Cell(kv.getValue());
						table.addCell(c1);
					}
					document.add(table);
					document.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					RCPUtil.showError(tableViewer.getTable().getShell(), "导出PDF文件出出错\n" + ex.getMessage());
				}
			}
		});
	}
	
	public static void addExportExcel(final TableViewer tableViewer, Menu menu){
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("导出为Excel...");
		menuItem.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_EXEL_PATH).createImage());
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String fileName = "导出文件" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".xls";
				File file = RCPUtil.openSaveDialog(tableViewer.getTable().getShell(), new String[]{"*.xls", "*.*"}, fileName);
				if(file == null){
					return;
				}
				
				try {
					WritableWorkbook book = Workbook.createWorkbook(file);		
					WritableSheet summarySheet = book.createSheet("OBD调用", 0);	// i为页数量
					
					TableColumn[] columns = tableViewer.getTable().getColumns();
					for(int i=0; i<columns.length; i++){
						TableColumn column = columns[i];
						Label label = new Label(i, 0, column.getText());
						// 将定义好的单元格添加到工作表中
						summarySheet.addCell(label);
					}
					
					List<KeyValue> keyValues = (List<KeyValue>)tableViewer.getInput();
					for(int i=0; i<keyValues.size(); i++){
						KeyValue kv = keyValues.get(i);
						Label label = new Label(0, i + 1, kv.getKey());
						// 将定义好的单元格添加到工作表中
						summarySheet.addCell(label);
						if(NumberUtils.isNumber(kv.getValue())){
							jxl.write.Number number = new jxl.write.Number(1, i + 1, Integer.parseInt(kv.getValue()));
							summarySheet.addCell(number);
						}else{
							Label number = new Label(1, i + 1, kv.getValue());
							summarySheet.addCell(number);
						}

					}
					// 写入数据并关闭文件
					book.write();
					book.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					RCPUtil.showError(tableViewer.getTable().getShell(), "导出Excel文件出出错\n" + ex.getMessage());
				}
			}
		});
	}
	
	
}
