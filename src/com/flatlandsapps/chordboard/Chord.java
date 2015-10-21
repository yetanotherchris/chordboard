package com.flatlandsapps.chordboard;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class Chord 
{
	private FretPositions _positions;
	private String _chordName;
	private ArrayList<FingerInfo> _fingers;
	private ArrayList<Integer> _closedRows;
	private float _radius;
	private int _circleColor;
	private int _textColor;
	private int _closedStringColor;
	
	public Chord(String chordName)
	{
		_fingers = new ArrayList<FingerInfo>();
		_closedRows = new ArrayList<Integer>();
		_chordName = chordName;
		
		_radius = 10;
		_circleColor = 0xFFABABAB;
		_textColor = 0xFFFFFFFF;
		_closedStringColor = 0xFFFFFFFF;
	}

	public void addClosedRow(int row)
	{
		_closedRows.add(row);
	}
	
	public ArrayList<Integer> getClosedRows()
	{
		return _closedRows;
	}
	
	public void setCircleColor(int circleColor)
	{
		_circleColor = circleColor;
	}

	public int getCircleColor()
	{
		return _circleColor;
	}

	public void setTextColor(int circleTextColor)
	{
		_textColor = circleTextColor;
	}

	public int getTextColor() 
	{
		return _textColor;
	}
	
	public void setRadius(float radius)
	{
		_radius = radius;
	}

	public float getRadius()
	{
		return _radius;
	}
	
	public void addClosedString(int column)
	{
		_fingers.add(new FingerInfo(0,column,0));
	}

	public void addFingerPosition(int row,int column,int finger)
	{
		_fingers.add(new FingerInfo(row,column,finger));
	}
	
	public void addBarreChord(int row,int column,int endColumn,int finger)
	{
		FingerInfo fingerInfo = new FingerInfo(row,column,finger);
		fingerInfo.setIsBarreChord(true);
		fingerInfo.setBarreEndColumn(endColumn);
		
		_fingers.add(fingerInfo);
	}
	
	/*
	 * Changes the FingerInfos so their StartPoint and EndPoints contain valid values.
	 */
	public void calculateChordPoints()
	{
		for (int i=0;i < _fingers.size();i++)
		{
			FingerInfo info = _fingers.get(i);
			int row = info.getRow();
			info.setStartPoint(_positions.getPoint(row,info.getColumn()));
			
			if (info.isBarreChord())
			{
				info.setEndPoint(_positions.getPoint(row,info.getBarreEndColumn()));
			}
		}
	}
	
	public void setChordName(String chordName)
	{
		_chordName = chordName;
	}

	public String getChordName()
	{
		return _chordName;
	}
	
	public void setClosedStringColor(int closedStringColor)
	{
		_closedStringColor = closedStringColor;
	}

	public int getClosedStringColor()
	{
		return _closedStringColor;
	}
	
	public void paint(Canvas canvas,FretPositions positions)
	{
		_positions = positions;
		
		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setColor(getCircleColor());
		paint.setAntiAlias(true);
		
		Paint paintText = new Paint();
		paintText.setColor(getTextColor());
		paintText.setAntiAlias(true);
		paintText.setTextAlign(Align.CENTER);
		paintText.setTextSize(_radius);
		
		Paint paintClosed = new Paint();
		paintClosed.setColor(getClosedStringColor());
		paintClosed.setAntiAlias(true);
		paintClosed.setTextAlign(Align.CENTER);
		paintClosed.setTextSize(_radius);
		
		calculateChordPoints();
		for (int i=0;i < _fingers.size();i++)
		{
			FingerInfo currentFinger = _fingers.get(i);
			int finger = currentFinger.getFinger();
			String label = String.valueOf(finger);
			Point startPoint = currentFinger.getStartPoint();
			
			if (finger >0 && currentFinger.isBarreChord())
			{
				//
				// Draw barre fingers
				//
				Point endPoint = currentFinger.getEndPoint();
				
				canvas.drawCircle(startPoint.x, startPoint.y, _radius, paint);
				canvas.drawCircle(endPoint.x, endPoint.y, _radius, paint);
				
				// Draw the rectangle to join the two circles
				int left = startPoint.x;;
				int top = (int) (startPoint.y - _radius);
				int right = endPoint.x;
				int bottom = (int) (startPoint.y + _radius);
				Rect rect = new Rect(left,top,right,bottom);
				
				canvas.drawRect(rect, paint);
				
				canvas.drawText(label, (startPoint.x + endPoint.x) / 2, startPoint.y +(_radius /3), paintText);
			}
			else
			{
				//
				// Draw single finger
				//
				
				if (finger > 0)
				{			
					canvas.drawCircle(startPoint.x, startPoint.y, _radius, paint);
					canvas.drawText(label, startPoint.x, startPoint.y +(_radius /3), paintText);
				}
				else
				{		
					label = "X";
					paintClosed.setFakeBoldText(true);
					paintClosed.setTextSize(_radius + 4);
					canvas.drawText(label, startPoint.x, startPoint.y +(_radius /3), paintClosed);
				}
			}
		}
	}

	private class FingerInfo
	{
		private int _row;
		private int _column;
		private int _barreEndColumn;
		private int _finger;
		private boolean _isBarreChord;
		private Point _startPoint;
		private Point _endPoint;
		
		public FingerInfo(int row,int column,int finger)
		{
			setRow(row);
			setColumn(column);
			setFinger(finger);
			setIsBarreChord(false);
			_startPoint = new Point();
			_endPoint = new Point();
		}
		
		public void setIsBarreChord(boolean isBarreChord)
		{
			_isBarreChord = isBarreChord;
		}

		public boolean isBarreChord()
		{
			return _isBarreChord;
		}

		public int getFinger() 
		{
			return _finger;
		}

		public int getColumn()
		{
			return _column;
		}
		
		public int getRow()
		{
			return _row;
		}
		
		public int getBarreEndColumn()
		{
			return _barreEndColumn;
		}
		
		public Point getStartPoint()
		{
			return _startPoint;
		}
		
		public Point getEndPoint()
		{
			return _endPoint;
		}
		
		public void setStartPoint(Point point)
		{
			_startPoint = point;
		}
		
		public void setEndPoint(Point point)
		{
			_endPoint = point;
		}
		
		public void setFinger(int finger)
		{
			_finger = finger;
		}
		
		public void setColumn(int column)
		{
			_column = column;
		}
		
		public void setBarreEndColumn(int column)
		{
			_barreEndColumn = column;
		}

		public void setRow(int row)
		{
			_row = row;
		}
	}
}
