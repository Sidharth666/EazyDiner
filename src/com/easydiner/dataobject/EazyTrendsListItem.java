package com.easydiner.dataobject;

public class EazyTrendsListItem {
	private String listImg, listTitle, listBody, detailsImg, listDetails;
	private int listId, listCount;

	public int getListId() {
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public String getListImg() {
		return listImg;
	}

	public void setListImg(String listImg) {
		this.listImg = listImg;
	}

	public String getDetailsImg() {
		return detailsImg;
	}

	public void setDetailsImg(String detailsImg) {
		this.detailsImg = detailsImg;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}

	public String getListBody() {
		return listBody;
	}

	public void setListBody(String listBody) {
		this.listBody = listBody;
	}

	public String getListDetails() {
		return listDetails;
	}

	public void setListDetails(String listDetails) {
		this.listDetails = listDetails;
	}
}
