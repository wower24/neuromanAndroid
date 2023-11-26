package com.wower.neuromanandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Scenario {
    String name;
    List<Board> board = new ArrayList<>();

    private static String baseDirectory;


    public static String getBaseDirectory() {
        return baseDirectory;
    }

    public static void setBaseDirectory(String baseDirectory) {
        Scenario.baseDirectory = baseDirectory;
    }

    public Scenario() {}

    public Scenario(List<Board> board) {
        super();
        this.board = board;
    }

    public List<Board> getBoard() {
        return board;
    }

    public void setBoard(List<Board> board) {
        this.board = board;
    }

    public void save(String filename) {
        saveScenarioXML(filename, this);
    }


    public static Scenario openScenarioXML(String fileName) {
        Scenario.setBaseDirectory(fileName);
        System.out.println("base:"+fileName);
        //run a scenario with a given xml name, unxml xml
        Scenario scenario = new Scenario();

        //reset all boards
        for(Board b:scenario.getBoard()) {
            b.resetBoard();
        }


        return scenario;
    }

    public Board getBoard(String boardName) {
        for(Board b:getBoard()) {
            if(b.getName().equals(boardName))
                return b;
        }
        return null;
    }

    public static void saveScenarioXML(String fileName, Scenario scenario) {
        //Save scenario? Do we have to save it tho?
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void deleteScenario(File file) {
        System.out.println("main:"+file);
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                System.out.println("deleting "+file);
                deleteScenario(f);
            }
        }
        file.delete();
    }

}
