package pl.pwr.hiervis.dimensionReduction.ui;

import javax.swing.JPanel;

import pl.pwr.hiervis.dimensionReduction.methods.PrincipalComponentAnalysis;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;

public class PcaDialog extends DimensionReductionDialog {

    public static void main(String[] args) {
	DimensionReductionDialog di = new PcaDialog();
	System.out.println(di.showDialog(8, 1000));
    }

    /**
     * 
     */
    private static final long serialVersionUID = 6963918197925755697L;
    private static final PcaPanel mainPanel = new PcaPanel();

    public PcaDialog() {
	super();
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
	return PrincipalComponentAnalysis.sGetName();
    }

    @Override
    public String getSimpleName() {
	return PrincipalComponentAnalysis.sGetSimpleName();
    }

    @Override
    public Class<? extends FeatureExtraction> getResultClass() {
	return PrincipalComponentAnalysis.class;
    }

    @Override
    public void remodel() {
	mainPanel.remodel(maxOutputDimensions);
    }

    @Override
    public void setResult() {
	int initialDimensions = (int) mainPanel.getValues().get(PcaPanel.OutputDimensions);
	result = new PrincipalComponentAnalysis(initialDimensions);
    }

}
