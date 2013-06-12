package com.xdd.coolreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xdd.coolreader.model.BookMark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BookmarkActivity extends Activity {

	private ListView myListView;  
	private SimpleAdapter myAdapter=null;
	private int[] markList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bookmark);
		myListView=(ListView)findViewById(R.id.listView);   
		myListView.setOnItemClickListener(new OnItemClickListenerImpl());  
		
		//Cope with intent
		Bundle b = this.getIntent().getBundleExtra("marksBunld");
		markList = b.getIntArray("pageNos");
		ArrayList<String> dateList =new ArrayList<String>();
		dateList = getIntent().getStringArrayListExtra("DateList");
		 
		ArrayList<Map<String, String>> mylist = new ArrayList<Map<String, String>>();    
		for (int i=0;i<markList.length;i++) {    
			HashMap<String, String> map = new HashMap<String, String>();    
			map.put("title","Page."+markList[i]);    
			map.put("content",dateList.get(i));    
			mylist.add(map);    
		}    
		myAdapter= new SimpleAdapter(this, //没什么解释      
				mylist,//数据来源       
				R.layout.bookmark_item,//ListItem的XML实现      

				//动态数组与ListItem对应的子项              
				new String[] {"title", "content"},       

				//ListItem的XML文件里面的两个TextView ID      
				new int[] {R.id.ItemMark,R.id.Itemdate});     
		//添加并且显示     

		myListView.setAdapter(myAdapter);   
	}
	private class OnItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//Transport Marked page no. back
			Intent i = new Intent();
			i.putExtra("PageNo", markList[arg2]);
			BookmarkActivity.this.setResult(RESULT_OK, i);
			BookmarkActivity.this.finish();
			//Toast.makeText(getApplicationContext(), "Click"+arg2, Toast.LENGTH_SHORT).show();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.bookmark, menu);
		return true;
	}

}
