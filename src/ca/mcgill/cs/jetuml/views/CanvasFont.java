package ca.mcgill.cs.jetuml.views;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.cs.jetuml.application.UserPreferences;
import ca.mcgill.cs.jetuml.application.UserPreferences.IntegerPreference;
import ca.mcgill.cs.jetuml.geom.Dimension;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Singleton class which packages together the functionalities pertaining to fonts.
 *
 */
public final class CanvasFont 
{
	public static final int DEFAULT_FONT_SIZE = 12;
	private static final CanvasFont INSTANCE = new CanvasFont();
	// Maps fontSize, bold tuples to a font
	private final Map<Integer, Map<Boolean, Font>> aFontStore;
	private final Map<Font, FontMetrics> aFontMetricsStore;
	
	private CanvasFont()
	{
		aFontStore = new HashMap<>();
		aFontMetricsStore = new HashMap<>();
	}
	
	/**
	 * @return The singleton instance
	 */
	public static CanvasFont instance()
	{
		return INSTANCE;
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
