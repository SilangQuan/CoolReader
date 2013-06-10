package com.xdd.coolreader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;

import com.artifex.mupdfdemo.*;
import com.xdd.coolreader.BookShelfActivity;

public class ImageUtil {

	private MuPDFCore core;
	private String mFileName;
	private File orgImage;
	private File thumbImage;
	private Context mContext;
	public ImageUtil(Context c)
	{
		mContext = c;
	}
	public void savePDFThumbnail(File f, int page) {
		Bitmap orgBm;
		Bitmap thumbBm;
		String filename=FileUtil.getFileName(f.getName());
		//core = openFile(f.getPath());
		//core.gotoPage(0);

		if (core == null) {
			Log.i("saveThumbnail", "open file failed!");
			return;
		}

		Log.i("saveThumbnail", "pageNum:" + core.countPages());

		System.out.println("Count:"+core.countPages());
		PointF pageSize=core.getPageSize(0);
		System.out.println("HW:"+pageSize.x+pageSize.y);
		orgBm = Bitmap.createBitmap((int)pageSize.x,(int)pageSize.y, Bitmap.Config.ARGB_8888);
		core.gotoPage(page);
		core.drawPage(orgBm,(int)pageSize.x,(int)pageSize.y, 0, 0, (int)pageSize.x,(int)pageSize.y);

		orgImage = new File("/"+filename+".png");

		//保存原始图片
		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOut = null;

		try {
			fOut = new FileOutputStream(orgImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		orgBm.compress(Bitmap.CompressFormat.PNG, 100, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*thumbImage = new File(ChooseActivity.CACHE_PATH+"/"+filename+ "_thumb.png" );
		// 保存缩略�?		try {
			thumbImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOutT = null;

		try {
			fOutT = new FileOutputStream(thumbImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		thumbBm = decodeFile( orgImage );
		thumbBm.compress(Bitmap.CompressFormat.PNG, 100, fOutT);

		try {
			fOutT.flush();
			fOutT.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
	}

	public void screenShotPDF(String filepath,int page)
	{
		Bitmap orgBm;
		MuPDFCore core = openFile(filepath);
		String bookName = FileUtil.getFileNameByPath(filepath);
		if (core == null) {
			Log.i("screenShot", "Core is null!");
			return;
		}
		Log.i("saveThumbnail", "pageNum:" + core.countPages());

		System.out.println("Count:"+core.countPages());
		PointF pageSize=core.getPageSize(0);
		System.out.println("HW:"+pageSize.x+pageSize.y);
		orgBm = Bitmap.createBitmap((int)pageSize.x,(int)pageSize.y, Bitmap.Config.ARGB_8888);
		core.gotoPage(page);
		core.drawPage(orgBm,(int)pageSize.x,(int)pageSize.y, 0, 0, (int)pageSize.x,(int)pageSize.y);

		//Bitmap shadowBm = drawImageDropShadow(orgBm);
		//Bitmap shadowBm = toRoundCorner(orgBm,5);

		File orgImage = new File(BookShelfActivity.CACHE_PATH+File.separator+FileUtil.getFileName(bookName)+"_"+page+".png");

		//保存原始图片
		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOut = null;

		try {
			fOut = new FileOutputStream(orgImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		orgBm.compress(Bitmap.CompressFormat.PNG, 100, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	private MuPDFCore openFile(String path) {
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1 ? path
				: path.substring(lastSlashPos + 1));
		System.out.println("Trying to open " + path);
		try {
			core = new MuPDFCore(mContext,path);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	public static Bitmap drawImageDropShadow(Bitmap originalBitmap, int radius) {
		BlurMaskFilter blurFilter = new BlurMaskFilter(radius,
				BlurMaskFilter.Blur.NORMAL);
		Paint shadowPaint = new Paint();
		//shadowPaint.setColor(0xFFFFFF00);
		//shadowPaint.setAlpha(20);
		shadowPaint.setMaskFilter(blurFilter);
		int[] offsetXY = new int[2];
		Bitmap shadowBitmap = originalBitmap.extractAlpha(shadowPaint, offsetXY);
		Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
		Canvas c = new Canvas(shadowImage32);
		c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);
		return shadowImage32;
	}

	public static void screenShotPDF2(MuPDFCore core,String filename,int page)
	{
		Bitmap orgBm;
		if (core == null) {
			Log.i("screenShot", "Core is null!");
			return;
		}
		Log.i("saveThumbnail", "pageNum:" + core.countPages());

		System.out.println("Count:"+core.countPages());
		PointF pageSize=core.getPageSize(0);
		System.out.println("HW:"+pageSize.x+pageSize.y);
		orgBm = Bitmap.createBitmap((int)pageSize.x,(int)pageSize.y, Bitmap.Config.ARGB_8888);
		core.gotoPage(page);
		core.drawPage(orgBm,(int)pageSize.x,(int)pageSize.y, 0, 0, (int)pageSize.x,(int)pageSize.y);

		File orgImage = new File(BookShelfActivity.CACHE_PATH+File.separator+FileUtil.getFileName(filename)+"_"+page+".png");

		//保存原始图片
		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOut = null;

		try {
			fOut = new FileOutputStream(orgImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		orgBm.compress(Bitmap.CompressFormat.PNG, 100, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static float getColorR(int c)  
	{  
		int R = (c & 0x00FF0000 )>>16;  
		return (float) (R/255.0);  
	}  

	public static float getColorG(int c)  
	{  

		int G =(c & 0x0000FF00 )>>8;  
		return (float) (G/255.0);  
	}  

	public static float getColorB(int c)  
	{  

		int B = c & 0x000000FF;  
		return (float) (B/255.0);  
	}  

}
