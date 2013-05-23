package com.xdd.coolreader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.util.Log;

import com.artifex.mupdfdemo.*;

public class ImageUtil {

	private MuPDFCore core;
	private String mFileName;
	private File orgImage;
	private File thumbImage;
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
	
public static void screenShotPDF(MuPDFCore core,String filename,int page)
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

		File orgImage = new File("/"+filename+"_"+page+".png");

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
		/*
	public static void screenShotPPT(SlideShow ppt,String filename,int page)
	{
		Bitmap orgBmp;
		Slide[] slide;
		slide = ppt.getSlides();
		final Dimension pgsize = ppt.getPageSize();
		final Handler handler = new Handler();

		orgBmp = Bitmap.createBitmap((int) pgsize.getWidth(),(int) pgsize.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(orgBmp);
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawPaint(paint);

		final Graphics2D graphics2d = new Graphics2D(canvas);

		final AtomicBoolean isCanceled = new AtomicBoolean(false);


		slide[page].draw(graphics2d, isCanceled, handler,page);

		File orgImage = new File(ChooseActivity.CACHE_PATH+"/"+filename+"_thumb.png");

		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(orgImage);
			orgBmp.compress(Bitmap.CompressFormat.PNG, 50, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void savePPTThumbnail(File f, int page)throws IOException {
		Bitmap orgBmp;
		Bitmap thumbBmp;
		Slide[] slide;
		SlideShow ppt;
		final long cur = System.currentTimeMillis();

		ppt = new SlideShow(new File(f.getPath()));
		final Handler handler = new Handler();
		final Dimension pgsize = ppt.getPageSize();
		System.out.println("PageHW:"+pgsize.getHeight()+pgsize.getWidth());
		//pgsize.
		slide = ppt.getSlides();
		//slide[0].draw(graphics, isCanceled, handler, position)
		int slideCount = slide.length;

		orgBmp = Bitmap.createBitmap((int) pgsize.getWidth(),(int) pgsize.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(orgBmp);
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawPaint(paint);

		final Graphics2D graphics2d = new Graphics2D(canvas);

		final AtomicBoolean isCanceled = new AtomicBoolean(false);


		Log.d("TIME", "new SlideShow: " + (System.currentTimeMillis() - cur));

		slide[page].draw(graphics2d, isCanceled, handler,page);

		
		Bitmap bm = Bitmap.createBitmap(320, 480, Config.ARGB_8888);  
        Canvas canvas = new Canvas(bm);  
        Paint p = new Paint();
        p.setColor(android.graphics.Color.WHITE);
        canvas.drawRect(50, 50, 200, 200, p);  
        canvas.save(Canvas.ALL_SAVE_FLAG );  
        canvas.restore();  
		String filename=FileUtil.getFileName(f.getName());
		File orgImage = new File(ChooseActivity.CACHE_PATH+"/"+filename+"_"+page+".png");

		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(orgImage);
			orgBmp.compress(Bitmap.CompressFormat.PNG, 50, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	private Bitmap decodeFile(File f) {
		try {
			// 修改图片尺寸
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, option);
			// 设置新的尺寸
			final int REQUIRED_HEIGHT =(int)core.pageHeight/15;
			final int REQUIRED_WIDTH = (int)core.pageWidth/15;
			int width_tmp = option.outWidth, height_tmp = option.outHeight;

			Log.w("thumbnail scale", (width_tmp + "  " + height_tmp));

			int scale = 10;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			Log.e("thumbnail", e.getMessage());
		}
		return null;
	}
*/

}
