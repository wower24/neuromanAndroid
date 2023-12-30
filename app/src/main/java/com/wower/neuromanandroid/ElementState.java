package com.wower.neuromanandroid;

import java.util.List;

public class ElementState {
    String stateID;
    int locX;
    int locY;
    int width;
    int height;
    String source;
    String originalSource;
    String fgcolor;
    int clickOnEnd;
    int autoClick;
    int duration;
    List<ElementAction> actions;

    public ElementState() {}

    public ElementState(String stateID, int locX, int locY, int width, int height, String source) {
        super();
        this.stateID = stateID;
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
        this.source = source;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public void setLocX(int locX) {
        this.locX = locX;
    }

    public void setLocY(int locY) {
        this.locY = locY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    public void setFgcolor(String fgcolor) {
        this.fgcolor = fgcolor;
    }

    public void setClickOnEnd(int clickOnEnd) {
        this.clickOnEnd = clickOnEnd;
    }

    public void setAutoClick(int autoCLick) {
        this.autoClick = autoCLick;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setActions(List<ElementAction> actions) {
        this.actions = actions;
    }

    public String getStateID() {
        return stateID;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocY() {
        return locY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSource() {
        return source;
    }

    public String getFgcolor() {
        return fgcolor;
    }

    public int getClickOnEnd() {
        return clickOnEnd;
    }

    public int getAutoCLick() {
        return autoClick;
    }

    public int getDuration() {
        return duration;
    }

    public List<ElementAction> getActions() {
        return actions;
    }

    public void reset() {
        if(originalSource!=null)
            source = originalSource;
    }

}


