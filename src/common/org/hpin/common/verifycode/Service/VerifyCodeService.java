package org.hpin.common.verifycode.Service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.servlet.http.HttpSession;

import org.hpin.common.verifycode.util.GenerateCodeUtil;


/**
 * <p>@desc : 验证码处理Service</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 13, 2013 4:28:42 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class VerifyCodeService{
	
	public static final String CHECK_CODE_KEY = "check_code" ;
	
	public static final int WIDTH = 64 ;
	
	public static final int HEIGHT = 20 ;

	/**
	 * 根据具体需求生成相应的验证码图片
	 * @param session 会话
	 * @param codeLength 验证码长度
	 * @param type 验证码类型：1->字母数字组合(去除字母O)；2->数字；3->字母(去除字母O)；4->中文
	 * @return
	 */
	public static BufferedImage getImage(HttpSession session , int codeLength , int type){
		BufferedImage image = new BufferedImage(WIDTH , HEIGHT , BufferedImage.TYPE_INT_RGB) ;
		Graphics graphics = image.getGraphics() ;
		char[] chars = GenerateCodeUtil.generatorCodeChars(codeLength , type) ;
		drawBackGround(graphics) ;
		drawRands(graphics , chars , codeLength) ;
		graphics.dispose() ; 
		session.setAttribute(CHECK_CODE_KEY , new String(chars)) ;
		return image ;
	}
	
	/**
	 * 画验证码的底层背景
	 * @param g
	 */
	private static void drawBackGround(Graphics g){
		g.setColor(new Color(0xDCDCDC)) ;
		g.fillRect(0, 0, WIDTH, HEIGHT) ;
		//随机产生500个随机颜色的干扰点
		for(int i = 0 ; i < 5 ; i ++){
			int x = (int)(Math.random() * WIDTH ) ; 
			int y = (int)(Math.random() * HEIGHT) ;
			int red = (int)(Math.random() * 255) ;
			int green = (int)(Math.random() * 255) ;
			int blue = (int)(Math.random() * 255) ;
			g.setColor(new Color(red , green , blue)) ;
			//在坐标(x,y)的位置画圆点
			g.drawOval(x , y , 1 , 0 ) ;
		}
		
		//画150条随机颜色，长短方向都随机的直线
		for(int i = 0 ; i < 5 ; i ++){
			int red = (int)(Math.random() * 255 ) ;
			int green = (int)(Math.random() * 255 ) ;
			int blue = (int)(Math.random() * 255 ) ;
			g.setColor(new Color(red , green , blue)) ;
			g.drawLine((int)(Math.random()*WIDTH) , (int)(Math.random()*HEIGHT) , (int)(Math.random()*WIDTH) , (int)(Math.random()*HEIGHT) ) ; 
		}
	}
	
	/**
	 * 绘制出4位随机验证码
	 * @param g
	 * @param rands
	 */
	private static void drawRands(Graphics g , char[] rands , int length){
		g.setColor(Color.black) ;
		g.setFont(new Font(null , Font.ITALIC | Font.BOLD , 18)) ;
		for(int i = 0 ; i < length ; i ++){
			g.drawString("" + rands[i] , (15 * i) + 1 , getRangeNumber()) ;
		}
	}
	
	/**
	 * 获取13-20之间的随机数，用来确定验证码每个字符产生的位置
	 * @return
	 */
	public static int getRangeNumber(){
		return (int)(13 + Math.random() * 7) ;
	}
}

