package org.hpin.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileUtil {

	/**
	 * 创建新文件并且保存
	 * 
	 * @param src
	 * @return
	 */
	public static int createNewFile(File src, String newFileName,
			String uploadPath) {
		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File dst = new File(dir, newFileName);
		InputStream is = null;
		OutputStream os = null;
		int fileSize = 0;
		try {
			is = new BufferedInputStream(new FileInputStream(src));
			os = new BufferedOutputStream(new FileOutputStream(dst));
			fileSize = is.available();
			byte buffer[] = new byte[8192];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileSize;
	}

	/**
	 * 删除物理文件
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 获取附件原名陈
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {
		if (fileName.indexOf(".") >= 0) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}
		return fileName;
	}

	/**
	 * 创建新名称
	 * 
	 * @param seq
	 * @param fileNames
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String createNewFileName(Long seq, String fileNames) {
		Calendar now = Calendar.getInstance();
		String newFileName = String.valueOf(now.get(now.YEAR))
				+ String.valueOf(now.get(now.MONTH) + 1)
				+ String.valueOf(now.get(now.DATE));
		newFileName += String.valueOf(now.get(now.HOUR))
				+ String.valueOf(now.get(now.MINUTE))
				+ String.valueOf(now.get(now.SECOND));
		if (null == seq) {
			newFileName = newFileName + "-" + seq + "-" + fileNames;
		} else {
			newFileName = newFileName + "-" + fileNames;
		}
		return newFileName;
	}

	/**
	 * @description 根据文件名查找文件
	 * @param fileName 文件名
	 * @author YoumingDeng
	 * @since: 2016/12/20 13:39
	 */
	public static List<File> findFile(String fileName){
		return findFile(fileName,null,true) ;
	}
	/**
	 * @description 根据文件名查找文件
	 * @param fileName 文件名
	 * @param dir 查找目录，若为null，则为查找项目目录内的文件
	 * @param isExact 是否严格等于，ture：查找文件名完全一致的文件 ,false:查找包含改文件名的文件
	 * @return List
	 * @author YoumingDeng
	 * @since: 2016/12/20 2:57
	 */
	public static List<File> findFile(String fileName, String dir, boolean isExact ){
		List<File> list = null;
		if(fileName!=null&&fileName.trim().length()>0){
			if(StringUtils.isEmpty(dir)){
				//dir = System.getProperty("user.dir");
				dir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			}
			File dirFile = new File(dir);
			list = new ArrayList<File>();
			if(dirFile.exists()&&dirFile.isDirectory()) {
				File[] files = dirFile.listFiles();
				if(files!=null&&files.length>0){
					for (int i=0; i<files.length; i++){
						File existDir = files[i];
						if(existDir.isDirectory()||!existDir.getPath().contains("lib")){
							list.addAll(findFile(fileName, files[i].getPath(), isExact));
						}
					}
				}
			}else{
				//严格等于
				if (isExact) {
					if (fileName.equals(dirFile.getName())){
						list.add(dirFile);
					}
				}else {
					if(StringUtils.containsIgnoreCase(dirFile.getName(),fileName)){
						list.add(dirFile);
					}
				}
			}
		}
		return list;
	}

}
