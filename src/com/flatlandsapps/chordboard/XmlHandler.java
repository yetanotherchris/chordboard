package com.flatlandsapps.chordboard;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler
{
	// http://www.chordbook.com/guitarchords.php
	private String _currentValue;
	private Boolean _insideElement;
	private ArrayList<Chord> _chords;
	private Chord _currentChord;
	
	public XmlHandler()
	{
		_chords = new ArrayList<Chord>();
	}
	
	public ArrayList<Chord> getChords()
	{
		return _chords;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		_insideElement = true;
		
		if (localName.equalsIgnoreCase("chord"))
		{
			_currentChord = new Chord(attributes.getValue("name"));
			_chords.add(_currentChord);
		}
		else if (localName.equalsIgnoreCase("finger"))
		{
			int row = Integer.parseInt(attributes.getValue("row"));
			int column = Integer.parseInt(attributes.getValue("column"));
			int finger = Integer.parseInt(attributes.getValue("finger"));
			
			_currentChord.addFingerPosition(row, column, finger);
		}
		else if (localName.equalsIgnoreCase("barre"))
		{
			int row = Integer.parseInt(attributes.getValue("row"));
			int startColumn = Integer.parseInt(attributes.getValue("start"));
			int endColumn = Integer.parseInt(attributes.getValue("end"));
			int finger = Integer.parseInt(attributes.getValue("finger"));
			
			_currentChord.addBarreChord(row, startColumn, endColumn, finger);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		_insideElement = false;
		
		if (localName.equalsIgnoreCase("closedFinger"))
		{
			_currentChord.addClosedString(Integer.parseInt(_currentValue));
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		if (_insideElement)
		{
			_currentValue = new String(ch, start, length);
			_insideElement = false;
		}
	}
}

