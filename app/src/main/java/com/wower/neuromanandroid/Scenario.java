package com.wower.neuromanandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Scenario {
    String name;
    List<Board> board = new ArrayList<>();
    int currentBoardIndex = 0;
    boolean isScaled = false;

    public Scenario() {}

    public Scenario(List<Board> board) {
        super();
        this.board = board;
    }

    public List<Board> getBoard() {
        return board;
    }

    public Board getCurrentBoard() {
        if(currentBoardIndex >= 0 && currentBoardIndex < board.size()) {
            return board.get(currentBoardIndex);
        }
        return null;
    }

    public void setBoard(List<Board> board) {
        this.board = board;
    }

    public boolean isScaled() {
        return isScaled;
    }

    public void setScaled(boolean isScaled) {
        this.isScaled = isScaled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
