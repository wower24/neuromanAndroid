package com.wower.neuromanandroid;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Unit tests for the XmlPullParserHandlerScenario class, focusing on the method that
 * transforms source strings for images.
 */
public class XmlPullParserHandlerScenarioUnitTest {
    /**
     * Parser object for testing.
     */
    XmlPullParserHandlerScenario parser = new XmlPullParserHandlerScenario();
    /**
     * Tests transformation of source strings with an "img:file=" prefix.
     */
    @Test
    public void testTransformSourceString_WithImgPrefix() {
        String source = "img:file=image-name.png";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }
    /**
     * Tests transformation of source strings without the "img:file=" prefix.
     */
    @Test
    public void testTransformSourceString_WithoutImgPrefix() {
        String source = "image-name.png";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }
    /**
     * Tests transformation of source strings containing special characters.
     */
    @Test
    public void testTransformSourceString_SpecialCharacters() {
        String source = "img:file=image@name#1.png";
        String expected = "img_imagexnamex1";
        assertEquals(expected, parser.transformSourceString(source));
    }
    /**
     * Tests transformation of source strings with uppercase characters.
     */
    @Test
    public void testTransformSourceString_UppercaseCharacters() {
        String source = "IMG:file=IMAGE-NAME.PNG";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }
}

