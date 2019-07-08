package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class SeparatorComboBox extends JComboBox<Object> {

    private static final long serialVersionUID = 501616773146829231L;

    private static List<Integer> separatorsIndexes = new ArrayList<Integer>();
    private static final JSeparator SEPARATOR = new JSeparator(JSeparator.HORIZONTAL);
    int prevSelection = 0;

    @SuppressWarnings("unchecked")
    public SeparatorComboBox() {
	super();
	setRenderer(new SeparatorComboBoxRenderer());
	setEditable(false);
	setFocusable(false);
    }

    public void addSeparator() {
	addItem(SEPARATOR);
	separatorsIndexes.add(getItemCount() - 1);
    }

    public int getTrueIndex() {
	int superIndex = super.getSelectedIndex();
	int trueIndex = superIndex;
	for (int i : separatorsIndexes) {
	    if (superIndex >= i) {
		trueIndex--;
	    }
	}
	return trueIndex;
    }

    public void setTrueIndex(int anIndex) {
	int superIndex = anIndex;
	for (int i : separatorsIndexes) {
	    if (anIndex >= i) {
		superIndex++;
	    }
	}
	prevSelection = superIndex;
	super.setSelectedIndex(superIndex);
    }

    @Override
    public int getSelectedIndex() {
	return super.getSelectedIndex();
    }

    @Override
    public void setSelectedIndex(int anIndex) {
	if (separatorsIndexes.contains(anIndex)) {
	    super.setSelectedIndex(prevSelection);
	}
	else {
	    prevSelection = anIndex;
	    super.setSelectedIndex(anIndex);
	}
    }

    @SuppressWarnings("rawtypes")
    class SeparatorComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer {

	private static final long serialVersionUID = -3901940790817557085L;

	public SeparatorComboBoxRenderer() {
	    super();
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    }
	    else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	    setFont(list.getFont());
	    if (value instanceof Icon) {
		setIcon((Icon) value);
	    }
	    if (value instanceof JSeparator) {
		return (Component) value;
	    }
	    else {
		setText((value == null) ? "" : value.toString());
	    }

	    return this;
	}
    }
}
