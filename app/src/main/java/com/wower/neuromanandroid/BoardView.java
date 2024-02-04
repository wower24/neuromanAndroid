package com.wower.neuromanandroid;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * Custom view for displaying and interacting with the game board.
 */
public class BoardView extends View {
    /**
     * Scenario being displayed in the view.
     */
    private Scenario scenario;
    /**
     * Context of the application.
     */
    private Context context;
    /**
     * Name of the patient.
     */
    private String patient;
    /**
     * Scaling factors for the drawing.
     */
    private float scaleX;
    private float scaleY;
    /**
     * Paint object for drawing elements.
     */
    private Paint paint = new Paint();
    /**
     * Cache for drawable resources to optimize performance.
     */
    private Map<String, Drawable> drawableCache = new HashMap<>();
    /**
     * StringBuilder for building XML content.
     */
    StringBuilder xmlBuilder = new StringBuilder();
    /**
     * Interface for scenario completion callbacks.
     */
    public interface BoardViewListener {
        void onScenarioCompleted();
    }
    /**
     * Listener for scenario completion events.
     */
    private BoardViewListener listener;
    /**
     * Constructor for the BoardView class.
     * Initializes the view with the specified context and attribute set.
     *
     * @param context The context of the application.
     * @param attrs A collection of attributes.
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    /**
     * Sets the listener for board view events.
     *
     * @param listener The listener to set for board view events.
     */
    public void setBoardViewListener(BoardViewListener listener) {
        this.listener = listener;
    }
    /**
     * Sets the scenario for the board view.
     * Invalidates the view to trigger a redraw with the new scenario.
     *
     * @param scenario The scenario to be set for the board view.
     */
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
        invalidate();
    }
    /**
     * Sets the patient's name for the board view.
     *
     * @param patient The name of the patient.
     */
    public void setPatient(String patient) {
        this.patient = patient;
    }
    /**
     * Called when the view should render its content.
     * This is where the board and its elements are drawn.
     *
     * @param canvas The canvas on which the view will draw.
     */
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
    /**
     * Draws the specified board on the canvas.
     * @param board The board to draw.
     * @param canvas The canvas on which to draw the board.
     */
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
    /**
     * Draws the specified state of an element on the canvas.
     * @param state The state of the element to draw.
     * @param canvas The canvas on which to draw the state.
     */
    private void drawState(ElementState state, Canvas canvas) {
        int x = state.getLocX();
        int y =state.getLocY();
        int width = state.getWidth();
        int height = state.getHeight();

        if(state.getSource() != null) {
            x = (int) (x * scaleX);
            y = (int) (y * scaleX);
            width = (int) (width * scaleX);
            height = (int) (height * scaleX);
            if (state.getSource().startsWith("img_")) {
                if(state.getSource().contains("ff6024892361")){
                    y += 80;
                }

                Drawable drawable = getDrawable(state.getSource());
                if (drawable != null) {
                    drawable.setBounds(x, y, x + width, y + height);
                    drawable.draw(canvas);
                }
            } else {
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
                    canvas.drawText(text, x, (y + textSize), paint);
                }
            }
        }
    }
    /**
     * Parses a color string and returns the corresponding color value.
     * @param colorString The string representing the color.
     * @return The parsed color value.
     */
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
    /**
     * Handles touch screen motion events.
     * This method is responsible for handling user interactions with the board. It detects where
     * the user has touched the board and performs actions based on the state and type of elements
     * at that position.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Board board = scenario.getCurrentBoard();
            List<Element> elementsCopy = new ArrayList<>(board.getElement());
            for (Element element : elementsCopy) {
                ElementState state = element.getState().get(element.getCurrentStateIndex());
                String elementID = element.getElementID();
                if(scenario.getName().equals("test9p")) {
                    if (!elementID.equals("frame")) {
                        if (isInsideElement(x, y, state) && element.getState().size() > 1) {
                            element.toggleState(); // Toggle the state of the element
                            invalidate(); // Redraw the view
                            checkCompletion(); // Check if the test is complete
                            break;
                        }
                    }
                }

                if(scenario.getName().equals("MOCA")) {
                    if (elementID.equals("przycisk_dalej") && isInsideElement(x, y, state)) {
                        goToNextBoard();
                        return true;
                    }

                }

                if(board.isActive()) {
                    if ((isInsideElement(x, y, state) && element.getState().size() > 1)
                            || element.getCurrentState().getAutoCLick() == 1) {
                        if((board.getName().equals("03_łączenie") && elementID.contains("koło")) || (!elementID.equals("kwadrat") && !elementID.contains("pole")
                                && !elementID.endsWith("ścianka") && !elementID.endsWith("scianka"))) {
                            board.getClickedElements().add(element);
                        }

                        changeStates(board, element);
                    }
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
    /**
     * Changes the states of elements based on the specified action.
     * @param currentBoard The current board being interacted with.
     * @param currentElement The element on which the action occurred.
     */
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
    /**
     * Updates the specified element's appearance.
     * @param element The element to update.
     */
    private void updateElement(Element element) {
        ElementState currentState = element.getCurrentState();
        int locX = (int) (currentState.getLocX() * scaleX);
        int locY = (int) (currentState.getLocY() * scaleX);
        int width = (int) (currentState.getWidth() * scaleX);
        int height = (int) (currentState.getHeight() * scaleX);

        invalidate(locX, locY, locX + width, locY + height);
    }
    /**
     * Checks if the current scenario is completed.
     */
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
    /**
     * Determines if the specified coordinates are inside the given element state.
     * @param x The x-coordinate of the touch event.
     * @param y The y-coordinate of the touch event.
     * @param state The state of the element to check against.
     * @return True if the coordinates are inside the element state, false otherwise.
     */
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
    /**
     * Calculates the scaling factors based on the frame element.
     * @param frame The frame element to use for calculations.
     */
    private void calculateScalingFactors(Element frame) {
        int frameWidth = frame.getState().get(0).getWidth();
        int frameHeight = frame.getState().get(0).getHeight();

        int viewWidth = getWidth() - 30;
        int viewHeight = getHeight() - 30;

        scaleX = (float) viewWidth / frameWidth;
        scaleY = (float) viewHeight / frameHeight;
    }
    /**
     * Advances to the next board in the scenario.
     */
    public void goToNextBoard() {
        String boardStateXml = serializeBoardState(scenario.getCurrentBoard());
        scenario.getCurrentBoard().getClickedElements().clear();
        if(scenario.currentBoardIndex < scenario.getBoard().size() - 2) {
            scenario.currentBoardIndex++;
            invalidate();
        } else {
            saveBoardStateToFile(boardStateXml, patient + "_" + scenario.getName());
            listener.onScenarioCompleted();
        }
    }
    /**
     * Retrieves a drawable from the cache or loads it if not present.
     * @param filename The filename of the drawable to retrieve.
     * @return The drawable object.
     */
    private Drawable getDrawable(String filename) {
        if (!drawableCache.containsKey(filename)) {
            int resId = getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
            Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
            drawableCache.put(filename, drawable);
        }
        return drawableCache.get(filename);
    }
    /**
     * Adds a line element to the board.
     * @param centerXFrom The x-coordinate of the start of the line.
     * @param centerYFrom The y-coordinate of the start of the line.
     * @param centerXTo The x-coordinate of the end of the line.
     * @param centerYTo The y-coordinate of the end of the line.
     * @return The new line element.
     */
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
    /**
     * Serializes the state of the specified board into XML format.
     * @param board The board to serialize.
     * @return The XML string representation of the board state.
     */
    private String serializeBoardState(Board board) {
        if(xmlBuilder.length() == 0) {
            xmlBuilder.append("<test>\n");
            xmlBuilder.append("\t<scenario>").append(scenario.getName()).append("</scenario>\n");
            xmlBuilder.append("\t<patient>").append(patient).append("</patient>\n");
        }
        String name = board.getName();
        if(!name.contains("07_") && !name.contains("08_") && !name.contains("11_") && !name.contains("16_skojarzenia")) {
            xmlBuilder.append("\t<board>\n");
            xmlBuilder.append("\t\t<name>").append(name).append("</name>\n");


            boolean isCorrect = board.isCorrect();
            String result;

            if (isCorrect) {
                result = "1";
            } else {
                result = "0";
            }

            xmlBuilder.append("\t\t<result>").append(result).append("</result>\n");

            xmlBuilder.append("\t</board>\n");
        }
        return xmlBuilder.toString();
    }
    /**
     * Saves the board state to a file.
     * @param boardStateXml The XML string representation of the board state.
     * @param fileName The name of the file to save to.
     */
    private void saveBoardStateToFile(String boardStateXml, String fileName) {
        try {
            // Append closing tag to the XML content
            boardStateXml += "</test>";
            String currentDateAndTime = new SimpleDateFormat("ddMMyyyy_HHmm",
                    Locale.getDefault()).format(new Date());
            String newFileName = fileName + "_" + currentDateAndTime;
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, newFileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                outputStream.write(boardStateXml.getBytes());
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, newFileName + "XML");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/xml");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                outputStream.write(boardStateXml.getBytes());
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
