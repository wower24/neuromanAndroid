package com.wower.neuromanandroid;
/**
 * Represents an action associated with an element state in the application.
 * This class defines the actions that can be triggered based on element states.
 */
public class ElementAction {
    /**
     * Identifier for the element associated with this action.
     */
    String elementID;
    /**
     * Identifier for the state to be triggered by this action.
     */
    String stateID;
    /**
     * Default constructor for ElementAction.
     */
    public ElementAction() {}
    /**
     * Constructs an ElementAction with specified element and state identifiers.
     *
     * @param element The identifier for the element associated with this action.
     * @param state The identifier for the state to be triggered by this action.
     */
    public ElementAction(String element, String state) {
        super();
        this.elementID = element;
        this.stateID = state;
    }
    /**
     * Gets the identifier for the element associated with this action.
     *
     * @return The element identifier.
     */
    public String getElementID() {
        return elementID;
    }
    /**
     * Sets the identifier for the element associated with this action.
     *
     * @param element The element identifier to set.
     */
    public void setElementID(String element) {
        this.elementID = element;
    }
    /**
     * Gets the identifier for the state to be triggered by this action.
     *
     * @return The state identifier.
     */
    public String getStateID() {
        return stateID;
    }
    /**
     * Sets the identifier for the state to be triggered by this action.
     *
     * @param state The state identifier to set.
     */
    public void setStateID(String state) {
        this.stateID = state;
    }

}
