package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import pl.pwr.hiervis.core.HVConstants;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.prefuse.DisplayEx;
import pl.pwr.hiervis.prefuse.histogram.HistogramGraph;
import pl.pwr.hiervis.prefuse.histogram.HistogramTable;
import prefuse.Visualization;
import prefuse.data.Table;

public class BarGraph extends DisplayEx {

    private static final long serialVersionUID = 5267764077101440361L;
    private double[] data;
    private String[] labels;

    public BarGraph(double[] data, String[] labels, HVContext context) {
	super(new Visualization());

	this.data = data;
	this.labels = labels;
	this.m_vis = createHistogramDisplayFor(context).getVisualization();
    }

    private Table createTable() {
	Table table = new Table();

	table.addColumn("points", double.class);
	table.addColumn(HVConstants.PREFUSE_INSTANCE_LABEL_COLUMN_NAME, String.class);
	table.addColumn(HVConstants.PREFUSE_INSTANCE_AXIS_X_COLUMN_NAME, String.class);

	for (int i = 0; i < data.length; i++) {
	    int row = table.addRow();
	    table.set(row, "points", data[i]);
	    table.set(row, HVConstants.PREFUSE_INSTANCE_LABEL_COLUMN_NAME, labels[i]);
	    table.set(row, HVConstants.PREFUSE_INSTANCE_AXIS_X_COLUMN_NAME, labels[i]);
	}

	return table;
    }

    private HistogramTable cheatCreateHistoTable(Table table) {
	HistogramTable histoTable = new HistogramTable(table, table.getRowCount());
	for (int i = 0; i < table.getRowCount(); i++) {
	    histoTable.set(i, 0, table.get(i, 0));
	    histoTable.set(i, 1, table.get(i, 0));
	    histoTable.set(i, 2, table.get(i, 1));
	    histoTable.set(i, 3, table.get(i, 0));
	}
	return histoTable;
    }

    /**
     * Creates a histogram display for the specified dimension
     * 
     * @param dim dimension index
     * @return the display
     */
    private DisplayEx createHistogramDisplayFor(HVContext context) {
	// Table table = context.getHierarchy().getInstanceTable();

	Table table2 = createTable();

	// Color histogramColor = context.getConfig().getHistogramColor();
	Color histogramColor = Color.BLUE;
	// Color backgroundColor = context.getConfig().getBackgroundColor();
	Color backgroundColor = Color.WHITE;

	HistogramTable histoTable = cheatCreateHistoTable(table2);
	int visWidth = 200;
	int visHeight = 200;
	/*
	 * display.addControlListener(new PanControl(true)); ZoomScrollControl zoomControl = new
	 * ZoomScrollControl(); zoomControl.setModifierControl(true);
	 * display.addControlListener(zoomControl); display.addMouseWheelListener(new
	 * MouseWheelEventBubbler(display, e -> !e.isControlDown() && !e.isAltDown()));
	 * display.addControlListener(new CustomToolTipControl(item -> { StringBuilder buf = new
	 * StringBuilder(); buf.append("<html>");
	 * buf.append("Count: ").append(item.get(HVConstants.PREFUSE_VISUAL_TABLE_COLUMN_OFFSET +
	 * dim * 2 + 1)); // TODO: Add bin value range buf.append("</html>"); return
	 * buf.toString(); }));
	 */

	HistogramGraph display = new HistogramGraph(histoTable, table2.getColumnName(0), histogramColor);

	display.setBackground(backgroundColor);
	display.setPreferredSize(new Dimension(visWidth, visHeight));

	display.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
		    // Consume alt key releases, so that the display doesn't lose focus
		    // (default behaviour of Alt key on Windows is to switch to menu bar when Alt
		    // is pressed, but this window has no menu bar anyway)
		    e.consume();
		}
	    }
	});

	display.addComponentListener(new ComponentAdapter() {
	    public void componentResized(ComponentEvent e) {
		redrawDisplayIfVisible((DisplayEx) e.getComponent());
	    }
	});

	return display;
    }

    private void redrawDisplayIfVisible(DisplayEx d) {
	if (d.isVisible()) {
	    // Unzoom the display so that drawing is not botched.
	    // Utils.unzoom( d, 0 );
	    d.getVisualization().run("draw");
	}
	else {
	    d.reset();
	}
    }
}
