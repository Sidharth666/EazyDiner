package com.easydiner.dataobject;

import java.util.ArrayList;

public class FilterGroupItem {
	private String groupItemName;
	private ArrayList<FilterChildItem> childItems;
	private int openedList;

	public int getOpenedList() {
		return openedList;
	}

	public void setOpenedList(int openedList) {
		this.openedList = openedList;
	}

	public ArrayList<FilterChildItem> getChildItems() {
		return childItems;
	}

	public void setChildItems(ArrayList<FilterChildItem> childItems) {
		this.childItems = childItems;
	}

	public String getGroupItemName() {
		return groupItemName;
	}

	public void setGroupItemName(String groupItemName) {
		this.groupItemName = groupItemName;
	}
}
