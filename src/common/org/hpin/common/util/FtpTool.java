package org.hpin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
 
public class FtpTool {

	 private static FTPClient ftp;
	 
	 public static FTPClient ftp_conn(String server, String user, String password) {
	 
	  ftp = new FTPClient();
	  // ftp.setDefaultTimeout(5000);
	  try {
	   int reply;
	 
	   ftp.connect(server);
	   // ftp.connect(server,21,InetAddress.getLocalHost(),21);
	   System.out.println("Connected to " + server + ".");
	   System.out.println(ftp.getReplyString());
	 
	 
	   reply = ftp.getReplyCode();
	 
	   if (!FTPReply.isPositiveCompletion(reply)) {
	    ftp.disconnect();
	    System.out.println("FTP server refused connection.");
	    return null;
	   } else {
	    ftp.login(user, password);
	    System.out.println("Login success.");
	    ftp.pasv();
	    ftp.enterLocalPassiveMode();
	   }
	  } catch (SocketTimeoutException ste) {
	   ste.printStackTrace();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return ftp;
	 }
	 
	 /**
	  * @param ftp
	  * @param remoteFile
	  * @param localFile
	  * @return
	  * @throws FileNotFoundException
	  */
	 public static boolean uploadToFtp(FTPClient ftp, String remoteFile,
	   String localFile) throws FileNotFoundException {
	 
	  boolean result = false;
	  if (ftp == null) {
	   return result;
	  }
	 
	  String dir = "/";
	  remoteFile = remoteFile.replaceAll("\\\\", "/");
	  if (remoteFile.indexOf("/") != -1) {
	   dir = (String) remoteFile.subSequence(0, remoteFile.lastIndexOf("/"));
	  }
	 
	  FileInputStream fis = new FileInputStream(new File(localFile));
	 
	  System.out.println("Upload " + localFile + " To " + remoteFile);
	  try {
	   ftp.makeDirectory(dir);
	   ftp.changeWorkingDirectory(dir);
	   ftp.setFileType(FTP.BINARY_FILE_TYPE); // 以BINARY格式传送文件
	   if (ftp.storeFile(remoteFile, fis)) {
	    result = true;
	    //文件上传成功
	    System.out.println("文件为："+new String(remoteFile.getBytes("ISO-8859-1"), "GBK"));
	   }
	   // System.out.println(ftp.getReplyCode());
	   fis.close();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 
	  return result;
	 }
	 public static String gbkToIso(String para) {
		 try {
		 return new String(para.getBytes("GBK"), "ISO-8859-1");
		 } catch (UnsupportedEncodingException e) {
		 return "";
		 } catch (Exception e) {
		 return "";
		 }
		 }
	 public static void logout(FTPClient ftp) {
	  try {
	   if (ftp != null) {
	    ftp.logout();
	    ftp.disconnect();
	   }
	   ftp = null;
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	 }
	 
	}

 
