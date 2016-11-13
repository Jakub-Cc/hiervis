package pl.pwr.hiervis.util.prefuse.histogram;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import pl.pwr.hiervis.core.ElementRole;
import pl.pwr.hiervis.core.HVConfig;
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
public class BarRenderer extends AbstractShapeRenderer
{
	private Rectangle2D m_bounds;
	private boolean m_isVertical;
	private int m_orientation = Constants.ORIENT_BOTTOM_TOP;
	protected double m_barWidth = 10;
	protected Rectangle2D m_rect = new Rectangle2D.Double();
	protected HVConfig params;


	public BarRenderer( double aWidth, HVConfig params )
	{
		// super(barWidth);
		this.params = params;
		m_barWidth = aWidth;
		setOrientation( m_orientation );
	}

	public void setBounds( Rectangle2D bounds )
	{
		m_bounds = bounds;
	}

	/**
	 * Sets the orientation of this layout. Must be one of
	 * {@link Constants#ORIENT_BOTTOM_TOP} (to grow bottom-up),
	 * {@link Constants#ORIENT_TOP_BOTTOM} (to grow top-down),
	 * {@link Constants#ORIENT_LEFT_RIGHT} (to grow left-right), or
	 * {@link Constants#ORIENT_RIGHT_LEFT} (to grow right-left).
	 * 
	 * @param orient
	 *            the desired orientation of this layout
	 * @throws IllegalArgumentException
	 *             if the orientation value
	 *             is not a valid value
	 */

	public void setOrientation( int orient )
	{

		if ( orient != Constants.ORIENT_TOP_BOTTOM &&
			orient != Constants.ORIENT_BOTTOM_TOP &&
			orient != Constants.ORIENT_LEFT_RIGHT &&
			orient != Constants.ORIENT_RIGHT_LEFT ) {
			throw new IllegalArgumentException(
				"Invalid orientation value: " + orient
			);
		}
		m_orientation = orient;
		m_isVertical = ( m_orientation == Constants.ORIENT_TOP_BOTTOM ||
			m_orientation == Constants.ORIENT_BOTTOM_TOP );
	}


	protected Shape getRawShape( VisualItem item )
	{
		double width, height;

		double x = item.getX();
		if ( Double.isNaN( x ) || Double.isInfinite( x ) )
			x = 0;
		double y = item.getY();
		if ( Double.isNaN( y ) || Double.isInfinite( y ) )
			y = 0;


		if ( m_isVertical ) {
			// @@@ what is the getSize for?
			width = m_barWidth * item.getSize();
			if ( m_orientation == Constants.ORIENT_BOTTOM_TOP ) {
				height = m_bounds.getMaxY() - y;
			}
			else {
				height = y;
				y = m_bounds.getMinY();
			}
			// Center the bar around the x-location
			// NO! x value should shows the LEFT side of bar
			// if ( width > 1 ) {
			// x = x-width/2;
			//
			// }
		}
		else {
			height = m_barWidth * item.getSize();
			if ( m_orientation == Constants.ORIENT_LEFT_RIGHT ) {
				width = x;
				x = m_bounds.getMinX();
			}
			else {
				width = m_bounds.getMaxX() - x;
			}

			// Center the bar around the y-location
			if ( height > 1 ) {
				y = y - height / 2;
			}

		}

		int roleNum = item.getInt( "role" );

		if ( height > 0.0001 && height < 1.0 ) {
			System.err.println(
				"Ones' (non-empty) bar height is less than 1 px, so it won't be visible on " +
					"the output histogram image. Consider increasing the image size of histograms. That bar's role is: " + roleNum + "."
			);
		}

		if ( roleNum == ElementRole.CURRENT.getNumber() ) {
			item.setFillColor( params.getCurrentGroupColor().getRGB() );
		}
		else if ( roleNum == ElementRole.DIRECT_PARENT.getNumber() ) {
			item.setFillColor( params.getParentGroupColor().getRGB() );
		}
		m_rect.setRect( x, y, width, height );
		return m_rect;
	}

	public void setBarWidth( double m_barWidth )
	{
		this.m_barWidth = m_barWidth;
	}

	public double getBarWidth( double m_barWidth )
	{
		return this.m_barWidth;
	}
}
