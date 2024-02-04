package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an element in the application.
 * This class manages the state and behavior of an element, including its identifier and state transitions.
 */
public class Element {
    /**
     * Unique identifier for the element.
     */
    String elementID;
    /**
     * List of states that the element can be in.
     */
    List<ElementState> state = new ArrayList<>();
    /**
     * Identifier of the current state of the element.
     */
    String currentStateID;
    /**
     * Thread for handling click events.
     */
    public Thread clickThread;
    /**
     * Index of the current state in the state list.
     */
    private int currentStateIndex = 0;
    /**
     * Flag to indicate whether the state of the element has been toggled.
     */
    private boolean stateToggled = false; // Flag to indicate if the state has been toggled
    /**
     * Default constructor for the Element class.
     */
    public Element() {}
    /**
     * Constructs an Element with a specified element ID.
     *
     * @param elementID The unique identifier for the element.
     */
    public Element(String elementID) {
        super();
        this.elementID = elementID;
    }
    /**
     * Constructs an Element with a specified element ID and list of states.
     *
     * @param elementID The unique identifier for the element.
     * @param states The list of states that the element can be in.
     */
    public Element(String elementID, List<ElementState> states) {
        super();
        this.elementID = elementID;
        this.state = states;
        this.currentStateID = states.get(0).getStateID();
    }
    /**
     * Gets the element's unique identifier.
     *
     * @return The unique identifier of the element.
     */
    public String getElementID() {
        return elementID;
    }
    /**
     * Sets the element's unique identifier.
     *
     * @param elementID The unique identifier to set for the element.
     */
    public void setElementID(String elementID) {
        this.elementID = elementID;
    }
    /**
     * Gets the list of states associated with the element.
     *
     * @return A list of ElementState objects.
     */
    public List<ElementState> getState() {
        return state;
    }
    /**
     * Sets the list of states for the element.
     *
     * @param state The list of ElementState objects to set.
     */
    public void setState(List<ElementState> state) {
        this.state = state;
    }
    /**
     * Gets the identifier of the element's current state.
     *
     * @return The identifier of the current state.
     */
    public String getCurrentStateID() {
        return currentStateID;
    }
    /**
     * Sets the identifier for the element's current state.
     * Special handling for elements with IDs "result" or "wynik".
     *
     * @param newStateID The new state identifier to set.
     */
    public void setCurrentStateID(String newStateID) {
        if(elementID.equals("result") || elementID.equals("wynik")) {
            ElementState es = new ElementState(newStateID, getCurrentState().getLocX(), getCurrentState().getLocY(),
                    getCurrentState().getWidth(), getCurrentState().getHeight(), getCurrentState().getSource());
            es.setSource(es.getSource() + newStateID);
            state.add(es);
        }

        this.currentStateID = newStateID;
    }
    /**
     * Gets the current state of the element.
     *
     * @return The current ElementState object.
     */
    public ElementState getCurrentState() {
        for(ElementState es: state) {
            if(es.getStateID().equals(getCurrentStateID()))
                return es;
        }
        return state.get(0);
    }
    /**
     * Toggles the state of the element if it has two states.
     * Sets the current state ID to "2" and marks the state as toggled.
     */
    public void toggleState() {
        if(this.getState().size() == 2) {
            setCurrentStateID("2");
            stateToggled = true;
        }
    }
    /**
     * Gets the index of the current state in the state list.
     *
     * @return The index of the current state.
     */
    public int getCurrentStateIndex() {
        return currentStateIndex;
    }
    /**
     * Checks if the state of the element has been toggled.
     *
     * @return True if the state has been toggled, false otherwise.
     */
    public boolean isStateToggled() {
        return stateToggled;
    }

}
