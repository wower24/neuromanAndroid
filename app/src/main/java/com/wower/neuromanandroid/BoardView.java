package com.wower.neuromanandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BoardView extends View {
    private Scenario scenario;
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
        if(scenario != null) {
            drawBoard(scenario.getBoard().get(0), canvas);
        }
    }

    private void drawBoard(Board board, Canvas canvas) {
        for (Element element : board.getElement()) {
            for(ElementState state : element.getState()) {
                drawState(state, canvas);
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
        int y = state.getLocY();
        int width = state.getWidth();
        int height = state.getHeight();

        if (state.getSource().contains("rect")) {
            canvas.drawRect(x, y, x + width, y + height, paint);
        } else if (state.getSource().contains("circle")) {
            canvas.drawCircle(x + width / 2f, y + height / 2f, Math.min(width, height) / 2f, paint);
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

}
