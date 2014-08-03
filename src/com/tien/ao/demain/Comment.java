package com.tien.ao.demain;

public class Comment {
	
	private String commentid = "";
	private String secretid = "";
	private String showid = "";
	private String content = "";
	private long addtime;
	private int favorcount;
	
	
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getSecretid() {
		return secretid;
	}
	public void setSecretid(String secretid) {
		this.secretid = secretid;
	}
	public String getShowid() {
		return showid;
	}
	public void setShowid(String showid) {
		this.showid = showid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getAddtime() {
		return addtime;
	}
	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}
	public int getFavorcount() {
		return favorcount;
	}
	public void setFavorcount(int favorcount) {
		this.favorcount = favorcount;
	}
	
	

}
