package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;

public class Evaluate {
    List<Condition> required;
    List<Condition> requiredOrdered;

    public Evaluate() {
        required = new ArrayList<>();
        requiredOrdered = new ArrayList<>();
    }

    public List<Condition> getRequired() {
        return required;
    }

    public List<Condition> getRequiredOrdered() {
        return requiredOrdered;
    }

    public void setRequired(List<Condition> required) {
        this.required = required;
    }
}
