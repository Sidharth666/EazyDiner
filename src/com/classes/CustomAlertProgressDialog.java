package com.classes;

import java.io.IOException;

import com.easydiner.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertProgressDialog extends ProgressDialog{
	
	private AlertDialog.Builder progressAlertDiagogBuilder;
	private AlertDialog dialog;
	private GifAnimationDrawable laoding;
	private TextView textProgressBar;
	private ImageView ivProgressBar;
	private static Context _context;
	private static String _displayText;
	private LayoutInflater inflater;
	private View progressView;
	private AnimationDrawable animation;
	

	public CustomAlertProgressDialog(Context context, int mytheme) {
		super(context,mytheme);
	}
	
	
	public static CustomAlertProgressDialog getCustomDialog(Context context,String displayText){
		
		_context = context;
		_displayText = displayText;
		CustomAlertProgressDialog customAlertProgressDialog = new CustomAlertProgressDialog(_context,R.style.MyTheme); 
		customAlertProgressDialog.setIndeterminate(true);
		customAlertProgressDialog.setCancelable(false);
		return customAlertProgressDialog;
		
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog_layout);
//		textProgressBar = (TextView) findViewById(R.id.textProgressBar);
		ivProgressBar = (ImageView) findViewById(R.id.ivProgressBar);
		setTextFont();
//		textProgressBar.setText(_displayText);
		ivProgressBar.setBackgroundResource(R.drawable.custom_animation);
		animation = (AnimationDrawable) ivProgressBar.getBackground();
		
	}
	
	@Override
	public void show() {
		super.show();
		animation.start();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		animation.stop();
	}
	
	
	
	

	/*public CustomAlertProgressDialog(Context context, String displayText) {
		try {
			this._context = context;
			this.displayText = displayText;
			progressAlertDiagogBuilder = new AlertDialog.Builder(this._context);
			
			inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			progressView = inflater.inflate(R.layout.progress_dialog_layout,null);
			textProgressBar = (TextView) progressView.findViewById(R.id.textProgressBar);
			ivProgressBar = (ImageView) progressView.findViewById(R.id.ivProgressBar);
			setTextFont();
			textProgressBar.setText(this.displayText);
			laoding = new GifAnimationDrawable(this._context.getResources().openRawResource(R.raw.loading));
			laoding.setOneShot(false);
			ivProgressBar.setImageDrawable(laoding);
			ivProgressBar.getDrawable().setVisible(true, true);
			progressAlertDiagogBuilder.setView(progressView);
			dialog = progressAlertDiagogBuilder.create();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public AlertDialog getDialog(){
		return dialog;
	}

	private void setTextFont() {
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(_context.getAssets(), fontPath2);

//		textProgressBar.setTypeface(tf1);
	}
}
