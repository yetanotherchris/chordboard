package com.flatlandsapps.chordboard;

import java.util.ArrayList;
import android.graphics.Point;
import android.widget.Toast;

/*
 * Used by FretView to track the x,y positions of each row and column
 * on the fretboard.
 */
public class FretPositions
{
	private ArrayList<ArrayList<Point>> _pointsList;

	public FretPositions()
	{
		_pointsList = new ArrayList<ArrayList<Point>>();
	}

	public ArrayList<ArrayList<Point>> getPoints()
	{
		return _pointsList;
	}

	public Point getPoint(int row, int column)
	{
		try
		{
			ArrayList<Point> columns = _pointsList.get(row);
			return columns.get(column);
		}
		catch (Exception e)
		{
			return new Point();
		}
	}

	public void addRow(int row, int column, int x, int y)
	{
		// Check if the row exists first
		if (_pointsList.size() - 1 < row)
		{
			_pointsList.add(new ArrayList<Point>());
		}
		else if (_pointsList.get(row) == null)
		{
			_pointsList.set(row, new ArrayList<Point>());
		}

		ArrayList<Point> columns = _pointsList.get(row);

		if (columns.size() - 1 < column)
			columns.add(new Point(x, y));
		else
			columns.set(column, new Point(x, y));
	}
}
