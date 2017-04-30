package org.hpin.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {
	
	/**
	 * 发送短信接口
	 * @param Mobile 手机号
	 * @param Content 短信内容
	 * @param send_time 定时发送时间
	 * @return 返回结果
	 */
	public static int send(String Mobile,String Content,String send_time) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		String CorpID="ymkj";//账户名
		String Pwd="healthlink";//密码
		String send_content=URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://qxt.ccme.cc/WS/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		int inputLine = 0;
		try {
			System.out.println("开始发送短信手机号码为 ："+Mobile);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			System.out.println("网络异常,发送短信失败！");
			inputLine=-2;
		}
		System.out.println("结束发送短信返回值：  "+inputLine);
		return inputLine;
	}

}
