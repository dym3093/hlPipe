package org.hpin.common.util;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
 
  
 
public class FtpTest {
 
 /**
  * @param args
  */
 public static void main(String[] args) {
 
  
  String ftp_server = "192.168.1.108";
  String ftp_user = "ruitaihl";
  String ftp_password ="ruitaihl";
  FTPClient ftp = FtpTool.ftp_conn(ftp_server, ftp_user, ftp_password);
   
  String localFilename="D:/projectv1/20150407/20150407_盐城国寿私人医生服务卡_新增(承保).xls";
   String localF=localFilename.substring(12,localFilename.length());
   
  String remoteFilename="/ruitai/20150407/20150407_盐城国寿私人医生服务卡_新增(承保).xls";
  remoteFilename="/ruitai"+localF;
  remoteFilename=new FtpTool().gbkToIso(remoteFilename);
   
   
     System.out.println("upload ...");
     System.out.println(localFilename + " to " + remoteFilename);
     try {
      FtpTool.uploadToFtp(ftp, remoteFilename, localFilename);
  } catch (FileNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
     
  try {
   ftp.logout();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  if (ftp.isConnected()) {
   try {
    ftp.disconnect();
   } catch (IOException ioe) {
    ioe.printStackTrace();
   }
  }
   
   
 
 }
 
}
