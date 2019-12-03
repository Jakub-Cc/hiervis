package pl.pwr.hiervis.prefuse.histogram;

import java.awt.Color;

/*
 * Adapted for HocusLocus by Ajish George <ajishg@gmail.com>
 * from code by
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author <a href="http://webfoot.com/ducky.home.html">Kaitlin Duck Sherwood</a>
 *
 * See HistogramTable.java for details 
 */

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;

import pl.pwr.hiervis.prefuse.DisplayEx;
import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.AxisLabelLayout;
import prefuse.action.layout.AxisLayout;
import prefuse.data.query.NumberRangeModel;
import prefuse.render.AxisRenderer;
import prefuse.render.Renderer;
import prefuse.render.RendererFactory;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;
import prefuse.visual.sort.ItemSorter;

/**
 * A simple histogram visualization that allows different columns in a data
 * table to be histogramized and displayed. The starting point was
 * ScatterPlot.java by Jeffrey Heer, but Kaitlin Duck Sherwood has modified it
 * quite extensively.
 * 
 * Kaitlin Duck Sherwood's modifications are granted as is for any commercial or
 * non-commercial use, with or without attribution. The only conditions are that
 * you can't pretend that you wrote them, and that you leave these notices about
 * her authorship in the source.
 * 
 * Known bug: See the comments for HistogramFrame; there might be a bug in
 * HistogramGraph::updateAxes(), but I sure can't figure out what it is. I
 * suspect that it is a prefuse bug.
 * 
 * Note: I wanted to use Prefuse's StackedAreaChart, but couldn't get it to
 * work. If you figure out how to make it work, please email me. -- KDS
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author <a href="http://webfoot.com/ducky.home.html">Kaitlin Duck
 *         Sherwood</a>
 */
public class HistogramGraph extends DisplayEx {
	private static final String COLOR = "color";
	/**
	* 
	*/
	private static final long serialVersionUID = 314808975013373053L;
	public static final String DATA_ID = "data";
	public static final String XLABELS_ID = "xlabels";
	public static final String YLABELS_ID = "ylabels";

	// KDS -- I tend to make things protected instead of private so
	// that people can subclass them. I'm not sure that's the right
	// thing to do.
	protected transient Rectangle2D mDataB = new Rectangle2D.Double();
	protected transient Rectangle2D mXlabB = new Rectangle2D.Double();
	protected transient Rectangle2D mYlabB = new Rectangle2D.Double();

	protected transient BarRenderer mShapeR = new BarRenderer(5);

	protected transient HistogramTable mHistoTable;

	/**
	 * @param histoTable    a histogrammized version of dataTable
	 * @param startingField the name of the field (column) of the data table whose
	 *                      histogram is to be shown in the histogram graph.
	 */
	public HistogramGraph(HistogramTable histoTable, String startingField, Color histogramColor) {
		super(new Visualization());

		mHistoTable = histoTable;
		startingField = getStartingField(startingField);

		// --------------------------------------------------------------------
		// STEP 1: setup the visualized data

		m_vis.addTable(DATA_ID, mHistoTable);

		initializeRenderer();

		// --------------------------------------------------------------------
		// STEP 2: create actions to process the visual data

		ActionList colors = createColorizeActions(histogramColor);
		m_vis.putAction(COLOR, colors);

		ActionList draw = new ActionList();
		draw.add(initializeAxes(startingField));
		draw.add(colors);
		draw.add(new RepaintAction());
		m_vis.putAction("draw", draw);

		// --------------------------------------------------------------------
		// STEP 3: set up a display and ui components to show the visualization

		initializeWindowCharacteristics();

		// --------------------------------------------------------------------
		// STEP 4: launching the visualization

		m_vis.run("draw");
	}

	public void setBarColor(Color color) {
		m_vis.removeAction(COLOR);
		ActionList colors = createColorizeActions(color);
		m_vis.putAction(COLOR, colors);

		ActionList draw = (ActionList) m_vis.getAction("draw");
		draw.remove(1).cancel();
		draw.add(1, colors);
	}

	private ActionList createColorizeActions(Color color) {
		ActionList list = new ActionList();

		list.add(new ColorAction(DATA_ID, VisualItem.FILLCOLOR, color.getRGB()));
		list.add(new ColorAction(DATA_ID, VisualItem.STROKECOLOR, color.darker().getRGB()));

		return list;
	}

	/**
	 * Updates the width of individual bars to the new value. Display needs to be
	 * redrawn afterwards.
	 * 
	 * @param width the new bar width
	 */
	public void setBarWidth(double width) {
		mShapeR.setBarWidth(width);
	}

