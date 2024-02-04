package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a board in the application.
 * This class manages the board details including its elements, clicked elements,
 * elements to add, and its evaluation state.
 */
public class Board {
    /**
     * Name of the board.
     */
    String name;
    /**
     * List of elements present on the board.
     */
    List<Element> element = new ArrayList<Element>();
    /**
     * List of elements that have been clicked.
     */
    private List<Element> clickedElements = new ArrayList<>();
    /**
     * List of elements to be added to the board.
     */
    private List<Element> elementsToAdd = new ArrayList<>();
    /**
     * Evaluation object associated with the board.
     */
    Evaluate evaluate = new Evaluate();
    /**
     * Flag indicating if the board is active.
     */
    boolean isActive = false;
    /**
     * Default constructor for the Board class.
     */
    public Board() {}
    /**
     * Gets the name of the board.
     *
     * @return The name of the board.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the board.
     *
     * @param name The name to set for the board.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the list of elements on the board.
     *
     * @return List of elements on the board.
     */
    public List<Element> getElement() {
        return element;
    }
    /**
     * Finds an element by its ID.
     *
     * @param elementID The ID of the element to find.
     * @return The Element with the specified ID, or null if not found.
     */
    public Element getElementByID(String elementID) {
        Element result = null;

        for(Element e : element) {
            if(e.getElementID().equals(elementID)) {
                result = e;
            }
        }
        return result;
    }
    /**
     * Checks if the board is currently active.
     *
     * @return True if the board is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }
    /**
     * Sets the list of elements for the board.
     *
     * @param elements The list of elements to set for the board.
     */
    public void setElement(List<Element> elements) {
        this.element = elements;
    }
    /**
     * Sets the active state of the board.
     *
     * @param active The active state to set for the board.
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    /**
     * Sets the evaluate object for the board.
     *
     * @param evaluate The evaluate object to set for the board.
     */
    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }
    /**
     * Gets the evaluate object associated with the board.
     *
     * @return The evaluate object of the board.
     */
    public Evaluate getEvaluate() {
        return evaluate;
    }
    /**
     * Gets the list of elements that have been clicked on the board.
     *
     * @return The list of clicked elements.
     */
    public List<Element> getClickedElements() {
        return clickedElements;
    }
    /**
     * Gets the list of elements to be added to the board.
     *
     * @return The list of elements to add.
     */
    public List<Element> getElementsToAdd() {
        return elementsToAdd;
    }

    /**
     * Resets the board to its initial state.
     * This includes clearing clicked elements and removing dynamic elements.
     */
    public void resetBoard() {
        List<Element> elementsToAddCopy = new ArrayList<>(elementsToAdd);
        clickedElements = new ArrayList<>();
        // Clear elementsToAdd
        elementsToAdd.clear();

        Iterator<Element> it = getElement().iterator();
        while(it.hasNext()) {
            Element e = it.next();
            if(e.getElementID().startsWith(":")) {
                it.remove();
            }
        }

        getElement().addAll(elementsToAddCopy);

        for(Element e:getElement()) {
            e.setCurrentStateID(e.state.get(0).getStateID());
            if(e.getCurrentStateID().equals("edit"))
                e.getCurrentState().reset();
            if(e.clickThread!=null && e.clickThread.isAlive())
                e.clickThread.interrupt();
        }
    }
    /**
     * Checks if the current state of the board meets the required conditions.
     *
     * @return True if the board meets the required conditions, false otherwise.
     */
    public boolean isCorrect() {
        if (evaluate.getRequired() == null) {
            return true;
        }

        if(evaluate.getRequired().isEmpty() && clickedElements.isEmpty()) {
            return true;
        } else if (!evaluate.getRequired().isEmpty() && clickedElements.isEmpty()) {
            return false;
        } else if(name.contains("zegar")) {
            if(clickedElements.get(1).getElementID().equals("godz11")
                    && clickedElements.get(2).getElementID().equals("min2")) {
                return true;
            } else {
                return false;
            }
        } else if(!evaluate.getRequiredOrdered().isEmpty()) {
            clickedElements = clickedElements.stream().distinct().collect(Collectors.toList());
            return listsEqual(clickedElements, evaluate.getRequiredOrdered());
        } else {
            clickedElements = clickedElements.stream().distinct().collect(Collectors.toList());
            return containsAllConditions(clickedElements, evaluate.getRequired());
        }
    }
    /**
     * Compares two lists to check if they are equal by comparing the IDs of their elements.
     *
     * @param list1 The first list of elements to compare.
     * @param list2 The second list of conditions to compare.
     * @return True if the lists are equal in terms of element IDs, false otherwise.
     */
    boolean listsEqual(List<Element> list1, List<Condition> list2) {
        if(list1.size() != list2.size()) return false;

        for(int i = 0; i < list1.size(); i++) {
            if(!list1.get(i).getElementID().equals(list2.get(i).getElementID())) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if a list of clicked elements contains only the required conditions.
     *
     * @param clickedElements The list of clicked elements.
     * @param required The list of required conditions to be met.
     * @return True if all required conditions are met by the clicked elements, false otherwise.
     */
    boolean containsAllConditions(List<Element> clickedElements, List<Condition> required) {
        if(clickedElements.size() != required.size()) {
            return false;
        }

        List<String> clickedIDs = clickedElements.stream().map(Element::getElementID).collect(Collectors.toList());
        List<String> requiredIDs = required.stream().map(Condition::getElementID).collect(Collectors.toList());

        return clickedIDs.containsAll(requiredIDs);
    }
    /**
     * Sets the list of elements that have been clicked on the board.
     *
     * @param clickedElements The list of clicked elements to set.
     */
    public void setClickedElements(List<Element> clickedElements) {
        this.clickedElements = clickedElements;
    }
}
