package com.wower.neuromanandroid;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlPullParserHandlerScenario {
    Scenario scenario;
    List<Board> boards = new ArrayList<>();
    Board board;
    List<Element> elements = new ArrayList<>();
    Element element;
    List<ElementState> states = new ArrayList<>();
    ElementState state;
    List<ElementAction> actions = new ArrayList<>();
    ElementAction action;
    String text;
    Evaluate evaluate;
    Condition condition;

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
                        } else if(tagName.equalsIgnoreCase("evaluate")) {
                            evaluate = new Evaluate();
                        } else if(tagName.equalsIgnoreCase("requiredCondition") || tagName.equalsIgnoreCase("requiredOrderedCondition")) {
                            condition = new Condition();
                        }  else if(tagName.equalsIgnoreCase("element")) {
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
                        } else if(tagName.equalsIgnoreCase("evaluate")) {
                            board.setEvaluate(evaluate);
                        } else if(tagName.equalsIgnoreCase("requiredCondition")) {
                            if(!condition.getElementID().contains("gotów")) {
                                evaluate.getRequired().add(condition);
                            }
                            condition = null;
                        } else if(tagName.equalsIgnoreCase("requiredOrderedCondition")) {
                            if(!condition.getElementID().contains("gotów")) {
                                evaluate.getRequiredOrdered().add(condition);
                            }
                            condition = null;
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
                            } else if(condition != null && condition.getElementID().equals("ready")) {
                                    condition.setElementID(text);
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
                                state.setOriginalSource(transformedSource);
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

    String transformSourceString(String source) {
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
