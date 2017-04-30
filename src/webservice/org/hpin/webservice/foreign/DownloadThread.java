/**
 * @author DengYouming
 * @since 2016-8-15 下午3:14:58
 */
package org.hpin.webservice.foreign;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.webservice.util.HttpUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 下载文件线程
 * @author DengYouming
 * @since 2016-8-15 下午3:14:58
 */
public class DownloadThread implements Callable<Map<String,String>> {
	
	static Logger log = Logger.getLogger("DownloadThread");
	
	//下载地址
	private String url;
	//文件名
	private String fileName;
	//保存的路径
	private String savePath;
	
	public DownloadThread(String url, String fileName, String savePath) {
		this.url = url;
		this.fileName = fileName;
		this.savePath = savePath;
	}

	@Override
	public Map<String, String> call() throws IOException {
		Map<String,String> result = new HashMap<String, String>();
		try {
			result = downLoadFromUrl(url, fileName, savePath);
			log.info("返回的filePath: "+result.get(fileName));
		} catch (URISyntaxException e) {
			log.info(e);
		}
		return result;
	}
		
	/**
	 * 保存网络url的文件
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @return 保存文件的地址
	 * @throws IOException
	 * @author DengYouming
	 * @throws URISyntaxException 
	 * @since 2016-8-16 上午9:55:31
	 */
	private static Map<String,String> downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException, URISyntaxException{  
		Map<String,String> result = null;
		Logger logger = Logger.getLogger("downLoadFromUrl");
		
		logger.info("url: "+urlStr);
		logger.info("fileName: "+fileName);
		logger.info("savePath: "+savePath);

        URL url;
        String scheme;
        HttpURLConnection conn = null;
        HttpsURLConnection conns = null;
        int code = 0;
		//返回的提示信息
		String msg = null;

		try {
			url = new URL(urlStr);
			scheme = url.toURI().getScheme();
			result = new HashMap<String, String>();

			//HTTP链接
			if("http".equalsIgnoreCase(scheme)){
				conn = (HttpURLConnection)url.openConnection();
				//设置超时间为60秒  
		        conn.setConnectTimeout(2*60*1000);  
		        //设置服务器没有返回数据的超时
		        conn.setReadTimeout(2*60*1000);
				code = conn.getResponseCode();
				logger.info("HTTP code: "+code);
			}
			// HTTPS链接
			if("https".equalsIgnoreCase(scheme)){
				conns = (HttpsURLConnection)url.openConnection();
				//设置超时间为60秒  
				conns.setConnectTimeout(2*60*1000);
				conns.setReadTimeout(2*60*100);
				//KEY认证
				conns.setHostnameVerifier(new MyHostnameVerifier());
				code = conns.getResponseCode();
				logger.info("HTTPS code: "+code);
			}
			//根据返回码判定是否可以下载
			if(HttpURLConnection.HTTP_OK==code){
				if("http".equalsIgnoreCase(scheme)){
					result = dealDownload(conn, fileName, savePath);
				}
				if("https".equalsIgnoreCase(scheme)){
					result = dealDownload(conns, fileName, savePath);
				}
			}else{
				msg = "无法下载，状态码["+code+"]， 会员条码["+fileName.substring(0, fileName.indexOf("."))+"],报告地址["+urlStr+"]";
				log.info("无法下载，状态码["+code+"]， 会员条码["+fileName.substring(0, fileName.indexOf("."))+"],报告地址["+urlStr+"]");
				logger.info("无法下载，状态码["+code+"]，  会员条码["+fileName.substring(0, fileName.indexOf("."))+"],报告地址["+urlStr+"]");
				result = new HashMap<String, String>();
				result.put(HttpUtils.CODE, ""+code);
			}

		} catch (MalformedURLException e1) {
			code = 500;
			msg = "无法预览的会员条码: "+fileName.substring(0, fileName.indexOf("."))+ "无法预览的会员报告地址："+urlStr;;
			log.info("无法预览的会员条码: "+fileName.substring(0, fileName.indexOf(".")));
			log.info("无法预览的会员报告地址：  "+urlStr);
			logger.info("无法预览的会员条码: "+fileName.substring(0, fileName.indexOf(".")));
			logger.info("无法预览的会员报告地址：  "+urlStr);
			logger.info(e1.getMessage());
			//throw e1;
		} catch (IOException e2) {
			code = 500;
			msg = e2.getMessage();
			logger.info(e2.getMessage());	
			//throw e2;
		} catch (URISyntaxException e3) {
			code = 500;
			msg = e3.getMessage();
			logger.info(e3.getMessage());
			//throw e3;
		} finally {
			logger.info("返回的路径：  "+result.get(fileName));
			if(StringUtils.isNotEmpty(msg)){
				//返回提示信息
				result.put("msg",msg);
			}
		}
		return result;

    }  

