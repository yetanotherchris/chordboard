package com.flatlandsapps.chordboard;

import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flatlandsapps.chordboard.R;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGestureListener
{
	private FretView _fretView;
	private HashMap<String,Chord> _chords;
	private String _rootName;
	private String _chordType;
	
	private ImageButton _buttonA;
	private ImageButton _buttonB;
	private ImageButton _buttonC;
	private ImageButton _buttonD;
	private ImageButton _buttonE;
	private ImageButton _buttonF;
	private ImageButton _buttonG;
	
	private ImageButton _buttonMajor;
	private ImageButton _buttonMinor;
	private ImageButton _buttonSeven;
	private ImageButton _buttonMajorSeven;
	private ImageButton _buttonMinorSeven;
	
	private GestureDetector _gestureScanner;
	
	public MainActivity()
	{
		_rootName = "D";
		_chordType = "Maj";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addChords();

		setContentView(R.layout.main);
		
		_gestureScanner = new GestureDetector(this);
		
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.freyBoardLayout);
		
		_fretView = new FretView(this);
		_fretView.setBackgroundColor(color.transparent);
		
        // Small screen : 240x320
        // Normal : 240x400, 320x480, 480x800, 480x854
        // Large : 480x800 (5"+ screens)
        // Tattoo: 240 x 320 pixels, 2.8 inches
		// - FretView takes half the screen 
		
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        
        if (height <= 320)
        {
        	_fretView.setLayoutParams(new LayoutParams(170,170));
        }
        else if (height > 320)
        {
        	height = (int) (height * 0.45);
        	_fretView.setLayoutParams(new LayoutParams(height,height));
        }
        
		layout.addView(_fretView);
		
		_buttonA = (ImageButton) this.findViewById(R.id.buttonA);
		_buttonB = (ImageButton) this.findViewById(R.id.buttonB);
		_buttonC = (ImageButton) this.findViewById(R.id.buttonC);
		_buttonD = (ImageButton) this.findViewById(R.id.buttonD);
		_buttonE = (ImageButton) this.findViewById(R.id.buttonE);
		_buttonF = (ImageButton) this.findViewById(R.id.buttonF);
		_buttonG = (ImageButton) this.findViewById(R.id.buttonG);
		
		_buttonMajor = (ImageButton) this.findViewById(R.id.buttonMajor);
		_buttonMinor = (ImageButton) this.findViewById(R.id.buttonMinor);
		_buttonSeven = (ImageButton) this.findViewById(R.id.buttonSeven);
		_buttonMajorSeven = (ImageButton) this.findViewById(R.id.buttonMajor7);
		_buttonMinorSeven = (ImageButton) this.findViewById(R.id.buttonMinor7);
		
		_buttonD.setBackgroundResource(R.drawable.d_selected);
		_buttonMajor.setBackgroundResource(R.drawable.major_selected);
	}
	
	private void addChords()
	{
		try
		{
			_chords = new HashMap<String,Chord>();
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			
			InputStream stream = this.getResources().openRawResource(R.raw.chords);
			
			XmlHandler handler = new XmlHandler();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(stream));
			
			ArrayList<Chord> chordsList = handler.getChords();
			for (int i=0;i < chordsList.size();i++)
			{
				Chord chord = chordsList.get(i);
				_chords.put(chord.getChordName().toLowerCase(), chord);
			}
			
			
		}
		catch (Exception e)
		{
			Toast toast = Toast.makeText(this,e.toString(), 3000);
			toast.show();
		}
		catch (Throwable t)
		{
			Toast toast = Toast.makeText(this,t.toString(), 3000);
			toast.show();
		}
	}

	public void buttonOnClick(View view)
	{	
		switch (view.getId())
		{
			/* ----- ROOTS ----- */
			case R.id.buttonA:
				resetChordButtons();
				_buttonA.setBackgroundResource(R.drawable.a_selected);
				_rootName = "A";
				break;
				
			case R.id.buttonB:
				resetChordButtons();
				_buttonB.setBackgroundResource(R.drawable.b_selected);
				_rootName = "B";
				break;
				
			case R.id.buttonC:
				resetChordButtons();
				_buttonC.setBackgroundResource(R.drawable.c_selected);
				_rootName = "C";
				break;
				
			case R.id.buttonD:
				resetChordButtons();
				_buttonD.setBackgroundResource(R.drawable.d_selected);
				_rootName = "D";
				break;
				
			case R.id.buttonE:
				resetChordButtons();
				_buttonE.setBackgroundResource(R.drawable.e_selected);
				_rootName = "E";
				break;
				
			case R.id.buttonF:
				resetChordButtons();
				_buttonF.setBackgroundResource(R.drawable.f_selected);
				_rootName = "F";
				break;
				
			case R.id.buttonG:
				resetChordButtons();
				_buttonG.setBackgroundResource(R.drawable.g_selected);
				_rootName = "G";
				break;
				
			/* ----- TYPES -----  */
			
			case R.id.buttonMajor:
				resetTypeButtons();
				_buttonMajor.setBackgroundResource(R.drawable.major_selected);
				_chordType = "Maj";
				break;
				
			case R.id.buttonMinor:
				resetTypeButtons();
				_buttonMinor.setBackgroundResource(R.drawable.minor_selected);
				_chordType = "min";
				break;
				
			case R.id.buttonSeven:
				resetTypeButtons();
				_buttonSeven.setBackgroundResource(R.drawable.seven_selected);
				_chordType = "7";
				break;
				
			case R.id.buttonMajor7:
				resetTypeButtons();
				_buttonMajorSeven.setBackgroundResource(R.drawable.majorseven_selected);
				_chordType = "maj7";
				break;	
				
			case R.id.buttonMinor7:
				resetTypeButtons();
				_buttonMinorSeven.setBackgroundResource(R.drawable.minorseven_selected);
				_chordType = "min7";
				break;
		}
		
		String chordName = String.format("%s%s",_rootName,_chordType);
		chordName = chordName.toLowerCase();
		
		if (_chords.containsKey(chordName))
		{
			TextView textView = (TextView) findViewById(R.id.textViewChordName);
			
			Chord chord = _chords.get(chordName);
			textView.setText(chord.getChordName());
			_fretView.setChord(chord);
		}
	}
	
	private void resetChordButtons()
	{
		_buttonA.setBackgroundResource(R.drawable.a);
		_buttonB.setBackgroundResource(R.drawable.b);
		_buttonC.setBackgroundResource(R.drawable.c);
		_buttonD.setBackgroundResource(R.drawable.d);
		_buttonE.setBackgroundResource(R.drawable.e);
		_buttonF.setBackgroundResource(R.drawable.f);
		_buttonG.setBackgroundResource(R.drawable.g);
	}
	
	private void resetTypeButtons()
	{
		_buttonMajor.setBackgroundResource(R.drawable.major);
		_buttonMinor.setBackgroundResource(R.drawable.minor);
		_buttonSeven.setBackgroundResource(R.drawable.seven);
		_buttonMajorSeven.setBackgroundResource(R.drawable.majorseven);
		_buttonMinorSeven.setBackgroundResource(R.drawable.minorseven);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return _gestureScanner.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		// right: if (distanceX > 0)
		// left : if (distanceX < 0)
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}
}