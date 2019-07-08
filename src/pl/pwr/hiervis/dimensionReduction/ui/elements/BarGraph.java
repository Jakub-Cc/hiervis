package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarGraph {

    public static JPanel CreateBarGraph(double[] data, String[] labels, Color backgroundColor, Color barColor) {

	CategoryDataset dataset = createDataset(data, labels);
	JFreeChart chart = createChart(dataset, backgroundColor, barColor);

	ChartPanel chartPanel = new ChartPanel(chart, false);
	chartPanel.setPreferredSize(new Dimension(500, 270));
	chartPanel.setMouseZoomable(false);
	chartPanel.setPopupMenu(null);

	return chartPanel;
    }

    private static CategoryDataset createDataset(double[] data, String[] labels) {
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	for (int i = 0; i < data.length; i++) {
	    dataset.addValue(data[i], "", labels[i]);

	}
	return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset, Color backgroundColor, Color barColor) {
	String title = "Top " + dataset.getColumnCount() + " Scores";
	JFreeChart chart = ChartFactory.createBarChart(title, null, "Score", dataset);
	chart.setBackgroundPaint(backgroundColor);
	chart.removeLegend();

	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	plot.setBackgroundPaint(backgroundColor);
	plot.setRangeGridlinePaint(Color.BLACK);

	CategoryAxis domainAxis = plot.getDomainAxis();
	domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	BarRenderer renderer = (BarRenderer) plot.getRenderer();
	renderer.setDrawBarOutline(false);

	renderer.setBarPainter(new StandardBarPainter());

	CategoryToolTipGenerator tooltipGenerator = new CategoryToolTipGenerator() {

	    @Override
	    public String generateToolTip(CategoryDataset dataset, int series, int item) {
		// TODO tak

		String s = "#" + (item + 1) + " " + dataset.getColumnKey(item) + " (" + dataset.getValue(series, item) + ")";
		return s;
	    }
	};
	// and assign it to the renderer
	renderer.setBaseToolTipGenerator(tooltipGenerator);

	BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();

	r.setSeriesPaint(0, barColor);

	return chart;
    }

}
