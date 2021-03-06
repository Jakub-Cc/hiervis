package pl.pwr.hiervis.dimension_reduction.ui.elements;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class LoadingIcon extends JLabel {
	private static final long serialVersionUID = 6914976916388241044L;
	private JLabel message;

	public LoadingIcon(int x, int y) {
		super();
		setBounds(x, y, 200, 200);
		ImageIcon img = new ImageIcon(
				this.getClass().getResource("/pl/pwr/hiervis/dimension_reduction/ui/elements/loading.gif"));
		setIcon(img);
		message = new JLabel("<html>Computing");
		message.setVerticalAlignment(SwingConstants.CENTER);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setBounds(0, 0, 200, 200);
		message.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(message);
	}

	public LoadingIcon() {
		this(0, 0);
	}

	public void showIcon(int x, int y) {
		setLocation(x, y);
		setVisible(true);
	}

	public void hideIcon() {
		setVisible(false);
	}
}
