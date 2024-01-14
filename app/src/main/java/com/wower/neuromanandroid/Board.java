package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    String name;
    List<Element> element = new ArrayList<Element>();
    private List<Element> clickedElements = new ArrayList<>();
    private List<Element> elementsToAdd = new ArrayList<>();
    Evaluate evaluate = new Evaluate();

    boolean isActive = false;

    public Board() {}

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

    public List<Element> getClickedElements() {
        return clickedElements;
    }

    public List<Element> getElementsToAdd() {
        return elementsToAdd;
    }

    /**
     * remove dynamic elements created during display (starting with :)
     */
    public void resetBoard() {
        List<Element> elementsToAddCopy = new ArrayList<>(elementsToAdd);
        clickedElements = new ArrayList<>();
        // Clear elementsToAdd
        elementsToAdd.clear();

        Iterator<Element> it = getElement().iterator();
        while(it.hasNext()) {
            Element e = it.next();
            if(e.getElementID().startsWith(":")) {
                it.remove();
            }
        }

        getElement().addAll(elementsToAddCopy);

        for(Element e:getElement()) {
            e.setCurrentStateID(e.state.get(0).getStateID());
            if(e.getCurrentStateID().equals("edit"))
                e.getCurrentState().reset();
            if(e.clickThread!=null && e.clickThread.isAlive())
                e.clickThread.interrupt();
        }
    }

    public boolean isCorrect() {
        if (evaluate.getRequired() == null) {
            return true;
        }

        if(name.contains("07_") || name.contains("08_") || name.contains("11_") || name.contains("16_skojarzenia")) {
            return false;
        } else if(evaluate.getRequired().isEmpty() && clickedElements.isEmpty()) {
            return true;
        } else if (!evaluate.getRequired().isEmpty() && clickedElements.isEmpty()) {
            return false;
        } else if(name.contains("zegar")) {
            if(clickedElements.get(1).getElementID().equals("godz11")
                    && clickedElements.get(2).getElementID().equals("min2")) {
                return true;
            } else {
                return false;
            }
        } else if(!evaluate.getRequiredOrdered().isEmpty()) {
            clickedElements = clickedElements.stream().distinct().collect(Collectors.toList());
            return listsEqual(clickedElements, evaluate.getRequiredOrdered());
        } else {
            clickedElements = clickedElements.stream().distinct().collect(Collectors.toList());
            return containsAllConditions(clickedElements, evaluate.getRequired());
        }
    }

    boolean listsEqual(List<Element> list1, List<Condition> list2) {
        if(list1.size() != list2.size()) return false;

        for(int i = 0; i < list1.size(); i++) {
            if(!list1.get(i).getElementID().equals(list2.get(i).getElementID())) {
                return false;
            }
        }
        return true;
    }

    boolean containsAllConditions(List<Element> clickedElements, List<Condition> required) {
        if(clickedElements.size() != required.size()) {
            return false;
        }

        List<String> clickedIDs = clickedElements.stream().map(Element::getElementID).collect(Collectors.toList());
        List<String> requiredIDs = required.stream().map(Condition::getElementID).collect(Collectors.toList());

        return clickedIDs.containsAll(requiredIDs);
    }

    public void setClickedElements(List<Element> clickedElements) {
        this.clickedElements = clickedElements;
    }
}
