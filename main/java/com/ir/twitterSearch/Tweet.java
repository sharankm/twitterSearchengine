package com.ir.twitterSearch;

public class Tweet {
	private String userName;
	private String status;
	private String id;
	private String time;
	private String name;
	private Integer rank;
	private String location;
	private String geolat;
	private String geolang;
	private String hashtags;
	private String followers;
	public String getFollowers() {
		return followers;
	}

	public void setFollowers(String followers) {
		this.followers = followers;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHashtags() {
		return hashtags;
	}

	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGeolat() {
		return geolat;
	}

	public void setGeolat(String geolat) {
		this.geolat = geolat;
	}

	public String getGeolang() {
		return geolang;
	}

	public void setGeolang(String geolang) {
		this.geolang = geolang;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String text) {
		this.status = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}
