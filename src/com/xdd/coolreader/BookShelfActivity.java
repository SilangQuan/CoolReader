package com.xdd.coolreader;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.artifex.mupdfdemo.MuPDFCore;

import com.xdd.coolreader.adapter.ShelfAdapter;
import com.xdd.coolreader.util.FileUtil;
import com.xdd.coolreader.util.ImageUtil;
import com.xdd.coolreader.view.MyGridView;

import com.xdd.coolreader.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class BookShelfActivity extends Activity {
	private MyGridView bookShelf;
	public static String TEMP_PATH;
	public static String APP_PATH;
	public static String CACHE_PATH;
	public static String BOOK_PATH;
	private ArrayList<String> dataList;
	private ShelfAdapter shelfAdapter;
	private ArrayList<String> bookNames;
	private ImageUtil iUtil;
	private Handler mHandler;
	public static final int FINISHED= 0x000001; 
	private Animation scaleAnim;
	private int clickedBook;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shelf_main);

		scaleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_alpha);
		scaleAnim.setAnimationListener(new animationListenerImpl());
		scaleAnim.setDuration(200);

		bookShelf = (MyGridView) findViewById(R.id.bookShelf);
		dataList = new ArrayList<String>();
		iUtil = new ImageUtil(this);


		CheckSD();
		initEvn();
		initHandler();
		initBooks();



		bookShelf.setAdapter(shelfAdapter);
		bookShelf.setOnItemClickListener(new OnItemClickListenerImpl());
		loadApps();   

		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {    
			bookShelf.setBgPic(R.drawable.body_bg_horizontal); 
			bookShelf.setNumColumns(6);

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {    
			bookShelf.setBgPic(R.drawable.body_bg_vertical);    
			bookShelf.setNumColumns(4);
		}    



	}

	private class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			clickedBook = arg2;
			arg1.startAnimation(scaleAnim);
			//Toast.makeText(getApplicationContext(), ""+arg2, Toast.LENGTH_SHORT).show();
		}

	}

	private class animationListenerImpl implements AnimationListener{

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse(BOOK_PATH+File.separator+bookNames.get(clickedBook));
			Intent intent=new Intent();
			intent.setClass(BookShelfActivity.this,MuPDFActivity.class); 
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(uri);
			startActivity(intent);
		}}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Sure to quit?")
			.setCancelable(false)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int id) {
					finish();
				}
			})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


	private void loadApps() {  
		Intent intent = new Intent(Intent.ACTION_MAIN, null);  
		intent.addCategory(Intent.CATEGORY_LAUNCHER);  
	}  

	@Override  
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
		//newConfig.orientation获得当前屏幕状态是横向或者竖向  
		//Configuration.ORIENTATION_PORTRAIT 表示竖向  
		//Configuration.ORIENTATION_LANDSCAPE 表示横屏  
		if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){  
			bookShelf.setBgPic(R.drawable.body_bg_vertical);
			bookShelf.setNumColumns(3);
			bookShelf.setAdapter(shelfAdapter);
		}  
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){  
			bookShelf.setBgPic(R.drawable.body_bg_horizontal);
			bookShelf.setNumColumns(6);
			bookShelf.setAdapter(shelfAdapter);
		}  
	}  

	//Check SDCard
	private void CheckSD()
	{
		if (!FileUtil.hasSdcard()) {
			alert(R.string.no_sdcard);
			return;
		}
	}
	//alert and exit
	public void alert(int msg)
	{
		new AlertDialog.Builder(this).setMessage(msg).setPositiveButton(R.string.yes, // 保存
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				BookShelfActivity.this.finish();
			}
		}).show();
	}

	//Do some initialization fo application.
	private void initEvn()
	{
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			APP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/coolreader";
		}else
		{
			APP_PATH = getFilesDir().getAbsolutePath() + "/coolreader";
		}
		TEMP_PATH = APP_PATH+"/tmp";
		CACHE_PATH = APP_PATH+"/cache";
		BOOK_PATH = APP_PATH+"/books";

		File temp = new File(APP_PATH);
		if(!temp.isDirectory() && !temp.mkdir())
		{
			alert(R.string.can_not_create_temp_path);
		}		
		FileUtil.unZip("configuration.zip", APP_PATH, this.getApplicationContext());
	}


	private void initBooks()
	{
		Log.i("init", "thumbimage");
		File bookCover;
		boolean hasNewbook = false;
		bookNames = FileUtil.getFilenames(BOOK_PATH);
		for(int i=0; i<bookNames.size(); i++)
		{
			dataList.add(FileUtil.getFileName(bookNames.get(i)));
			//Check the cover of book exit already or not. 		
			bookCover = new File(CACHE_PATH +File.separator+ FileUtil.getFileName(bookNames.get(i))+"_"+0+ ".png");
			if (!bookCover.exists()) {
				Log.i("file is not exist!", "thumbimage");
				hasNewbook = true;				
			}
		}

		shelfAdapter = new ShelfAdapter(dataList,this);

		if(hasNewbook)
		{
			Thread t = new Thread() {  

				public void run() {  
					Message msg = new Message();   
					msg.what = FINISHED; 
					for(int i=0; i<bookNames.size(); i++)
					{
						File bC;
						//Check the cover of book exit already or not. 		
						bC = new File(CACHE_PATH +File.separator+ FileUtil.getFileName(bookNames.get(i))+"_"+0+ ".png");
						if (!bC.exists()) {
							Log.i("file is not exist!", "thumbimage");
							iUtil.screenShotPDF(BOOK_PATH+ File.separator +bookNames.get(i), 0);
						}
					}
					mHandler.sendMessage(msg);
				}
			};
			t.start();
		}
		else shelfAdapter.enableCache();

	}

	private void initHandler()
	{
		mHandler =  new Handler()  
		{  
			@Override    
			public void handleMessage(Message msg) {   

				if (msg.what == FINISHED) {    
					Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
					shelfAdapter.enableCache();
					shelfAdapter.notifyDataSetChanged();
				}    
				super.handleMessage(msg);    
			}    
		};  
	}

}