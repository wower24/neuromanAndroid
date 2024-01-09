package com.wower.neuromanandroid;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardUnitTest {
    Board board = new Board();
    @Test
    public void testIsCorrect() {
        Board board = new Board();
        Evaluate evaluate = new Evaluate();
        List<Condition> required = new ArrayList<>();
        List<Element> clickedElements = new ArrayList<>();

        // Case 1: Evaluate required is null
        board.setEvaluate(evaluate);
        assertTrue(board.isCorrect());

        // Case 2: Evaluate required is empty and clickedElements is empty
        evaluate.setRequired(required);
        board.setClickedElements(clickedElements);
        assertTrue(board.isCorrect());

        // Case 3: Evaluate required is not empty, clickedElements is empty
        required.add(new Condition("someElement"));
        assertFalse(board.isCorrect());

        // Add more cases based on your method's logic
    }

    @Test
    public void testListsEqual() {
        List<Element> elements = Arrays.asList(new Element("element1"), new Element("element2"));
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition("element1"));
        conditions.add(new Condition("element2"));
        // Test for equal lists
        assertTrue(board.listsEqual(elements, conditions));

        // Test for lists of different sizes
        conditions.remove(1);
        assertFalse(board.listsEqual(elements, conditions));

        // Test for lists with different elementIDs
        conditions.add(new Condition("differentElement"));
        assertFalse(board.listsEqual(elements, conditions));
    }

    @Test
    public void testContainsAllConditions() {
        List<Element> clickedElements = new ArrayList<>();
        clickedElements.add(new Element("element1"));
        clickedElements.add(new Element("element2"));
        List<Condition> required = new ArrayList<>();
        required.add(new Condition("element1"));
        required.add(new Condition("element2"));
        // Test for matching conditions
        assertTrue(board.containsAllConditions(clickedElements, required));

        // Test for non-matching conditions
        required.add(new Condition("element3"));
        assertFalse(board.containsAllConditions(clickedElements, required));
    }
}
