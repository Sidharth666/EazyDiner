package com.easydiner.dataobject;

import android.view.View;

public class GalleryImageList {
	private String imageAbsPath;
	private int selectStatus;
	private View itemView;

	public View getItemView() {
		return itemView;
	}

	public void setItemView(View itemView) {
		this.itemView = itemView;
	}

	public int getSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(int selectStatus) {
		this.selectStatus = selectStatus;
	}

	public String getImageAbsPath() {
		return imageAbsPath;
	}

	public void setImageAbsPath(String imageAbsPath) {
		this.imageAbsPath = imageAbsPath;
	}
}
