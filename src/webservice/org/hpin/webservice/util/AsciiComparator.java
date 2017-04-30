/**
 * @author DengYouming
 * @since 2016-7-29 下午2:25:54
 */
package org.hpin.webservice.util;

import java.util.Comparator;

/**
 * ASCII码比较器
 * @author DengYouming
 * @since 2016-7-29 下午2:25:54
 */
public class AsciiComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return compareOneByOne(o1, o2);
	}

	/**
	 * 逐字比较字符串ascii码
	 * @param o1
	 * @param o2
	 * @return int 
	 * @author DengYouming
	 * @since 2016-7-29 下午4:33:42
	 */
	private static int compareOneByOne(String o1, String o2){
		int res = 0;
		if(o1!=null&&o1.length()>0&&o2!=null&&o2.length()>0){
			int len = o1.length()<=o2.length()?o1.length():o2.length();
			for (int i = 0; i < len; i++) {
				if(o1.charAt(i)==o2.charAt(i)){
					if(i==len-1){
						if(o1.length()>o2.length()){
							res = 1;
							break;
						}else{
							res = -1;
							break;
						}
					}
					continue;
				}
				if(o1.charAt(i)>o2.charAt(i)){
					res = 1;
					break;
				}else{
					res = -1;
					break;
				}
			}
		}
		return res;
	}

}
