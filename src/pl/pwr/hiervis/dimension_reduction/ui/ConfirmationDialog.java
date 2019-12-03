package pl.pwr.hiervis.dimension_reduction.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ConfirmationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton okButton;

	public void showDialog(int x, int y) {
		setLocation(x, y);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	}

	/**
	 * Create the dialog.
	 */
	public ConfirmationDialog() {
		this.setResizable(false);
		setBounds(100, 100, 270, 150);
		getContentPane().setLayout(null);

		okButton = new JButton("OK");
		okButton.setBounds(105, 84, 54, 26);
		getContentPane().add(okButton);
		okButton.setActionCommand("OK");
		okButton.addActionListener(this::buttonClicked);
		getRootPane().setDefaultButton(okButton);

		JLabel label = new JLabel("<html>           Dimension Reduction <br> computering task started");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(21, 0, 222, 83);
		getContentPane().add(label);

	}

	private void buttonClicked(@SuppressWarnings("unused") ActionEvent e) {
		dispose();
	}
}