	/**
	 * @description 执行下载
	 * @author YoumingDeng
	 * @since: 2016/12/7 15:12
	 */
	private static  Map<String,String> dealDownload(HttpURLConnection conn, String fileName,String savePath) throws IOException{
		Map<String,String> result = null;
		String saveFilePath = savePath + File.separator + fileName;
        //得到输入流  
        InputStream is = null;
        //获取自己数组  
        byte[] getData = null; 
        FileOutputStream fos = null;  
        //下载的文件
        File file = null;
        //文件大小
        long fileSize = 0;
		//源文件大小
		Integer size = 0;
		if(null!=conn){
			result = new HashMap<String, String>();
			is = conn.getInputStream();
			//获取源文件大小
			size = conn.getContentLength();
			log.info("源文件实际大小size: "+size+" Byte");
			getData = readInputStream(is);
			//防止屏蔽程序抓取而返回403错误
			//conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			//文件保存位置
			file = new File(saveFilePath);
			// 如果路径不存在,则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(file!=null){
				fos = new FileOutputStream(file);
				fos.write(getData);
				fos.flush();
				//获取文件大小
				fileSize = file.length();
				//下载文件的实际大小
				log.info("下载的文件实际大小size: "+fileSize+" Byte");
			}
			if(fos!=null){
				fos.close();
			}
			if(is!=null){
				is.close();
			}
			//下载的文件大小与源文件大小相等，则成功,否则为失败，删除之前下载的破损文件
			if(fileSize==size) {
				result.put(fileName, saveFilePath);//文件保存地址
				result.put("fileSize", "" + fileSize);//文件大小
				result.put("msg", "下载成功");//提示信息
				result.put(HttpUtils.CODE, ""+200);
				log.info("下载成功 : " + fileName + " 已下载 ");
				log.info("保存地址： " + saveFilePath);
				log.info("下载的文件大小： " + fileSize/1024+" KB" );
			}else{
				result.put(fileName, "");//文件保存地址
				result.put("fileSize", "" + fileSize);//文件大小
				result.put("msg", "下载失败，文件未下载完全，下载的文件大小： " + fileSize +" Byte ; 源文件大小： "+size+" Byte");//提示信息
				//错误代码
				result.put(HttpUtils.CODE, ""+304);
				log.info("下载失败，下载的文件不全");
				log.info("下载的文件大小： " + fileSize +" Byte ; 源文件大小： "+size+" Byte");
				log.info("开始删除不完整的文件["+saveFilePath+"] ...");
				if(file.delete()) {
					log.info("已删除文件[" + saveFilePath + "] !!!");
				}else {
					log.info("文件[" + saveFilePath + "] 删除失败###");
				}
			}
		}else{
			log.info("无效链接");
			result.put("msg", "无法下载，无效链接["+conn.getURL().toString()+"]");//提示信息
		}
		return result;
	}
	
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    private static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }

    /**
     * @description 判断文件是否下载完全
	 * @param file 文件
	 * @param size 原定大小
     * @author YoumingDeng
     * @since: 2016/12/7 14:31
     */
	private boolean isDone(File file, Integer size){
		boolean flag = false;
		try {
			if(file.isFile()&&file.exists()) {
				FileInputStream fis = new FileInputStream(file);

			}
		} catch (FileNotFoundException e) {
			log.info(e);
		}
		return flag;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}


}
