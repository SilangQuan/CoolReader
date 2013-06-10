package com.xdd.coolreader.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xdd.coolreader.BookShelfActivity;
import com.xdd.coolreader.R;
import com.xdd.coolreader.util.FileUtil;
import com.xdd.coolreader.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShelfAdapter extends BaseAdapter{

	// 上下文
	private Context mContext;
	// 用来导入布局
	private boolean isCached;
	private LayoutInflater inflater = null;
	private List<Map<String, Object>> mData;
	// 填充数据的list
	private ArrayList<String> nameList;

	// 构造器
	public ShelfAdapter(ArrayList<String> list, Context context) {
		this.mContext = context;
		this.nameList = list;
		inflater = LayoutInflater.from(context);
		isCached = false;
		// 初始化数据
		//initDate();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		contentView = inflater.inflate(R.layout.book_item, null);

		TextView titleView = (TextView )contentView.findViewById(R.id.titleView);
		ImageView coverView = (ImageView)contentView.findViewById(R.id.coverView);
		if(nameList.size()>position)
		{
			if(isCached)
			{
				Bitmap bitmap;
				String imageUrl = BookShelfActivity.CACHE_PATH+File.separator+nameList.get(position)+"_"+0+ ".png";
				Log.e("book"+position,imageUrl);
				BitmapFactory.Options option = new BitmapFactory.Options();

				option.inSampleSize = 5;	
				bitmap = BitmapFactory.decodeFile(imageUrl, option);
				//Log.e("Height:", ""+bitmap.getHeight());
				//Draw shadow according to bitmap's size
				if(bitmap.getHeight()>300) coverView.setImageBitmap(ImageUtil.drawImageDropShadow(bitmap,3));
				else if (bitmap.getHeight()>200) coverView.setImageBitmap(ImageUtil.drawImageDropShadow(bitmap,2));
				else coverView.setImageBitmap(ImageUtil.drawImageDropShadow(bitmap,1));
				
				//titleView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
			else
			{
				titleView.setText(nameList.get(position));
				titleView.setBackgroundResource(R.drawable.general__book_cover_view__default_3);
					//contentView.setBackgroundResource(R.drawable.general__book_cover_view__default_02);
			}
		}
		return contentView;
	}
	
	public void enableCache()
	{
		isCached = true;
		Log.e("name","Cached!");
	}

}