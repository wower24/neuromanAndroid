package com.wower.neuromanandroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scenario in the application.
 * This class manages the scenario details including its name, the list of boards,
 * and its scaled state.
 */
public class Scenario {
    // Holds the name of the scenario.
    private String name;

    // A list that holds multiple Board objects associated with this scenario.
    private List<Board> board = new ArrayList<>();

    // Tracks the index of the current board in use.
    int currentBoardIndex = 0;

    // Flag to indicate whether the scenario is scaled.
    private boolean isScaled = false;

    /**
     * Default constructor for the Scenario class.
     */
    public Scenario() {}

    /**
     * Constructs a Scenario with a specified list of boards.
     *
     * @param board A list of Board objects.
     */
    public Scenario(List<Board> board) {
        this.board = board;
    }

    /**
     * Returns the list of Board objects.
     *
     * @return List of Board objects.
     */
    public List<Board> getBoard() {
        return board;
    }

    /**
     * Retrieves the current Board object based on the currentBoardIndex.
     *
     * @return The current Board object, or null if the index is out of range.
     */
    public Board getCurrentBoard() {
        if(currentBoardIndex >= 0 && currentBoardIndex < board.size()) {
            return board.get(currentBoardIndex);
        }
        return null;
    }

    /**
     * Sets the list of Board objects for the scenario.
     *
     * @param board List of Board objects.
     */
    public void setBoard(List<Board> board) {
        this.board = board;
    }

    /**
     * Checks if the scenario is scaled.
     *
     * @return True if the scenario is scaled, false otherwise.
     */
    public boolean isScaled() {
        return isScaled;
    }

    /**
     * Sets the scaling flag for the scenario.
     *
     * @param isScaled The scaling flag to set.
     */
    public void setScaled(boolean isScaled) {
        this.isScaled = isScaled;
    }

    /**
     * Gets the name of the scenario.
     *
     * @return The name of the scenario.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the scenario.
     *
     * @param name The name to set for the scenario.
     */
    public void setName(String name) {
        this.name = name;
    }
}
