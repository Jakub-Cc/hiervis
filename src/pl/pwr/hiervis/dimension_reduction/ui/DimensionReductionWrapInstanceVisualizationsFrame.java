package pl.pwr.hiervis.dimension_reduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.CalculatedDimensionReduction;
import pl.pwr.hiervis.dimension_reduction.DimensionReductionRunnerManager;
import pl.pwr.hiervis.dimension_reduction.FeatureSelectionResultDialogManager;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.ui.elements.LoadingIcon;
import pl.pwr.hiervis.dimension_reduction.ui.elements.SeparatorComboBox;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionWrapInstanceVisualizationsFrame extends JFrame {

	private class HierarchyChangingClass implements Consumer<LoadedHierarchy> {
		@Override
		public void accept(LoadedHierarchy t) {
			disableSelection();
			disableRecalcBtn();
		}
	}

	private class HierarchyChangedClass implements Consumer<LoadedHierarchy> {
		@Override
		public void accept(LoadedHierarchy t) {
			previousSelection = -1;
			comboBox.setTrueIndex(0);
			enableSelection();
		}

		private void enableSelection() {
			comboBox.setEnabled(true);
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private transient HVContext context;
	private transient HierarchyChangedClass changedClass;
	private transient HierarchyChangingClass changingClass;
	private transient FeatureSelectionResultDialogManager fSResultdialogManager;
	private transient DimensionReductionRunnerManager dimensionReductionRunnerManager;
	private int previousSelection;
	private JButton forceBtn;
	private Component horizontalStrut;
	private LoadingIcon loadingIcon;
	private Component horizontalStrut1;
	private JButton btnStop;
	private Container instanceContainer;
	private Box horizontalBox;
	SeparatorComboBox comboBox;

	/**
	 * Create the frame.
	 */
	public DimensionReductionWrapInstanceVisualizationsFrame(HVContext context) {
		this.context = context;
		fSResultdialogManager = new FeatureSelectionResultDialogManager();
		changedClass = new HierarchyChangedClass();
		changingClass = new HierarchyChangingClass();
		dimensionReductionRunnerManager = new DimensionReductionRunnerManager(context);
		previousSelection = 0;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();

		contentPane.add(panel, BorderLayout.PAGE_START);
		panel.setLayout(new BorderLayout(0, 0));

		context.hierarchyChanging.addListener(changingClass);
		context.hierarchyChanged.addListener(changedClass);

		context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);

		horizontalBox = Box.createHorizontalBox();
		panel.add(horizontalBox, BorderLayout.WEST);

		comboBox = new SeparatorComboBox();
		horizontalBox.add(comboBox);

		horizontalStrut = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut);

		btnStop = new JButton("Stop Calculation");
		horizontalBox.add(btnStop);
		btnStop.setVerticalAlignment(SwingConstants.TOP);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnStop.addActionListener(this::cancelCurrentCalculation);
		btnStop.setFocusable(false);
		btnStop.setVisible(false);

		horizontalStrut1 = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut1);

		forceBtn = new JButton("Recalculate");
		horizontalBox.add(forceBtn);
		forceBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		forceBtn.setVerticalAlignment(SwingConstants.TOP);
		forceBtn.setFocusable(false);

		forceBtn.addActionListener(this::recalculateReduction);

		comboBox.addItem("Orginal data space");

		loadingIcon = new LoadingIcon();
		loadingIcon.hideIcon();

		instanceContainer = this.context.getInstanceFrame().getContentPane();

		contentPane.add(loadingIcon);
		contentPane.add(instanceContainer, BorderLayout.CENTER);

		getContentPane().addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
				// not used
			}

			@Override
			public void componentResized(ComponentEvent e) {
				if (loadingIcon.isVisible()) {
					displayLoadingIcon();
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// not used
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// not used
			}
		});

		comboBox.addSeparator();

		for (int i = 0; i < context.getDimensionReductionMenager().getNames().length; i++) {
			if (checkIfSeparatorNeeded(i)) {
				comboBox.addSeparator();
			}
			String s = context.getDimensionReductionMenager().getNames()[i];
			comboBox.addItem(s);
		}

		comboBox.addActionListener(e -> {
			onDimensionReductionSelected(comboBox.getTrueIndex());
			previousSelection = comboBox.getTrueIndex();
		});

		disableSelection();
		disableRecalcBtn();
	}

	private boolean checkIfSeparatorNeeded(int index) {
		if (index == 0)
			return false;

		Class<?> currentClass = context.getDimensionReductionMenager().getResaultClass(index);
		Class<?> previousClass = context.getDimensionReductionMenager().getResaultClass(index - 1);

		return !((FeatureExtraction.class.isAssignableFrom(currentClass)
				&& FeatureExtraction.class.isAssignableFrom(previousClass))
				|| (FeatureSelection.class.isAssignableFrom(currentClass)
						&& FeatureSelection.class.isAssignableFrom(previousClass)));
	}

	private void recalculateReduction(@SuppressWarnings("unused") ActionEvent e) {
		int index = comboBox.getTrueIndex();
		DimensionReductionI dimensionReduction = handleDialog(index);

		if (dimensionReduction != null) {
			context.getHierarchy().getHierarchyWraper().getReducedHierarchy()[index - 1] = null;
			disableRecalcBtn();
		}
	}

	private void calculateReduction(int index) {
		DimensionReductionI dimensionReduction = handleDialog(index);

		if (dimensionReduction == null) {
			comboBox.setTrueIndex(previousSelection);
			onDimensionReductionSelected(previousSelection);
		}
	}

	private DimensionReductionI handleDialog(int index) {
		int x = (int) getContentPane().getLocationOnScreen().getX();
		int y = (int) getContentPane().getLocationOnScreen().getY();

		DimensionReductionI dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
				context.getHierarchy().getHierarchyWraper().getOriginalHierarchy(), x, y);

		if (dimensionReduction != null) {
			afterConfirmedReduction(context.getHierarchy(), dimensionReduction);
		}

		return dimensionReduction;
	}

	private void onDimensionReductionSelected(int index) {
		btnStop.setVisible(false);
		loadingIcon.hideIcon();

		if (index == -1) {
			previousSelection = -1;
			comboBox.setTrueIndex(0);
		} else if (index == 0) {
			afterOrginalHierSeleted();
		} else if (context.getHierarchy().getHierarchyWraper().getHierarchyWithoutChange(index) != null) {
			afterCalculatedFExtractionSelected(index);
		} else if (context.getHierarchy().getHierarchyWraper().getFSResult(index) != null) {
			afterCalculatedFSelectionSelected(index);
		} else if (context.getDimensionReductionMenager().isInQueue(context.getHierarchy(), index - 1)) {
			reductonCalculating();
		} else {
			calculateReduction(index);
		}
	}

	private void afterOrginalHierSeleted() {
		disableRecalcBtn();
		if (previousSelection != comboBox.getTrueIndex()) {
			reloadHierarchy(0);
		}
	}

	private void afterCalculatedFExtractionSelected(int index) {
		enableRecalcBtn();
		if (previousSelection != comboBox.getTrueIndex()) {
			reloadHierarchy(index);
		}
	}

	/**
	 * ustawia hierarchie na nowa zalezna od indexu oraz odswieza widoki omijajac
	 * liste z wyborem aby nie ustawila sie zpowrotem na poczatkowych wartosciach
	 */
	private void reloadHierarchy(int index) {
		context.getHierarchy().getHierarchyWraper().setHierarchy(index);
		context.getHierarchy().recalculateInstanceTable();

		context.hierarchyChanging.removeListener(changingClass);
		context.hierarchyChanged.removeListener(changedClass);

		context.hierarchyChanging.broadcast(context.getHierarchy());
		context.hierarchyChanged.broadcast(context.getHierarchy());

		context.hierarchyChanging.addListener(changingClass);
		context.hierarchyChanged.addListener(changedClass);
	}

	/**
	 * Will not validate if index correct or FS result calculated
	 */
	private void afterCalculatedFSelectionSelected(int index) {
		fSResultdialogManager.showDialog(context.getHierarchy(),
				context.getDimensionReductionMenager().getResaultClass(index - 1));

		enableRecalcBtn();

		if (previousSelection != index) {
			context.getInstanceFrame().clearUI();
			context.dimensionReductionCalculating.broadcast(null);
		}
	}

	private void reductonCalculating() {
		disableRecalcBtn();
		context.getInstanceFrame().clearUI();
		context.dimensionReductionCalculating.broadcast(null);
		displayLoadingIcon();
	}

	private void afterConfirmedReduction(LoadedHierarchy loadedHierarchy, DimensionReductionI dimensionReduction) {
		reductonCalculating();
		context.dimensionReductionCalculating.broadcast(dimensionReduction);
		context.getDimensionReductionMenager().addToQueue(loadedHierarchy, dimensionReduction.getClass());
		dimensionReductionRunnerManager.addTask(loadedHierarchy, dimensionReduction);
	}

	private void onDimensionReductionCalculated(CalculatedDimensionReduction reduction) {
		context.getDimensionReductionMenager().removeFromQueue(reduction.getInputLoadedHierarchy(),
				reduction.getDimensionReduction().getClass());
		if (FeatureExtraction.class.isAssignableFrom(reduction.getDimensionReduction().getClass())
				&& reduction.getOutputHierarchy() != null) {
			fExtractionCalculated(reduction);
		} else if (FeatureSelection.class.isAssignableFrom(reduction.getDimensionReduction().getClass())
				&& reduction.getFsResult() != null) {
			fSelectionCalculated(reduction);
		} else {
			previousSelection = -1;
			comboBox.setTrueIndex(0);
		}
	}

	private void fExtractionCalculated(CalculatedDimensionReduction reduction) {
		int index = context.getDimensionReductionMenager().getIndex(reduction.getDimensionReduction()) + 1;
		if (reduction.getInputLoadedHierarchy() == context.getHierarchy() && comboBox.getTrueIndex() == index) {
			context.getHierarchy().getHierarchyWraper().addReducedHierarchy(reduction);
			previousSelection = -1;
			comboBox.setTrueIndex(index);
		} else {
			for (LoadedHierarchy l : context.getHierarchyList()) {
				if (l == reduction.getInputLoadedHierarchy()) {
					l.getHierarchyWraper().addReducedHierarchy(reduction);
					return;
				}
			}
		}
	}

	private void fSelectionCalculated(CalculatedDimensionReduction reduction) {
		int index = context.getDimensionReductionMenager().getIndex(reduction.getDimensionReduction()) + 1;

		if (reduction.getInputLoadedHierarchy() == context.getHierarchy() && comboBox.getTrueIndex() == index) {
			previousSelection = -1;
			comboBox.setTrueIndex(0);
			context.getHierarchy().getHierarchyWraper().addFeatureSelectionResult(reduction);
			fSResultdialogManager.addResultAndShow(reduction);
		} else {
			for (LoadedHierarchy l : context.getHierarchyList()) {
				if (l == reduction.getInputLoadedHierarchy()) {
					l.getHierarchyWraper().addFeatureSelectionResult(reduction);
					return;
				}
			}
		}
	}

	private void cancelCurrentCalculation(@SuppressWarnings("unused") ActionEvent e) {
		dimensionReductionRunnerManager.interuptTask(context.getHierarchy(),
				context.getDimensionReductionMenager().getResaultClass(comboBox.getTrueIndex() - 1));
	}

	private void disableSelection() {
		comboBox.setEnabled(false);
	}

	private void disableRecalcBtn() {
		forceBtn.setEnabled(false);
		forceBtn.setVisible(false);
	}

	private void enableRecalcBtn() {
		forceBtn.setEnabled(true);
		forceBtn.setVisible(true);
	}

	private void displayLoadingIcon() {
		Component component = context.getInstanceFrame().getContentPane().getComponent(2);
		double x = component.getX();
		double y = component.getY();
		double width = component.getWidth();
		double height = component.getHeight();
		double x1 = instanceContainer.getX();
		double y1 = instanceContainer.getY();
		double posX = 0;
		double posY = 0;
		if (width < 210) {
			posX = x + x1 + 10;
		} else {
			posX = x + x1 + width / 2 - 100 + 10;
		}
		if (height < 210) {
			posY = y + y1 + 10;
		} else {
			posY = y + y1 + height / 2 - 100 + 10;
		}

		loadingIcon.showIcon((int) (posX), (int) (posY));
		btnStop.setVisible(true);
	}
}
