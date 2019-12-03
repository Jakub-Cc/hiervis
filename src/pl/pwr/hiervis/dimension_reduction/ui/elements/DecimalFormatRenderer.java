package pl.pwr.hiervis.dimension_reduction.ui.elements;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.StringUtils;

import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;

public class DecimalFormatRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -3961056851479449270L;
	private DecimalFormat formatter;

	public DecimalFormatRenderer(List<FeatureSelectionResult> selectionResult) {
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		int minDecimal = getMinToDistinguish(selectionResult);
		int maxInt = getMaxIntegerPartLength(selectionResult);
		String decimalFormat = "";
		decimalFormat = StringUtils.repeat("#", maxInt % 3);
		decimalFormat += StringUtils.repeat(",###", maxInt / 3);
		if (minDecimal > 0) {
			if (decimalFormat.length() > 1)
				decimalFormat = decimalFormat.substring(0, decimalFormat.length() - 1);
			decimalFormat += "0." + StringUtils.repeat('0', minDecimal);
		}
		this.formatter = new DecimalFormat(decimalFormat);
	}

	private int getMaxIntegerPartLength(List<FeatureSelectionResult> selectionResult) {
		int max = 0;
		for (int i = 0; i < selectionResult.size() - 1; i++) {
			int current = getDefaultValueLength(selectionResult.get(i).getScore());
			if (current > max)
				max = current;
		}
		return max;
	}

	private int getMinToDistinguish(List<FeatureSelectionResult> selectionResult) {
		int min = 0;
		for (int i = 0; i < selectionResult.size() - 1; i++) {
			int curent = getMinToDistinguish(selectionResult.get(i).getScore(), selectionResult.get(i + 1).getScore());
			if (curent > min)
				min = curent;
		}
		return min;
	}

	private int getMinToDistinguish(double a, double b) {
		int max = Math.max(getDefaultDecimalLength(a), getDefaultDecimalLength(b));
		long multi = 1;
		for (int i = 0; i < max; i++) {
			long c = (long) Math.floor(a * multi);
			long d = (long) Math.floor(b * multi);
			if (c != d) {
				return i;
			}
			multi *= 10;
		}
		return max;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		value = formatter.format((Number) value);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	private int getDefaultValueLength(double value) {
		String valueString = "" + value;
		String subString1 = StringUtils.substringBeforeLast(valueString, ",");
		String subString2 = StringUtils.substringBeforeLast(valueString, ".");
		return Math.min(subString1.length(), subString2.length());
	}

	private int getDefaultDecimalLength(double value) {
		String valueString = "" + value;
		String subString1 = StringUtils.substringAfterLast(valueString, ",");
		String subString2 = StringUtils.substringAfterLast(valueString, ".");
		return Math.max(subString1.length(), subString2.length());
	}
}
