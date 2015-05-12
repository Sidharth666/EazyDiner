package com.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.easydiner.R;

public class AlertDialogManager {
	@SuppressWarnings("deprecation")
	
	public void showAlertDialog(Context context, String title, String message,
            Boolean status){
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		
		alertDialog.setTitle(title);
		
		alertDialog.setMessage(message);
		
		if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.three_star : R.drawable.two_star);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        
        // Showing Alert Message
        alertDialog.show();
	}
}
