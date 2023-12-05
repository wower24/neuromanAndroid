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

import java.util.List;

public class BoardView extends View {
    private Scenario scenario;
    private float scaleX;
    private float scaleY;
    boolean isScaled = false;
    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isScaled) {
            calculateScalingFactors(scenario.getBoard().get(0).getElement().get(0));
            isScaled = true;
        }
        if(scenario != null) {
            drawBoard(scenario.getCurrentBoard(), canvas);
        }
    }

    private void drawBoard(Board board, Canvas canvas) {
        for (Element element : board.getElement()) {
            if ("frame".equals(element.getElementID())) {
                // Handle the frame element
                // If the frame does not change or does not need special handling, you can skip it
                // For example, just draw its first state:
                drawState(element.getState().get(0), canvas);
            } else {
                // For other elements, use the existing logic
                List<ElementState> states = element.getState();
                if (element.getCurrentStateIndex() < states.size()) {
                    ElementState state = states.get(element.getCurrentStateIndex());
                    drawState(state, canvas);
                } else {
                    Log.e("BoardView", "Invalid state index for element: " + element.getElementID());
                }
            }
        }
    }

    private void drawState(ElementState state, Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        if(state.getFgcolor() != null) {
            paint.setColor(parseColorString(state.getFgcolor()));
        }
        else {
            paint.setColor(Color.parseColor("#FFFFFF"));
        }

        int x = state.getLocX();
        int y =state.getLocY();
        int width = state.getWidth();
        int height = state.getHeight();

        if (state.getSource() != null && state.getSource().startsWith("img_")) {
            x = (int) (x * scaleX);
            y = (int) (y * scaleX);
            width = (int) (width * scaleX);
            height = (int) (height * scaleX);
            String filename = state.getSource();
            Log.d("Drawable Name", "Resource name: " + filename);
            int resId = getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
            Log.d("Drawable ID", "Resource ID: " + resId);
            Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
            if (drawable != null) {
                drawable.setBounds(x, y, x + width, y + height);
                drawable.draw(canvas);
            }
        } else {
            x = (int) (x * scaleX);
            y = (int) (y * scaleY);
            width = (int) (width * scaleX);
            height = (int) (height * scaleY);

            if (state.getSource().contains("rect")) {
            canvas.drawRect(x, y, x + width, y + height, paint);
            } else if (state.getSource().contains("circle")) {
            canvas.drawCircle(x + width / 2f, y + height / 2f, Math.min(width, height) / 2f, paint);
            }
        }
        //update state parameters for other functions to use
        state.setLocX(x);
        state.setLocY(y);
        state.setWidth(width);
        state.setHeight(height);
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
            for (Element element : board.getElement()) {
                ElementState state = element.getState().get(element.getCurrentStateIndex());

                if(scenario.getName().equals("test9p")) {
                    if (!element.getElementID().equals("frame")) {
                        if (isInsideElement(x, y, state)) {
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
            }
        }
        return true;
    }

    private void checkCompletion() {
        for (Element element : scenario.getCurrentBoard().getElement()) {
            // Skip the 'frame' element
            if ("frame".equals(element.getElementID())) {
                continue;
            }

            if (!element.isStateToggled()) { // Check if the state has been toggled for non-frame elements
                return; // At least one element (other than frame) is still in its initial state or not toggled
            }
        }
        Context context = getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private boolean isInsideElement(int x, int y, ElementState state) {
        // Apply scaling to the element's dimensions and position
        int scaledX = state.getLocX();
        int scaledY = state.getLocY();
        int scaledWidth = state.getWidth();
        int scaledHeight = state.getHeight();

        //for circle - test9p
        int centerX = scaledX + scaledWidth / 2;
        int centerY = scaledY + scaledHeight / 2;
        int radius = Math.min(scaledWidth, scaledHeight) / 2;
        return Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)) <= radius;

        //TODO: prepare isInsideElements for pictures
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
        if(scenario.currentBoardIndex < scenario.getBoard().size() - 1) {
            scenario.currentBoardIndex++;
            invalidate();
        }
    }

}
