package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;

import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Manhattan;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Minkowski;
import pl.pwr.hiervis.dimensionReduction.ui.elements.DoubleNumberTextField;

public class MdsPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1136628801949866909L;

    private DoubleNumberTextField powerValueTextField;
    private DistanceMeasure[] distanceMeasures;
    JLabel lblPowerValue;
    JList<DistanceMeasure> measuresList;

    public MdsPanel() {
	setLayout(new BorderLayout(0, 0));

	JLabel lblTitle = new JLabel("Select distance measure");
	lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
	add(lblTitle, BorderLayout.NORTH);

	JScrollPane scrollPane = new JScrollPane();
	add(scrollPane, BorderLayout.CENTER);

	measuresList = new JList<DistanceMeasure>();
	measuresList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	scrollPane.setViewportView(measuresList);

	measuresList.setFont(new Font("Tahoma", Font.BOLD, 20));
	distanceMeasures = new DistanceMeasure[3];
	distanceMeasures[0] = new Euclidean();
	distanceMeasures[1] = new Manhattan();
	distanceMeasures[2] = new Minkowski();
	measuresList.setListData(distanceMeasures);

	if (measuresList.getModel().getSize() >= 0) {
	    measuresList.setSelectedIndex(0);
	}
	measuresList.addListSelectionListener(this::selectionChanged);

	JPanel panel = new JPanel();
	FlowLayout flowLayout = (FlowLayout) panel.getLayout();
	flowLayout.setHgap(10);
	add(panel, BorderLayout.SOUTH);

	lblPowerValue = new JLabel("Power");
	panel.add(lblPowerValue);
	lblPowerValue.setEnabled(false);

	powerValueTextField = new DoubleNumberTextField();
	panel.add(powerValueTextField);
	powerValueTextField.setColumns(10);
	powerValueTextField.setEnabled(false);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	add(horizontalStrut_1, BorderLayout.EAST);
    }

    private void selectionChanged(ListSelectionEvent e) {
	if (measuresList.getSelectedValue().getClass() == Minkowski.class) {
	    powerValueTextField.setEnabled(true);
	    lblPowerValue.setEnabled(true);
	    powerValueTextField.grabFocus();
	    powerValueTextField.setCaret();
	}
	else {
	    powerValueTextField.setEnabled(false);
	    lblPowerValue.setEnabled(false);
	}
    }

    public Map<String, Object> getValues() {
	HashMap<String, Object> results = new HashMap<String, Object>();

	if (measuresList.getSelectedValue().getClass() == Minkowski.class)
	    results.put("DistanceMeasure", new Minkowski(powerValueTextField.getDouble()));
	else
	    results.put("DistanceMeasure", measuresList.getSelectedValue());

	return results;
    }
}
