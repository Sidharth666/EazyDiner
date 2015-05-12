package com.easydiner.dataobject;

public class ListItem {

	private String image, itemname, itemtype, itemlocation, itemdistence, lat,
			lng, itemEazyDeals, newItemStatus;
	private int pinStatus;

	public String getItemEazyDeals() {
		return itemEazyDeals;
	}

	public void setItemEazyDeals(String itemEazyDeals) {
		this.itemEazyDeals = itemEazyDeals;
	}

	public String getNewItemStatus() {
		return newItemStatus;
	}

	public void setNewItemStatus(String newItemStatus) {
		this.newItemStatus = newItemStatus;
	}

	public int getPinStatus() {
		return pinStatus;
	}

	public void setPinStatus(int pinStatus) {
		this.pinStatus = pinStatus;
	}

	private double itemprice;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	Boolean reviewed;
	int itemTotalReviews, itemCritcsRating, itemUserRating, itemPrice, itemId;

	public int getItemTotalReviews() {
		return itemTotalReviews;
	}

	public void setItemTotalReviews(int itemTotalReviews) {
		this.itemTotalReviews = itemTotalReviews;
	}

	public int getItemCritcsRating() {
		return itemCritcsRating;
	}

	public void setItemCritcsRating(int itemCritcsRating) {
		this.itemCritcsRating = itemCritcsRating;
	}

	public int getItemUserRating() {
		return itemUserRating;
	}

	public void setItemUserRating(int itemUserRating) {
		this.itemUserRating = itemUserRating;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getItemlocation() {
		return itemlocation;
	}

	public void setItemlocation(String itemlocation) {
		this.itemlocation = itemlocation;
	}

	public double getItemprice() {
		return itemprice;
	}

	public void setItemprice(double itemprice) {
		this.itemprice = itemprice;
	}

	public String getItemdistence() {
		return itemdistence;
	}

	public void setItemdistence(String itemdistence) {
		this.itemdistence = itemdistence;
	}

	public Boolean getReviewed() {
		return reviewed;
	}

	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

}
