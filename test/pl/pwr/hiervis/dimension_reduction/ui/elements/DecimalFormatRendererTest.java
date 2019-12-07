package pl.pwr.hiervis.dimension_reduction.ui.elements;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;

import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;

public class DecimalFormatRendererTest {

	DecimalFormatRenderer dfr;

	@Before
	public void testDecimalFormatRenderer() {
		List<FeatureSelectionResult> selectionResult = new ArrayList<>();
		dfr = new DecimalFormatRenderer(selectionResult);
	}

	@Test
	public void testGetTableCellRendererComponentJTableObjectBooleanBooleanIntInt() {
		JTable table = new JTable();
		Object value = 1;
		boolean isSelected = false;
		boolean hasFocus = false;
		int row = 0;
		int column = 0;
		assertNotNull(dfr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
	}

}
