package pl.pwr.hiervis.hk.ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hk.HKPlusPlusWrapper;
import pl.pwr.hiervis.util.SwingUIUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

public class HKOptionsJDialog extends JDialog {
    private static final Logger logHK = LogManager.getLogger(HKOptionsJDialog.class);

    private HVContext context;
    private Node node;
    private static final long serialVersionUID = -621178124268390305L;
    private JButton btnGenerate;
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
	content.setupDefaultValues(context.getConfig());

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	scrollPane.setViewportView(content);

	panel.add(scrollPane, builder.fill().position(0, 0).build());
	btnGenerate = new JButton("Generate (ENTER)");
	btnGenerate.addActionListener(this::generate);

	panel.add(btnGenerate, builder.insets(5).fillHorizontal().position(0, 1).build());
	getRootPane().setDefaultButton(btnGenerate);

	HKPlusPlusWrapper currentWrapper = context.getCurrentHKWrapper();
	if (currentWrapper != null) {
	    btnGenerate.setEnabled(false);
	    currentWrapper.subprocessAborted.addListener(e -> btnGenerate.setEnabled(true));
	    currentWrapper.subprocessFinished.addListener(e -> btnGenerate.setEnabled(true));
	}

	btnGenerate.setMnemonic(KeyEvent.VK_ENTER);

	setKeybindCancel((JPanel) getContentPane());
	setKeybindConfirm((JPanel) getContentPane());

    }

    private void setKeybindCancel(JComponent contentPanel) {
	// escape button to cancel dialog
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");

	contentPanel.getActionMap().put("ESC", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
	    }
	});
    }

    private void setKeybindConfirm(JComponent contentPanel) {
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

	contentPanel.getActionMap().put("ENTER", generateAction);
    }

    protected void generate(ActionEvent e) {
	btnGenerate.setEnabled(false);
	if (!context.isHKSubprocessActive()) {
	    dispose();

	    HKPlusPlusWrapper wrapper = content.generate(callerWindow);
	    context.setCurrentHKWrapper(wrapper);
	}
    }
}
