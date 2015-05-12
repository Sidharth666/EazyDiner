package com.easydiner.dataobject;

import java.util.ArrayList;

public class ReviewsDashboardItem {
	private String itemImage, itemTitle;
	private int itemId, openTagHome;

	public int getOpenTagHome() {
		return openTagHome;
	}

	public void setOpenTagHome(int openTagHome) {
		this.openTagHome = openTagHome;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	private ArrayList<ReviewsDashboardChildItem> childList;

	public ArrayList<ReviewsDashboardChildItem> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<ReviewsDashboardChildItem> childList) {
		this.childList = childList;
	}

	public String getItemImage() {
		return itemImage;
	}

	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
}