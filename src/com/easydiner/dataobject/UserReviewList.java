package com.easydiner.dataobject;

public class UserReviewList {
	private String userImage, userName, reviewDate, userReviews, reviewSubject, address;
	private int userId, userRating, reviewOn;
	
	public String getReviewSubject() {
		return reviewSubject;
	}
	public void setReviewSubject(String reviewSubject) {
		this.reviewSubject = reviewSubject;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getUserReviews() {
		return userReviews;
	}
	public void setUserReviews(String userReviews) {
		this.userReviews = userReviews;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUserRating() {
		return userRating;
	}
	public void setUserRating(int userRating) {
		this.userRating = userRating;
	}
	public int getReviewOn() {
		return reviewOn;
	}
	public void setReviewOn(int reviewOn) {
		this.reviewOn = reviewOn;
	}
}
