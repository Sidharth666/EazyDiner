package com.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyFixedListView extends ListView{

	public MyFixedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
        catch (IllegalStateException e){
        	
        }
    }

}
