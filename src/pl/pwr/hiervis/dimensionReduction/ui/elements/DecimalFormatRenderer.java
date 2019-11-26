package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;

public class DecimalFormatRenderer extends DefaultTableCellRenderer {
	private static final Logger log = LogManager.getLogger(DecimalFormatRenderer.class);

	private static final long serialVersionUID = -3961056851479449270L;
	private static DecimalFormat formatter;

	public DecimalFormatRenderer(List<FeatureSelectionResult> selectionResult) {
		this.setHorizontalAlignment(JLabel.RIGHT);
		int maxDecimal = -1;
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');

		/*
		 * int maxNumber = -1; for (FeatureSelectionResult res : selectionResult) { int
		 * decimalPlaces = getDefaultDecimalLength(res.getScore()); if (maxDecimal <
		 * decimalPlaces) { maxDecimal = decimalPlaces; } int numberPlaces =
		 * getDefaultValueLength(res.getScore()); if (maxNumber < numberPlaces) {
		 * maxNumber = numberPlaces; } } log.debug(maxDecimal); log.debug(maxNumber);
		 * String numberFormat = StringUtils.repeat("#", maxNumber % 3); numberFormat +=
		 * StringUtils.repeat(",###", maxNumber / 3); if (maxNumber > 2) maxDecimal = 2;
		 * String decimalFormat = StringUtils.repeat('0', maxDecimal);
		 * 
		 * formatter = new DecimalFormat(numberFormat + "0." + decimalFormat);
		 */
		int minDecimal = getMinToDistinguish(selectionResult);
		String decimalFormat = "";
		if (minDecimal > 0) {
			System.out.println("cos");
			decimalFormat = "###,###,###,###,###,###,###,###,###,###,##0." + StringUtils.repeat('0', minDecimal);

		} else {
			decimalFormat = "###,###,###,###,###,###,###,###,###,###,###";
		}
		formatter = new DecimalFormat(decimalFormat);
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
