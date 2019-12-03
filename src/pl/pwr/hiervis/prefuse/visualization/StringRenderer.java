package pl.pwr.hiervis.prefuse.visualization;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

import prefuse.Constants;
import prefuse.render.AbstractShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.GraphicsLib;
import prefuse.util.StringLib;
import prefuse.visual.VisualItem;

public class StringRenderer extends AbstractShapeRenderer {
	protected String mDelim = "\n";

	protected String mLabelName = "label";

	protected int mXAlign = Constants.CENTER;
	protected int mYAlign = Constants.CENTER;
	protected int mHTextAlign = Constants.CENTER;
	protected int mVTextAlign = Constants.CENTER;

	protected int mHorizBorder = 2;
	protected int mVertBorder = 0;
	protected int mArcWidth = 0;
	protected int mArcHeight = 0;

	protected int mMaxTextWidth = -1;

	/** The holder for the currently computed bounding box */
	protected RectangularShape mBbox = new Rectangle2D.Double();
	protected Point2D mPt = new Point2D.Double(); // temp point
	protected Font mFont; // temp font holder
	protected String mText; // label text
	protected Dimension mTextDim = new Dimension(); // text width / height

	// ------------------------------------------------------------------------

	/**
	 * Rounds the corners of the bounding rectangle in which the text string is
	 * rendered. This will only be seen if either the stroke or fill color is
	 * non-transparent.
	 * 
	 * @param arcWidth  the width of the curved corner
	 * @param arcHeight the height of the curved corner
	 */
	public void setRoundedCorner(int arcWidth, int arcHeight) {
		if ((arcWidth == 0 || arcHeight == 0) && !(mBbox instanceof Rectangle2D)) {
			mBbox = new Rectangle2D.Double();
		} else {
			if (!(mBbox instanceof RoundRectangle2D))
				mBbox = new RoundRectangle2D.Double();
			((RoundRectangle2D) mBbox).setRoundRect(0, 0, 10, 10, arcWidth, arcHeight);
			mArcWidth = arcWidth;
			mArcHeight = arcHeight;
		}
	}

	/**
	 * Get the field name to use for text labels.
	 * 
	 * @return the data field for text labels, or null for no text
	 */
	public String getTextField() {
		return mLabelName;
	}

	/**
	 * Set the field name to use for text labels.
	 * 
	 * @param textField the data field for text labels, or null for no text
	 */
	public void setTextField(String textField) {
		mLabelName = textField;
	}

	/**
	 * Sets the maximum width that should be allowed of the text label. A value of
	 * -1 specifies no limit (this is the default).
	 * 
	 * @param maxWidth the maximum width of the text or -1 for no limit
	 */
	public void setMaxTextWidth(int maxWidth) {
		mMaxTextWidth = maxWidth;
	}