	/**
	 * This sets up various things about the window, including the size. TODO
	 * include the size in the constructor
	 */
	private void initializeWindowCharacteristics() {
		setBorder(BorderFactory.createEmptyBorder());

		// main display controls

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (!isTranformInProgress()) {
					int margin = 10;

					Rectangle2D bounds = m_vis.getBounds(Visualization.ALL_ITEMS);
					double barWidth = bounds.getWidth() / mHistoTable.getBinCount();

					GraphicsLib.expand(bounds, margin + (1 / getScale()));
					DisplayLib.fitViewToBounds(HistogramGraph.this, bounds, 0);

					setBarWidth(barWidth);
				}
			}
		});

		setHighQuality(true);

		initializeLayoutBoundsForDisplay();
		mShapeR.setBounds(mDataB);
	}

	/**
	 * @param fieldName the name of the field (column) to display
	 */
	private ActionList initializeAxes(String fieldName) {
		AxisLayout xAxis = new AxisLayout(DATA_ID, fieldName, Constants.X_AXIS, VisiblePredicate.TRUE);
		xAxis.setLayoutBounds(mDataB);
		m_vis.putAction("x", xAxis);

		String countField = HistogramTable.getCountField(fieldName);
		AxisLayout yAxis = new AxisLayout(DATA_ID, countField, Constants.Y_AXIS, VisiblePredicate.TRUE);

		yAxis.setLayoutBounds(mDataB);
		m_vis.putAction("y", yAxis);

		DecimalFormat valueNumberFormat = new DecimalFormat();
		valueNumberFormat.setMaximumFractionDigits(2);
		valueNumberFormat.setMinimumFractionDigits(2);

		DecimalFormat countNumberFormat = new DecimalFormat();
		countNumberFormat.setMaximumFractionDigits(0);
		countNumberFormat.setMinimumFractionDigits(0);

		AxisLabelLayout xLabels = new AxisLabelLayout(XLABELS_ID, xAxis, mXlabB);
		xLabels.setNumberFormat(valueNumberFormat);
		m_vis.putAction(XLABELS_ID, xLabels);

		AxisLabelLayout yLabels = new AxisLabelLayout(YLABELS_ID, yAxis, mYlabB);
		yLabels.setNumberFormat(countNumberFormat);
		m_vis.putAction(YLABELS_ID, yLabels);

		updateAxes(fieldName, xAxis, yAxis, xLabels, yLabels);

		ActionList axes = new ActionList();
		axes.add(xAxis);
		axes.add(yAxis);
		axes.add(xLabels);
		axes.add(yLabels);

		return axes;
	}

	private void initializeRenderer() {
		m_vis.setRendererFactory(new RendererFactory() {
			Renderer xAxisRenderer = new AxisRenderer(Constants.CENTER, Constants.FAR_BOTTOM);
			Renderer yAxisRenderer = new AxisRenderer(Constants.FAR_LEFT, Constants.CENTER);

			public Renderer getRenderer(VisualItem item) {
				if (item.isInGroup(XLABELS_ID))
					return xAxisRenderer;
				if (item.isInGroup(YLABELS_ID))
					return yAxisRenderer;
				return mShapeR;
			}
		});

		// Via: http://www.ifs.tuwien.ac.at/~rind/w/doku.php/java/prefuse-scatterplot
		// Sort items so that axes and lines are drawn below the actual histogram bars
		setItemSorter(new ItemSorter() {
			@Override
			public int score(VisualItem item) {
				if (item.isInGroup(DATA_ID)) {
					return Integer.MAX_VALUE;
				}

				return 0;
			}
		});
	}

	public void initializeLayoutBoundsForDisplay() {
		Insets i = getInsets();
		double w = getWidth();
		double h = getHeight();
		double insetWidth = i.left + (double) i.right;
		double insetHeight = i.top + (double) i.bottom;
		double yAxisWidth = 5;
		double xAxisHeight = 5;

		mDataB.setRect(i.left, i.top, w - insetWidth - yAxisWidth, h - insetHeight - xAxisHeight);
		mXlabB.setRect(i.left, h - xAxisHeight - i.bottom, w - insetWidth - yAxisWidth, xAxisHeight);
		mYlabB.setRect(i.left, i.top, w - insetWidth, h - insetHeight - xAxisHeight);

		m_vis.run("update");
	}

	/**
	 * @param dataField the name of the column in histoTable to display
	 */
	private void updateAxes(String dataField, AxisLayout xAxis, AxisLayout yAxis, AxisLabelLayout xLabel,
			AxisLabelLayout yLabel) {
		xAxis.setScale(Constants.LINEAR_SCALE);
		xAxis.setDataField(dataField);
		xAxis.setDataType(getAxisType(dataField));

		double min = mHistoTable.getBinMin(dataField);
		double max = mHistoTable.getBinMax(dataField);
		NumberRangeModel xrangeModel = new NumberRangeModel(min, max, min, max);
		xLabel.setRangeModel(null); // setting to null seems to force a recalc -> redraw
		xLabel.setRangeModel(xrangeModel);

		// yaxis is the bin counts, which are always numeric
		yAxis.setDataField(HistogramTable.getCountField(dataField));
		yAxis.setScale(Constants.LINEAR_SCALE);
		NumberRangeModel rangeModel = new NumberRangeModel(0, mHistoTable.getCountMax(dataField), 0,
				mHistoTable.getCountMax(dataField));
		yAxis.setRangeModel(rangeModel);
		yLabel.setRangeModel(rangeModel);

		m_vis.run("draw");
	}

	/**
	 * @param dataField the name of a column in histoTable to display
	 * @return isNumeric boolean which says if the column named by dataField is int,
	 *         float, or double or if it is not. Note that booleans are treated as
	 *         non-numerics under this logic.
	 */
	private boolean isNumeric(String dataField) {
		return mHistoTable.getColumn(dataField).canGetDouble();
	}

	/**
	 * @param dataField the name of a column to display
	 * @return the type of the axis (NUMERICAL for numbers and ORDINAL for strings)
	 *         Note that HistogramGraph hasn't been tested with boolean or derived
	 *         fields. I believe that HistogramTable treats booleans as strings. --
	 *         KDS
	 */
	protected int getAxisType(String dataField) {
		if (isNumeric(dataField)) {
			return Constants.NUMERICAL;
		}
		return Constants.ORDINAL;
	}

	/**
	 * @param startingField
	 * @return either the input or the first field in the data table
	 */
	protected String getStartingField(String startingField) {
		if (startingField == null) {
			startingField = mHistoTable.getColumnName(0);
		}
		return startingField;
	}

	protected HistogramTable getHistoTable() {
		return mHistoTable;
	}

	@Override
	public void dispose() {
		super.dispose();

		mHistoTable.clear();
		mHistoTable.dispose();
	}
}
