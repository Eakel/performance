package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class MyBarChart extends JFrame {
	ChartPanel cp;

	public MyBarChart() {
		this.cp = new ChartPanel(createChart());
		add(this.cp);
		setSize(400, 300);
		setDefaultCloseOperation(3);
		setVisible(true);
	}

	private static JFreeChart createChart() {
		JFreeChart chart = null;

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		dataSet.addValue(100.0D, "sony", "a1");

		dataSet.addValue(60.0D, "摩托罗拉", "手机品牌");
		dataSet.addValue(40.0D, "索爱", "手机品牌");
		dataSet.addValue(10.0D, "三星", "手机品牌");

		chart = ChartFactory.createBarChart3D("2006手机销售柱状图", "2006年", "销售数量", dataSet, PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}

	public static void main(String[] args) {
		new MyBarChart();
	}
}