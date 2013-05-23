/**
 *   920 Text Editor is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   920 Text Editor is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with 920 Text Editor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdd.coolreader.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;

public class FileUtil
{
	/***
	 * The number of bytes in a kilobyte.
	 */
	public static final double ONE_KB = 1024.0;

	/***
	 * The number of bytes in a megabyte.
	 */
	public static final double ONE_MB = ONE_KB * ONE_KB;

	/***
	 * The number of bytes in a gigabyte.
	 */
	public static final double ONE_GB = ONE_KB * ONE_MB;

	/***
	 * Returns a human-readable version of the file size, where the input
	 * represents a specific number of bytes.
	 * 
	 * @param size
	 *            the number of bytes
	 * @return a human-readable display value (includes units)
	 */
	public static String byteCountToDisplaySize(long size)
	{
		return byteCountToDisplaySize((double) size);
	}

	public static String byteCountToDisplaySize(double size)
	{
		String displaySize;
		double ret;

		if((ret = size / ONE_GB) > 1.0)
		{
			displaySize = " G";
		}else if((ret = size / ONE_MB) > 1.0)
		{
			displaySize = " M";
		}else if((ret = size / ONE_KB) > 1.0)
		{
			displaySize = " KB";
		}else
		{
			ret = size;
			displaySize = " B";
		}

		DecimalFormat df = new DecimalFormat("0.00");

		return df.format(ret) + displaySize;
	}

	public static String ReadFile(String filename)
	{
		return ReadFile(filename, "UTF-8");
	}

	public static String Read(String filename, String encoding)
	{
		return Read(new File(filename), encoding);
	}

	public static String Read(File file, String encoding)
	{

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));

			// Create an array of characters the size of the file
			char[] allChars = new char[(int) file.length()];

			// Read the characters into the allChars array
			in.read(allChars, 0, (int) file.length());
			in.close();

			// Convert to a string
			String allCharsString = new String(allChars);
			return allCharsString;
		}catch (IOException ex)
		{
			throw new RuntimeException(file + ": trouble reading", ex);
		}

	}

	/**
	 * 读取整个文件, android默认编码为utf-8,如果文件编码是gbk或其它编�?要是没有指定正确的编�?就会统一当成ut-8编码处理
	 * 
	 * @param filename
	 *            文件�?	 * @param encoding
	 *            指定文件编码,否则使用系统默认的编�?	 * @return
	 */
	public static String ReadFile(String filename, String encoding)
	{
		return ReadFile(new File(filename), encoding);
	}

	public static String ReadFile(File filename, String encoding)
	{
		try
		{
			FileInputStream fis = new FileInputStream(filename);
			return ReadFile(fis, encoding);
		}catch (FileNotFoundException e)
		{
			return "";
		}
	}

	public static String ReadFile(InputStream fis, String encoding)
	{
		BufferedReader br;
		StringBuilder b = new StringBuilder();
		String line;
		String sp = System.getProperty("line.separator");

		try
		{
			br = new BufferedReader(new InputStreamReader(fis, encoding));
			try
			{
				while ((line = br.readLine()) != null)
				{
					b.append(line).append(sp);
				}
				br.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return b.toString();
	}

	public static void writeFile(String path, String text) throws IOException
	{
		writeFile(path, text, "UTF-8", true);
	}

	/**
	 * 写入文件, �?��指定编码
	 * 
	 * @param path
	 * @param text
	 * @param encoding
	 * @return
	 * @throws IOException 
	 */
	public static boolean writeFile(String path, String text, String encoding, boolean isRoot) throws IOException
	{
		File file = new File(path);
		String fileString = path;
		boolean root = false;

		BufferedWriter bw = null;
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileString),encoding));
		bw.write(text);
		bw.close();
		return true;
	}
	public static boolean saveToSDCard(String filename, String text)
	{
		System.out.println("fUCK");
		System.out.println(filename);
		return true;
	}
	public static String getExt(String path)
	{
		int lastIndex = path.lastIndexOf(".");
		if(lastIndex == -1)
			return null;
		return path.substring(lastIndex + 1).trim().toLowerCase();
	}


	public static ArrayList<File> getFileList(String path, boolean runAtRoot)
	{
		ArrayList<File> fileList = new ArrayList<File>();
		ArrayList<File> folderList = new ArrayList<File>();
		if(runAtRoot == false)
		{
			File base = new File(path);
			File[] files = base.listFiles();
			if(files == null)
				return null;
			for(File file: files)
			{
				if(file.isDirectory())
				{
					folderList.add(file);
				} else {
					fileList.add(file);
				}
			}
		}else{
			/** �?root */
		}
		Comparator<File> mComparator = new Comparator<File>() {
			public int compare(File fl1, File fl2)
			{
				return fl1.getName().compareToIgnoreCase(fl2.getName());
			}
		};
		//排序
		Collections.sort(fileList, mComparator);
		Collections.sort(folderList, mComparator);

		ArrayList<File> list = new ArrayList<File>();
		for(File f:folderList)
			list.add(f);
		for(File f:fileList)
			list.add(f);

		fileList = null;
		folderList = null;

		return list;
	}
	//delete a folder
	public static void deleteAllFiles(String path) {
		System.out.println("delete");
		File root= new File(path);
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				// if path is a folder,delete all files in it first.
				if (f.isDirectory()) {
					try {
						f.delete();
						System.out.println("delete");
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否�?��（未被使用）

						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
		//delete folder
		root.delete();
	}
	//递归求取目录文件个数
	public static long getFileNum(String path){
		long size = 0;
		File file = new File(path);
		File flist[] = file.listFiles();
		size=flist.length;
		return size;
	}

	//判断SD卡是否存�?	
	public static boolean hasSdcard() {
	String status = Environment.getExternalStorageState();
	if (status.equals(Environment.MEDIA_MOUNTED)) {
		return true;
	} else {

		return false;
	}
}

//创建目录
public static void createPath(String path) {
	File file = new File(path);
	if (!file.exists()) {
		file.mkdir();
	}
}

//Unzip File from assert
public static boolean unZip(String zipName,String path,Context c)
{
	try
	{
		InputStream is =c.getAssets().open(zipName);
		ZipInputStream zin = new ZipInputStream(is);
		ZipEntry ze = null;
		String name;
		File file;
		while ((ze = zin.getNextEntry()) != null)
		{
			name = ze.getName();
			// Log.v("Decompress", "Unzipping " + name);

			if(ze.isDirectory())
			{
				file = new File(path+ File.separator + name);
				if(!file.exists())
				{
					if(!file.mkdir())
					{
						return false;
					}
				}
			}else
			{
				FileOutputStream fout = new FileOutputStream(path+ File.separator + name);
				byte[] buf = new byte[1024 * 4];
				int len;
				while ((len = zin.read(buf)) > 0)
				{
					fout.write(buf, 0, len);
				}
				buf = null;
				zin.closeEntry();
				fout.close();
			}
		}
		zin.close();
	}catch (Exception e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
//Get filename without ext.
public  static String getFileName(String file)
{
	int lastIndex =file.lastIndexOf(".");
	if(lastIndex == -1)
		return null;
	return file.substring(0,lastIndex).trim().toLowerCase();
}
public  static String getFileNameByPath(String path)
{
	int lastIndex =path.lastIndexOf("/");
	if(lastIndex == -1)
		return null;
	return path.substring(lastIndex+1,path.length()).trim();
}

public static File[] getDocs(File f)
{
	File[] tmpFiles=f.listFiles(new FilenameFilter() {
		public boolean accept(File file, String name) {
			if (name.toLowerCase().endsWith(".pdf"))
				return true;
			if (name.toLowerCase().endsWith(".xps"))
				return true;
			if (name.toLowerCase().endsWith(".cbz"))
				return true;
			if (name.toLowerCase().endsWith(".ppt"))
				return true;
			if (name.toLowerCase().endsWith(".xls"))
				return true;
			if (name.toLowerCase().endsWith(".doc"))
				return true;
			if (name.toLowerCase().endsWith(".png"))
				return true;
			return false;
		}
	});
	return tmpFiles;
}


}
