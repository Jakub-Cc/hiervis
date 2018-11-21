package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.pwr.hiervis.dimensionReduction.methods.Tsne;

public class TsneDialog extends DimensionReductionDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner spinner;
	private JLabel lblPcaInitialDimensions;
	private JCheckBox chckbxUsePca;
	private JSpinner spinner_iter;
	private JSpinner spinner_per;
	private JSpinner spinner_tetha;
	private JLabel lblPerplexity;
	private JLabel lblMaxIterations;
	private JLabel lblTetha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			TsneDialog dialog = new TsneDialog();
			dialog.showDialog(10, 200);
			// dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			// dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TsneDialog()
	{
		this.setResizable(false);
		setBounds(100, 100, 450, 455);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 383, 434, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						result = new Tsne(chckbxUsePca.isSelected(), (int) spinner.getValue(), 2,
								(int) spinner_iter.getValue(), (double) spinner_per.getValue(),
								chckbxUsePca.isSelected(), (double) spinner_tetha.getValue(), true, true);
						dispose();
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		{
			JCheckBox chckbxParallerCalculations = new JCheckBox("Paraller calculations");
			chckbxParallerCalculations.setSelected(true);
			chckbxParallerCalculations.setBounds(146, 189, 278, 23);
			getContentPane().add(chckbxParallerCalculations);
		}
		{
			chckbxUsePca = new JCheckBox("Use PCA for predimension reduction");
			chckbxUsePca.setBounds(146, 244, 278, 23);
			chckbxUsePca.addChangeListener(new ChangeListener()
			{

				@Override
				public void stateChanged(ChangeEvent e)
				{

					if (((JCheckBox) e.getSource()).isSelected())
					{
						spinner.setVisible(true);
						lblPcaInitialDimensions.setVisible(true);
					}
					else
					{
						spinner.setVisible(false);
						lblPcaInitialDimensions.setVisible(false);
					}

				}
			});
			getContentPane().add(chckbxUsePca);
		}

		JPanel maxIterationsPanel = new JPanel();
		maxIterationsPanel.setBounds(146, 34, 200, 28);
		getContentPane().add(maxIterationsPanel);
		{
			spinner_iter = new JSpinner();
			spinner_iter.setModel(new SpinnerNumberModel(1000, 100, 5000, 100));
			{
				lblMaxIterations = new JLabel("Max iterations");
			}
			GroupLayout gl_maxIterationsPanel = new GroupLayout(maxIterationsPanel);
			gl_maxIterationsPanel.setHorizontalGroup(gl_maxIterationsPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_maxIterationsPanel
							.createSequentialGroup().addGap(5).addComponent(spinner_iter, GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(5).addComponent(lblMaxIterations)));
			gl_maxIterationsPanel.setVerticalGroup(gl_maxIterationsPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_maxIterationsPanel.createSequentialGroup().addGap(5).addComponent(spinner_iter,
							GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_maxIterationsPanel.createSequentialGroup().addGap(8).addComponent(lblMaxIterations)));
			maxIterationsPanel.setLayout(gl_maxIterationsPanel);
			maxIterationsPanel.addMouseWheelListener(new spin(spinner_iter));
		}

		JPanel perplexityPanel = new JPanel();
		perplexityPanel.setBounds(146, 83, 200, 28);
		getContentPane().add(perplexityPanel);
		{
			spinner_per = new JSpinner();
			spinner_per.setModel(new SpinnerNumberModel(20.0, 5.0, 50, 5.0));
			spinner_per.setBounds(146, 137, 53, 20);
		}
		{
			lblPerplexity = new JLabel("Perplexity");
		}
		GroupLayout gl_perplexityPanel = new GroupLayout(perplexityPanel);
		gl_perplexityPanel.setHorizontalGroup(gl_perplexityPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_perplexityPanel.createSequentialGroup().addGap(5)
						.addComponent(spinner_per, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE).addGap(5)
						.addComponent(lblPerplexity)));
		gl_perplexityPanel.setVerticalGroup(gl_perplexityPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_perplexityPanel.createSequentialGroup().addGap(5).addComponent(spinner_per,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_perplexityPanel.createSequentialGroup().addGap(8).addComponent(lblPerplexity)));
		perplexityPanel.setLayout(gl_perplexityPanel);
		perplexityPanel.addMouseWheelListener(new spin(spinner_per));
		{
			JPanel tethaPanel = new JPanel();
			tethaPanel.setBounds(146, 137, 200, 28);
			getContentPane().add(tethaPanel);
			{
				spinner_tetha = new JSpinner();
				spinner_tetha.setModel(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.05));
			}
			{
				lblTetha = new JLabel("Theta");
			}
			GroupLayout gl_tethaPanel = new GroupLayout(tethaPanel);
			gl_tethaPanel.setHorizontalGroup(gl_tethaPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_tethaPanel.createSequentialGroup().addGap(5)
							.addComponent(spinner_tetha, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addGap(5).addComponent(lblTetha)));
			gl_tethaPanel.setVerticalGroup(gl_tethaPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_tethaPanel.createSequentialGroup().addGap(5).addComponent(spinner_tetha,
							GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_tethaPanel.createSequentialGroup().addGap(8).addComponent(lblTetha)));
			tethaPanel.setLayout(gl_tethaPanel);
			tethaPanel.addMouseWheelListener(new spin(spinner_tetha));
		}
		{
			JPanel pcaPanel = new JPanel();
			pcaPanel.setBounds(146, 289, 200, 28);
			getContentPane().add(pcaPanel);

			spinner = new JSpinner();
			spinner.setVisible(false);

			spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));

			lblPcaInitialDimensions = new JLabel("PCA initial dimensions");
			GroupLayout gl_pcaPanel = new GroupLayout(pcaPanel);
			gl_pcaPanel.setHorizontalGroup(gl_pcaPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pcaPanel.createSequentialGroup().addGap(5)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE).addGap(5)
							.addComponent(lblPcaInitialDimensions)));
			gl_pcaPanel.setVerticalGroup(gl_pcaPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pcaPanel.createSequentialGroup().addGap(5).addComponent(spinner,
							GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_pcaPanel.createSequentialGroup().addGap(8).addComponent(lblPcaInitialDimensions)));
			pcaPanel.setLayout(gl_pcaPanel);
			lblPcaInitialDimensions.setVisible(false);

			pcaPanel.addMouseWheelListener(new spin(spinner));
		}
	}

	@Override
	public String getName()
	{
		return "t-SNE";
	}

	@Override
	public String getSimpleName()
	{
		return "t-SNE";
	}

	@Override
	public void remodel()
	{
		spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
		spinner_per.setModel(new SpinnerNumberModel(20.0, 5.0, Math.round((pointsAmount - 1) / 3), 5.0));

	}

	private class spin implements MouseWheelListener
	{
		JSpinner spinner;

		spin(JSpinner spinner)
		{
			this.spinner = spinner;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{

			SpinnerNumberModel spinnerModel = (SpinnerNumberModel) spinner.getModel();

			double multi = 1;

			if (e.isAltDown())
				multi = 5;
			if (e.isControlDown())
				multi = 0.5;

			if (spinnerModel.getValue().getClass() == Integer.class)
			{
				Integer min = ((Integer) spinnerModel.getMinimum());
				Integer max = ((Integer) spinnerModel.getMaximum());
				Integer step = (int) (((Integer) spinnerModel.getStepSize()) * multi);
				Integer value = ((Integer) spinner.getValue());

				if (value - e.getWheelRotation() * step >= min && value - e.getWheelRotation() * step <= max)
					spinner.setValue(value - e.getWheelRotation() * step);
			}
			else if (spinnerModel.getValue().getClass() == Double.class)
			{
				Double min = (Double) spinnerModel.getMinimum();
				Double max = (Double) spinnerModel.getMaximum();
				Double step = (Double) spinnerModel.getStepSize() * multi;
				Double value = (Double) spinner.getValue();

				if (value - e.getWheelRotation() * step >= min && value - e.getWheelRotation() * step <= max)
					spinner.setValue(value - e.getWheelRotation() * step);
			}
			else
			{
				System.out.println(spinnerModel.getValue().getClass());
			}

		}

	}
}