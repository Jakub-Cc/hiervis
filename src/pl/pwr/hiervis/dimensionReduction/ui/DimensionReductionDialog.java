package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.util.HierarchyUtils;

public abstract class DimensionReductionDialog extends JDialog {
    public DimensionReductionDialog() {
	super();
	getContentPane().setLayout(new BorderLayout(0, 0));

	JPanel buttonPanel = new JPanel();
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	btnOk = new JButton("OK");
	buttonPanel.add(btnOk);
	btnOk.setActionCommand("OK");
	btnOk.addActionListener(this::confirmDialog);
	getRootPane().setDefaultButton(btnOk);

	JButton btnCancel = new JButton("Cancel");
	buttonPanel.add(btnCancel);
	btnCancel.setActionCommand("Cancel");

	JLabel lblHelpicon = new HelpIcon();
	lblHelpicon.setHorizontalAlignment(SwingConstants.RIGHT);
	getContentPane().add(lblHelpicon, BorderLayout.NORTH);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	getContentPane().add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	getContentPane().add(horizontalStrut_1, BorderLayout.EAST);
	btnCancel.addActionListener(this::closeDialog);

	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

	this.setTitle(getName());
    }

    protected abstract JPanel getMainPanel();

    protected abstract long getRequiredMemorry();

    private boolean isEnoughMemoryAvaliable() {
	return (Runtime.getRuntime().freeMemory() - getRequiredMemorry() > 0);
    }

    private JPanel getMemmoryWaringMessage() {
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout(0, 0));

	JLabel lblWarning = new JLabel("<html> Not enough memory to process this Dimensionality Reduction "
		+ "<br>            Assign more memory to Java Virtual Machine"
		+ "<br>                 for example use \"-Xmx4G\" argument");

	lblWarning.setFont(new Font("Tahoma", Font.BOLD, 16));
	lblWarning.setHorizontalAlignment(SwingConstants.CENTER);

	mainPanel.add(lblWarning, BorderLayout.CENTER);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut_1, BorderLayout.EAST);

	Component verticalStrut = Box.createVerticalStrut(20);
	mainPanel.add(verticalStrut, BorderLayout.SOUTH);

	return mainPanel;
    };

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected int maxOutputDimensions = 2;
    protected int pointsAmount = 0;
    FeatureExtraction result;
    private JButton btnOk;

    public abstract String getName();

    public abstract String getSimpleName();

    public FeatureExtraction showDialog(int inputDataDimensions, int pointsAmount, int x, int y) {
	this.maxOutputDimensions = inputDataDimensions;
	this.pointsAmount = pointsAmount;
	this.result = null;

	this.addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		result = null;
	    }
	});

	// move to showDialog?
	if (isEnoughMemoryAvaliable()) {
	    getContentPane().add(getMainPanel(), BorderLayout.CENTER);
	    setKeybindConfirm((JPanel) getContentPane());
	}
	else {
	    getContentPane().add(getMemmoryWaringMessage(), BorderLayout.CENTER);
	    btnOk.setEnabled(false); // test if hotkeys baypass the disable
	}

	setKeybindCancel((JPanel) getContentPane());

	setLocation(x, y);
	remodel();
	pack();
	setMinimumSize(getSize());
	setVisible(true);

	return this.result;
    }

    public FeatureExtraction showDialog(int inputDataDimensions, int pointsAmount) {
	return showDialog(inputDataDimensions, pointsAmount, 100, 100);
    }

    public FeatureExtraction showDialog(Hierarchy hierarchy) {
	return showDialog(HierarchyUtils.getFirstInstance(hierarchy).getData().length,
		hierarchy.getOverallNumberOfInstances(), 100, 100);
    }

    public FeatureExtraction showDialog(Hierarchy hierarchy, int x, int y) {
	return showDialog(HierarchyUtils.getFirstInstance(hierarchy).getData().length,
		hierarchy.getOverallNumberOfInstances(), x, y);
    }

    public abstract Class<? extends FeatureExtraction> getResultClass();

    public abstract void remodel();

    public abstract void setResult();

    private void setKeybindCancel(JPanel contentPanel) {
	// escape button to cancel dialog
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		"ESC");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");

	contentPanel.getActionMap().put("ESC", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeDialog(e);
	    }
	});
    }

    private void setKeybindConfirm(JPanel contentPanel) {
	// press "enter" or "space" to confirm dialog
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
		"ENTER");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
		"ENTER");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "ENTER");
	contentPanel.getActionMap().put("ENTER", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		confirmDialog(e);
	    }
	});
    }

    private void closeDialog(ActionEvent e) {
	result = null;
	dispose();
    }

    private void confirmDialog(ActionEvent e) {
	setResult();
	dispose();
    }
}
