package pl.pwr.hiervis.hk.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.util.SwingUIUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

@SuppressWarnings("serial")
public class HKOptionsFrame extends JFrame {
	private static final Logger logHK = LogManager.getLogger(HKOptionsFrame.class);

	private HVContext context;
	private Node node;

	public HKOptionsFrame(HVContext context, Window frame, Node node, String subtitle) {
		super("HK++: " + node.getId() + (subtitle == null ? "" : (" [ " + subtitle + " ]")));

		this.context = context;
		this.node = node;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(400, 200));
		setSize(400, 500);

		createGUI();

		SwingUIUtils.addCloseCallback(frame, this);
		SwingUIUtils.installEscapeCloseOperation(this);
	}

	// ----------------------------------------------------------------------------------------
	// GUI creation methods

	private void createGUI() {
		GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0 };
		layout.rowHeights = new int[] { 0, 0 };
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights = new double[] { 1.0, 0.0 };
		panel.setLayout(layout);

		HKOptionsPanel content = new HKOptionsPanel(context, node, logHK, null);
		content.setupDefaultValues(context.getConfig());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setViewportView(content);

		panel.add(scrollPane, builder.fill().position(0, 0).build());

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(e -> {
			if (content.generate(this))
				dispose();
		});
		panel.add(btnGenerate, builder.insets(5).fillHorizontal().position(0, 1).build());
	}
}
