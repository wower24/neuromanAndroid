package com.wower.neuromanandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView extends View {
    private Scenario scenario;
    private Context context;
    private String badany;
    private float scaleX;
    private float scaleY;
    private Paint paint = new Paint();

    private Map<String, Drawable> drawableCache = new HashMap<>();
    StringBuilder xmlBuilder = new StringBuilder();

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public interface BoardViewListener {
        void onScenarioCompleted();
    }

    private BoardViewListener listener;

    public void setBoardViewListener(BoardViewListener listener) {
        this.listener = listener;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
        invalidate();
    }

    public void setBadany(String badany) {
        this.badany = badany;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!scenario.isScaled()) {
            calculateScalingFactors(scenario.getBoard().get(0).getElement().get(0));
            scenario.setScaled(true);
        }
        if(scenario != null) {
            drawBoard(scenario.getCurrentBoard(), canvas);
        }
    }

    private void drawBoard(Board board, Canvas canvas) {
        for (Element element : board.getElement()) {
            //Log.d("ELEMENT ID", "Drawing: " +  element.getElementID());
            List<ElementState> states = element.getState();
            if (element.getCurrentStateIndex() < states.size()) {
                ElementState state = element.getCurrentState();
                drawState(state, canvas);
            } else {
                Log.e("BoardView", "Invalid state index for element: " + element.getElementID());
            }
        }
    }

    private void drawState(ElementState state, Canvas canvas) {
        int x = state.getLocX();
        int y =state.getLocY();
        int width = state.getWidth();
        int height = state.getHeight();

        if(state.getSource() != null) {
            if (state.getSource().startsWith("img_")) {
                x = (int) (x * scaleX);
                y = (int) (y * scaleX);
                if(state.getSource().contains("ff6024892361")){
                    y += 80;
                }
                width = (int) (width * scaleX);
                height = (int) (height * scaleX);

                Drawable drawable = getDrawable(state.getSource());
                if (drawable != null) {
                    drawable.setBounds(x, y, x + width, y + height);
                    drawable.draw(canvas);
                }
            } else {
                x = (int) (x * scaleX);
                y = (int) (y * scaleY);
                width = (int) (width * scaleX);
                height = (int) (height * scaleY);

                paint.reset();
                paint.setStyle(Paint.Style.FILL);

                if (state.getSource().contains("rect")) {
                    paint.setColor(Color.parseColor("#FFFFFF"));
                    canvas.drawRect(x, y, x + width, y + height, paint);
                } else if (state.getSource().contains("circle")) {
                    if(state.getFgcolor() != null) {
                        paint.setColor(parseColorString(state.getFgcolor()));
                    } else {
                        paint.setColor(Color.parseColor("#FFFFFF"));
                    }
                    canvas.drawCircle(x + width / 2f, y + height / 2f, Math.min(width, height) / 2f, paint);
                } else if (state.getSource().contains("line")) {
                    paint.setStrokeWidth(3);
                    canvas.drawLine(state.getLocX(), state.getLocY(),
                            state.getLocX() + state.getWidth(), state.getLocY() + state.getHeight(), paint);
                } else if (state.getSource().contains("edit") || state.getSource().contains("text")) {
                    String source = state.getSource();
                    String text;
                    if(state.getSource().contains("edit")) {
                        text = source.substring(source.indexOf(";t=") + 3);
                    } else {
                        text = source.substring(source.indexOf(":t=") + 3);
                    }
                    text = text.split(";")[0];
                    float textSize = paint.getTextSize();
                    if (source.contains("height=")) {
                        String heightStr = source.split("height=")[1];
                        heightStr = heightStr.split(";")[0];
                        int textHeight = Integer.parseInt(heightStr);
                        textSize = textHeight * scaleX;
                    }

                    paint.setTextSize(textSize);
                    paint.setTextAlign(Paint.Align.LEFT);
                    float textWidth = paint.measureText(text);
                    while (textWidth > width && textSize > 0) {
                        textSize -= 1;
                        paint.setTextSize(textSize);
                        textWidth = paint.measureText(text);
                    }
                    canvas.drawText(text, x, (y + textSize)/5, paint);
                }
            }
        }
    }

    public static int parseColorString(String colorString) {
        // Split the string into RGB components
        String[] rgb = colorString.split(",");

        // Parse each component as an integer
        int r = Integer.parseInt(rgb[0].trim());
        int g = Integer.parseInt(rgb[1].trim());
        int b = Integer.parseInt(rgb[2].trim());

        // Return the color created from these components
        return Color.rgb(r, g, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Board board = scenario.getCurrentBoard();
            List<Element> elementsCopy = new ArrayList<>(board.getElement());
            for (Element element : elementsCopy) {
                ElementState state = element.getState().get(element.getCurrentStateIndex());

                if(scenario.getName().equals("test9p")) {
                    if (!element.getElementID().equals("frame")) {
                        if (isInsideElement(x, y, state) && element.getState().size() > 1) {
                            element.toggleState(); // Toggle the state of the element
                            invalidate(); // Redraw the view
                            checkCompletion(); // Check if the test is complete
                            break;
                        }
                    }
                }

                if(scenario.getName().equals("MOCA")) {
                    if (element.getElementID().equals("przycisk_dalej") && isInsideElement(x, y, state)) {
                        goToNextBoard();
                        return true;
                    }

                    }

                    if(board.isActive()) {
                        if (board.getName().equals("03_łączenie") && (isInsideElement(x, y, state) || element.getCurrentState().getAutoCLick() == 1)) {
                            if(element.getElementID().contains("koło")) {
                                board.getClickedElements().add(element);
                            }
                            changeStates(board, element);
                        } else if (isInsideElement(x, y, state) && element.getState().size() > 1) {
                            if(!element.elementID.equals("kwadrat") && !element.elementID.contains("pole") && !element.elementID.endsWith("ścianka") && !element.elementID.endsWith("scianka")) {
                                board.getClickedElements().add(element);
                            }

                            changeStates(board, element);
                        }
                        return true;
                    }

                    if(element.getElementID().equals("przycisk_gotów_nieaktywny") && isInsideElement(x, y, state)) {
                        changeStates(board, element);
                        updateElement(board.getElementByID("przycisk_gotów_aktywny"));
                        board.setActive(true);
                    }

                if (element.getElementID().equals("przycisk_reset") && isInsideElement(x, y, state)) {
                    scenario.getCurrentBoard().resetBoard();
                    invalidate();
                }

                if(element.getElementID().contains("poprawnie") && isInsideElement(x, y, state)) {
                    scenario.getCurrentBoard().getClickedElements().clear();
                    scenario.getCurrentBoard().getClickedElements().add(element);
                    invalidate();
                }
            }

            if(board.getElementsToAdd().size()>0) {
                board.getElement().addAll(board.getElementsToAdd());
                invalidate();
            }
        }
        return true;
    }

    private void changeStates(Board currentBoard, Element currentElement) {
        ElementState state = currentElement.getCurrentState();
            for (ElementAction action : state.getActions()) {
                if(action.getElementID().equals(":-1") && action.getStateID().equals(":line")
                        && currentBoard.getClickedElements().size()>0) {
                    int lastClickedIndex = currentBoard.getClickedElements().size() - 2;
                    ElementState lastClickedState = currentBoard.getClickedElements().get(lastClickedIndex).getCurrentState();
                    int centerXLastClicked = lastClickedState.getLocX() + lastClickedState.getWidth() / 2;
                    int centerYLastClicked = lastClickedState.getLocY() + lastClickedState.getHeight() / 2;
                    int centerXCurrent = state.getLocX() + state.getWidth() / 2;
                    int centerYCurrent = state.getLocY() + state.getHeight() / 2;

                    Element line = addLineElement(centerXLastClicked, centerYLastClicked, centerXCurrent, centerYCurrent);
                        currentBoard.getElementsToAdd().add(0, line);

                } else {
                    Element element = currentBoard.getElementByID(action.getElementID());
                    if(element != null) {
                        element.setCurrentStateID(action.getStateID());
                        updateElement(element);
                    }
                }
            }
    }
    private void updateElement(Element element) {
        ElementState currentState = element.getCurrentState();
        int locX = (int) (currentState.getLocX() * scaleX);
        int locY = (int) (currentState.getLocY() * scaleX);
        int width = (int) (currentState.getWidth() * scaleX);
        int height = (int) (currentState.getHeight() * scaleX);

        invalidate(locX, locY, locX + width, locY + height);
    }

    private void checkCompletion() {
        if(scenario.getName().equals("test9p")) {
            for (Element element : scenario.getCurrentBoard().getElement()) {
                // Skip the 'frame' element
                if ("frame".equals(element.getElementID())) {
                    continue;
                }

                if (!element.isStateToggled()) {
                    return;
                }
            }
            listener.onScenarioCompleted();
        }
    }

    private boolean isInsideElement(int x, int y, ElementState state) {
        int scaledX = state.getLocX();
        int scaledY = state.getLocY();
        int scaledWidth = state.getWidth();
        int scaledHeight = state.getHeight();

        if(state.getSource().contains("circle")) {
            scaledX *= scaleX;
            scaledY *= scaleY;
            scaledWidth *= scaleX;
            scaledHeight *= scaleY;
            int centerX = scaledX + scaledWidth / 2;
            int centerY = scaledY + scaledHeight / 2;
            int radius = Math.min(scaledWidth, scaledHeight) / 2;
            return Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)) <= radius;
        } else {
            scaledX *= scaleX;
            scaledY *= scaleX;
            scaledWidth *= scaleX;
            scaledHeight *= scaleX;
            return x >= scaledX && x <= (scaledX + scaledWidth) && y >= scaledY && y <= (scaledY + scaledHeight);
        }
    }

    private void calculateScalingFactors(Element frame) {
        int frameWidth = frame.getState().get(0).getWidth();
        int frameHeight = frame.getState().get(0).getHeight();

        int viewWidth = getWidth() - 30;
        int viewHeight = getHeight() - 30;

        scaleX = (float) viewWidth / frameWidth;
        scaleY = (float) viewHeight / frameHeight;
    }

    public void goToNextBoard() {
        String boardStateXml = serializeBoardState(scenario.getCurrentBoard());
        scenario.getCurrentBoard().getClickedElements().clear();
        if(scenario.currentBoardIndex < scenario.getBoard().size() - 2) {
            scenario.currentBoardIndex++;
            invalidate();
        } else {
            saveBoardStateToFile(boardStateXml, badany + "_" + scenario.getName() + ".xml");
            listener.onScenarioCompleted();
        }
    }

    private Drawable getDrawable(String filename) {
        if (!drawableCache.containsKey(filename)) {
            int resId = getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
            Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
            drawableCache.put(filename, drawable);
        }
        return drawableCache.get(filename);
    }

    Element addLineElement(int centerXFrom, int centerYFrom, int centerXTo, int centerYTo) {
        // Apply scaling to the coordinates
        int scaledCenterXFrom = (int) (centerXFrom * scaleX);
        int scaledCenterYFrom = (int) (centerYFrom * scaleX);
        int scaledCenterXTo = (int) (centerXTo * scaleX);
        int scaledCenterYTo = (int) (centerYTo * scaleX);

        ElementState lineState = new ElementState("line", scaledCenterXFrom, scaledCenterYFrom, scaledCenterXTo - scaledCenterXFrom, scaledCenterYTo - scaledCenterYFrom, "line:");
        List<ElementState> list = new ArrayList<>();
        list.add(lineState);
        Element line = new Element(":line", list);
        line.setCurrentStateID("line");
        return line;
    }

    private String serializeBoardState(Board board) {
        if(xmlBuilder.length() == 0) {
            xmlBuilder.append("<test>\n");
            xmlBuilder.append("\t<scenario>").append(scenario.getName()).append("</scenario>\n");
            xmlBuilder.append("\t<patient>").append(badany).append("</patient>\n");
        }

        xmlBuilder.append("\t<board>\n");
        xmlBuilder.append("\t\t<name>").append(board.getName()).append("</name>\n");

        boolean isCorrect = board.isCorrect();
        String result;

        if(isCorrect) {
            result = "1";
        } else {
            result = "0";
        }

        xmlBuilder.append("\t\t<result>").append(result).append("</result>\n");

        xmlBuilder.append("\t</board>\n");
        return xmlBuilder.toString();
    }

    private void saveBoardStateToFile(String boardStateXml, String fileName) {
        try {
            boardStateXml += ("</test>");
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(boardStateXml.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
