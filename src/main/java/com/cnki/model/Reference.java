package com.cnki.model;

import java.io.Serializable;
import java.util.Arrays;

public class Reference implements Serializable {
	private String citDocName = "";
	private int count = 1; //至少为1
	private String date;
	private String[] author;
	private String publisher;
	private String category;
	
	public Reference() {
	}
	public Reference(String citDocName, int count, String date,
			String[] author, String pulisher, String category) {
		super();
		this.citDocName = citDocName;
		this.count = count;
		this.date = date;
		this.author = author;
		this.publisher = pulisher;
		this.category = category;
	}
	public Reference(String filename, int count) {
		super();
		this.citDocName = filename;
		this.count = count;
	}
	public String getCitDocName() {
		return citDocName;
	}
	public void setCitDocName(String citDocName) {
		this.citDocName = citDocName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	//这个是故意少算一次，以排除参考文献中的索引号
	public Reference addCount() {
		++count;
		return this;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String[] getAuthor() {
		return author;
	}
	public void setAuthor(String[] author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Reference [citDocName=" + citDocName + ", count=" + count
				+ ", date=" + date + ", author=" + Arrays.toString(author)
				+ ", publisher=" + publisher + ", category=" + category + "]";
	}
	
}
