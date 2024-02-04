package com.wower.neuromanandroid;
/**
 * Represents a condition in an evaluation object.
 * This class holds an identifier for an element associated with the condition.
 */
public class Condition {
    /**
     * Identifier for the element related to this condition.
     */
    String elementID;
    /**
     * Default constructor for Condition.
     * Initializes the elementID to a default value indicating readiness.
     */
    public Condition() {
        this.elementID = "ready";
    }
    /**
     * Constructs a Condition with a specified element ID.
     *
     * @param elementID The unique identifier for the element associated with this condition.
     */
    public Condition(String elementID) {
        this.elementID = elementID;
    }
    /**
     * Sets the element ID associated with this condition.
     *
     * @param elementID The unique identifier to set for the associated element.
     */
    public void setElementID(String elementID) {
        this.elementID = elementID;
    }
    /**
     * Gets the element ID associated with this condition.
     *
     * @return The unique identifier of the associated element.
     */
    public String getElementID() {
        return elementID;
    }
}
