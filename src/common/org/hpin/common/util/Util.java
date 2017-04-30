package org.hpin.common.util;

import java.util.Set;
import java.util.TreeSet;

public class Util {

	public static String getOrderBy(Object[] columns) {
		if (columns == null) {
			return "";
		}
		int len = columns.length;
		if (len == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(" ORDER BY ");
		for (int i = 0; i < len; i++) {
			buffer.append(columns[i].toString());
			if (i < len - 1) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	public static int getUnsignedInteger(String s, int defaultValue) {
		if (s == null) {
			return defaultValue;
		}
		int i;
		try {
			i = Integer.parseInt(s);
			if (i < 0) {
				return defaultValue;
			}
			return i;
		} catch (Exception e) {
			// swallowed exception, don't need to care about it.
		}
		return defaultValue;
	}

	public static boolean equals(Object o1, Object o2) {
		if (o1 != null) {
			return o1.equals(o2);
		} else { // o1 is null
			if (o2 != null) {
				return false;
			} else {
				return true;
			}
		}
	}

	public static boolean isEmpty(String[] s) {
		if (s == null) {
			return true;
		}
		if (s.length == 0) {
			return true;
		}
		return false;
	}

	public static String[] removeDup(String[] strings) {
		if (strings == null) {
			return null;
		}
		Set<String> set = new TreeSet<String>();
		for (String string : strings) {
			string = trimToNull(string);
			if (string != null) {
				set.add(string);
			}
		}
		return set.toArray(new String[] {});
	}

	public static String trimToNull(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.length() == 0) {
			return null;
		}
		return s;
	}

	public static String[][] arryChange(String[] a,int row) {
		String[][] b = new String[a.length/row][row];
		int num = -1;
		for (int i = 0; i < a.length/row; i++) {
			for (int j = 0; j < row; j++) {
				num++;
				b[i][j] = a[num];
			}
		}
		return b;
	}
	
	public static void main(String[] args) 
	 {
//		String str = " 1000 以上";
//		str.substring(0,str.lastIndexOf("以上")).trim();
//		System.out.println(str.substring(0,str.lastIndexOf("以上")).trim());
	 }
}
