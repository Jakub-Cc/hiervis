package pl.pwr.hiervis.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import internal_measures.statistics.AvgWithStdev;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.measures.MeasureManager;
import pl.pwr.hiervis.measures.MeasureTask;


/*
 * TODO:
 * This class uses a somewhat hacky solution to make the frame always-on-top ONLY
 * within the application. Normally we could use a JDialog for this, however we
 * also want the user to be able to disable this functionality at will.
 * 
 * Another possible solution to this problem would be having shared GUI-creation code,
 * and then calling it inside of a JFrame or a JDialog, depending on the user-selected setting.
 * Changing the setting while the frame was open would close the old frame and create the new one.
 */


@SuppressWarnings("serial")
public class HierarchyStatisticsFrame extends JFrame
{
	private static final Logger log = LogManager.getLogger( HierarchyStatisticsFrame.class );

	private HVContext context;
	private Window owner;

	private JTabbedPane tabPane;
	private JPanel cMeasures;
	private JMenuItem mntmDump;

	private WindowListener ownerListener;
	private int verticalScrollValue = 0;


	public HierarchyStatisticsFrame( HVContext context, Window frame, String subtitle )
	{
		super( "Statistics Frame" + ( subtitle == null ? "" : ( " [ " + subtitle + " ]" ) ) );
		this.context = context;
		this.owner = frame;

		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setMinimumSize( new Dimension( 400, 200 ) );
		setSize( 400, 350 );

		ownerListener = new WindowAdapter() {
			@Override
			public void windowActivated( WindowEvent e )
			{
				HierarchyStatisticsFrame.this.setAlwaysOnTop( true );
			}

			@Override
			public void windowDeactivated( WindowEvent e )
			{
				if ( e.getOppositeWindow() == null ) {
					// Disable 'always on top' ONLY when the opposite window
					// (the one that stole focus from us) is not part of our
					// own application.
					HierarchyStatisticsFrame.this.setAlwaysOnTop( false );
				}
			}
		};

		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowDeactivated( WindowEvent e )
				{
					if ( e.getOppositeWindow() == null ) {
						// Disable 'always on top' ONLY when the opposite window
						// (the one that stole focus from us) is not part of our
						// own application.
						HierarchyStatisticsFrame.this.setAlwaysOnTop( false );
					}
				}
			}
		);

		createMenu();
		createGUI();

		VisualizerFrame.createFileDrop( this, log, "csv", file -> context.loadFile( this, file ) );

		if ( context.isHierarchyDataLoaded() ) {
			createMeasurePanels();
		}

		MeasureManager measureManager = context.getMeasureManager();

		measureManager.measureComputing.addListener( this::onMeasureComputing );
		measureManager.measureComputed.addListener( this::onMeasureComputed );
		measureManager.taskFailed.addListener( this::onTaskFailed );
		context.hierarchyChanging.addListener( this::onHierarchyChanging );
		context.hierarchyChanged.addListener( this::onHierarchyChanged );

		if ( context.isHierarchyDataLoaded() ) {
			context.getHierarchy().measureHolder.forComputedMeasures(
				set -> {
					set.stream().forEach( this::updateMeasurePanel );
				}
			);
		}
	}

	public void setKeepOnTop( boolean onTop )
	{
		setAlwaysOnTop( onTop );

		if ( onTop ) {
			owner.addWindowListener( ownerListener );
		}
		else {
			owner.removeWindowListener( ownerListener );
		}
	}

	// ----------------------------------------------------------------------------------------

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );

		JMenu mnOptions = new JMenu( "Options" );
		menuBar.add( mnOptions );

		mntmDump = new JMenuItem( "Dump Measures" );
		mntmDump.setEnabled( context.isHierarchyDataLoaded() );
		mnOptions.add( mntmDump );

		mntmDump.addActionListener(
			( e ) -> {
				JFileChooser fileDialog = new JFileChooser();
				fileDialog.setCurrentDirectory( new File( "." ) );
				fileDialog.setDialogTitle( "Choose a file" );
				fileDialog.setFileSelectionMode( JFileChooser.FILES_ONLY );
				fileDialog.setAcceptAllFileFilterUsed( false );
				fileDialog.setFileFilter( new FileNameExtensionFilter( "*.csv", "csv" ) );
				fileDialog.setSelectedFile( new File( "dump.csv" ) );

				if ( fileDialog.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
					context.getMeasureManager().dumpMeasures(
						Paths.get( fileDialog.getSelectedFile().getAbsolutePath() ),
						context.getHierarchy()
					);
				}
			}
		);

		JCheckBoxMenuItem mntmAlwaysOnTop = new JCheckBoxMenuItem( "Always On Top" );
		mnOptions.add( mntmAlwaysOnTop );

		mntmAlwaysOnTop.addActionListener(
			( e ) -> {
				setKeepOnTop( mntmAlwaysOnTop.isSelected() );
			}
		);
	}

	private void createGUI()
	{
		tabPane = new JTabbedPane();

		JScrollPane pane = createScrollableMeasurePanel();
		cMeasures = (JPanel)pane.getViewport().getView();
		tabPane.addTab( "Hierarchy Statistics", pane );
		tabPane.addTab( "Node Statistics", createScrollableMeasurePanel() );
		tabPane.setEnabledAt( 1, false );

		getContentPane().add( tabPane, BorderLayout.CENTER );
	}

	private JScrollPane createScrollableMeasurePanel()
	{
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 8 );
		scrollPane.setBorder( BorderFactory.createEmptyBorder() );
		scrollPane.setAutoscrolls( false );

		scrollPane.setViewportView( createRootMeasurePanel() );

		return scrollPane;
	}

	private JPanel createRootMeasurePanel()
	{
		JPanel p = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights = new double[] { Double.MIN_VALUE };
		p.setLayout( layout );

		return p;
	}

	private void createMeasurePanels()
	{
		MeasureManager measureManager = context.getMeasureManager();

		Collection<MeasureTask> validMeasureTasks = measureManager.getMeasureTasks(
			task -> task.applicabilityFunction.apply( context.getHierarchy().data )
		);

		addMeasurePanels( createBulkTaskPanel( "Calculate All", validMeasureTasks ) );

		for ( String groupPath : measureManager.listMeasureTaskGroups() ) {
			Collection<MeasureTask> measureTasks = measureManager.getMeasureTaskGroup( groupPath ).stream()
				.sorted()
				.filter( task -> task.applicabilityFunction.apply( context.getHierarchy().data ) )
				.collect( Collectors.toList() );

			if ( !measureTasks.isEmpty() ) {
				String friendlyGroupName = groupPath.contains( "/" )
					? groupPath.substring( groupPath.lastIndexOf( "/" ) + 1 )
					: groupPath;
				friendlyGroupName = toCamelCase( friendlyGroupName.replaceAll( "_([a-z])", " $1" ), " " );

				addMeasurePanels(
					createFillerPanel( 10 ),
					createSeparatorPanel( friendlyGroupName )
				);

				if ( measureTasks.size() > 1 ) {
					addMeasurePanels( createBulkTaskPanel( "Calculate All " + friendlyGroupName, measureTasks ) );
				}

				for ( MeasureTask task : measureTasks ) {
					addMeasurePanels( createMeasurePanel( task ) );
				}
			}
		}
	}

	private void addMeasurePanels( JPanel... panels )
	{
		int curItems = cMeasures.getComponentCount();
		int newItems = curItems + panels.length;

		GridBagLayout layout = (GridBagLayout)cMeasures.getLayout();
		layout.rowHeights = new int[newItems + 1];
		layout.rowWeights = new double[newItems + 1];
		layout.rowWeights[newItems] = Double.MIN_VALUE;
		cMeasures.setLayout( layout );

		int i = curItems;
		for ( JPanel panel : panels ) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.insets = new Insets( 5, 5, 0, 5 );

			cMeasures.add( panel, constraints );

			++i;
		}

		cMeasures.revalidate();
		cMeasures.repaint();
	}

	private JPanel createFillerPanel( int height )
	{
		JPanel cFiller = new JPanel();

		cFiller.add( Box.createVerticalStrut( height ) );

		return cFiller;
	}

	private JPanel createSeparatorPanel( String title )
	{
		JPanel cSeparator = new JPanel();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0, 0, 0 };
		layout.rowHeights = new int[] { 0, 0 };
		layout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		cSeparator.setLayout( layout );

		JSeparator sepLeft = new JSeparator();
		GridBagConstraints constraintsSepLeft = new GridBagConstraints();
		constraintsSepLeft.insets = new Insets( 0, 5, 5, 5 );
		constraintsSepLeft.fill = GridBagConstraints.HORIZONTAL;
		constraintsSepLeft.gridx = 0;
		constraintsSepLeft.gridy = 0;
		cSeparator.add( sepLeft, constraintsSepLeft );

		JLabel lblTitle = new JLabel( title );
		GridBagConstraints constraintsLabel = new GridBagConstraints();
		constraintsLabel.insets = new Insets( 0, 0, 5, 0 );
		constraintsLabel.fill = GridBagConstraints.VERTICAL;
		constraintsLabel.gridx = 1;
		constraintsLabel.gridy = 0;
		cSeparator.add( lblTitle, constraintsLabel );

		JSeparator sepRight = new JSeparator();
		GridBagConstraints constraintsSepRight = new GridBagConstraints();
		constraintsSepRight.insets = new Insets( 0, 5, 5, 5 );
		constraintsSepRight.fill = GridBagConstraints.HORIZONTAL;
		constraintsSepRight.gridx = 2;
		constraintsSepRight.gridy = 0;
		cSeparator.add( sepRight, constraintsSepRight );

		return cSeparator;
	}

	private JPanel createMeasurePanel( MeasureTask task )
	{
		JPanel cMeasure = new JPanel();
		cMeasure.setBorder( new TitledBorder( null, task.identifier, TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
		cMeasure.setLayout( new BorderLayout( 0, 0 ) );

		if ( context.getHierarchy().measureHolder.isMeasureComputed( task.identifier ) ) {
			cMeasure.add(
				createMeasureContent( context.getHierarchy().measureHolder.getMeasureResult( task.identifier ) ),
				BorderLayout.NORTH
			);
		}
		else {
			cMeasure.add( createTaskButton( Pair.of( context.getHierarchy(), task ) ), BorderLayout.NORTH );
		}

		return cMeasure;
	}

	private JPanel createBulkTaskPanel( String title, Collection<MeasureTask> tasks )
	{
		JPanel cMeasure = new JPanel();
		cMeasure.setLayout( new BorderLayout( 0, 0 ) );
		cMeasure.add( createTaskButton( title, tasks ), BorderLayout.NORTH );

		return cMeasure;
	}

	private JButton createTaskButton( String title, Collection<MeasureTask> tasks )
	{
		JButton button = new JButton();
		button.addActionListener(
			( e ) -> {
				button.setEnabled( false );

				MeasureManager measureManager = context.getMeasureManager();
				LoadedHierarchy hierarchy = context.getHierarchy();
				for ( MeasureTask task : tasks ) {
					if ( !hierarchy.measureHolder.isMeasureComputed( task.identifier )
						&& !measureManager.isMeasurePending( hierarchy, task.identifier ) ) {
						measureManager.postTask( hierarchy, task );
					}
				}
			}
		);

		LoadedHierarchy lh = context.getHierarchy();
		boolean allComplete = !tasks.stream()
			.filter( task -> !lh.measureHolder.isMeasureComputed( task.identifier ) )
			.findAny().isPresent();

		button.setEnabled( context.isHierarchyDataLoaded() && !allComplete );
		button.setText( title );

		return button;
	}

	private JButton createTaskButton( Pair<LoadedHierarchy, MeasureTask> task )
	{
		JButton button = new JButton();
		button.addActionListener(
			( e ) -> {
				MeasureManager measureManager = context.getMeasureManager();

				boolean pending = measureManager.isMeasurePending(
					task.getLeft(),
					task.getRight().identifier
				);
				updateTaskButton( button, !pending );

				if ( pending ) {
					measureManager.removeTask( task );
				}
				else {
					measureManager.postTask( task );
				}
			}
		);
		updateTaskButton( button, false );
		return button;
	}

	private void updateTaskButton( JButton button, boolean pending )
	{
		button.setEnabled( context.isHierarchyDataLoaded() );
		button.setText( pending ? "Abort" : "Calculate" );
	}

	/**
	 * Creates a GUI component used to represent the specified measure computation result.
	 * 
	 * @param result
	 *            the measure computation result to create the component for
	 * @return the GUI component
	 */
	private JComponent createMeasureContent( Object result )
	{
		if ( result == null ) {
			throw new IllegalArgumentException( "Result must not be null!" );
		}

		if ( result instanceof double[] ) {
			// Histogram data // TODO
			double[] data = (double[])result;

			StringBuilder buf = new StringBuilder();
			for ( int i = 0; i < data.length; ++i ) {
				buf.append( Integer.toString( i ) )
					.append( ": " )
					.append( Double.toString( data[i] ) );

				if ( i + 1 < data.length )
					buf.append( "\n" );
			}

			return createFixedTextComponent( buf.toString() );
		}
		else if ( result instanceof AvgWithStdev ) {
			AvgWithStdev avg = (AvgWithStdev)result;
			return createFixedTextComponent( String.format( "%s ± %s", avg.getAvg(), avg.getStdev() ) );
		}
		else if ( result instanceof Double ) {
			return createFixedTextComponent( result.toString() );
		}
		else if ( result instanceof String ) {
			return createFixedTextComponent( result.toString() );
		}
		else {
			throw new IllegalArgumentException(
				String.format(
					"No case defined for data type '%s'",
					result.getClass().getSimpleName()
				)
			);
		}
	}

	private JTextComponent createFixedTextComponent( String msg )
	{
		JTextArea result = new JTextArea( msg );
		result.setEditable( false );
		result.setBorder( UIManager.getBorder( "TextField.border" ) );

		return result;
	}

	private JPanel findMeasurePanelHierarchy( String title )
	{
		return findMeasurePanel( 0, title );
	}

	private JPanel findMeasurePanelNode( String title )
	{
		return findMeasurePanel( 1, title );
	}

	private JPanel findMeasurePanel( int tabIndex, String title )
	{
		JScrollPane hierarchyPane = (JScrollPane)tabPane.getComponentAt( tabIndex );
		JPanel panel = (JPanel)hierarchyPane.getViewport().getView();
		return findMeasurePanel( panel, title );
	}

	private JPanel findMeasurePanel( JPanel panel, String title )
	{
		for ( Component c : panel.getComponents() ) {
			if ( c instanceof JPanel ) {
				JPanel p = (JPanel)c;
				Border b = p.getBorder();

				if ( b instanceof TitledBorder ) {
					TitledBorder tb = (TitledBorder)b;
					if ( tb.getTitle().equals( title ) ) {
						return p;
					}
				}
			}
		}

		return null;
	}

	private void updateMeasurePanel( String measureName, Object measureResult )
	{
		// Inserting components into a JScrollPane tends to trigger its
		// autoscrolling functionality, even when it has been explicitly disabled.
		// Bandaid fix is to re-set the scrollbar ourselves when we're done.
		JScrollPane scrollPane = (JScrollPane)tabPane.getComponentAt( 0 );
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		int scrollValue = vertical.getValue();

		JPanel panel = findMeasurePanelHierarchy( measureName );
		panel.removeAll();

		panel.add( createMeasureContent( measureResult ), BorderLayout.NORTH );
		panel.revalidate();
		panel.repaint();

		SwingUtilities.invokeLater( () -> vertical.setValue( scrollValue ) );
	}

	private void updateMeasurePanel( Entry<String, Object> result )
	{
		updateMeasurePanel( result.getKey(), result.getValue() );
	}

	private void recreateMeasurePanel( Pair<LoadedHierarchy, MeasureTask> task )
	{
		JPanel panel = findMeasurePanelHierarchy( task.getRight().identifier );
		panel.removeAll();

		panel.add( createTaskButton( task ), BorderLayout.NORTH );
		panel.revalidate();
		panel.repaint();
	}

	private static String toCamelCase( String input, String delimiter )
	{
		String[] words = input.split( delimiter );
		for ( int i = 0; i < words.length; ++i ) {
			String word = words[i];
			words[i] = Character.toUpperCase( word.charAt( 0 ) ) + word.substring( 1 ).toLowerCase();
		}
		return String.join( " ", words );
	}

	// ----------------------------------------------------------------------------------------
	// Listeners

	private void onMeasureComputing( Pair<LoadedHierarchy, String> task )
	{
		if ( task.getLeft().equals( context.getHierarchy() ) ) {
			SwingUtilities.invokeLater(
				() -> {
					// This code is deferred, check hierarchies again.
					if ( !task.getLeft().equals( context.getHierarchy() ) )
						return;

					String measureName = task.getRight();

					// This code is deferred and executed on the main thread, so there's no guarantee that
					// it will actually get to run before the measure is computed.
					// If the measure was computed before we got here, then there's nothing for us to do.
					if ( !context.getHierarchy().measureHolder.isMeasureComputed( measureName ) ) {
						JPanel panel = findMeasurePanelHierarchy( measureName );
						JButton button = (JButton)panel.getComponent( 0 );
						button.setEnabled( false );
						button.setText( "Calculating..." );
					}
				}
			);
		}
	}

	private void onMeasureComputed( Triple<LoadedHierarchy, String, Object> result )
	{
		if ( result.getLeft().equals( context.getHierarchy() ) ) {
			SwingUtilities.invokeLater( () -> updateMeasurePanel( result.getMiddle(), result.getRight() ) );
		}
	}

	private void onTaskFailed( Pair<LoadedHierarchy, MeasureTask> task )
	{
		if ( task.getLeft().equals( context.getHierarchy() ) ) {
			SwingUtilities.invokeLater( () -> recreateMeasurePanel( task ) );
		}
	}

	private void onHierarchyChanging( LoadedHierarchy oldHierarchy )
	{
		// Store the current scroll before the hierarchy is changed, so that we can
		// restore it when the new hierarchy is loaded.
		JScrollPane scrollPane = (JScrollPane)tabPane.getComponentAt( 0 );
		verticalScrollValue = scrollPane.getVerticalScrollBar().getValue();

		cMeasures.removeAll();
		cMeasures.revalidate();
		cMeasures.repaint();
	}

	private void onHierarchyChanged( LoadedHierarchy newHierarchy )
	{
		mntmDump.setEnabled( newHierarchy != null );
		createMeasurePanels();

		if ( newHierarchy != null ) {
			SwingUtilities.invokeLater(
				() -> {
					JScrollPane scrollPane = (JScrollPane)tabPane.getComponentAt( 0 );
					scrollPane.getVerticalScrollBar().setValue( verticalScrollValue );
				}
			);
		}

		cMeasures.revalidate();
		cMeasures.repaint();
	}
}
