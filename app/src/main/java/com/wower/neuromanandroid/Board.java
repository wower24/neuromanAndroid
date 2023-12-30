package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    String name;
    List<Element> element = new ArrayList<Element>();
    Evaluate evaluate;

    boolean isActive = false;

    public Board() {}

    public Board(String name, List<Element> element) {
        super();
        this.name = name;
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Element> getElement() {
        return element;
    }

    public Element getElementByID(String elementID) {
        Element result = null;

        for(Element e : element) {
            if(e.getElementID().equals(elementID)) {
                result = e;
            }
        }
        return result;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setElement(List<Element> elements) {
        this.element = elements;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    /**
     * remove dynamic elements created during display (starting with :)
     */
    public void resetBoard() {
        Iterator<Element> it = getElement().iterator();
        while(it.hasNext()) {
            Element e = it.next();
            if(e.getElementID().startsWith(":")) {
                it.remove();
            }
        }

        for(Element e:getElement()) {
            e.setCurrentStateID(e.state.get(0).getStateID());
            if(e.getCurrentStateID().equals("edit"))
                e.getCurrentState().reset();
            if(e.clickThread!=null && e.clickThread.isAlive())
                e.clickThread.interrupt();
        }


    }


}
