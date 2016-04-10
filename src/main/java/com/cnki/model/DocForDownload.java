package com.cnki.model;

import com.yjh.util.JsonOperator;

import java.util.List;

public class DocForDownload {
	private String downURL = "";
	private String name = "";
	private String journal = "";
	private String[] author;
	private String[] author_corporation;
	private String date = "";
	private String institution;
	private String category;
	private List<Reference> refers;
	
	public DocForDownload() {
		super();
	}
	public DocForDownload(String downURL, String name, String journal,
			String[] author, String date) {
		super();
		this.downURL = downURL;
		this.name = name;
		this.journal = journal;
		this.author = author;
		this.date = date;
	}
	public DocForDownload(String name, String journal,
			String[] author, String date) {
		super();
		this.name = name;
		this.journal = journal;
		this.author = author;
		this.date = date;
	}
	
	public DocForDownload(String name, String journal, String[] author,
			String[] author_corporation, String date, String institution) {
		super();
		this.name = name;
		this.journal = journal;
		this.author = author;
		this.author_corporation = author_corporation;
		this.date = date;
		this.institution = institution;
	}
	public String getDownURL() {
		return downURL;
	}
	public void setDownURL(String downURL) {
		this.downURL = downURL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String[] getAuthor() {
		return author;
	}
	public void setAuthor(String[] author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String[] getAuthor_corporation() {
		return author_corporation;
	}
	public void setAuthor_corporation(String[] author_corporation) {
		this.author_corporation = author_corporation;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public List<Reference> getRefers() {
		return refers;
	}
	public void setRefers(List<Reference> refers) {
		this.refers = refers;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String toString() {
		String result = "";
		JsonOperator jsoner = new JsonOperator();
		try {
			result = jsoner.getJsonMapper().writeValueAsString(this) + ",";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
