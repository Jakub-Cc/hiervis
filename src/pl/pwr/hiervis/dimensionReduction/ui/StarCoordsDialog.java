package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.pwr.hiervis.dimensionReduction.methods.StarCoordinates;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;

public class StarCoordsDialog extends DimensionReductionDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 7265764698630467230L;

    public static void main(String[] args) {
	DimensionReductionDialog di = new StarCoordsDialog();
	System.out.println(di.showDialog(8, 1000));
    }

    public StarCoordsDialog() {
	// TODO Auto-generated constructor stub
    }

    @Override
    protected JPanel getMainPanel() {
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout(0, 0));

	JLabel lblConfirmUsingStar = new JLabel(
		"<html>Confirm using Star Coordinates <br> as dimension reduction method?\r\n");
	lblConfirmUsingStar.setBounds(11, 27, 231, 40);
	lblConfirmUsingStar.setFont(new Font("Tahoma", Font.PLAIN, 16));
	mainPanel.add(lblConfirmUsingStar, BorderLayout.CENTER);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut_1, BorderLayout.EAST);

	Component verticalStrut = Box.createVerticalStrut(20);
	mainPanel.add(verticalStrut, BorderLayout.SOUTH);

	return mainPanel;
    }

    @Override
    protected long getRequiredMemorry() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public String getName() {
	return StarCoordinates.sGetName();
    }

    @Override
    public String getSimpleName() {
	return StarCoordinates.sGetSimpleName();
    }

    @Override
    public void remodel() {
	// No needed

    }

    @Override
    public Class<? extends FeatureExtraction> getResultClass() {
	return StarCoordinates.class;
    }

    @Override
    public void setResult() {
	result = new StarCoordinates();
    }

}
