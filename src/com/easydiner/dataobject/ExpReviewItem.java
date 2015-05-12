package com.easydiner.dataobject;

import java.util.ArrayList;

public class ExpReviewItem {
	String image, name, deg;
	ArrayList<SubExpListItem> subList;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeg() {
		return deg;
	}
	public void setDeg(String deg) {
		this.deg = deg;
	}
	public ArrayList<SubExpListItem> getSubList() {
		return subList;
	}
	public void setSubList(ArrayList<SubExpListItem> subList) {
		this.subList = subList;
	}
	
}
