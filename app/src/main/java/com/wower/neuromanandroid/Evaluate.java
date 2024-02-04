package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the evaluation criteria for a board or scenario.
 * This class manages the conditions required and the order of those conditions if necessary.
 */
public class Evaluate {
    /**
     * List of conditions that are required to be met.
     */
    List<Condition> required;
    /**
     * List of conditions that are required to be met in a specific order.
     */
    List<Condition> requiredOrdered;
    /**
     * Default constructor for Evaluate.
     * Initializes the lists for required and requiredOrdered conditions.
     */
    public Evaluate() {
        required = new ArrayList<>();
        requiredOrdered = new ArrayList<>();
    }
    /**
     * Gets the list of required conditions.
     *
     * @return A list of Condition objects that are required.
     */
    public List<Condition> getRequired() {
        return required;
    }
    /**
     * Gets the list of required conditions that must be met in a specific order.
     *
     * @return A list of Condition objects that are required in a specific order.
     */
    public List<Condition> getRequiredOrdered() {
        return requiredOrdered;
    }
    /**
     * Sets the list of required conditions that must be met in a specific order.
     *
     * @param required The list of Condition objects to set as required in a specific order.
     */
    public void setRequiredOrdered(List<Condition> required) {
        this.requiredOrdered = required;
    }
    /**
     * Sets the list of required conditions.
     *
     * @param required The list of Condition objects to set as required.
     */
    public void setRequired(List<Condition> required) {
        this.required = required;
    }
}
