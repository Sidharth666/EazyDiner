package com.easydiner.dataobject;

import java.util.ArrayList;

public class ExtCriticReviewListItem {
	
	int itemId, isOpened;
	String itemName, itemType, itemAddress, itemRating;
	public String getItemRating() {
		return itemRating;
	}
	public void setItemRating(String itemRating) {
		this.itemRating = itemRating;
	}
	ArrayList<CriticReviewItem> subList;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getIsOpened() {
		return isOpened;
	}
	public void setIsOpened(int isOpened) {
		this.isOpened = isOpened;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemAddress() {
		return itemAddress;
	}
	public void setItemAddress(String itemAddress) {
		this.itemAddress = itemAddress;
	}
	public ArrayList<CriticReviewItem> getSubList() {
		return subList;
	}
	public void setSubList(ArrayList<CriticReviewItem> subList) {
		this.subList = subList;
	}
}
