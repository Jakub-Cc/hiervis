package pl.pwr.hiervis.hk;

import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.LoadedHierarchyUtils;

public class HKPlusPlusScheaduler {

	private Logger logger = null;
	private HKPlusPlusWrapper curentWraper;
	private List<HKPlusPlusParameter> que;
	private static HKPlusPlusScheaduler hkPlusPlusScheaduler;

	private HKPlusPlusScheaduler() {
		que = new ArrayList<>();
		curentWraper = null;
		logger = LogManager.getLogger(HKPlusPlusScheaduler.class);
	}

	public static HKPlusPlusScheaduler getHKPlusPlusScheaduler() {
		if (hkPlusPlusScheaduler == null) {
			hkPlusPlusScheaduler = new HKPlusPlusScheaduler();
		}
		return hkPlusPlusScheaduler;
	}

	public void addToQue(LoadedHierarchy hierarchy, Node node, Window owner, boolean trueClassAttribute,
			boolean instanceNames, boolean diagonalMatrix, boolean disableStaticCenter, boolean generateImages,
			int epsilon, int littleValue, int clusters, int iterations, int repeats, int dendrogramSize,
			int maxNodeCount, boolean verbose) {

		que.add(new HKPlusPlusParameter(hierarchy, node, owner, trueClassAttribute, instanceNames, diagonalMatrix,
				disableStaticCenter, generateImages, epsilon, littleValue, clusters, iterations, repeats,
				dendrogramSize, maxNodeCount, verbose));

		schaduleNext();
	}

	private void schaduleNext() {
		if (curentWraper != null || que.isEmpty())
			return;

		HKPlusPlusParameter pPPar = que.remove(0);
		pPPar.saveSettingsToConfig();
		pPPar.saveParrameterToHierarchy();

		try {
			HVConfig cfg = HVContext.getContext().getConfig();
			HKPlusPlusWrapper wrapper = new HKPlusPlusWrapper(cfg);
			wrapper.subprocessFinished.addListener(this::onSubprocessFinished);
			wrapper.subprocessAborted.addListener(this::onSubprocessAborted);

			logger.trace("Preparing input file...");
			wrapper.prepareInputFile(pPPar.getHierarchy().getMainHierarchy(), pPPar.getNode(), pPPar.isTrueClassAttribute(),
					pPPar.isInstanceNames());

			logger.trace("Starting...");
			wrapper.start(pPPar);
			curentWraper = wrapper;
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	private void onSubprocessAborted(@SuppressWarnings("unused") HKPlusPlusWrapper wrapper) {
		logger.trace("Aborted.");

		logger.debug(curentWraper);
		curentWraper = null;
		schaduleNext();
	}

	private void onSubprocessFinished(Triple<HKPlusPlusWrapper, Integer, HKPlusPlusParameter> args) {
		HKPlusPlusWrapper wrapper = args.getLeft();
		int exitCode = args.getMiddle();
		HKPlusPlusParameter pPPar = args.getRight();

		if (exitCode == 0) {
			logger.trace("Finished successfully.");

			try {
				HVConfig cfg = wrapper.getConfig();

				LoadedHierarchy outputHierarchy = wrapper.getOutputHierarchy(cfg.isHkWithTrueClass(),
						cfg.isHkWithInstanceNames(), false);

				LoadedHierarchy finalHierarchy = LoadedHierarchyUtils.merge(outputHierarchy, pPPar.getHierarchy(),
						pPPar.getNode().getId());
				HVContext.getContext().loadHierarchy(getParameterString(pPPar), finalHierarchy);
			} catch (IOException ex) {
				logger.error("Subprocess finished successfully, but failed during processing: ", ex);
				logger.error(ex.getStackTrace());
			}
		} else {
			logger.error("Failed! Error code: {}", exitCode);
		}

		curentWraper = null;
		schaduleNext();
	}

	private String getParameterString(HKPlusPlusParameter pPPar) {
		int maxNodes = pPPar.getMaxNodeCount();
		String maxNodesStr = maxNodes < 0 ? "MAX_INT" : ("" + maxNodes);

		return String.format("%s / -k %s / -n %s / -r %s / -s %s / -e %s / -l %s / -w %s%s", pPPar.getNode().getId(),
				pPPar.getClusters(), pPPar.getIterations(), pPPar.getIterations(), pPPar.getDendrogramSize(), pPPar.getEpsilon(),
				pPPar.getLittleValue(), maxNodesStr, pPPar.isDiagonalMatrix() ? " / DM" : "");
	}
}
