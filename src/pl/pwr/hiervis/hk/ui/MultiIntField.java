package pl.pwr.hiervis.hk.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public class MultiIntField extends JTextField {

    private static final long serialVersionUID = 8156637284931934759L;
    private int min;
    private int max;

    public MultiIntField(int min, int max) {
	super();
	this.min = min;
	this.max = max;

	this.setHorizontalAlignment(SwingConstants.RIGHT);
	this.setColumns(10);

	this.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyTyped(KeyEvent keyEvent) {
		if (!charIsNumberOrColon(keyEvent.getKeyChar()))
		    keyEvent.consume();
		else {
		    markAsMandatory(false);
		}
	    }
	});

	this.getActionMap().put(DefaultEditorKit.deletePrevCharAction, new MyDeletePrevCharAction());
	this.getActionMap().put(DefaultEditorKit.deleteNextCharAction, new MyDeleteNextCharAction());

	this.addFocusListener(new FocusAdapter() {
	    @Override
	    public void focusLost(FocusEvent arg0) {
		areValidFieldValues();
	    }
	});
    }

    public boolean areValidFieldValues() {
	try {
	    validateText();
	    markAsMandatory(false);
	    this.setToolTipText(null);
	}
	catch (Exception e) {
	    markAsMandatory(true);
	    setToolTipText(e.getMessage());
	    return false;
	}
	return true;
    }

    private void validateText() throws Exception {
	String txt = this.getText();
	if (txt.isEmpty()) {
	    throw new Exception("Value mandatory");
	}
	List<Integer> valueList = new ArrayList<Integer>();
	String[] values = txt.split(";");
	for (String value : values) {
	    try {
		int val = Integer.parseInt(value);
		if (val < min) {
		    throw new Exception("\"" + val + "\" must be biger that minimum - " + min);
		}
		else if (val > max) {
		    throw new Exception("\"" + val + "\" must be smaller that maximum - " + max);
		}
		else {
		    valueList.add(val);
		}
	    }
	    catch (NumberFormatException e) {
		throw new Exception("\"" + value + "\" could not be parsed to a value");
	    }
	}
	if (valueList.isEmpty()) {
	    throw new Exception("\"" + txt + "\" could not be parsed to a value");
	}

    }

    public List<Integer> getValues() {
	List<Integer> valueList = new ArrayList<Integer>();
	String txt = this.getText();
	String[] values = txt.split(";");
	for (String value : values) {
	    try {
		int val = Integer.parseInt(value);
		if (val >= min && val <= max)
		    valueList.add(val);
		else {
		}
	    }
	    catch (NumberFormatException e) {
	    }
	}
	return valueList;
    }

    @SuppressWarnings("unused")
    private MultiIntField() {
	// Only to restrict creation
    }

    @SuppressWarnings("unused")
    private MultiIntField(String s) {
	// Only to restrict creation
    }

    @SuppressWarnings("unused")
    private MultiIntField(String s, int i) {
	// Only to restrict creation
    }

    @SuppressWarnings("unused")
    private MultiIntField(int i) {
	// Only to restrict creation
    }

    @SuppressWarnings("unused")
    private MultiIntField(Document doc, String s, int i) {
	// Only to restrict creation
    }

    private boolean charIsNumberOrColon(char character) {
	return character == '1' || character == '2' || character == '3' || character == '4' || character == '5' || character == '6'
		|| character == '7' || character == '8' || character == '9' || character == '0' || character == ';' || character == '-';
    }

    private void markAsMandatory(boolean visible) {
	if (visible)
	    this.setBackground(new Color(255, 45, 45));
	else {
	    this.setBackground(Color.WHITE);
	    this.setToolTipText(null);
	}
    }

    class MyDeletePrevCharAction extends TextAction {

	private static final long serialVersionUID = -8284188424159639872L;

	MyDeletePrevCharAction() {
	    super(DefaultEditorKit.deletePrevCharAction);
	}

	public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);
	    if ((target != null) && (target.isEditable())) {
		try {
		    Document doc = target.getDocument();
		    Caret caret = target.getCaret();
		    int dot = caret.getDot();
		    if (dot > 1) {
			int delChars = 1;
			String dotChars = doc.getText(dot - 2, 2);
			char c0 = dotChars.charAt(0);
			char c1 = dotChars.charAt(1);
			if (c0 >= '\uD800' && c0 <= '\uDBFF' && c1 >= '\uDC00' && c1 <= '\uDFFF') {
			    delChars = 2;
			}
			doc.remove(dot - delChars, delChars);
		    }
		    else if (dot > 0) {
			doc.remove(dot - 1, 1);
		    }
		    if (doc.getLength() == 0)
			markAsMandatory(true);
		}
		catch (BadLocationException bl) {
		}
	    }
	}
    }

    class MyDeleteNextCharAction extends TextAction {

	private static final long serialVersionUID = -8284188424159639872L;

	MyDeleteNextCharAction() {
	    super(DefaultEditorKit.deleteNextCharAction);
	}

	public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);

	    if ((target != null) && (target.isEditable())) {
		try {
		    Document doc = target.getDocument();
		    Caret caret = target.getCaret();
		    int dot = caret.getDot();
		    int mark = caret.getMark();
		    if (dot != mark) {
			doc.remove(Math.min(dot, mark), Math.abs(dot - mark));

		    }
		    else if (dot < doc.getLength()) {
			int delChars = 1;

			if (dot < doc.getLength() - 1) {
			    String dotChars = doc.getText(dot, 2);
			    char c0 = dotChars.charAt(0);
			    char c1 = dotChars.charAt(1);

			    if (c0 >= '\uD800' && c0 <= '\uDBFF' && c1 >= '\uDC00' && c1 <= '\uDFFF') {
				delChars = 2;
			    }
			}
			doc.remove(dot, delChars);
		    }
		    if (doc.getLength() == 0) {
			markAsMandatory(true);
		    }
		}
		catch (BadLocationException bl) {
		}
	    }

	}
    }
}
