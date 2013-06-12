package com.xdd.coolreader.model;

public class BookMark{
	public int id;      
	public String bookTitle;
	public String date;
	public int markPage;
	public int totalPage;
	
	public BookMark() {    
	}    

	public BookMark(String title,int mpage,int tpage) {    
		this.bookTitle = title;    
		this.markPage = mpage;     
		this.totalPage = tpage;
	}  
}
