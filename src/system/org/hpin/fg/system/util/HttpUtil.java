package org.hpin.fg.system.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class HttpUtil {

	public static String http(String url, LinkedHashMap<String, String> params,String sig) {
		
		StringBuffer sb = new StringBuffer();// 构建请求参数
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				try {
					sb.append(URLEncoder.encode(e.getValue(),"UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sb.append("&");
			}
			try {
				sb.append("sig").append("=").append(URLEncoder.encode(sig,"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sb.substring(0, sb.length() - 1);
		}
		System.out.println("send_url:" + url);
		System.out.println("send_data:" + sb.toString());
		// 尝试发送请求
		URL _url = null;
		HttpURLConnection con = null;
		try {
			_url = new URL(url);
			con = (HttpURLConnection) _url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");

			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (con != null)
				con.disconnect();
		}
		return buffer.toString();
	}
	
	public static void main(String[]args){
		//System.out.println(HttpUtil.http("http://www.baidu.com", null));
	}
}
