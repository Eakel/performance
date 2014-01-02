package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public final class ImageUtil {
	private static transient Log log = LogFactory.getLog(ImageUtil.class);

	private static String[] COLOR = { "#EE82EE", "#FFFF00", "#191970",
			"#FFD700", "#FF1493", "#006400", "#DC143C", "#7FFF00", "#A52A2A",
			"#F0F8FF", "#FAEBD7", "#00FFFF", "#7FFFD4", "#F0FFFF", "#F5F5DC",
			"#FFE4C4", "#000000", "#FFEBCD", "#0000FF", "#8A2BE2", "#DEB887",
			"#5F9EA0", "#D2691E", "#FF7F50", "#6495ED", "#FFF8DC", "#00FFFF",
			"#00008B", "#008B8B", "#B8860B", "#A9A9A9", "#BDB76B", "#8B008B",
			"#556B2F", "#FF8C00", "#9932CC", "#8B0000", "#E9967A", "#8FBC8B",
			"#483D8B", "#2F4F4F", "#00CED1", "#9400D3", "#00BFFF", "#696969",
			"#1E90FF", "#B22222", "#FFFAF0", "#228B22", "#FF00FF", "#DCDCDC",
			"#F8F8FF", "#DAA520", "#808080", "#008000", "#ADFF2F", "#F0FFF0",
			"#FF69B4", "#CD5C5C", "#4B0082", "#FFFFF0", "#F0E68C", "#E6E6FA",
			"#FFF0F5", "#7CFC00", "#FFFACD", "#ADD8E6", "#F08080", "#E0FFFF",
			"#FAFAD2", "#90EE90", "#D3D3D3", "#FFB6C1", "#FFA07A", "#20B2AA",
			"#87CEFA", "#778899", "#B0C4DE", "#FFFFE0", "#00FF00", "#32CD32",
			"#FAF0E6", "#FF00FF", "#800000", "#66CDAA", "#0000CD", "#BA55D3",
			"#9370DB", "#3CB371", "#7B68EE", "#00FA9A", "#48D1CC", "#C71585",
			"#F5FFFA", "#FFE4E1", "#FFE4B5", "#FFDEAD", "#000080", "#FDF5E6",
			"#808000", "#6B8E23", "#FFA500", "#FF4500", "#DA70D6", "#EEE8AA",
			"#98FB98", "#AFEEEE", "#DB7093", "#FFEFD5", "#FFDAB9", "#CD853F",
			"#FFC0CB", "#DDA0DD", "#B0E0E6", "#800080", "#FF0000", "#BC8F8F",
			"#4169E1", "#8B4513", "#FA8072", "#F4A460", "#2E8B57", "#FFF5EE",
			"#A0522D", "#C0C0C0", "#87CEEB", "#6A5ACD", "#708090", "#FFFAFA",
			"#00FF7F", "#4682B4", "#D2B48C", "#008080", "#D8BFD8", "#FF6347",
			"#40E0D0", "#F5DEB3", "#FFFFFF", "#F5F5F5", "#9ACD32" };

	private static final HashMap REGION_MAP = new HashMap();
	

	static {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("RegionId.properties");
		if (is != null) {
			try {
				Properties p = new Properties();
				p.load(is);

				Set set = p.keySet();
				for (Iterator iter = set.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					REGION_MAP.put(p.getProperty(item), item);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			log.info("无法获得RegionId.properties文件,采用默认的河南配置");
			REGION_MAP.put("鹤壁", "F");
			REGION_MAP.put("郑州", "A");
			REGION_MAP.put("漯河", "L");
			REGION_MAP.put("许昌", "K");
			REGION_MAP.put("洛阳", "C");
			REGION_MAP.put("新乡", "G");
			REGION_MAP.put("开封", "B");
			REGION_MAP.put("驻马店", "Q");
			REGION_MAP.put("安阳", "E");
			REGION_MAP.put("信阳", "S");
			REGION_MAP.put("南阳", "R");
			REGION_MAP.put("焦作", "H");
			REGION_MAP.put("周口", "P");
			REGION_MAP.put("濮阳", "J");
			REGION_MAP.put("三门峡", "M");
			REGION_MAP.put("商丘", "N");
			REGION_MAP.put("平顶山", "D");
		}

		log.info("地市对应关系:" + REGION_MAP);
	}

	/** 根据HashMap内容创建图片，写入OutputStream*/
	public static void createImage(String titleName, String xName, String yName, HashMap map, OutputStream output) throws Exception {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();

		Set key = map.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String lineName = (String) iter.next();
			TimeSeries objTimeSeries = new TimeSeries(lineName, Millisecond.class);
			timeseriescollection.addSeries(objTimeSeries);
			List l = (List) map.get(lineName);
			for (Iterator iter2 = l.iterator(); iter2.hasNext();) {
				LineValue objitem = (LineValue) iter2.next();
				objTimeSeries.addOrUpdate(new Millisecond(objitem.date), objitem.value);
			}
		}
		DateAxis dateaxis = new DateAxis(xName);
		dateaxis.setTickLabelFont(new Font("宋体", 0, 12));
		dateaxis.setLabelFont(new Font("宋体", 0, 14));
		dateaxis.setAutoRange(true);
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);
		dateaxis.setTickLabelsVisible(true);

		NumberAxis numberaxis = new NumberAxis(yName);
		numberaxis.setTickLabelFont(new Font("宋体", 0, 12));
		numberaxis.setLabelFont(new Font("宋体", 0, 14));
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);

		int i = 0;
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			xylineandshaperenderer.setSeriesPaint(i, Color.decode(COLOR[i]));
			i++;
		}

		xylineandshaperenderer.setStroke(new BasicStroke(3.0F, 0, 2));

		XYPlot xyplot = new XYPlot(timeseriescollection, dateaxis, numberaxis, xylineandshaperenderer);
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5.0D, 5.0D, 5.0D, 5.0D));

		JFreeChart jfreechart = new JFreeChart(titleName, new Font("宋体", 1, 24), xyplot, true);
		jfreechart.setBackgroundPaint(Color.white);

		BufferedImage objBufferedImage = jfreechart.createBufferedImage(800, 600);
		ImageIO.write(objBufferedImage, "JPEG", output);
	}

	public static String createFlexMSAreaXml(String titleName, HashMap map) throws Exception {
		HashMap xml = new HashMap();
		TreeMap time = new TreeMap();
		Set key = map.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String lineName = (String) iter.next();
			List l = (List) map.get(lineName);
			HashMap tmp = new HashMap();
			for (Iterator iter2 = l.iterator(); iter2.hasNext();) {
				LineValue objitem = (LineValue) iter2.next();
				String tmpTime = TimeUtil.format7(objitem.date);
				tmp.put(tmpTime, "<set value='" + objitem.value + "'/>");
				time.put(tmpTime, null);
			}
			xml.put(lineName, tmp);
		}

		StringBuffer rtn = new StringBuffer();
		rtn.append("<chart bgColor='E9E9E9' baseFontSize='15' numberSuffix='%' outCnvBaseFontColor='666666' caption='"
				+ titleName + "' showNames='0' showValues='0' plotFillAlpha='50' numVDivLines='10' showAlternateVGridColor='1' AlternateVGridColor='e1f5ff' divLineColor='e1f5ff' vdivLineColor='e1f5ff'  baseFontColor='666666' canvasBorderThickness='1' showPlotBorder='1' plotBorderThickness='0'>");
		rtn.append("<categories>");
		Set key2 = time.keySet();
		for (Iterator iter = key2.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			rtn.append("<category label='" + item + "' />");
		}
		rtn.append("</categories>");

		Set key3 = xml.keySet();
		for (Iterator iter = key3.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String shortName = item;
			rtn.append("<dataset seriesName='" + shortName + "'>");
			HashMap tmp = (HashMap) xml.get(item);
			Set timeSet = time.keySet();
			for (Iterator iter2 = timeSet.iterator(); iter2.hasNext();) {
				String tmpTime = (String) iter2.next();
				if (!tmp.containsKey(tmpTime)) {
					rtn.append("<set value='0'/>");
				} else {
					rtn.append(tmp.get(tmpTime).toString());
				}
			}
			rtn.append("</dataset>");
		}

		rtn.append("</chart>");

		return rtn.toString();
	}

	public static String createFlexMSLineXml(String titleName, HashMap map) throws Exception {
		HashMap xml = new HashMap();
		TreeMap time = new TreeMap();
		Set key = map.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String lineName = (String) iter.next();
			List l = (List) map.get(lineName);
			HashMap tmp = new HashMap();
			for (Iterator iter2 = l.iterator(); iter2.hasNext();) {
				LineValue objitem = (LineValue) iter2.next();
				String tmpTime = TimeUtil.format7(objitem.date);
				tmp.put(tmpTime, "<set value='" + objitem.value + "'/>");
				time.put(tmpTime, null);
			}
			xml.put(lineName, tmp);
		}

		StringBuffer rtn = new StringBuffer();
		rtn.append("<chart caption='" + titleName + "' baseFontSize='15' anchorRadius='2' showNames='0' showValues='0' numberSuffix='%'>");
		rtn.append("<categories>");
		Set key2 = time.keySet();
		for (Iterator iter = key2.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			rtn.append("<category label='" + item + "' />");
		}
		rtn.append("</categories>");

		Set key3 = xml.keySet();
		for (Iterator iter = key3.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String shortName = item;
			rtn.append("<dataset seriesName='" + shortName + "'>");
			HashMap tmp = (HashMap) xml.get(item);
			Set timeSet = time.keySet();
			for (Iterator iter2 = timeSet.iterator(); iter2.hasNext();) {
				String tmpTime = (String) iter2.next();
				if (!tmp.containsKey(tmpTime)) {
					rtn.append("<set value='0'/>");
				} else {
					rtn.append(tmp.get(tmpTime).toString());
				}
			}
			rtn.append("</dataset>");
		}

		rtn.append("</chart>");

		return rtn.toString();
	}

	public static String createFlexXml(String titleName, HashMap map) throws Exception {
		HashMap xml = new HashMap();
		TreeMap time = new TreeMap();
		Set key = map.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String lineName = (String) iter.next();
			List l = (List) map.get(lineName);
			HashMap tmp = new HashMap();
			for (Iterator iter2 = l.iterator(); iter2.hasNext();) {
				LineValue objitem = (LineValue) iter2.next();
				String tmpTime = getYYYYMMDDHH(objitem.date);
				tmp.put(tmpTime, "<set value='" + objitem.value + "'/>");
				time.put(tmpTime, null);
			}
			xml.put(lineName, tmp);
		}

		StringBuffer rtn = new StringBuffer();
		rtn.append("<chart baseFontSize='18' palette='1' caption='" + titleName
				+ "' shownames='1' numVisiblePlot='51' showvalues='0'  showLabels='1' useRoundEdges='1' legendBorderAlpha='0' legendMarkerCircle='1'>");
		rtn.append("<categories>");
		Set key2 = time.keySet();
		for (Iterator iter = key2.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			rtn.append("<category label='" + item + "' />");
		}
		rtn.append("</categories>");

		Set key3 = xml.keySet();
		for (Iterator iter = key3.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String shortName = getShortName(item);
			rtn.append("<dataset seriesName='" + shortName + "'>");
			HashMap tmp = (HashMap) xml.get(item);
			Set timeSet = time.keySet();
			for (Iterator iter2 = timeSet.iterator(); iter2.hasNext();) {
				String tmpTime = (String) iter2.next();
				if (!tmp.containsKey(tmpTime)) {
					rtn.append("<set value='0'/>");
				} else {
					rtn.append(tmp.get(tmpTime).toString());
				}
			}
			rtn.append("</dataset>");
		}

		rtn.append("</chart>");

		return rtn.toString();
	}

	private static String getYYYYMMDDHH(Date date) {
		String end = TimeUtil.getHH(date);
		Date d = TimeUtil.addOrMinusHours(date.getTime(), -1);
		String rtn = TimeUtil.getYYYYMMDDHH(d) + "-" + end;
		return rtn;
	}

	private static String getShortName(String name) {
		String rtn = null;
		Set key = REGION_MAP.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			if (StringUtils.contains(name, item)) {
				rtn = item;
				break;
			}
		}
		return rtn;
	}

}