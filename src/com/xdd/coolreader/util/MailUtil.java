package com.xdd.coolreader.util;

import java.io.File;  


import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.net.Uri;  

public class MailUtil {  



  public static void sendMail(Context c, String path)  
  {  
      File file = new File(path); //�����ļ���ַ  

      Intent intent = new Intent(Intent.ACTION_SEND);  
      intent.putExtra("subject", file.getName()); //  
      intent.putExtra("body", "--Email from CoolReader"); //����  
      intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); //��Ӹ���������Ϊfile����  
      if (file.getName().endsWith(".gz")) {  
          intent.setType("application/x-gzip"); //�����gzʹ��gzip��mime  
      } else if (file.getName().endsWith(".txt")) {  
          intent.setType("text/plain"); //���ı�����text/plain��mime  
      } else {  
          intent.setType("application/octet-stream"); //�����ľ�ʹ������������������������  
      }  
      c.startActivity(intent); //����ϵͳ��mail�ͻ��˽��з���}  
  }  
}