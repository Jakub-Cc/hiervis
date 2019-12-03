package pl.pwr.hiervis.dimension_reduction.ui.elements;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

public class KeyBinds {

	private KeyBinds() {
		throw new AssertionError("Static class");
	}

	private static final String SPACE = "SPACE";
	private static final String ENTER = "ENTER";
	private static final String ESC = "ESC";

	public static void setKeybindESC(JComponent contentPanel, AbstractAction action) {
		contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				ESC);
		contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESC);
		contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESC);

		contentPanel.getActionMap().put(ESC, action);
	}

	public static void setKeybindENTER(JComponent contentPanel, AbstractAction action) {
		contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				ENTER);
		contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER);
		contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER);

		contentPanel.getActionMap().put(ENTER, action);
	}

	public static void setKeybindSPACE(JComponent contentPanel, AbstractAction action) {
		contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
				SPACE);
		contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
		contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);

		contentPanel.getActionMap().put(SPACE, action);
	}

	public static AbstractAction createDisposeAction(JDialog jDialog) {
		return new AbstractAction() {
			private static final long serialVersionUID = 4558599572989637665L;

			@Override
			public void actionPerformed(ActionEvent e) {
				jDialog.dispose();
			}
		};
	}

}
