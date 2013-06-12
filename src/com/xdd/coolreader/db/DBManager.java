package com.xdd.coolreader.db;

import java.util.ArrayList;  
import java.util.Date;
import java.util.List;  

import com.xdd.coolreader.model.BookMark;


import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
  
public class DBManager {  
    private DBHelper helper;  
    private SQLiteDatabase db;  
    private String bookTitle;
      
    public DBManager(Context context,String title) {  
        helper = new DBHelper(context); 
        bookTitle = title;
        System.out.println("New DBManager!");
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    }  
      

    public void addOneBookmark(BookMark mark)
    {
    	db.execSQL("INSERT INTO markstable VALUES(null, ?, ?, ?, ?)", new Object[]{mark.bookTitle, new Date().toString(), mark.markPage, mark.totalPage});   	
    }
    public void delBookmark(int index)
    {
    	db.execSQL("DELETE FROM markstable where booktitle='"+bookTitle+"'"+" and markpage='"+index+"'");   	
    }

    public ArrayList<BookMark> query() {  
        ArrayList<BookMark> marks = new ArrayList<BookMark>();  
        Cursor c = queryTheCursor(); 
        if(c.moveToLast())
        while (!c.isBeforeFirst()) {  
        	BookMark tmpMark = new BookMark();  
        	tmpMark.id = c.getInt(c.getColumnIndex("id"));  
        	tmpMark.bookTitle = c.getString(c.getColumnIndex("booktitle"));  
        	//System.out.println("c.getColumnIndex(detail)"+c.getColumnIndex("detail"));
        	tmpMark.date = c.getString(c.getColumnIndex("date"));  
        	tmpMark.markPage = Integer.parseInt(c.getString(c.getColumnIndex("markpage"))) ; 
        	tmpMark.totalPage = Integer.parseInt(c.getString(c.getColumnIndex("totalpage"))) ;   	
        	marks.add(tmpMark);  
            c.moveToPrevious();
        }  
        c.close();  
        return marks;  
    }  

    public Cursor queryTheCursor() {  
    	String sqlString = "SELECT * FROM markstable where booktitle='"+this.bookTitle+"'";
    	System.out.println(sqlString);
        Cursor c = db.rawQuery(sqlString, null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
