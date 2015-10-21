package com.flatlandsapps.chordboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class FretView extends View
{
	private FretPositions _fretPositions;
	private Chord _chord;

	public FretView(Context context)
	{
		super(context);
		setDefaultChord();
	}
	
	public void setChord(Chord chord)
	{
		_chord = chord;
		this.invalidate();
	}
	
	private void setDefaultChord()
	{
		// D chord
		_chord = new Chord("D");
		
		// Three X's
		_chord.addClosedString(0);
		_chord.addClosedString(1);
		
		_chord.addFingerPosition(2, 3, 1);
		_chord.addFingerPosition(2, 5, 2);
		_chord.addFingerPosition(3, 4, 3);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		_fretPositions = new FretPositions();
		
		// Temporary code to get start X,Y of the view
		Rect rect = canvas.getClipBounds();
		
		// Add 25 padding so the X fingers get drawn
		int startX = rect.left + 25;
		int startY = rect.top + 25;
		
		// Remove 50 (25 each side) for the X fingers and circles
		int width = (rect.width() - 50) / 5;		
		int height = width;
		
		// Finger circles radius
		int radius = (int) ((width / 2) / 1.5); // make it slightly larger than quarter of the box
		int circleColor = 0xFFFFFFFF;
		int textColor = 0xFF000000;

		// Draw 5 horizontal blocks
		int top = startY;
		for (int row=0;row < 6;row++)
		{
			boolean paintVerticalLines = (row < 5); // 6th row is just a single line, but needs to register for chordPositions
			drawRow(canvas, startX, top, width, height,row,paintVerticalLines);
			top += height;
		}

		// Paint the finger positions
		_chord.setRadius(radius);
		_chord.setCircleColor(circleColor);
		_chord.setTextColor(textColor);
		_chord.paint(canvas,_fretPositions);

		invalidate();
	}

	private void drawRow(Canvas canvas, int left, int top, int width, int height,int row, boolean paintVerticalLines)
	{
		Paint paintLine = new Paint();
		paintLine.setStrokeWidth(1);
		paintLine.setColor(0xFFFFFFFF);

		int startLeft = left;
		int bottom = top + height +1;

		// Draw 5 vertical lines
		for (int column=0;column < 6;column++)
		{	
			if (paintVerticalLines)
			{
				canvas.drawRect(new Rect(left, top, left + 2, bottom), paintLine);
			}
			
			// The first row uses the top position for the X (closed string), the others need to sit on the fret rather than the string.
			// Add 1 to both top and left as the width/height of the lines is 2
			if (row == 0)
				_fretPositions.addRow(row, column, left+1, top +1);
			else
				_fretPositions.addRow(row, column, left+1, top - (height /2) +1);
			
			left += width;
		}

		// The bottom white line
		int right = startLeft + width *5;
		canvas.drawRect(new Rect(startLeft, top, right, top+2), paintLine);
	}
}
