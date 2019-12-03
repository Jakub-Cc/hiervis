package pl.pwr.hiervis.hk.ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.ui.elements.KeyBinds;
import pl.pwr.hiervis.util.SwingUIUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

public class HKOptionsJDialog extends JDialog {
	private static final Logger logHK = LogManager.getLogger(HKOptionsJDialog.class);

	private transient HVContext context;
	private transient Node node;
	private static final long serialVersionUID = -621178124268390305L;

	private Window callerWindow;
	private HKOptionsPanel content;

	private AbstractAction generateAction = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			generate(e);
		}
	};

	public HKOptionsJDialog(HVContext context, Window frame, Node node, String subtitle) {

		String title = "HK++: " + node.getId() + (subtitle == null ? "" : (" [ " + subtitle + " ]"));

		setTitle(title);

		this.context = context;
		this.node = node;
		this.callerWindow = frame;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 500);

		createGUI();

		SwingUIUtils.addCloseCallback(frame, this);
		SwingUIUtils.installEscapeCloseOperation(this);
		setModal(true);
		pack();
		setMinimumSize(getSize());
	}

	public void showDialog(int x, int y) {
		content.setCarret();
		setLocation(x, y);
		setVisible(true);

	}

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

		content = new HKOptionsPanel(context, node, logHK, generateAction);
		content.setupDefaultValues();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setViewportView(content);

		panel.add(scrollPane, builder.fill().position(0, 0).build());
		JButton btnGenerate = new JButton("Generate (ENTER)");
		btnGenerate.addActionListener(this::generate);

		panel.add(btnGenerate, builder.insets(5).fillHorizontal().position(0, 1).build());
		getRootPane().setDefaultButton(btnGenerate);

		btnGenerate.setMnemonic(KeyEvent.VK_ENTER);

		KeyBinds.setKeybindESC((JPanel) getContentPane(), new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		KeyBinds.setKeybindENTER((JPanel) getContentPane(), generateAction);
	}

	protected void generate(@SuppressWarnings("unused") ActionEvent e) {
		if (content.generate(callerWindow))
			dispose();
	}
}
