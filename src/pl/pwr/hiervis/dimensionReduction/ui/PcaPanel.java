package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import pl.pwr.hiervis.dimensionReduction.ui.elements.SpinnerMouseWheelChanger;

public class PcaPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 6332252243556659739L;
    public static final String OutputDimensions = "OutputDimensions";
    private JSpinner spinner;

    public PcaPanel() {
	setLayout(new BorderLayout(10, 10));

	JLabel lblTitle = new JLabel("Final number of dimensions");
	lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
	lblTitle.setBounds(40, 11, 204, 33);
	add(lblTitle, BorderLayout.NORTH);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	add(horizontalStrut_1, BorderLayout.EAST);

	Component verticalStrut = Box.createVerticalStrut(20);
	add(verticalStrut, BorderLayout.SOUTH);

	spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(2, 2, 2, 1));
	spinner.addMouseWheelListener(new SpinnerMouseWheelChanger());

	add(spinner, BorderLayout.CENTER);

    }

    public Map<String, Object> getValues() {
	HashMap<String, Object> results = new HashMap<String, Object>();
	results.put(OutputDimensions, spinner.getValue());
	return results;
    }

    public void remodel(int maxOutputDimensions) {
	spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
    }
}