	/**
	 * Returns the text to draw. Subclasses can override this class to perform
	 * custom text selection.
	 * 
	 * @param item the item to represent as a <code>String</code>
	 * @return a <code>String</code> to draw
	 */
	protected String getText(VisualItem item) {
		if (item.canGetString(mLabelName)) {
			return item.getString(mLabelName);
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Rendering

	private String computeTextDimensions(VisualItem item, String text, double size) {
		// put item font in temp member variable
		mFont = item.getFont();
		// scale the font as needed
		if (size != 1) {
			mFont = FontLib.getFont(mFont.getName(), mFont.getStyle(), size * mFont.getSize());
		}

		FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(mFont);
		StringBuilder str = null;

		// compute the number of lines and the maximum width
		int nlines = 1;
		int w = 0;
		int start = 0;
		int end = text.indexOf(mDelim);
		mTextDim.width = 0;
		String line;
		for (; end >= 0; ++nlines) {
			line = text.substring(start, end);
			w = fm.stringWidth(line);
			// abbreviate line as needed
			if (mMaxTextWidth > -1 && w > mMaxTextWidth) {
				if (str == null)
					str = new StringBuilder(text.substring(0, start));
				str.append(StringLib.abbreviate(line, fm, mMaxTextWidth));
				str.append(mDelim);
				w = mMaxTextWidth;
			} else if (str != null) {
				str.append(line).append(mDelim);
			}
			// update maximum width and substring indices
			mTextDim.width = Math.max(mTextDim.width, w);
			start = end + 1;
			end = text.indexOf(mDelim, start);
		}
		line = text.substring(start);
		w = fm.stringWidth(line);
		// abbreviate line as needed
		if (mMaxTextWidth > -1 && w > mMaxTextWidth) {
			if (str == null)
				str = new StringBuilder(text.substring(0, start));
			str.append(StringLib.abbreviate(line, fm, mMaxTextWidth));
			w = mMaxTextWidth;
		} else if (str != null) {
			str.append(line);
		}
		// update maximum width
		mTextDim.width = Math.max(mTextDim.width, w);

		// compute the text height
		mTextDim.height = fm.getHeight() * nlines;

		return str == null ? text : str.toString();
	}

	/**
	 * @see prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem)
	 */
	protected Shape getRawShape(VisualItem item) {
		mText = getText(item);
		double size = item.getSize();

		// get text dimensions
		int tw = 0;
		int th = 0;
		if (mText != null) {
			mText = computeTextDimensions(item, mText, size);
			th = mTextDim.height;
			tw = mTextDim.width;
		}

		// get bounding box dimensions
		double w = tw;
		double h = th;

		// get the top-left point, using the current alignment settings
		getAlignedPoint(mPt, item, w, h, mXAlign, mYAlign);

		if (mBbox instanceof RoundRectangle2D) {
			RoundRectangle2D rr = (RoundRectangle2D) mBbox;
			rr.setRoundRect(mPt.getX(), mPt.getY(), w, h, size * mArcWidth, size * mArcHeight);
		} else {
			mBbox.setFrame(mPt.getX(), mPt.getY(), w, h);
		}
		return mBbox;
	}

	/**
	 * Helper method, which calculates the top-left co-ordinate of an item given the
	 * item's alignment.
	 */
	protected static void getAlignedPoint(Point2D p, VisualItem item, double w, double h, int xAlign, int yAlign) {
		double x = item.getX();
		double y = item.getY();
		if (Double.isNaN(x) || Double.isInfinite(x))
			x = 0; // safety check
		if (Double.isNaN(y) || Double.isInfinite(y))
			y = 0; // safety check

		if (xAlign == Constants.CENTER) {
			x = x - (w / 2);
		} else if (xAlign == Constants.RIGHT) {
			x = x - w;
		}
		if (yAlign == Constants.CENTER) {
			y = y - (h / 2);
		} else if (yAlign == Constants.BOTTOM) {
			y = y - h;
		}
		p.setLocation(x, y);
	}

	/**
	 * @see prefuse.render.Renderer#render(java.awt.Graphics2D,
	 *      prefuse.visual.VisualItem)
	 */
	@Override
	public void render(Graphics2D g, VisualItem item) {
		RectangularShape shape = (RectangularShape) getShape(item);
		if (shape == null)
			return;

		// fill the shape, if requested
		int type = getRenderType(item);
		if (type == RENDER_TYPE_FILL || type == RENDER_TYPE_DRAW_AND_FILL)
			GraphicsLib.paint(g, item, shape, getStroke(item), RENDER_TYPE_FILL);

		// now render the text
		String text = mText;

		if (text == null)
			return;

		double size = item.getSize();
		boolean useInt = 1.5 > Math.max(g.getTransform().getScaleX(), g.getTransform().getScaleY());
		double x = shape.getMinX() + size * mHorizBorder;
		double y = shape.getMinY() + size * mVertBorder;

		// render text
		int textColor = item.getTextColor();
		if (ColorLib.alpha(textColor) > 0) {
			g.setPaint(ColorLib.getColor(textColor));
			g.setFont(mFont);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(mFont);

			// compute available width and height
			double tw = mTextDim.width;
			double th = mTextDim.height;

			// compute starting y-coordinate
			y += fm.getAscent();
			switch (mVTextAlign) {
			case Constants.BOTTOM:
				y += th - mTextDim.height;
				break;
			case Constants.CENTER:
				y += (th - mTextDim.height) / 2;
				break;
			default:
				break;
			}

			// render each line of text
			int lh = fm.getHeight(); // the line height
			int start = 0;
			int end = text.indexOf(mDelim);
			for (; end >= 0; y += lh) {
				drawString(g, fm, text.substring(start, end), useInt, x, y, tw);
				start = end + 1;
				end = text.indexOf(mDelim, start);
			}
			drawString(g, fm, text.substring(start), useInt, x, y, tw);
		}

		// draw border
		if (type == RENDER_TYPE_DRAW || type == RENDER_TYPE_DRAW_AND_FILL) {
			GraphicsLib.paint(g, item, shape, getStroke(item), RENDER_TYPE_DRAW);
		}
	}

	private final void drawString(Graphics2D g, FontMetrics fm, String text, boolean useInt, double x, double y,
			double w) {
		// compute the x-coordinate
		double tx;
		switch (mHTextAlign) {
		case Constants.LEFT:
			tx = x;
			break;
		case Constants.RIGHT:
			tx = x + w - fm.stringWidth(text);
			break;
		case Constants.CENTER:
			tx = x + (w - fm.stringWidth(text)) / 2;
			break;
		default:
			throw new IllegalStateException("Unrecognized text alignment setting.");
		}
		// use integer precision unless zoomed-in
		// results in more stable drawing
		if (useInt) {
			g.drawString(text, (int) tx, (int) y);
		} else {
			g.drawString(text, (float) tx, (float) y);
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the horizontal text alignment within the layout. One of
	 * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
	 * {@link prefuse.Constants#CENTER}. The default is centered text.
	 * 
	 * @return the horizontal text alignment
	 */
	public int getHorizontalTextAlignment() {
		return mHTextAlign;
	}

	/**
	 * Set the horizontal text alignment within the layout. One of
	 * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
	 * {@link prefuse.Constants#CENTER}. The default is centered text.
	 * 
	 * @param halign the desired horizontal text alignment
	 */
	public void setHorizontalTextAlignment(int halign) {
		if (halign != Constants.LEFT && halign != Constants.RIGHT && halign != Constants.CENTER)
			throw new IllegalArgumentException("Illegal horizontal text alignment value.");
		mHTextAlign = halign;
	}

	/**
	 * Get the vertical text alignment within the layout. One of
	 * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
	 * {@link prefuse.Constants#CENTER}. The default is centered text.
	 * 
	 * @return the vertical text alignment
	 */
	public int getVerticalTextAlignment() {
		return mVTextAlign;
	}

	/**
	 * Set the vertical text alignment within the layout. One of
	 * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
	 * {@link prefuse.Constants#CENTER}. The default is centered text.
	 * 
	 * @param valign the desired vertical text alignment
	 */
	public void setVerticalTextAlignment(int valign) {
		if (valign != Constants.TOP && valign != Constants.BOTTOM && valign != Constants.CENTER)
			throw new IllegalArgumentException("Illegal vertical text alignment value.");
		mVTextAlign = valign;
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the horizontal alignment of this node with respect to its x, y
	 * coordinates.
	 * 
	 * @return the horizontal alignment, one of {@link prefuse.Constants#LEFT},
	 *         {@link prefuse.Constants#RIGHT}, or {@link prefuse.Constants#CENTER}.
	 */
	public int getHorizontalAlignment() {
		return mXAlign;
	}

	/**
	 * Get the vertical alignment of this node with respect to its x, y coordinates.
	 * 
	 * @return the vertical alignment, one of {@link prefuse.Constants#TOP},
	 *         {@link prefuse.Constants#BOTTOM}, or
	 *         {@link prefuse.Constants#CENTER}.
	 */
	public int getVerticalAlignment() {
		return mYAlign;
	}

	/**
	 * Set the horizontal alignment of this node with respect to its x, y
	 * coordinates.
	 * 
	 * @param align the horizontal alignment, one of {@link prefuse.Constants#LEFT},
	 *              {@link prefuse.Constants#RIGHT}, or
	 *              {@link prefuse.Constants#CENTER}.
	 */
	public void setHorizontalAlignment(int align) {
		mXAlign = align;
	}

	/**
	 * Set the vertical alignment of this node with respect to its x, y coordinates.
	 * 
	 * @param align the vertical alignment, one of {@link prefuse.Constants#TOP},
	 *              {@link prefuse.Constants#BOTTOM}, or
	 *              {@link prefuse.Constants#CENTER}.
	 */
	public void setVerticalAlignment(int align) {
		mYAlign = align;
	}

	/**
	 * Returns the amount of padding in pixels between the content and the border of
	 * this item along the horizontal dimension.
	 * 
	 * @return the horizontal padding
	 */
	public int getHorizontalPadding() {
		return mHorizBorder;
	}

	/**
	 * Sets the amount of padding in pixels between the content and the border of
	 * this item along the horizontal dimension.
	 * 
	 * @param xpad the horizontal padding to set
	 */
	public void setHorizontalPadding(int xpad) {
		mHorizBorder = xpad;
	}

	/**
	 * Returns the amount of padding in pixels between the content and the border of
	 * this item along the vertical dimension.
	 * 
	 * @return the vertical padding
	 */
	public int getVerticalPadding() {
		return mVertBorder;
	}

	/**
	 * Sets the amount of padding in pixels between the content and the border of
	 * this item along the vertical dimension.
	 * 
	 * @param ypad the vertical padding
	 */
	public void setVerticalPadding(int ypad) {
		mVertBorder = ypad;
	}
}
