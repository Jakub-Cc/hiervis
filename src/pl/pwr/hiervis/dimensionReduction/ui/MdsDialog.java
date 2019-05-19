package pl.pwr.hiervis.dimensionReduction.ui;

import javax.swing.JPanel;

import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.methods.MultidimensionalScaling;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;

public class MdsDialog extends DimensionReductionDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 7991962619979656293L;
    private static final MdsPanel mainPanel = new MdsPanel();

    public static void main(String[] args) {
	DimensionReductionDialog dial = new MdsDialog();
	System.out.println(dial.showDialog(8, 100000));
    }

    public MdsDialog() {
	super();
    }

    @Override
    protected JPanel getMainPanel() {
	return mainPanel;
    }

    public String getName() {
	return MultidimensionalScaling.sGetName();
    }

    @Override
    public String getSimpleName() {
	return MultidimensionalScaling.sGetSimpleName();
    }

    @Override
    public Class<? extends FeatureExtraction> getResultClass() {
	return MultidimensionalScaling.class;
    }

    @Override
    public void remodel() {
	// Not needed
    }

    @Override
    public void setResult() {
	DistanceMeasure distanceMeasure = (DistanceMeasure) mainPanel.getValues().get("DistanceMeasure");
	result = new MultidimensionalScaling(distanceMeasure);
    }

    @Override
    protected long getRequiredMemorry() {
	long requiredMemmory = pointsAmount * pointsAmount;
	requiredMemmory = requiredMemmory + (long) (0.1 * requiredMemmory);
	requiredMemmory = requiredMemmory * 2 * 8 * 2;
	return requiredMemmory;
    }

}
