package pl.pwr.hiervis.prefuse.histogram;

/*
 * Adapted for HocusLocus by Ajish George <ajishg@gmail.com>
 * from code by
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author <a href="http://webfoot.com/ducky.home.html">Kaitlin Duck Sherwood</a>
 *
 * See HistogramFrame.java for details 
 */

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import prefuse.Constants;
import prefuse.render.AbstractShapeRenderer;
import prefuse.visual.VisualItem;

/*
 * This class renders bars like those you'd use in a bar chart.
 * Some code was borrowed from StackedAreaChart; some from ShapeRenderer.
 * 
 * @author <a href="http://webfoot.com/ducky.home.html">Kaitlin Duck Sherwood</a>
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class BarRenderer extends AbstractShapeRenderer {
	private Rectangle2D mBounds;
	private boolean mIsVertical;
	private int mOrientation = Constants.ORIENT_BOTTOM_TOP;

	protected double mBarWidth = 10;
	protected Rectangle2D mRect = new Rectangle2D.Double();

	public BarRenderer(double aWidth) {
		mBarWidth = aWidth;
		setOrientation(mOrientation);
	}

	public void setBarWidth(double newWidth) {
		mBarWidth = newWidth;
	}

	public void setBounds(Rectangle2D bounds) {
		mBounds = bounds;
	}

	/**
	 * Sets the orientation of this layout. Must be one of
	 * {@link Constants#ORIENT_BOTTOM_TOP} (to grow bottom-up),
	 * {@link Constants#ORIENT_TOP_BOTTOM} (to grow top-down),
	 * {@link Constants#ORIENT_LEFT_RIGHT} (to grow left-right), or
	 * {@link Constants#ORIENT_RIGHT_LEFT} (to grow right-left).
	 * 
	 * @param orient the desired orientation of this layout
	 * @throws IllegalArgumentException if the orientation value is not a valid
	 *                                  value
	 */
	public void setOrientation(int orient) {
		if (orient != Constants.ORIENT_TOP_BOTTOM && orient != Constants.ORIENT_BOTTOM_TOP
				&& orient != Constants.ORIENT_LEFT_RIGHT && orient != Constants.ORIENT_RIGHT_LEFT) {
			throw new IllegalArgumentException("Invalid orientation value: " + orient);
		}
		mOrientation = orient;
		mIsVertical = (mOrientation == Constants.ORIENT_TOP_BOTTOM || mOrientation == Constants.ORIENT_BOTTOM_TOP);
	}

	protected Shape getRawShape(VisualItem item) {
		double width;
		double height;

		double x = item.getX();
		if (Double.isNaN(x) || Double.isInfinite(x))
			x = 0;
		double y = item.getY();
		if (Double.isNaN(y) || Double.isInfinite(y))
			y = 0;

		if (mIsVertical) {
			// @@@ what is the getSize for?
			width = mBarWidth * item.getSize();
			if (mOrientation == Constants.ORIENT_BOTTOM_TOP) {
				height = mBounds.getMaxY() - y;
			} else {
				height = y;
				y = mBounds.getMinY();
			}

			// Center the bar around the x-location
			if (width > 1) {
				x = x - width / 2;
			}
		} else {
			height = mBarWidth * item.getSize();
			if (mOrientation == Constants.ORIENT_LEFT_RIGHT) {
				width = x;
				x = mBounds.getMinX();
			} else {
				width = mBounds.getMaxX() - x;
			}

			// Center the bar around the y-location
			if (height > 1) {
				y = y - height / 2;
			}
		}

		mRect.setFrame(x, y, width, height);
		return mRect;
	}
}
