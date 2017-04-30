package org.hpin.common.core.orm;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.util.ReflectionUtils;
import org.hpin.common.util.StrUtils;

/**
 * 底层封装（filter_matchType_propertyName_operation_propertyType）
 * 
 * @author thinkpad 2010-10-5
 */
public class OrmConverter {
	
	/**
	 * 定义HQL中的左括号(前and关系运算)
	 */
	private static final String BRACKET_LEFT_AND = "BRACKET_LEFT_AND" ;
	
	/**
	 * 定义HQL中的左括号(前or关系运算)
	 */
	private static final String BRACKET_LEFT_OR = "BRACKET_LEFT_OR" ;
	
	/**
	 * 定义HQL中的右括号
	 */
	private static final String BRACKET_RIGHT = "BRACKET_RIGHT" ; 

	/** 属性比较类型. */
	public enum MatchType {
		EQ, NEQ, LIKE, LT, GT, LE, GE, LTET, GTST, LEET, GEST, IS, ISNOT , LLIKE , RLIKE
	}

	/** 属性数据类型. */
	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), D(Double.class), T(
				Date.class), B(Boolean.class);

		private Class<?> clazz;

		PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	/**
	 * 获取属性类型
	 * 
	 * @param str
	 * @return
	 */
	public static Class getPropertyType(String str) {
		return Enum.valueOf(OrmConverter.PropertyType.class, str).getValue();
	}

	/**
	 * 获取属性值
	 * 
	 * @param str
	 * @return
	 */
	public static Object getPropertyValue(Object value, Class propertyType,
			String matchType) {
		if (null != matchType && "like".equals(matchType)) {
			value = "%" + value + "%";
		}
		if(null != matchType && "llike".equals(matchType)){
			value = value + "%" ;
		}
		if(null != matchType && "rlike".equals(matchType)){
			value = "%" + value ;
		}
		return ReflectionUtils.convertStringToObject(value, propertyType);
	}

	/**
	 * 拼写查询语句
	 * 
	 * @param queryString
	 * @param filterMap
	 * @param valuelist
	 */
	public static void assemblyQuery(StringBuffer queryString,
			Map<String, String> filterMap, List valueList) {
		String[] temp = null;
		Iterator it = filterMap.entrySet().iterator();
		Map.Entry entry = null;
		while (it.hasNext()) {
			entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			getQueryReal(null, queryString, filterMap, key.toString(), null,
					valueList);

		}

		boolean isOrder = false;
		// 第二次遍历排序参数
		for (String o : filterMap.keySet()) {
			if (o.toString().startsWith("order")) {
				temp = o.replace("__" , ".").split("_");
				if (queryString.toString().indexOf("order by") < 0) {
					queryString.append(" order by");
				} else {
					queryString.append(",");
				}
				queryString.append(" " + temp[1] + " " + filterMap.get(o));
				if (!isOrder) {
					isOrder = true;
				}
			}
		}
		if (!isOrder) {
			queryString.append(" order by id");
		}
	}

	/**
	 * 排序
	 * 
	 * @param queryString
	 * @param filterMap
	 */
	public static void getOrder(StringBuffer queryString,
			Map<String, String> filterMap) {
		String[] temp = null;
		for (String o : filterMap.keySet()) {
			if (o.toString().startsWith("order")) {
				temp = o.replace("__" , ".").split("_");
				if (queryString.toString().indexOf("order by") < 0) {
					queryString.append(" order by");
				} else {
					queryString.append(",");
				}
				queryString.append(" " + temp[1] + " " + filterMap.get(o));
			}
		}
	}

	/**
	 * 获取真实的比较类型
	 * 
	 * @param matchType
	 * @return
	 */
	public static String getMatchType(MatchType matchType) {
		if (MatchType.EQ.equals(matchType)) {
			return "=";
		} else if (MatchType.NEQ.equals(matchType)) {
			return "<>";
		} else if (MatchType.LIKE.equals(matchType)) {
			return "like";
		} else if (MatchType.LE.equals(matchType)||MatchType.LEET.equals(matchType)) {
			return "<=";
		} else if (MatchType.LT.equals(matchType)||MatchType.LTET.equals(matchType)) {
			return "<";
		} else if (MatchType.GE.equals(matchType)||MatchType.GEST.equals(matchType)) {
			return ">=";
		} else if (MatchType.GT.equals(matchType)||MatchType.GTST.equals(matchType)) {
			return ">";
		} else if (MatchType.IS.equals(matchType)) {
			return "is";
		} else if (MatchType.ISNOT.equals(matchType)) {
			return "is not";
		} else if (MatchType.LLIKE.equals(matchType)){
			return "llike" ;
		} else if (MatchType.RLIKE.equals(matchType)){
			return "rlike" ;
		}else{
			throw new IllegalArgumentException("无法得到属性比较类型.");
		}
	}
	/**
	 * 编写查询语句
	 * @param queryString
	 * @param filterMap
	 * @param queryName
	 * @param propertyName
	 * @param valueList
	 */
	public static void getQueryReal(String tableName, StringBuffer queryString,
			Map<String, String> filterMap, String queryName,
			String propertyName, List valueList) {
//		LOGGER.info("替换前：" + queryString.toString()) ;
		if(queryString.indexOf("( and") >= 0 || queryString.indexOf("( or") >= 0){
			StringBuffer _temp = replaceAll(queryString) ;
			queryString.setLength(0) ;
			queryString.append(_temp) ;
//			LOGGER.info("替换后：" + queryString.toString()) ;
		}
		
		if (StrUtils.isNullOrBlank(filterMap.get(queryName)) && queryName.indexOf("IS") < 0) {
			return;
		}
		if(!StrUtils.isNullOrBlank(filterMap.get(queryName)) && queryName.indexOf(BRACKET_LEFT_AND) >= 0){
			queryString.append(" and (") ;
			return ;
		}
		if(!StrUtils.isNullOrBlank(filterMap.get(queryName)) && queryName.indexOf(BRACKET_LEFT_OR) >= 0){
			queryString.append(" or (") ;
			return ;
		}
		if(!StrUtils.isNullOrBlank(filterMap.get(queryName)) && queryName.indexOf(BRACKET_RIGHT) >= 0){
			queryString.append(" ) ") ;
			return ;
		}
		String[] temp = queryName.replace("__", ".").split("_");
		if (temp.length < 5) {
			return;
		}
		if (null != propertyName) {
			temp[2] = propertyName;
		}
		if (null != tableName) {
			temp[2] = tableName + "." + temp[2];
		}
		MatchType matchType = null;
		Class propertyType = null;
		Object propertyValue = null;
		if("GEET".equals(temp[3])||"GTET".equals(temp[3])){
			filterMap.put(queryName, filterMap.get(queryName)+" 00:00:00");
		}
		if("LEET".equals(temp[3])||"LTET".equals(temp[3])){
			filterMap.put(queryName, filterMap.get(queryName)+" 23:59:59");
		}
		try {
			matchType = Enum.valueOf(MatchType.class, temp[3]);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + queryName
					+ "没有按规则编写,无法得到属性比较类型.", e);
		}
		
		try {
			propertyType = Enum.valueOf(PropertyType.class, temp[4]).getValue();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + queryName
					+ "没有按规则编写,无法得到属性值类型.", e);
		}
		propertyValue = getPropertyValue(filterMap.get(queryName),
				propertyType, getMatchType(matchType));
		if ((!MatchType.IS.equals(matchType))
				&& (!MatchType.ISNOT.equals(matchType))) {
			queryString.append(" " + temp[1] + " " + temp[2] + " "
					+ ("llike".equals(getMatchType(matchType)) || "rlike".equals(getMatchType(matchType)) ? " like " : getMatchType(matchType)) + " ?");
			valueList.add(propertyValue);
		} else {
			queryString.append(" " + temp[1] + " " + temp[2] + " "
					+ getMatchType(matchType) + " null");
		}
	}
	
	/**
	 * 替换queryBuffer中"( and"和"( or"等非法字符串
	 * @param queryString
	 * @return
	 */
	private static StringBuffer replaceAll(StringBuffer queryString){
		StringBuffer queryBufferNew = new StringBuffer() ;
		String query = queryString.toString() ;
		query = query.replaceAll("\\( and" , "\\(") ;
		query = query.replaceAll("\\( or" , "\\(") ;
		queryBufferNew.append(query) ;
		return queryBufferNew ;
	}

	public static void getQuery(StringBuffer queryString,
			Map<String, String> filterMap, String queryName, List valueList) {
		getQuery(null, queryString, filterMap, queryName, valueList);
	}

	public static void getQuery(String tableName, StringBuffer queryString,
			Map<String, String> filterMap, String queryName, List valueList) {
		if(StringUtils.isNotBlank(queryName) && (getBracket(1).equals(queryName) || getBracket(2).equals(queryName) || getBracket(3).equals(queryName))){
			getQueryReal(null , queryString , filterMap , queryName , null , valueList) ;
		}
		String[] temp = queryName.replace("__", ".").split("_");
		if (temp.length < 5) {
			return;
		}
		String propertyName = temp[2];
		getQueryReal(tableName, queryString, filterMap, queryName,
				propertyName, valueList);
	}
	
	/**
	 * 获取HQL中左、右括号的定义
	 * @param type
	 * @return
	 */
	public static String getBracket(int type){
		switch(type){
			case 1 : 
				return BRACKET_LEFT_AND ;
			case 2 : 
				return BRACKET_LEFT_OR ;
			case 3 : 
				return BRACKET_RIGHT ;
			default :
				return null ;	
		}
	}
}
