package org.hpin.common.log.listener;

import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hpin.common.core.SpringTool;
import org.hpin.common.log.annotation.SystemLogAnnotation;
import org.hpin.common.log.entity.ModifyHistory;
import org.hpin.common.log.entity.ParentEntity;
import org.hpin.common.util.StaticMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>@desc : 监听新增、修改、删除事件，做相应的处理 记录操作历史</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 9:31:56 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ModifyHistoryService {

	private static final long serialVersionUID = 4638067526108475248L;
	private SessionFactory _sessionFactory = null ;
	private Logger _log = LoggerFactory.getLogger(ModifyHistoryService.class);
	// 新增操作
	private static final String OPERATE_TYPE_CREATE = "insert";
	// 修改操作
	private static final String OPERATE_TYPE_UPDATE = "modify";
	// 删除操作
	private static final String OPERATE_TYPE_DELETE = "delete";

	/**
	 * 新增事件触发此方法
	 */
	public void onPostInsert(Object _event) {
		//新增历史记录
		if (_event instanceof ParentEntity) {
			SystemLogAnnotation _sysAnnotation = _event.getClass().getAnnotation(SystemLogAnnotation.class);
			if(null != _sysAnnotation){
				String _parentClazz = _sysAnnotation.parentClass();
				String _childClazz = _sysAnnotation.childClass();
				// 获取对象
				ParentEntity _parentEntity = (ParentEntity) _event;
				ModifyHistory _entity = null;
				try {
					_entity = ModifyHistoryUtil.getObjectValue(_parentEntity, true, _parentClazz, _childClazz);
				} catch (Exception _exception) {
					_log.error("[ERROR]Save the insertion history of  when an exception occurs.", _exception);
				}
				if (_entity != null) {
					_entity.setOperType(OPERATE_TYPE_CREATE);
					_entity.setTargetPrimaryKey(_parentEntity.getId() == null ? null : _parentEntity.getId().toString());
					_entity.setTargetClazz(_parentEntity.getClass().getName());
					//增加资源相关属性
					recordHistory(_entity);
				}
			}
		}
	}
	public void onPostDelete(Object _event) {
		//删除历史记录
		if (_event instanceof ParentEntity) {
			SystemLogAnnotation _sysAnnotation = _event.getClass().getAnnotation(SystemLogAnnotation.class);
			if(null != _sysAnnotation){
				String _parentClazz = _sysAnnotation.parentClass();
				String _childClazz = _sysAnnotation.childClass();
				// 获取对象
				ParentEntity _parentEntity = (ParentEntity) _event;
				//增加资源名称,资源类型
				ModifyHistory _entity = null;
				try {
					_entity = ModifyHistoryUtil.getObjectValue(_parentEntity, false, _parentClazz, _childClazz);
				} catch (Exception _exception) {
					_log.error("[ERROR]Save the delete history of  when an exception occurs.", _exception);
				}
				if (_entity != null) {
					_entity.setOperType(OPERATE_TYPE_DELETE);
					_entity.setTargetPrimaryKey(_parentEntity.getId().toString());
					_entity.setTargetClazz(_parentEntity.getClass().getName());
					//增加资源相关属性
					recordHistory(_entity);
				}
			}
		}
	}

	/**
	 * 修改事件触发此方法
	 */
	public void onSaveOrUpdate(Object _event) {
		//修改历史记录
		if (_event instanceof ParentEntity) {
			SystemLogAnnotation _sysAnnotation = _event.getClass().getAnnotation(SystemLogAnnotation.class);
			if(null != _sysAnnotation){
				String _parentClazz = _sysAnnotation.parentClass();
				String _childClazz = _sysAnnotation.childClass();
				// 获取对象
				ParentEntity _newEntity = (ParentEntity) _event;
				String id = _newEntity.getId().toString();
				String _id = StaticMethod.nullObject2String(id) ;
				ParentEntity _oldEntity = null;
				if (_id != null) {
					_sessionFactory = (SessionFactory)SpringTool.getBean("sessionFactory") ;
					Session session = _sessionFactory.openSession();
					_oldEntity = (ParentEntity) session.get(Hibernate.getClass(_event), _id);
					session.close();
					//获取被删除对象所有字段值
					ModifyHistory _entity = null;
					try {
						_entity = ModifyHistoryUtil.getModifiedValue(_newEntity, _oldEntity, _parentClazz, _childClazz);
					} catch (Exception _exception) {
						_log.error("[ERROR]Save the update history of  when an exception occurs.", _exception);
					}
					if (_entity != null) {
						_entity.setOperType(OPERATE_TYPE_UPDATE);
						_entity.setTargetPrimaryKey(id);
						_entity.setTargetClazz(_newEntity.getClass().getName());
						//增加资源相关属性
						recordHistory(_entity);
					}
				}
			}
		}
	}

	/**
	 * 另外启动一个session处理审核日志的保存
	 */
	private void recordHistory(ModifyHistory _entity) {
		String _userId = "";
		/**********************************************************
		 **********************************************************
		 ******   日志模块需要根据系统实际情况，获取_userId   ******
		 **********************************************************
		 **********************************************************/
		//User sessionUser = (User)HttpTool.getSession().getAttribute("currentUser");
			if(null != _entity){
				_entity.setUpdateTime(new Date());
				_entity.setUpdateUserId(_userId);
				if(_sessionFactory == null)_sessionFactory = (SessionFactory)SpringTool.getBean("sessionFactory") ;
				Session _openSession = _sessionFactory.openSession();
				Transaction _tx = _openSession.beginTransaction();
				try {
					_tx.begin();
					_openSession.save(_entity);
					_openSession.flush();
					_tx.commit();
				} catch (Exception ex) {
					_log.error("[ERROR]An exception occuring when saving history.", ex);
					_tx.rollback();
				}finally{
					_openSession.close();
				}
			}
	}

	public SessionFactory get_sessionFactory() {
		return _sessionFactory;
	}

	public void set_sessionFactory(SessionFactory _sessionFactory) {
		this._sessionFactory = _sessionFactory ;
	}
}
