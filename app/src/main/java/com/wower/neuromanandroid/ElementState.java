package com.wower.neuromanandroid;

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
    int autoCLick;
    int duration;
    StateAction action;

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

    public void setFgcolor(String fgcolor) {
        this.fgcolor = fgcolor;
    }

    public void setClickOnEnd(int clickOnEnd) {
        this.clickOnEnd = clickOnEnd;
    }

    public void setAutoCLick(int autoCLick) {
        this.autoCLick = autoCLick;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
        return autoCLick;
    }

    public int getDuration() {
        return duration;
    }

    public void reset() {
        if(originalSource!=null)
            source = originalSource;
    }

}


