package com.wower.neuromanandroid;

public class ElementAction {
    String elementID;
    String stateID;
    public ElementAction() {}

    public ElementAction(String element, String state) {
        super();
        this.elementID = element;
        this.stateID = state;
    }
    public String getElementID() {
        return elementID;
    }
    public void setElementID(String element) {
        this.elementID = element;
    }
    public String getStateID() {
        return stateID;
    }
    public void setStateID(String state) {
        this.stateID = state;
    }

}
