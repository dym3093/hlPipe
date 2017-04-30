package org.hpin.common.log.listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hpin.common.core.SpringTool;
import org.hpin.common.log.annotation.LogAnnotation;
import org.hpin.common.log.annotation.LogTransient;
import org.hpin.common.log.annotation.ParentFlag;
import org.hpin.common.log.entity.ModifyHistory;
import org.hpin.common.log.entity.ModifyHistoryItem;
import org.hpin.common.log.entity.ParentEntity;

/**
 * <p>@desc : 日志框架工具包</p>
 * <p>@see : 反射Reflect</p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 6:43:25 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ModifyHistoryUtil {
	
	private final static String default_date_pattern = "yyyy-MM-dd HH:mm:ss";
	
	private final static String ID2NAME_METHOD_NAME = "id2Name" ;

	public static ModifyHistory getModifiedValue(Object newObj, Object oldObj, String _parentLogClass, String _childLogClass) throws Exception {
		ModifyHistory mh = (ModifyHistory)Class.forName(_parentLogClass).newInstance();
		String parentClazz = null;
		String parentId = null;
		Field[] fields = newObj.getClass().getDeclaredFields();
		if (fields == null || fields.length == 0) // 如果没有成员变量，直接返回空结果集
			return null;
		Set<ModifyHistoryItem> itemList = new HashSet<ModifyHistoryItem>();
		for (Field field : fields) { // 循环比较每个字段的差异
			String fieldName = field.getName();
//			if (field.getAnnotation(Column.class) == null && field.getAnnotation(JoinColumn.class) == null)// 没有Column或JoinColumn注释的不比较
//				continue;
			if ("id".equals(fieldName))// id不比较
				continue;
			if (Modifier.isStatic(field.getModifiers())) { // 不比较静态成员变量
				continue;
			}
			if (field.getAnnotation(LogTransient.class) != null)// 注释了Transient不比较
				continue;
			if (field.getAnnotation(ParentFlag.class) != null) {// 注释了多对一的，取出父ID
					field.setAccessible(true); // 设置成员变量的访问属性
					parentClazz = field.getType().getName();
					Object objParent = field.get(oldObj);
					if (objParent instanceof ParentEntity) {
						if (objParent != null)
							parentId = ((ParentEntity) objParent).getId().toString();
					}
				continue;
			}
			field.setAccessible(true); // 设置成员变量的访问属性
			Object oldField = (oldObj == null) ? null : field.get(oldObj);
			Object newField = (newObj == null) ? null : field.get(newObj);
			
			if ("java.util.Set".equals(field.getType().getName())) {// 集合类型不做比较
				continue;
			} 
			String oldValue = ObjectUtils.toString(oldField);
			String newValue = ObjectUtils.toString(newField);
			boolean isModified = false;
			if (!ObjectUtils.equals(oldField, newField))
				isModified = true;
			if (StringUtils.isEmpty(oldValue) && StringUtils.isEmpty(newValue))
				isModified = false;
			if (oldField instanceof Date && newField instanceof Date) {
				if (DateUtils.isSameInstant((Date) oldField, (Date) newField))
					isModified = false;
				else
					isModified = true;
				oldValue = DateFormatUtils.format((Date) oldField, default_date_pattern);
				newValue = DateFormatUtils.format((Date) newField, default_date_pattern);
			}
			if (isModified) {
				LogAnnotation _logAnnotation = field.getAnnotation(LogAnnotation.class);
				String _annotaionName = (_logAnnotation == null ? null : _logAnnotation.name());
				ModifyHistoryItem item = (ModifyHistoryItem)Class.forName(_childLogClass).newInstance();
				item.setFieldName(fieldName);
				String id2NameClassName = "";
				if (_logAnnotation != null)
				id2NameClassName = _logAnnotation.id2NameClass() ;
				else continue;
				
				Object _tempOldField = oldField ;
				Object _tempNewField = newField ;
				
				if(StringUtils.isNotBlank(id2NameClassName)){
					Object _object = SpringTool.getBean(id2NameClassName) ;
					Method[] _methods = _object.getClass().getDeclaredMethods() ;
					for(Method m : _methods){
						if(m.getName().equals(ID2NAME_METHOD_NAME)){
							oldField = m.invoke(_object , oldField) ;
							newField = m.invoke(_object , newField) ;
							if(oldField == null){
								oldField = _tempOldField ;	
							}
							
							if(newField == null){
								newField = _tempNewField ;
							}
						}
					}
				}
				
				_tempOldField = null ;
				_tempNewField = null ;
				item.setCreateTime(new Date());
				item.setOldValue(ObjectUtils.toString(oldField));
				item.setNewValue(ObjectUtils.toString(newField));
				item.setModifyHistory(mh);
				item.setFieldDesc(_annotaionName);
				itemList.add(item);
			}
		}
		if (CollectionUtils.isEmpty(itemList)) {
			return null;
		}
			if (parentId != null)
				mh.setParentPrimaryKey(parentId);
			else {// 如果没有父对象，默认为当前对象id
				Field field = null;
				String id = null;
				field = newObj.getClass().getDeclaredField("id") ;
				field.setAccessible(true);
				id = field.get(newObj).toString();
				mh.setParentPrimaryKey(id);
			}
			if (StringUtils.isNotEmpty(parentClazz))
				mh.setParentClazz(parentClazz);
			else {// 如果没有父对象，默认为当前对象
				mh.setParentClazz(newObj.getClass().getName());
			}
			mh.setItems(itemList);
		return mh;
	}

	/**
	 * 获得对象的所有字段，构造修改历史的对象和行项目。
	 * 
	 * @param obj
	 * @return 修改历史的对象和行项目
	 * @throws Exception
	 */
	public static ModifyHistory getObjectValue(Object obj, boolean isNewValue, String _parentLogClass, String _childLogClass) throws Exception {
		ModifyHistory mh = (ModifyHistory)Class.forName(_parentLogClass).newInstance();
		String parentClazz = null;
		String parentId = null;
		Set<ModifyHistoryItem> itemList = new HashSet<ModifyHistoryItem>();
		// 获取所有成员变量
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields == null || fields.length == 0) // 如果没有成员变量，直接返回空结果集
			return null;
		// 循环成员变量进行记录处理
		for (Field field : fields) {
			String fieldName = field.getName();
//			if (field.getAnnotation(Column.class) == null && field.getAnnotation(JoinColumn.class) == null)// 没有Column或JoinColumn注释的无记录
//				continue;
			if ("id".equals(fieldName))// id无记录
				continue;
			if (Modifier.isStatic(field.getModifiers()))// 静态成员变量无记录
				continue;
			if (field.getAnnotation(LogTransient.class) != null)// 注释了Transient无记录
				continue;
			if (field.getAnnotation(ParentFlag.class) != null)  {// 注释了多对一的，取出父ID
					field.setAccessible(true); // 设置成员变量的访问属性
					parentClazz = field.getType().getName();
					Object objParent = field.get(obj);
					if (objParent instanceof ParentEntity && objParent != null) {// 如果父对象不为null，取出父对象Id
						parentId = ((ParentEntity) objParent).getId().toString();
					}
				continue;
			}
			field.setAccessible(true); // 设置成员变量的访问属性
			Object fieldObj = (obj == null) ? null : field.get(obj);
			if (fieldObj instanceof java.util.Collection) {// 集合类型不做记录
				continue;
			} 
			LogAnnotation _logAnnotation = field.getAnnotation(LogAnnotation.class);
			String id2NameClassName = "";
			if (_logAnnotation != null)
			id2NameClassName = _logAnnotation.id2NameClass() ;
			else continue;
			
			boolean flag = false ;
			
			Object tempObj = fieldObj ;
			
			if(StringUtils.isNotBlank(id2NameClassName)){
				Object _object = SpringTool.getBean(id2NameClassName) ;
				Method[] _methods = _object.getClass().getDeclaredMethods() ;
				for(Method m : _methods){
					if(m.getName().equals(ID2NAME_METHOD_NAME)){
						fieldObj = m.invoke(_object , fieldObj) ;
						flag = true ;
						break ;
					}
				}
			}
			String fieldValue = ObjectUtils.toString(fieldObj);
			if (fieldValue == null || fieldValue.trim().equals("")){
				// 如果未添加值，不做处理
				if(!flag){
					continue;
				}else{
					if(tempObj == null || StringUtils.isBlank(tempObj.toString())) continue ;
					fieldObj = tempObj ;
					fieldValue = ObjectUtils.toString(fieldObj); 
				}
			}
				
			if (fieldObj instanceof Date) {
				fieldValue = DateFormatUtils.format((Date) fieldObj, default_date_pattern);
			}
			
			ModifyHistoryItem item = (ModifyHistoryItem)Class.forName(_childLogClass).newInstance();
			item.setFieldName(fieldName);
			item.setModifyHistory(mh);
			item.setCreateTime(new Date());
			if (isNewValue){
				item.setNewValue(fieldValue);
			}else{
				item.setOldValue(fieldValue);
			}
			String _annotaionName = (_logAnnotation == null ? null : _logAnnotation.name());
			item.setFieldDesc(_annotaionName);
			itemList.add(item);
		}
		if (CollectionUtils.isEmpty(itemList)) {
			return null;
		}
			if (parentId != null)
				mh.setParentPrimaryKey(parentId);
			else {// 如果没有父对象，默认为当前对象id
				Field field = null;
				String id = null;
					field = obj.getClass().getDeclaredField("id");
				field.setAccessible(true);
					id = (String) field.get(obj);
				mh.setParentPrimaryKey(id);
			}
			if (StringUtils.isNotEmpty(parentClazz))
				mh.setParentClazz(parentClazz);
			else {// 如果没有父对象，默认为当前对象
				mh.setParentClazz(obj.getClass().getName());
			}
			mh.setItems(itemList);
		return mh;
	}

}
