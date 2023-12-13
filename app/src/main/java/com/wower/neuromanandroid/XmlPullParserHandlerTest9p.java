package com.wower.neuromanandroid;

import static android.content.ContentValues.TAG;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlPullParserHandlerTest9p {
    Scenario scenario;
    String name;
    List<Board> boards = new ArrayList<>();
    Board board;
    List<Element> elements = new ArrayList<>();
    Element element;
    String elementID;
    List<ElementState> states = new ArrayList<>();
    ElementState state;
    String stateID;
    int locX;
    int locY;
    int width;
    int height;
    String source;
    String fgcolor;
    int clickOnEnd;
    int autoClick;
    int duration;
    List<ElementAction> actions = new ArrayList<>();
    ElementAction action;
    String text;

    public Scenario parse(InputStream inputStream) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        if(tagName.equalsIgnoreCase("scenario")) {
                            scenario = new Scenario();
                            boards = new ArrayList<>();
                        } else if(tagName.equalsIgnoreCase("board")) {
                            board = new Board();
                            elements = new ArrayList<>();
                        } else if(tagName.equalsIgnoreCase("element")) {
                            element = new Element();
                            states = new ArrayList<>();
                        } else if(tagName.equalsIgnoreCase("state")) {
                            state = new ElementState();
                            actions = new ArrayList<>();
                        } else if(tagName.equalsIgnoreCase("action")) {
                            action = new ElementAction();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase("scenario")) {
                            scenario.setBoard(boards);
                            //boards.clear();
                        } else if(tagName.equalsIgnoreCase("board")) {
                            board.setElement(elements);
                            boards.add(board);
                            board = null;
                        } else if(tagName.equalsIgnoreCase("element")) {
                            element.setState(states);
                            elements.add(element);
                            element = null;
                        } else if(tagName.equalsIgnoreCase("state")) {
                            state.setActions(actions);
                            states.add(state);
                            state = null;
                        } else if(tagName.equalsIgnoreCase("action")) {
                            actions.add(action);
                            action = null;
                        } else if(tagName.equalsIgnoreCase("name")) {
                            if(scenario != null && scenario.getName() == null) {
                                scenario.setName(text);
                            } else if(board != null && board.getName() == null) {
                                board.setName(text);
                            }
                        } else if(tagName.equalsIgnoreCase("elementID")) {
                            if(element != null && element.getElementID() == null) {
                                element.setElementID(text);
                            } else if(action != null && action.getElementID() == null) {
                                action.setElementID(text);
                            }
                        } else if(tagName.equalsIgnoreCase("stateID")) {
                            if(state != null && state.getStateID() == null) {
                                state.setStateID(text);
                            } else if(action != null && action.getStateID() == null) {
                                action.setStateID(text);
                            }
                        } else if(tagName.equalsIgnoreCase("locX")) {
                            state.setLocX(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("locY")) {
                            state.setLocY(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("width")) {
                            state.setWidth(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("height")) {
                            state.setHeight(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("source")) {
                            if(text.startsWith("img:file=")) {
                                String transformedSource = transformSourceString(text);
                                state.setSource(transformedSource);
                            } else {
                                state.setSource(text);
                            }
                        } else if(tagName.equalsIgnoreCase("fgcolor")) {
                            state.setFgcolor(text);
                        } else if(tagName.equalsIgnoreCase("clickOnEnd")) {
                            state.setClickOnEnd(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("autoClick")) {
                            state.setAutoClick(Integer.parseInt(text));
                        } else if(tagName.equalsIgnoreCase("duration")) {
                            state.setDuration(Integer.parseInt(text));
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
        } catch(XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return scenario;
    }

    private String transformSourceString(String source) {
        source = source.replaceFirst("img:file=", "");
        // Step 1: Prepend "img_" if not present
        if (!source.startsWith("img_")) {
            source = "img_" + source;
        }

        // Step 2: Replace "-" with "_"
        source = source.replace("-", "_");

        // Step 3: Replace non-allowed characters with "x"
        source = source.replaceAll("[^a-zA-Z0-9_]", "x");

        // Step 4: Convert to lowercase
        source = source.toLowerCase();

        source = source.replace("xpng", "");
        source = source.replace("xgif", "");

        return source;
    }
}
