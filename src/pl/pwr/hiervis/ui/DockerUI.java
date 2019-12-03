package pl.pwr.hiervis.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexdock.dockbar.DockbarManager;
import org.flexdock.dockbar.event.DockbarEvent;
import org.flexdock.dockbar.event.DockbarListener;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.ui.DimensionReductionWrapInstanceVisualizationsFrame;
import pl.pwr.hiervis.dimension_reduction.ui.DropDimensionDialog;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.Event;
import pl.pwr.hiervis.util.LoadedHierarchyUtils;
import pl.pwr.hiervis.util.SwingUIUtils;

public class DockerUI extends JFrame implements DockingConstants {

	private static final long serialVersionUID = 1180591908743485596L;

	private static final Logger log = LogManager.getLogger(DockerUI.class);

	/** Sent when a hierarchy tab is closed. */
	public final transient Event<Integer> hierarchyTabClosed = new Event<>();
	/** Sent when a hierarchy tab is selected. */
	public final transient Event<Integer> hierarchyTabSelected = new Event<>();

	private transient HVContext context;

	private JMenuItem mntmCloseFile;
	private JMenuItem mntmSaveFile;
	private JMenuItem mntmFlatten;
	private JMenuItem mntmCut;
	private JMenuItem mntmDrop;
	private View viewHier;
	private View viewVis;
	private View viewStats;

	public static void main(String[] args) {
		SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		HVContext hVcontext = HVContext.getContext();
		hVcontext.createGUI("HierVis");

		EventQueue.invokeLater(() -> startup(hVcontext));

	}

	public DockerUI(HVContext hvContext) {
		super("HierVis");
		context = hvContext;

		setContentPane(createContentPane());
		createMenu();

		context.hierarchyChanging.addListener(this::onHierarchyChanging);
		context.hierarchyChanged.addListener(this::onHierarchyChanged);
		context.getHierarchyFrame().hierarchyTabClosed.addListener(this::onHierarchyTabClosed);

		context.dimensionReductionCalculating.addListener(e -> {
			viewVis.revalidate();
			viewVis.repaint();
		});

		SwingUIUtils.addCloseCallback(this, this::onWindowClosing);
	}

