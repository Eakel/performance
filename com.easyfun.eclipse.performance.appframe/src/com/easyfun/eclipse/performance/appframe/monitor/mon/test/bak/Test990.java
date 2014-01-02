package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class Test990 {
	public static void main(String[] args) throws Exception {
		TimeSeries total = new TimeSeries("Total Memory", Hour.class);

		TimeSeries free = new TimeSeries("Free Memory", Hour.class);

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(total);
		timeseriescollection.addSeries(free);
		DateAxis dateaxis = new DateAxis("时间");
		NumberAxis numberaxis = new NumberAxis("内存");
		dateaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
		numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
		dateaxis.setLabelFont(new Font("SansSerif", 0, 14));
		numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);
		xylineandshaperenderer.setSeriesPaint(0, Color.red);
		xylineandshaperenderer.setSeriesPaint(1, Color.decode("#F0F8FF"));
		xylineandshaperenderer.setStroke(new BasicStroke(3.0F, 0, 2));
		XYPlot xyplot = new XYPlot(timeseriescollection, dateaxis, numberaxis, xylineandshaperenderer);
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5.0D, 5.0D, 5.0D, 5.0D));
		dateaxis.setAutoRange(true);
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);
		dateaxis.setTickLabelsVisible(true);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		JFreeChart jfreechart = new JFreeChart("内存使用", new Font("SansSerif", 1, 24), xyplot, true);
		jfreechart.setBackgroundPaint(Color.white);

		long start = System.currentTimeMillis() - 100000L;
		for (int i = 0; i < 200; i++) {
			Millisecond yh = new Millisecond();
			total.addOrUpdate(new Millisecond(new Date(start + i * 1000)), 10.0D);
			free.addOrUpdate(new Millisecond(new Date(start + i * 1000)), i);
		}

		int j = 200;
		for (int i = 200; i > 0; i--) {
			Millisecond yh = new Millisecond();
			total.addOrUpdate(new Millisecond(new Date(start + 1000000000L + j * 1000)), 10.0D);
			free.addOrUpdate(new Millisecond(new Date(start + 1000000000L + j * 1000)), i);
			j++;
		}
		BufferedImage objBufferedImage = jfreechart.createBufferedImage(1024, 1024);
		ImageIO.write(objBufferedImage, "JPEG", new File("D:/a.jpg"));
	}
}