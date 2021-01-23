package ca.mcgill.cs.jetuml.views;

import ca.mcgill.cs.jetuml.geom.Conversions;
import ca.mcgill.cs.jetuml.geom.Direction;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;


/**
 * A utility class to draw labels for edges.
 *
 */
public final class EdgeLabelViewer extends StringViewer 
{

	private Direction aDirection;
	private int aDisplacement;
	/**
	 * Create a new EdgeLabelViewer.
	 * @param pAlignment The alignment of the label.
	 * @param pDirection The direction to displace from anchor points.
	 * @param pDisplacement The amount to displace from anchor points.
	 */
	public EdgeLabelViewer(Align pAlignment, Direction pDirection, int pDisplacement) 
	{
		super(pAlignment, false, false);
		aDirection = pDirection;
		aDisplacement = pDisplacement;
	}
	
	/**
	 * Creates an EdgeLabelViewer with no displacement.
	 * @param pAlignment The alignment of the label.
	 */
	public EdgeLabelViewer(Align pAlignment) 
	{
		this(pAlignment, Direction.NORTH, 0);
	}
	
	/**
	 * Creates an EdgeLabelViewer with no displacement but a direction.
	 * @param pAlignment The alignment of the label.
	 * @param pDirection The direction to displace from anchor points.
	 */
	public EdgeLabelViewer(Align pAlignment, Direction pDirection)
	{
		this(pAlignment, pDirection, 0);
	}
	
	/**
	 * Draws the label at the point with the displacement.
	 * @param pLabel The label to draw.
	 * @param pGraphics The graphics context onto which to draw.
	 * @param pPoint The point at which the direction and displacement should start.
	 */
	public void draw(String pLabel, GraphicsContext pGraphics, Point2D pPoint)
	{
		Point2D translation = Conversions.toPoint2D(aDirection.travelFromOrigin(aDisplacement));
		pGraphics.translate(translation.getX(), translation.getY());
		super.draw(pLabel, pGraphics, pPoint);
		pGraphics.translate(-translation.getX(), -translation.getY());
	}
	
	/**
	 * Sets the displacement.
	 * @param pDisplacement The new displacement.
	 */
	public void setDisplacement(int pDisplacement)
	{
		aDisplacement = pDisplacement;
	}
	
	/**
	 * Sets the direction.
	 * @param pDirection The new direction
	 */
	public void setDirection(Direction pDirection)
	{
		aDirection = pDirection;
	}
	
	/**
	 * Sets the alignment.
	 * @param pAlignment The new alignment
	 */
	public void setAlignment(Align pAlignment)
	{
		aAlignment = pAlignment;
	}

}
