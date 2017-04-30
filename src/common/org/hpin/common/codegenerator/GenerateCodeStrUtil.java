package org.hpin.common.codegenerator;

/**
 *<p>@Desc：随机生成指定位数、指定类型的字符串组合</p> 
 *
 *<p>@author : 胡五音</p>
 *
 *<p>@CreateDate：Apr 1, 2012 12:12:07 PM</p>
 *<p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>  
 */

public class GenerateCodeStrUtil {

	/**
	 * 默认字符串池-->字母数字组合，字母中没有"O"
	 */
	private static final String CODE_TYPE_DEFAULT = "1234567890ABCDEFGHIJKLMNPQRSTUVWXYZ" ;
	
	/**
	 * 数字字符池
	 */
	private static final String CODE_TYPE_NUMBER = "1234567890" ;
	
	/**
	 * 英文字母字符池
	 */
	private static final String CODE_TYPE_CHARACTER = "ABCDEFGHIJKLMNPQRSTUVWXYZ" ;
	
	/**
	 * 中文字符串字符池
	 */
	private static final String CODE_TYPE_CHINESE = "的一了是我不在人们有来他这上着个地到大里说就去子" +
			"得也和那要下看天时过出小么起你都把好还多没为又可家学只以主会样年想生同老中十从自面前头道它后然走很像见两用她" +
			"国动进成回什边作对开而己些现山民候经发工向事命给长水几义三声于高手知理眼志点心战二问但身方实吃做叫当住听革打" +
			"呢真全才四已所敌之最光产情路分总条白话东席次亲如被花口放儿常气五第使写军吧文运再果怎定许快明行因别飞外树物活" +
			"部门无往船望新带队先力完却站代员机更九您每风级跟笑啊孩万少直意夜比阶连车重便斗马哪化太指变社似士者干石满日决" +
			"百原拿群究各六本思解立河村八难早论吗根共让相研今其书坐接应关信觉步反处记将千找争领或师结块跑谁草越字加脚紧爱" +
			"等习阵怕月青半火法题建赶位唱海七女任件感准张团屋离色脸片科倒睛利世刚且由送切星导晚表够整认响雪流未场该并底深" +
			"刻平伟忙提确近亮轻讲农古黑告界拉名呀土清阳照办史改历转画造嘴此治北必服雨穿内识验传业菜爬睡兴形量咱观苦体众通" +
			"冲合破友度术饭公旁房极南枪读沙岁线野坚空收算至政城劳落钱特围弟胜教热展包歌类渐强数乡呼性音答哥际旧神座章帮啦" +
			"受系令跳非何牛取入岸敢掉忽种装顶急林停息句区衣般报叶压慢叔背细" ;
	
	/**
	 * 获取char数组
	 * @param length
	 * @param type
	 * @return
	 */
	private static char[] generatorCodeChars(int length , int type){
		String chars = getCharatersPond(type) ;
		char[] rands = new char[length] ;
		for(int i = 0 ; i < length ; i ++){
			int rand = (int)(Math.random() * chars.length()) ;
			rands[i] = chars.charAt(rand) ;
		}
		return rands ;
	}
	
	/**
	 * 随机生成指定位数、指定类型的字符串组合
	 * @param length
	 * @param type,随机码类型：1-->去除O的字母数字；2-->数字；3-->英文字母；4-->中文字符
	 * @return
	 */
	public static String generatorCode(int length , int type){
		if(length <= 0){
			return "" ;
		}else{
			StringBuffer sb = new StringBuffer("") ;
			char[] chars = generatorCodeChars(length, type) ;
			for(int i = 0 ; i < length ; i ++){
				sb.append(chars[i]) ;
			}
			return sb.toString() ;
		}
	}
	
	public static void main(String args[]){
		System.out.println(generatorCode(10 , 1)) ;
		System.out.println(generatorCode(10 , 2)) ;
		System.out.println(generatorCode(10 , 3)) ;
		System.out.println(CODE_TYPE_CHINESE.length()) ;
		System.out.println(generatorCode(10 , 4)) ;
	}
	
	/**
	 * 获取字符池，默认为字母加数字
	 * @param type
	 * @return
	 */
	private static String getCharatersPond(int type){
		switch(type){
		case 2 : {
			return CODE_TYPE_NUMBER ;
		}
		case 3 : {
			return CODE_TYPE_CHARACTER ; 
		}
		case 4 : {
			return CODE_TYPE_CHINESE ;
		}
		default : {
			return CODE_TYPE_DEFAULT ;
		}
		}
	}
	
}
