package pl.pwr.hiervis.prefuse.control;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import prefuse.controls.ControlAdapter;
import prefuse.data.Node;
import prefuse.data.Tree;
import prefuse.visual.VisualItem;

/**
 * Allows traversal of nodes in a tree structure using keyboard arrow keys.
 * 
 * @author Tomasz Bachmiński
 *
 */
public class NodeSelectionControl extends ControlAdapter {
	protected Supplier<Tree> treeSupplier;
	protected Supplier<Integer> getter;
	protected Consumer<Integer> setter;

	public NodeSelectionControl(Supplier<Tree> treeSupplier, Supplier<Integer> currentSelectionGetter,
			Consumer<Integer> currentSelectionSetter) {
		this.treeSupplier = treeSupplier;
		this.getter = currentSelectionGetter;
		this.setter = currentSelectionSetter;
	}

	@Override
	public void itemKeyPressed(VisualItem item, KeyEvent e) {
		processKeyEvent(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		processKeyEvent(e);
	}

	private void processKeyEvent(KeyEvent e) {
		if (isArrowEvent(e)) {
			Node n = treeSupplier.get().getNode(getter.get());

			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				n = n.getParent();
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				n = n.getFirstChild();
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				n = keyPresedLeft(n);
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				n = keyPresedRight(n);
				break;
			default:
			}

			if (n != null) {
				setter.accept(n.getRow());
			}
		}
	}

	private Node keyPresedLeft(Node n) {
		Node s = n.getPreviousSibling();
		if (s == null && n.getParent() != null) {
			s = n.getParent().getLastChild();
		}
		return s;
	}

	private Node keyPresedRight(Node n) {
		Node s = n.getNextSibling();
		if (s == null && n.getParent() != null) {
			s = n.getParent().getFirstChild();
		}
		return s;
	}

	private boolean isArrowEvent(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_UP:
		case KeyEvent.VK_KP_DOWN:
		case KeyEvent.VK_KP_LEFT:
		case KeyEvent.VK_KP_RIGHT:
			return true;
		default:
			return false;
		}
	}
}
