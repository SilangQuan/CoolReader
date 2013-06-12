package com.xdd.coolreader.util;

import java.io.File;  


import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.net.Uri;  

public class MailUtil {  



  public static void sendMail(Context c, String path)  
  {  
      File file = new File(path); //附件文件地址  

      Intent intent = new Intent(Intent.ACTION_SEND);  
      intent.putExtra("subject", file.getName()); //  
      intent.putExtra("body", "--Email from CoolReader"); //正文  
      intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); //添加附件，附件为file对象  
      if (file.getName().endsWith(".gz")) {  
          intent.setType("application/x-gzip"); //如果是gz使用gzip的mime  
      } else if (file.getName().endsWith(".txt")) {  
          intent.setType("text/plain"); //纯文本则用text/plain的mime  
      } else {  
          intent.setType("application/octet-stream"); //其他的均使用流当做二进制数据来发送  
      }  
      c.startActivity(intent); //调用系统的mail客户端进行发送}  
  }  
}