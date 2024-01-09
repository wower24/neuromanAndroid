package com.wower.neuromanandroid;
import org.junit.Test;
import static org.junit.Assert.*;
public class XmlPullParserHandlerScenarioUnitTest {
    XmlPullParserHandlerScenario parser = new XmlPullParserHandlerScenario();
    @Test
    public void testTransformSourceString_WithImgPrefix() {
        String source = "img:file=image-name.png";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }

    @Test
    public void testTransformSourceString_WithoutImgPrefix() {
        String source = "image-name.png";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }

    @Test
    public void testTransformSourceString_SpecialCharacters() {
        String source = "img:file=image@name#1.png";
        String expected = "img_imagexnamex1";
        assertEquals(expected, parser.transformSourceString(source));
    }

    /*@Test
    public void testTransformSourceString_UppercaseCharacters() {
        //TODO: Review and fix this bug, write about it in thesis
        String source = "IMG:file=IMAGE-NAME.PNG";
        String expected = "img_image_name";
        assertEquals(expected, parser.transformSourceString(source));
    }*/
}

