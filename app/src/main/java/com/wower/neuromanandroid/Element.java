package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;

public class Element {
    String elementID;
    List<ElementState> state = new ArrayList<>();

    String currentStateID;

    public Thread clickThread;

    public Element() {}

    public Element(String elementID, List<ElementState> states) {
        super();
        this.elementID = elementID;
        this.state = states;
        this.currentStateID = states.get(0).getStateID();
    }
    public String getElementID() {
        return elementID;
    }
    public void setElementID(String elementID) {
        this.elementID = elementID;
    }
    public List<ElementState> getState() {
        return state;
    }
    public void setState(List<ElementState> state) {
        this.state = state;
    }
    public String getCurrentStateID() {
        return currentStateID;
    }

    public void setCurrentStateID(String newStateID) {
        if(this.currentStateID!=null && this.currentStateID.equals("edit")) {
            ElementState es = getCurrentState();
            es.setSource(es.getSource() + newStateID);
        }
        else
            this.currentStateID = newStateID;
    }

    public ElementState getCurrentState() {
        for(ElementState es: state) {
            if(es.getStateID().equals(getCurrentStateID()))
                return es;
        }
        return null;
    }

    public String toString() {
        return elementID;
    }

}
