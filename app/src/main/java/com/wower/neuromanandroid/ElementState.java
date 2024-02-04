package com.wower.neuromanandroid;

import java.util.List;
/**
 * Represents the state of an element in the application.
 * This class manages the properties and actions associated with a specific state of an element.
 */
public class ElementState {
    /**
     * Identifier for the state. Unique within the context of the element.
     */
    private String stateID;
    /**
     * The X-coordinate location of the element in this state.
     */
    private int locX;
    /**
     * The Y-coordinate location of the element in this state.
     */
    private int locY;
    /**
     * The width of the element in this state.
     */
    private int width;
    /**
     * The height of the element in this state.
     */
    private int height;
    /**
     * The source identifier for the element in this state, such as an image or color.
     */
    private String source;
    /**
     * Original source of the element, used to reset the element's state.
     */
    private String originalSource;
    /**
     * Foreground color for the element in this state, typically used for text or graphics.
     */
    private String fgcolor;
    /**
     * Indicates if a click is required to end this state and move to the next.
     */
    private int clickOnEnd;
    /**
     * Indicates if the state should auto-progress without user interaction.
     */
    private int autoClick;
    /**
     * Duration that the state should last before automatically progressing.
     */
    private int duration;
    /**
     * List of actions associated with this state.
     */
    private List<ElementAction> actions;
    /**
     * Default constructor for ElementState.
     */
    public ElementState() {}
    /**
     * Constructs an ElementState with specified properties.
     *
     * @param stateID Identifier for the state.
     * @param locX The X-coordinate location of the element in this state.
     * @param locY The Y-coordinate location of the element in this state.
     * @param width The width of the element in this state.
     * @param height The height of the element in this state.
     * @param source The source identifier for the element in this state.
     */
    public ElementState(String stateID, int locX, int locY, int width, int height, String source) {
        super();
        this.stateID = stateID;
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
        this.source = source;
    }
    /**
     * Sets the state ID.
     *
     * @param stateID The unique identifier for the state.
     */
    public void setStateID(String stateID) {
        this.stateID = stateID;
    }
    /**
     * Sets the X-coordinate location.
     *
     * @param locX The X-coordinate location of the element in this state.
     */
    public void setLocX(int locX) {
        this.locX = locX;
    }
    /**
     * Sets the Y-coordinate location.
     *
     * @param locY The Y-coordinate location of the element in this state.
     */
    public void setLocY(int locY) {
        this.locY = locY;
    }
    /**
     * Sets the width of the element in this state.
     *
     * @param width The width of the element.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Sets the height of the element in this state.
     *
     * @param height The height of the element.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Sets the source identifier for the element in this state.
     *
     * @param source The source identifier, such as an image or color.
     */
    public void setSource(String source) {
        this.source = source;
    }
    /**
     * Sets the original source of the element, used to reset the element's state.
     *
     * @param originalSource The original source identifier.
     */
    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }
    /**
     * Sets the foreground color for the element in this state.
     *
     * @param fgcolor The foreground color.
     */
    public void setFgcolor(String fgcolor) {
        this.fgcolor = fgcolor;
    }
    /**
     * Sets the flag indicating if a click is required to end this state.
     *
     * @param clickOnEnd The click-on-end flag value.
     */
    public void setClickOnEnd(int clickOnEnd) {
        this.clickOnEnd = clickOnEnd;
    }
    /**
     * Sets the flag indicating if the state should auto-progress.
     *
     * @param autoClick The auto-click flag value.
     */
    public void setAutoClick(int autoClick) {
        this.autoClick = autoClick;
    }
    /**
     * Sets the duration that the state should last.
     *
     * @param duration The duration in milliseconds.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
    /**
     * Sets the list of actions associated with this state.
     *
     * @param actions The list of ElementAction objects.
     */
    public void setActions(List<ElementAction> actions) {
        this.actions = actions;
    }
    /**
     * Gets the state ID.
     *
     * @return The unique identifier for the state.
     */
    public String getStateID() {
        return stateID;
    }
    /**
     * Gets the X-coordinate location of the element in this state.
     *
     * @return The X-coordinate location.
     */
    public int getLocX() {
        return locX;
    }
    /**
     * Gets the Y-coordinate location of the element in this state.
     *
     * @return The Y-coordinate location.
     */
    public int getLocY() {
        return locY;
    }
    /**
     * Gets the width of the element in this state.
     *
     * @return The width of the element.
     */
    public int getWidth() {
        return width;
    }
    /**
     * Gets the height of the element in this state.
     *
     * @return The height of the element.
     */
    public int getHeight() {
        return height;
    }
    /**
     * Gets the source identifier for the element in this state.
     *
     * @return The source identifier, such as an image or color.
     */
    public String getSource() {
        return source;
    }
    /**
     * Gets the foreground color for the element in this state.
     *
     * @return The foreground color.
     */
    public String getFgcolor() {
        return fgcolor;
    }
    /**
     * Gets the flag indicating if a click is required to end this state.
     *
     * @return The click-on-end flag value.
     */
    public int getClickOnEnd() {
        return clickOnEnd;
    }
    /**
     * Gets the flag indicating if the state should auto-progress.
     *
     * @return The auto-click flag value.
     */
    public int getAutoCLick() {
        return autoClick;
    }
    /**
     * Gets the duration that the state should last.
     *
     * @return The duration in milliseconds.
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Gets the list of actions associated with this state.
     *
     * @return The list of ElementAction objects.
     */
    public List<ElementAction> getActions() {
        return actions;
    }
    /**
     * Resets the source of the element to its original source.
     * This is used when the state of the element needs to be reset.
     */
    public void reset() {
        if(originalSource!=null)
            source = originalSource;
    }

}


