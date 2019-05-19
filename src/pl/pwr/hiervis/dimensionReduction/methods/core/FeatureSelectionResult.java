package pl.pwr.hiervis.dimensionReduction.methods.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

public class FeatureSelectionResult implements Comparable<FeatureSelectionResult> {

    private int orginalIndex;
    private String dimensionName;
    private double score;
    private int rank;

    public FeatureSelectionResult() {
	this(-1, null, -1, -1);
    }

    public FeatureSelectionResult(int orginalIndex, String dimensionName, double score, int rank) {
	setOrginalIndex(orginalIndex);
	setDimensionName(dimensionName);
	setScore(score);
	setRank(rank);
    };

    /**
     * @return the orginalIndex
     */
    public int getOrginalIndex() {
	return orginalIndex;
    }

    /**
     * @param orginalIndex the orginalIndex to set
     */
    private void setOrginalIndex(int orginalIndex) {
	this.orginalIndex = orginalIndex;
    }

    /**
     * @return the score
     */
    public double getScore() {
	return score;
    }

    /**
     * @param score the score to set
     */
    private void setScore(double score) {
	this.score = score;
    }

    /**
     * @return the rank
     */
    public int getRank() {
	return rank;
    }

    /**
     * @param rank the rank to set
     */
    private void setRank(int rank) {
	this.rank = rank;
    }

    @Override
    public int compareTo(FeatureSelectionResult featureSelectionResult) {
	return this.getRank() - featureSelectionResult.getRank();
    }

    /**
     * @return the dimensionName
     */
    public String getDimensionName() {
	return dimensionName;
    }

    /**
     * @param dimensionName the dimensionName to set
     */
    private void setDimensionName(String dimensionName) {
	this.dimensionName = dimensionName;
    }

    public String toString() {
	return rank + " ;" + score + " ;" + dimensionName + " ;" + orginalIndex + " ;";
    }

    public static void saveToFile(File file, List<FeatureSelectionResult> resultList) throws IOException {
	OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8").newEncoder());

	StringBuilder buf = new StringBuilder(16 * resultList.size() * 4);

	buf.append("rank;score;dimension Name;orginal index;\n");
	for (FeatureSelectionResult result : resultList) {
	    buf.append(result);
	    buf.append("\n");
	}

	writer.write(buf.toString());
	writer.flush();
	writer.close();

    }
}
