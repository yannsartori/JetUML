package ca.mcgill.cs.jetuml.views;

import static ca.mcgill.cs.jetuml.testutils.GeometryUtils.osDependent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static ca.mcgill.cs.jetuml.views.CanvasFont.DEFAULT_FONT_SIZE;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ca.mcgill.cs.jetuml.geom.Dimension;
import javafx.scene.text.Font;

public class TestFontMetrics {

	private static final FontMetrics aMetrics = new FontMetrics(Font.font("System", DEFAULT_FONT_SIZE));
	// Ensures there is no caching of sorts when reusing the same Text object
	@ParameterizedTest
	@MethodSource("stringPairParameters")
	public void testStateNotPreserved(String firstString, String secondString)
	{
		
		assertNotEquals(aMetrics.getDimension(firstString), aMetrics.getDimension(secondString));
	}
	
	private static Stream<Arguments> stringPairParameters() {
	    return Stream.of(
	            Arguments.of("X", "XX"),
	            Arguments.of("XX", "XXX"),
	            Arguments.of("XXX", "XXXX"),
	            Arguments.of("XXXX", "XXXXX"),
	            Arguments.of("XXXXX", "XXXXXX")
	    );
	}
	
	@Test
	public void testGetDimensions()
	{
		assertEquals(new Dimension(0, osDependent(13,12,12)), aMetrics.getDimension(""));
		assertEquals(new Dimension(osDependent(95, 92, 92), osDependent(13, 12, 12)), aMetrics.getDimension("Single-Line-String"));
		assertEquals(new Dimension(osDependent(31, 30, 30), osDependent(45, 40, 45)), aMetrics.getDimension("Multi\nLine\nString"));
	}
}
