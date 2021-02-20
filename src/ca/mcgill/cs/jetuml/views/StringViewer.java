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

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.cs.jetuml.application.UserPreferences;
import ca.mcgill.cs.jetuml.application.UserPreferences.IntegerPreference;
import ca.mcgill.cs.jetuml.geom.Dimension;
import ca.mcgill.cs.jetuml.geom.Rectangle;
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
	private static final Dimension EMPTY = new Dimension(0, 0);
	private static final int HORIZONTAL_TEXT_PADDING = 7;
	private static final int VERTICAL_TEXT_PADDING = 7;
	private static final CanvasFont CANVAS_FONT_INSTANCE = new CanvasFont();
	
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
		Dimension dimension = CANVAS_FONT_INSTANCE.getDimension(pString);
		return new Dimension(Math.round(dimension.width() + HORIZONTAL_TEXT_PADDING*2), 
				Math.round(dimension.height() + VERTICAL_TEXT_PADDING*2));
	}
	
	private TextAlignment getTextAlignment()
	{		
		if(aAlignment == Align.LEFT)
		{
			return TextAlignment.LEFT;
		}
		else if(aAlignment == Align.CENTER)
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
		
		pGraphics.setTextAlign(getTextAlignment());
		
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
		CANVAS_FONT_INSTANCE.drawString(pGraphics, textX, textY, pString, aBold);
		
		if(aUnderlined && pString.trim().length() > 0)
		{
			int xOffset = 0;
			int yOffset = 0;
			Dimension dimension = CANVAS_FONT_INSTANCE.getDimension(pString);
			if(aAlignment == Align.CENTER)
			{
				xOffset = dimension.width()/2;
				yOffset = (int) (CANVAS_FONT_INSTANCE.fontSize()/2) + 1;
			}
			else if(aAlignment == Align.RIGHT)
			{
				xOffset = dimension.width();
			}
			
			ViewUtils.drawLine(pGraphics, textX-xOffset, textY+yOffset, 
					textX-xOffset+dimension.width(), textY+yOffset, LineStyle.SOLID);
		}
		pGraphics.translate(-pRectangle.getX(), -pRectangle.getY());
		pGraphics.setTextBaseline(oldVPos);
		pGraphics.setTextAlign(oldAlign);
	}
	
	private static final class CanvasFont
	{
		private final Map<Integer, Map<Boolean, Font>> aFontStore;
		private final Map<Font, FontMetrics> aFontMetricsStore;
		
		private CanvasFont() 
		{
			aFontStore = new HashMap<>();
			aFontMetricsStore = new HashMap<>();
		}
		
		private Font getFont(boolean pBold)
		{
			int fontSize = UserPreferences.instance().getInteger(IntegerPreference.fontSize);
			FontWeight fontWeight;
			if ( pBold )
			{
				fontWeight = FontWeight.BOLD;
			}
			else
			{
				fontWeight = FontWeight.NORMAL;
			}
			aFontStore.putIfAbsent(fontSize, new HashMap<>());
			return aFontStore.get(fontSize).computeIfAbsent(pBold, k -> Font.font("System", fontWeight, fontSize));
		}
		
		private FontMetrics getFontMetrics(boolean pBold)
		{
			Font font = getFont(pBold);
			return aFontMetricsStore.computeIfAbsent(font, k -> new FontMetrics(font));
		}
		
		/**
		 * Returns the dimension of a given string.
		 * @param pString The string to which the bounds pertain.
		 * @return The dimension of the string
		 */
		public Dimension getDimension(String pString)
		{
			return getFontMetrics(false).getDimension(pString);
		}
		
		/**
		 * Draws the string on the graphics context at the specified position.
		 * @param pGraphics The graphics context
		 * @param pTextX The x-position of the string
		 * @param pTextY The y-position of the string
		 * @param pString The canvas on which to draw the string
		 * @param pBold If the text should be bold
		 */
		public void drawString(GraphicsContext pGraphics, int pTextX, int pTextY, 
				String pString, boolean pBold)
		{
			ViewUtils.drawText(pGraphics, pTextX, pTextY, pString, getFont(pBold));
		}
		
		/**
		 * Returns the font size the user currently specifies.
		 * @return The font size
		 */
		public int fontSize()
		{
			 /* Note: This is technically an unnecessary method since
			  * the calling methods can just directly access the UserPreferences
			  * instance. However, I thought it makes sense to have all possible
			  * operations relating to fonts here
			  * Should I remove this method?
			  */
			return UserPreferences.instance().getInteger(IntegerPreference.fontSize);
		}
	}
}
