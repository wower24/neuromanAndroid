package com.wower.neuromanandroid;

public class Condition {
    String elementID;

    public Condition() {
        this.elementID = "ready";
    }
    public Condition(String elementID) {
        this.elementID = elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public String getElementID() {
        return elementID;
    }
}
