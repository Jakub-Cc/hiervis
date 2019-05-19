package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

public class DoubleNumberTextField extends JFormattedTextField {

    /**
     * 
     */
    private static final long serialVersionUID = 4050930789568568152L;
    private static AbstractFormatter doubleFormat = createNumberFormatter();

    public DoubleNumberTextField() {
	super(doubleFormat);
	this.addPropertyChangeListener("value", this::propertyChange);
	this.setValue(new Double(1.5));

    }

    public double getDouble() {
	Double value = (Double) getValue();
	return value;
    }

    public void setCaret() {
	this.setCaretPosition(this.getText().length());
    }

    private static AbstractFormatter createNumberFormatter() {
	DecimalFormat f = new DecimalFormat();
	f.setDecimalSeparatorAlwaysShown(true);

	NumberFormatter formatter = new NumberFormatter(f);
	formatter.setValueClass(Double.class);
	formatter.setMinimum(Double.MIN_VALUE);
	formatter.setMaximum(Double.MAX_VALUE);
	formatter.setAllowsInvalid(false);
	formatter.setCommitsOnValidEdit(true);

	return formatter;
    }

    public void propertyChange(PropertyChangeEvent e) {

    }
}
