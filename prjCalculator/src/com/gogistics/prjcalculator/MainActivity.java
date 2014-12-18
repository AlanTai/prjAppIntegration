package com.gogistics.prjcalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	/* inner variables */
	float vNum1, vNum2, cOutPut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//
		initXMLComponents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// listeners
	OnTouchListener firstValueTouchListener = new OnTouchListener() {
		
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			vFirstValue.setText("");
			return false;
		}
	};
	
	OnTouchListener secondValueTouchListener = new OnTouchListener() {
		
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			vFirstValue.setText("");
			return false;
		}
	};
	
	/* XML components declaration */
	EditText vFirstValue;
	EditText vSecondValue;
	TextView vResult;
	Button vCalculate;
	
	private void initXMLComponents(){
		vFirstValue = (EditText) findViewById(R.id.editTxt01);
		vFirstValue.setText("0");
		vFirstValue.setOnTouchListener(firstValueTouchListener);
		
		vSecondValue = (EditText) findViewById(R.id.editTxt02);
		vSecondValue.setText("0");
		vSecondValue.setOnTouchListener(secondValueTouchListener);
	}
}
