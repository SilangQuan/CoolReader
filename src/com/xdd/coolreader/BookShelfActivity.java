package com.xdd.coolreader;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xdd.coolreader.adapter.ShelfAdapter;
import com.xdd.coolreader.util.FileUtil;
import com.xdd.coolreader.view.MyGridView;

import com.xdd.coolreader.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
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
    private ArrayList<String> dataList;
    private ShelfAdapter shelfAdapter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_alpha);
        anim.setAnimationListener(new animationListenerImpl());
		anim.setDuration(200);
        bookShelf = (MyGridView) findViewById(R.id.bookShelf);
        
        dataList = new ArrayList<String>();
        for(int i=0;i<50;i++)
        {
        	dataList.add("Hello");
        }
        shelfAdapter=new ShelfAdapter(dataList,this);
        bookShelf.setAdapter(shelfAdapter);
        bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				arg1.startAnimation(anim);
				

				   Toast.makeText(getApplicationContext(), ""+arg2, Toast.LENGTH_SHORT).show();
				
			}
		});
        loadApps();   
        
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {    
        	bookShelf.setBgPic(R.drawable.body_bg_horizontal); 
        	bookShelf.setNumColumns(6);
        	
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {    
        	bookShelf.setBgPic(R.drawable.body_bg_vertical);    
        	bookShelf.setNumColumns(4);
        }    
		CheckSD();
		initEvn();
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
			
		}}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("你确定退出吗？")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							})
					.setNegativeButton("返回",
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
			TEMP_PATH=APP_PATH+"/tmp";
			CACHE_PATH=APP_PATH+"/cache";

			File temp = new File(APP_PATH);
			if(!temp.isDirectory() && !temp.mkdir())
			{
				alert(R.string.can_not_create_temp_path);
			}		
			FileUtil.unZip("configuration.zip", APP_PATH, this.getApplicationContext());
		}

}