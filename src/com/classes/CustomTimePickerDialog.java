package com.classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class CustomTimePickerDialog extends TimePickerDialog {

	private final static int TIME_PICKER_INTERVAL = 30;
	private TimePicker timePicker;
	private OnTimeSetListener callback;
	private int minHour = -1, minMinute = -1, maxHour = 100, maxMinute = 100;
	private int currentHour, currentMinute;
	
	public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		this.callback = callBack;
		
	}
	
	public TimePicker getTimePicker() {
		return timePicker;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (callback != null && timePicker != null) {
			timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(),timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
		}
	}
	
	@Override
	protected void onStop() {
	}
	
	@SuppressLint("NewApi") @Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		try { 
			Class<?> classForid = Class.forName("com.android.internal.R$id");
	        Field timePickerField = classForid.getField("timePicker");
	        this.timePicker = (TimePicker) findViewById(timePickerField.getInt(null));
	        Field field = classForid.getField("minute");
	
	        NumberPicker mMinuteSpinner = (NumberPicker) timePicker.findViewById(field.getInt(null));
	        mMinuteSpinner.setMinValue(0);
	        mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
	        List<String> displayedValues = new ArrayList<String>();
	        for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
	            displayedValues.add(String.format("%02d", i));
	        } 
	        mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
		} catch (Exception e) {
            e.printStackTrace();
        }      
	}
	public void setMin(int hour, int minute) {
        minHour = hour;
        minMinute = minute;
    }

    public void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		super.onTimeChanged(view, hourOfDay, minute);
		boolean validTime;
        if(hourOfDay < minHour) {
            validTime = false;
        }
        else if(hourOfDay == minHour) {
            validTime = minute >= minMinute;
        }
        else if(hourOfDay == maxHour) {
            validTime = minute <= maxMinute;
        }
        else {
            validTime = true;
        }

        if(validTime) {
            currentHour = hourOfDay;
            currentMinute = minute;
        }
        else {
            updateTime(currentHour, currentMinute);
        }
    }
}

