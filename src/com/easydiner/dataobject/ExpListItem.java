package com.easydiner.dataobject;

import java.util.ArrayList;

public class ExpListItem {

	String mainItem, id;
	int openTagHome, openTagAlllist, openTagDetails, openTagEazyTrands;
	
	public int getOpenTagHome() {
		return openTagHome;
	}
	public void setOpenTagHome(int openTagHome) {
		this.openTagHome = openTagHome;
	}
	public int getOpenTagAlllist() {
		return openTagAlllist;
	}
	public void setOpenTagAlllist(int openTagAlllist) {
		this.openTagAlllist = openTagAlllist;
	}
	public int getOpenTagDetails() {
		return openTagDetails;
	}
	public void setOpenTagDetails(int openTagDetails) {
		this.openTagDetails = openTagDetails;
	}
	public int getOpenTagEazyTrands() {
		return openTagEazyTrands;
	}
	public void setOpenTagEazyTrands(int openTagEazyTrands) {
		this.openTagEazyTrands = openTagEazyTrands;
	}
	ArrayList<SubExpListItem> subList;
	
	public String getMainItem() {
		return mainItem;
	}
	public void setMainItem(String mainItem) {
		this.mainItem = mainItem;
	}
	public ArrayList<SubExpListItem> getArrayList() {
		return subList;
	}
	public void setArrayList(ArrayList<SubExpListItem> arrayList) {
		this.subList = arrayList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
