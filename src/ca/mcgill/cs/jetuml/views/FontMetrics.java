/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/
package ca.mcgill.cs.jetuml.views;

import ca.mcgill.cs.jetuml.geom.Dimension;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.views.StringViewer.Align;
import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A utility class to determine various font position metrics
 * for the particular font.
 */
public class FontMetrics 
{
	private static final String BLANK = "";
	private final Text aTextNode;
	
	private int aLeading;
	
	
	/**
	 * Creates a new FontMetrics object.
	 * @param pFont The font to use.
	 */

	public FontMetrics(Font pFont)
	{
		aTextNode = new Text();
		updateFont(pFont);
	}
	
	/**
	 * Re-calculates the text-independent values upon font changes,
	 * if any. This should be called before any properties are checked.
	 * @param pFont The font to use.
	 */
	private void updateFont(Font pFont)
	{
		if ( aTextNode.getFont().equals(pFont) )
		{
			// Previously computed values are still the same
			return;
		}
		aTextNode.setFont(pFont);
		aTextNode.setText(BLANK);
		Bounds b = aTextNode.getLayoutBounds();
		aLeading = (int) b.getMaxY();
	}
	
	/* A visual diagram for why the bounds values are what they are
	 * (with word "Thy"):   ____________________
	 * getMinY() (ascent)  |*****  *           |
	 *                     |  *    *           |
	 *                     |  *    *****   *  *|
	 *                     |  *    *   *   *  *|
	 *                     |  *    *   *   ****|
	 * (baseline)          |------------------*| x=getWidth()
	 *                     |                  *|
	 *                     |                  *| 
	 * y = 0  (descent)    |                ***|
	 *                     |                   |
	 *                     |                   |
	 * getMaxY() (leading) |-------------------|
	 *
	 */
	
	/**
	 * Returns the bounding box of this text object.
	 * @param pFont The font to use.
	 * @param pString The string to which the bounds pertain.
	 * @param pAlignment The horizontal alignment of this string
	 * @return A rectangle bounding the string
	 */
	public Rectangle getBoundingBox(Font pFont, String pString, Align pAlignment)
	{
		updateFont(pFont);
		aTextNode.setText(pString);
		Bounds b = aTextNode.getLayoutBounds();
		int visualHeight = (int) (b.getHeight() - aLeading);
		int width = (int) (b.getWidth());
		return new Rectangle(getXPos(width, pAlignment), (int) (-visualHeight), width, visualHeight);
	}
	
	/**
	 * Returns the dimension of this text object.
	 * @param pFont The font to use.
	 * @param pString The string to which the bounds pertain.
	 * @return A rectangle bounding the string
	 */
	public Dimension getDimension(Font pFont, String pString)
	{
		Rectangle r = getBoundingBox(pFont, pString, Align.LEFT);
		return new Dimension(r.getWidth(), r.getHeight());
	}
	
	/**
	 * @param pFont The font to use.
	 * @return The leading length
	 */
	public int getLeading(Font pFont)
	{
		updateFont(pFont);
		return aLeading;
	}

	/**
	 * @param pFont The font to use.
	 * @param pString The string to use.
	 * @return The height defined by the ascent and descent
	 */
	public int getVisualHeight(Font pFont, String pString)
	{
		Rectangle r = getBoundingBox(pFont, pString, Align.LEFT);
		return r.getHeight();
	}
	
	/**
	 * @param pFont The font to use.
	 * @param pString The string to use.
	 * @return The height defined by the ascent, descent, and leading
	 */
	public int getLogicalHeight(Font pFont, String pString)
	{
		Rectangle r = getBoundingBox(pFont, pString, Align.LEFT);
		return r.getHeight() + aLeading;
	}
	
	/**
	 * @param pFont The font to use.
	 * @param pString The width of the string to determine
	 * @return The width
	 */
	public int getWidth(Font pFont, String pString)
	{
		Rectangle r = getBoundingBox(pFont, pString, Align.LEFT);
		return r.getWidth();
	}
	
	private int getXPos(int pWidth, Align pAlignment) 
	{
		if ( pAlignment == Align.LEFT )
		{
			return 0;
		}
		if ( pAlignment == Align.CENTER )
		{
			return -pWidth / 2;
			
		}
		// pAlignment == Align.RIGHT
		return -pWidth;
	}
}