package com.easydiner.dataobject;

public class WishlistItem {
	private String listImage, listTitle;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getListImage() {
		return listImage;
	}

	public void setListImage(String listImage) {
		this.listImage = listImage;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}
}
