package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    String name;
    List<Element> element = new ArrayList<Element>();

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

    public void setElement(List<Element> elements) {
        this.element = elements;
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
