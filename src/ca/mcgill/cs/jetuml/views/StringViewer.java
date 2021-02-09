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
import ca.mcgill.cs.jetuml.application.UserPreferences;
import ca.mcgill.cs.jetuml.application.UserPreferences.IntegerPreference;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A utility class to view strings with various decorations:
 * - underline
 * - bold
 * - different alignments.
 */
public final class StringViewer
{
	public static final int DEFAULT_FONT_SIZE = 12;

	private static final FontMetrics METRIC = new FontMetrics(getFont());
	
	private static final Dimension EMPTY = new Dimension(0, 0);
	private static final int HORIZONTAL_TEXT_PADDING = 7;
	private static final int VERTICAL_TEXT_PADDING = 7;
	
	/**
	 * How to align the text in this string.
	 */
	public enum Align
	{ LEFT, CENTER, RIGHT }
	
	private Align aAlignment = Align.CENTER;
	private final boolean aBold;
	private final boolean aUnderlined;
	
	/**
	 * Creates a new StringViewer.
	 * 
	 * @param pAlignment The alignment of the string.
	 * @param pBold True if the string is to be rendered bold.
	 * @param pUnderlined True if the string is to be rendered underlined.
	 * @pre pAlign != null.
	 */
	public StringViewer(Align pAlignment, boolean pBold, boolean pUnderlined) 
	{
		assert pAlignment != null;
		aAlignment = pAlignment;
		aBold = pBold;
		aUnderlined = pUnderlined;
	}

	/**
	 * Gets the font according to the user size preferences.
	 * @param pBold If the font is bold.
	 * @return The font
	 */
	public static Font getFont(boolean pBold)
	{
		if( pBold )
		{
			return Font.font("System", FontWeight.BOLD, UserPreferences.instance().getInteger(IntegerPreference.fontSize));
		}
		else
		{
			return Font.font("System", UserPreferences.instance().getInteger(IntegerPreference.fontSize));
		}
	}

	/**
	 * Gets the font according to the user size preferences.
	 * @return The font
	 */
	public static Font getFont()
	{
		return getFont(false);
	}
	
	/**
     * Gets the width and height required to show pString, including
     * padding around the string.
     * @param pString The input string. 
     * @return The dimension pString will use on the screen.
     * @pre pString != null.
	 */
	public Dimension getDimension(String pString)
	{
		assert pString != null;
		if(pString.length() == 0) 
		{
			return EMPTY;
		}
		Rectangle bounds = METRIC.getBoundingBox(getFont(aBold), pString, aAlignment);
		return new Dimension((int) Math.round(bounds.getWidth() + HORIZONTAL_TEXT_PADDING*2), 
				(int) Math.round(bounds.getHeight() + VERTICAL_TEXT_PADDING*2));
	}
	
	private TextAlignment getTextAlign()
	{	
		if(aAlignment == Align.LEFT)
		{
			return TextAlignment.LEFT;
		}
		if(aAlignment == Align.CENTER)
		{
			return TextAlignment.CENTER;
		}
		return TextAlignment.RIGHT;
	}
	
	/**
     * Draws the string inside a given rectangle.
     * @param pString The string to draw.
     * @param pGraphics the graphics context
     * @param pRectangle the rectangle into which to place the string
	 */
	public void draw(String pString, GraphicsContext pGraphics, Rectangle pRectangle)
	{
		final VPos oldVPos = pGraphics.getTextBaseline();
		final TextAlignment oldAlign = pGraphics.getTextAlign();
		
		pGraphics.setTextAlign(getTextAlign());
		
		int textX = 0;
		int textY = 0;
		if(aAlignment == Align.CENTER) 
		{
			textX = pRectangle.getWidth()/2;
			textY = pRectangle.getHeight()/2;
			pGraphics.setTextBaseline(VPos.CENTER);
		}
		else
		{
			pGraphics.setTextBaseline(VPos.TOP);
			textX = HORIZONTAL_TEXT_PADDING;
		}
		
		pGraphics.translate(pRectangle.getX(), pRectangle.getY());
		ViewUtils.drawText(pGraphics, textX, textY, pString.trim(), getFont(aBold));
		
		if(aUnderlined && pString.trim().length() > 0)
		{
			int xOffset = 0;
			int yOffset = 0;
			Rectangle bounds = METRIC.getBoundingBox(getFont(aBold), pString, aAlignment);
			if(aAlignment == Align.CENTER)
			{
				xOffset = (int) (bounds.getWidth()/2);
				yOffset = (int) (METRIC.getLogicalHeight(getFont(aBold), pString)/2) + 1;
			}
			else if(aAlignment == Align.RIGHT)
			{
				xOffset = (int) bounds.getWidth();
			}
			
			ViewUtils.drawLine(pGraphics, textX-xOffset, textY+yOffset, 
					(int) (textX-xOffset+bounds.getWidth()), textY+yOffset, LineStyle.SOLID);
		}
		pGraphics.translate(-pRectangle.getX(), -pRectangle.getY());
		pGraphics.setTextBaseline(oldVPos);
		pGraphics.setTextAlign(oldAlign);
	}
}
