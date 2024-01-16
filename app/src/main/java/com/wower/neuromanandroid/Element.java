package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;

public class Element {
    String elementID;
    List<ElementState> state = new ArrayList<>();

    String currentStateID;

    public Thread clickThread;

    private int currentStateIndex = 0;
    private boolean stateToggled = false; // Flag to indicate if the state has been toggled

    public Element() {}

    public Element(String elementID) {
        super();
        this.elementID = elementID;
    }

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
        if(elementID.equals("result") || elementID.equals("wynik")) {
            ElementState es = new ElementState(newStateID, getCurrentState().getLocX(), getCurrentState().getLocY(),
                    getCurrentState().getWidth(), getCurrentState().getHeight(), getCurrentState().getSource());
            es.setSource(es.getSource() + newStateID);
            state.add(es);
        }

        this.currentStateID = newStateID;
    }

    public ElementState getCurrentState() {
        for(ElementState es: state) {
            if(es.getStateID().equals(getCurrentStateID()))
                return es;
        }
        return state.get(0);
    }

    public void toggleState() {
            if(this.getState().size() == 2) {
                setCurrentStateID("2");
                stateToggled = true;
            }
    }

    public int getCurrentStateIndex() {
        return currentStateIndex;
    }

    public boolean isStateToggled() {
        return stateToggled;
    }

    public String toString() {
        return elementID;
    }

}
