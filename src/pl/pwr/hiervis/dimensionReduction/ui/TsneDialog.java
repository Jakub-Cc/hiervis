package pl.pwr.hiervis.dimensionReduction.ui;

import javax.swing.JPanel;

import pl.pwr.hiervis.dimensionReduction.methods.Tsne;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;

public class TsneDialog extends DimensionReductionDialog {

    public static void main(String[] args) {
	DimensionReductionDialog di = new TsneDialog();
	System.out.println(di.showDialog(8, 1000));
    }

    /**
     * 
     */
    private static final long serialVersionUID = -5230239232868489007L;
    private static final TsnePanel mainPanel = new TsnePanel();

    public TsneDialog() {

    }

    @Override
    protected JPanel getMainPanel() {
	return mainPanel;
    }

    @Override
    protected long getRequiredMemorry() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public String getName() {
	return Tsne.sGetName();
    }

    @Override
    public String getSimpleName() {
	return Tsne.sGetSimpleName();
    }

    @Override
    public Class<? extends FeatureExtraction> getResultClass() {
	return Tsne.class;
    }

    @Override
    public void remodel() {
	mainPanel.remodel(maxOutputDimensions, pointsAmount);
    }

    @Override
    public void setResult() {
	int maxIterations = (int) mainPanel.getValues().get(TsnePanel.MaxIterations);
	double perpexity = (double) mainPanel.getValues().get(TsnePanel.Perplexity);
	double theta = (double) mainPanel.getValues().get(TsnePanel.Theta);
	boolean pararell = (boolean) mainPanel.getValues().get(TsnePanel.ParallerCalculations);
	boolean usePca = (boolean) mainPanel.getValues().get(TsnePanel.UsePca);
	int pcaDimensions = (int) mainPanel.getValues().get(TsnePanel.PcaDimensions);

	result = new Tsne(maxIterations, perpexity, theta, pararell, usePca, pcaDimensions, true, true, 2);
    }

}