	private JPanel createContentPane() {
		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));

		Viewport viewport = new Viewport();
		p.add(viewport, BorderLayout.CENTER);

		viewHier = new View("Hier", "Hierarchy");
		viewHier.setName("Hier");
		viewHier.addAction(PIN_ACTION);
		viewHier.setContentPane(context.getHierarchyFrame().getContentPane());
		viewHier.setTerritoryBlocked(CENTER_REGION, true);
		viewHier.getDockingProperties().setDockingEnabled(false);

		viewVis = new View("Visu", "Visualisation");
		viewVis.setName("Visu");
		DimensionReductionWrapInstanceVisualizationsFrame wraper = new DimensionReductionWrapInstanceVisualizationsFrame(
				context);
		viewVis.setContentPane(wraper.getContentPane());
		viewVis.setTerritoryBlocked(CENTER_REGION, true);
		viewVis.getDockingProperties().setDockingEnabled(false);

		viewStats = new View("Stat", "Statistics");
		viewStats.setName("Stat");
		viewStats.addAction(PIN_ACTION);
		viewStats.setTerritoryBlocked(CENTER_REGION, true);
		viewStats.setContentPane(context.getStatisticsFrame().getContentPane());
		viewStats.getDockingProperties().setDockingEnabled(false);

		DockbarManager.addListener(new DockbarListener() {
			@Override
			public void minimizeStarted(DockbarEvent arg0) {
				// not needed
			}

			@Override
			public void minimizeCompleted(DockbarEvent arg0) {
				// not needed
			}

			@Override
			public void dockableLocked(DockbarEvent arg0) {
				// not needed
			}

			@Override
			public void dockableExpanded(DockbarEvent arg0) {
				if (arg0.getSource() == viewStats) {
					viewStats.repaint();
					viewStats.revalidate();
				}
			}

			@Override
			public void dockableCollapsed(DockbarEvent arg0) {
				((View) arg0.getSource()).getDockingProperties().setDockingEnabled(false);
				if (arg0.getSource() == viewHier && !viewHier.isMinimized() && !viewStats.isMinimized()) {
					viewHier.dock(viewStats, SOUTH_REGION, .5f);
				}
			}
		});

		viewport.dock(viewVis);
		viewVis.dock(viewHier, WEST_REGION, .3f);
		viewHier.dock(viewStats, SOUTH_REGION, .5f);

		if (!Boolean.getBoolean("disable.system.exit")) {
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		} else {
			this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}

		return p;
	}

	private static void startup(HVContext hvContext) {
		DockingManager.setFloatingEnabled(false);

		JFrame f = new DockerUI(hvContext);
		f.setSize(1000, 650);
		f.setVisible(true);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createAboutMenu(menuBar);

	}

	private void createFileMenu(JMenuBar menuBar) {
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');

		menuBar.add(mnFile);

		JMenuItem mntmOpenFile = new JMenuItem("Open file...");
		mntmOpenFile.setMnemonic('O');
		mntmOpenFile.addActionListener(e -> context.getHierarchyFrame().openFileSelectionDialog());
		mnFile.add(mntmOpenFile);

		mntmCloseFile = new JMenuItem("Close hierarchy");
		mntmCloseFile.setMnemonic('W');
		mntmCloseFile.addActionListener(e -> context.getHierarchyFrame().closeCurrentTab());
		mntmCloseFile.setEnabled(false);
		mnFile.add(mntmCloseFile);

		mntmSaveFile = new JMenuItem("Save current visible hierarchy...");
		mntmSaveFile.setMnemonic('S');
		mntmSaveFile.addActionListener(e -> context.getHierarchyFrame().openSaveDialog());
		mntmSaveFile.setEnabled(false);
		mnFile.add(mntmSaveFile);

		mnFile.add(new JSeparator());

		JMenuItem mntmConfig = new JMenuItem("Config");
		mntmConfig.setMnemonic('C');
		mntmConfig.addActionListener(e -> context.getHierarchyFrame().openConfigDialog());
		mnFile.add(mntmConfig);
	}

	private void createEditMenu(JMenuBar menuBar) {
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);

		mntmFlatten = new JMenuItem("Flatten Hierarchy");
		mntmFlatten.setMnemonic('F');
		mntmFlatten.addActionListener(e -> {
			String tabTitle = "[F] " + context.getHierarchyFrame().getSelectedTabTitle();
			context.loadHierarchy(tabTitle, LoadedHierarchyUtils.flattenHierarchyKeepReductons(context.getHierarchy()));
		});
		mntmFlatten.setEnabled(false);
		mnEdit.add(mntmFlatten);

		mntmCut = new JMenuItem("Create a subtree based on the curent node");
		mntmCut.setMnemonic('C');
		mntmCut.addActionListener(e -> {
			String nodeName = context.getHierarchy().getTree().getNode(context.getHierarchy().getSelectedRow()).get(0)
					.toString();
			String tabTitle = "[" + nodeName + "] " + context.getHierarchyFrame().getSelectedTabTitle();
			Hierarchy cutHierarchy = HierarchyUtils.subHierarchy(
					context.getHierarchy().getHierarchyWraper().getOriginalHierarchy(), nodeName, (String) null);
			context.loadHierarchy(tabTitle, new LoadedHierarchy(cutHierarchy, context.getHierarchy().options));
		});
		mntmCut.setEnabled(false);
		mnEdit.add(mntmCut);

		mntmDrop = new JMenuItem("Remove Dimensions");
		mntmDrop.setMnemonic('D');

		mntmDrop.addActionListener(e -> {
			DropDimensionDialog dropDimensionDialogn = new DropDimensionDialog(context.getHierarchy());
			LoadedHierarchy hierarchy = dropDimensionDialogn.showDialog();
			if (hierarchy != null) {
				String tabTitle = "[D] " + context.getHierarchyFrame().getSelectedTabTitle();
				context.loadHierarchy(tabTitle, hierarchy);
			}
		});

		mntmDrop.setEnabled(false);
		mnEdit.add(mntmDrop);
	}

	private void openAboutMenu() {
		int dialogButton = JOptionPane.INFORMATION_MESSAGE;
		try {
			Date d = new Date(DockerUI.class.getResource("DockerUI.class").openConnection().getLastModified());
			String message = "Hierarchical Visualizator (ver. 2.1)\n" + "Version copiled on: " + d.toString() + "\n"
					+ "Sourecode avaliable at: https://github.com/lpolech/hiervis";

			JOptionPane.showMessageDialog(this, message, "About", dialogButton);

		} catch (IOException e) {
			log.error(e);
		}

	}

	private void createAboutMenu(JMenuBar menuBar) {
		JMenu mnAbout = new JMenu("About");
		mnAbout.setMnemonic('A');

		mnAbout.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// not needed
			}

			@Override
			public void mousePressed(MouseEvent e) {
				openAboutMenu();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// not needed
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// not needed
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// not needed
			}
		});

		menuBar.add(mnAbout);
	}

	private void onHierarchyChanging(LoadedHierarchy oldHierarchy) {
		mntmCloseFile.setEnabled(false);
		mntmSaveFile.setEnabled(false);
		mntmFlatten.setEnabled(false);
		mntmCut.setEnabled(false);
		mntmDrop.setEnabled(false);
	}

	private void onHierarchyChanged(LoadedHierarchy newHierarchy) {
		viewVis.revalidate();
		viewVis.repaint();

		if (context.isHierarchyDataLoaded()) {
			mntmCloseFile.setEnabled(true);
			mntmSaveFile.setEnabled(true);
			mntmFlatten.setEnabled(true);
			mntmCut.setEnabled(true);
			mntmDrop.setEnabled(true);
		}
	}

	private void onHierarchyTabClosed(int index) {
		if (index == 0) {
			viewVis.revalidate();
			viewVis.repaint();
		}
	}

	private void onWindowClosing() {
		log.trace("Closing application...");

		// Save the current configuration on application exit.
		context.getConfig().to(new File(HVConfig.FILE_PATH));

		context.getStatisticsFrame().dispose();
		context.getInstanceFrame().dispose();
		context.getMeasureManager().dispose();
	}
}
