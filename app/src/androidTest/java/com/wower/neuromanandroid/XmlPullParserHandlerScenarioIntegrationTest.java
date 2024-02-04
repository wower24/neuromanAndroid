package com.wower.neuromanandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.io.InputStream;


/**
 * Tests the functionality of the XmlPullParserHandlerScenario by parsing predefined XML files.
 */
public class XmlPullParserHandlerScenarioIntegrationTest {
    /**
     * Tests parsing of a non-empty scenario XML file.
     * Validates if the parsed scenario matches the expected structure and content.
     * @throws Exception if there is an issue reading the XML file or parsing the content.
     */
    @Test
    public void testScenarioParsing() throws Exception {
        // Arrange
        // Get the InputStream for a test XML file
        InputStream is = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("test_scenario.xml");

        // Act
        // Parse the InputStream as a Scenario
        XmlPullParserHandlerScenario parser = new XmlPullParserHandlerScenario();
        Scenario scenario = parser.parse(is);

        // Verify the scenario name
        assertEquals("Test Scenario", scenario.getName());

        // Verify the number of boards
        assertEquals(2, scenario.getBoard().size());

        // Verify details of the first board
        Board board1 = scenario.getBoard().get(0);
        assertEquals("Board 1", board1.getName());
        assertEquals(1, board1.getElement().size());

        // Verify details of the first element's state
        Element element = board1.getElement().get(0);
        ElementState state1 = element.getState().get(0);
        assertEquals("state1", state1.getStateID());
        assertEquals(100, state1.getLocX());
        assertEquals(200, state1.getLocY());
        assertEquals(50, state1.getWidth());
        assertEquals(50, state1.getHeight());
        assertEquals("img_image1", state1.getSource());
        assertEquals("255,0,0", state1.getFgcolor());
        assertEquals(1, state1.getClickOnEnd());
        assertEquals(1000, state1.getDuration());

        assertEquals(1, state1.getActions().size());
        ElementAction action = state1.getActions().get(0);
        assertEquals("element2", action.getElementID());
        assertEquals("state2", action.getStateID());

        Evaluate evaluate = board1.getEvaluate();
        assertEquals(1, evaluate.getRequired().size());
        assertEquals("element1", evaluate.getRequired().get(0).getElementID());
        assertEquals(1, evaluate.getRequiredOrdered().size());
        assertEquals("element1", evaluate.getRequiredOrdered().get(0).getElementID());

        Board board2 = scenario.getBoard().get(1);
        assertEquals("Board 2", board2.getName());
        assertEquals(1, board2.getElement().size());

        Element element2 = board2.getElement().get(0);
        ElementState state2 = element2.getState().get(0);
        assertEquals("state2", state2.getStateID());
        assertEquals(150, state2.getLocX());
        assertEquals(250, state2.getLocY());
        assertEquals(60, state2.getWidth());
        assertEquals(60, state2.getHeight());
        assertEquals("img_image2", state2.getSource());
        assertEquals("0,255,0", state2.getFgcolor());
        assertEquals(1, state2.getClickOnEnd());
        assertEquals(1500, state2.getDuration());

        assertEquals(0, state2.getActions().size());
    }
    /**
     * Tests parsing of an empty scenario XML file.
     * Verifies that the parser correctly identifies the scenario as having no boards.
     * @throws Exception if there is an issue reading the XML file or parsing the content.
     */
    @Test
    public void testEmptyScenarioParsing() throws Exception {
        InputStream is = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("empty_scenario.xml");

        XmlPullParserHandlerScenario parser = new XmlPullParserHandlerScenario();
        Scenario scenario = parser.parse(is);

        assertNotNull("Scenario object should not be null", scenario);
        assertTrue("Scenario should have no boards", scenario.getBoard().isEmpty());
    }
}
