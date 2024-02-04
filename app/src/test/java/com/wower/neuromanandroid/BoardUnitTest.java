package com.wower.neuromanandroid;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Tests the Board class's methods to ensure correct functionality.
 */
public class BoardUnitTest {
    /**
     * Board object for testing.
     */
    Board board = new Board();
    /**
     * Tests the isCorrect() method to verify correct behavior under various conditions.
     */
    @Test
    public void testIsCorrect() {
        Evaluate evaluate = new Evaluate();
        List<Condition> required = new ArrayList<>();
        List<Element> clickedElements = new ArrayList<>();
        board.setEvaluate(evaluate);

        assertTrue(board.isCorrect());
        evaluate.setRequired(required);
        board.setClickedElements(clickedElements);
        assertTrue(board.isCorrect());

        required.add(new Condition("someElement"));
        assertFalse(board.isCorrect());
    }
    /**
     * Tests the listsEqual() method for comparing elements and conditions lists for equality.
     */
    @Test
    public void testListsEqual() {
        List<Element> elements = Arrays.asList(new Element("element1"), new Element("element2"));
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition("element1"));
        conditions.add(new Condition("element2"));

        assertTrue(board.listsEqual(elements, conditions));

        conditions.remove(1);
        assertFalse(board.listsEqual(elements, conditions));

        conditions.add(new Condition("differentElement"));
        assertFalse(board.listsEqual(elements, conditions));
    }
    /**
     * Tests the containsAllConditions() method to check if clicked elements meet all required conditions.
     */
    @Test
    public void testContainsAllConditions() {
        List<Element> clickedElements = new ArrayList<>();
        clickedElements.add(new Element("element1"));
        clickedElements.add(new Element("element2"));
        List<Condition> required = new ArrayList<>();
        required.add(new Condition("element1"));
        required.add(new Condition("element2"));

        assertTrue(board.containsAllConditions(clickedElements, required));

        required.add(new Condition("element3"));
        assertFalse(board.containsAllConditions(clickedElements, required));
    }
}
