package com.xdd.coolreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bookmark.db";  
    private static final int DATABASE_VERSION = 1;  
      
    public DBHelper(Context context) {  
        //CursorFactory设置为null,使用默认�? 
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        System.out.println("New DBHelper!");
    }  
  
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("DB", "New DB!");
		System.out.println("New DB!");
		db.execSQL("CREATE TABLE IF NOT EXISTS markstable" +  
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, booktitle VARCHAR, date TEXT,markpage INTEGER,totalpage INTEGER)");  

	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE thing ADD COLUMN other STRING");  
	}

}
