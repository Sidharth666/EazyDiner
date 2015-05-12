package com.classes;

public interface AsyncTaskListener {
	
	public void onTaskCompleted(String result);
	public void onTaskPreExecute();
}
