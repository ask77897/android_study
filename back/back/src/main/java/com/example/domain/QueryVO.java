package com.example.domain;

public class QueryVO {
	private String key;
	private String word;
	private int page;
	private int size;
	private int start;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = (this.page-1) * this.size;
	}
	
	@Override
	public String toString() {
		return "QueryVO [key=" + key + ", word=" + word + ", page=" + page + ", size=" + size + ", start=" + start
				+ "]";
	}
}
