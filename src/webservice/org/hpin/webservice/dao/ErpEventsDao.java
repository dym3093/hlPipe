/**
 * @author DengYouming
 * @since 2016-8-5 上午10:56:32
 */
package org.hpin.webservice.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.common.util.StrUtils;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.util.Tools;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * @author DengYouming
 * @since 2016-8-5 上午10:56:32
 */
@Repository
public class ErpEventsDao extends BaseDao{
	
	/**
	 * 根据传入参数条件查询对应场次是否存在;
	 * create by herny.xu 20170215
	 * @param branchCompanyId
	 * @param proId
	 * @param eventsDate
	 * @return
	 */
	public ErpEvents findByConditions(String branchCompanyId, String proId, String eventsDate) {
		String querySql = "select event.ID, " +
			"event.EVENTS_NO eventsNo, " +
			"event.EVENT_DATE eventDate, " +
			"event.ADDRESS address, " +
			"ship.BRANCH_COMMANY branchCompany, " +
			"event.LEVEL2 level2, " +
			"event.HEADCOUNT headCount, " +
			"event.NOW_HEADCOUNT nowHeadcount, " +
			"event.IS_DELETED isDeleted, " +
			"event.PROVINCE provice, " +
			"event.CITY city, " +
			"dep.DEPT_NAME ownedCompany, " +
			"event.IS_EXPRESS isExpress, " +
			"event.ENAME ename, " +
			"event.EDATE edate, " +
			"event.COMBO_ID comboId, " +
			"event.COMBO_NAME comboName, " +
			"event.EVENTS_TYPE eventsType, " +
			"event.BATCHNO batchNo, " +
			"event.YM_SALESMAN ymSalesman,  " +
			"event.BRANCH_COMPANY_ID branchCompanyId, " +
			"event.OWNED_COMPANY_ID ownedCompanyId, " +
			"event.AMOUNT amount, " +
			"event.CUSTOMERRELATIONSHIPPRO_ID customerRelationShipProId " +
			"from ERP_EVENTS event " +
			"left join HL_CUSTOMER_RELATIONSHIP ship on ship.id = event.BRANCH_COMPANY_ID " +
			"left join UM_DEPT dep on dep.id = event.OWNED_COMPANY_ID  " +
			"where event.BRANCH_COMPANY_ID='" + branchCompanyId + "' " +
			"and event.CUSTOMERRELATIONSHIPPRO_ID='" + proId + "' " +
			"and event.EVENT_DATE = to_date('" + eventsDate + "',  'yyyy-MM-dd HH24:mi:ss')";
		BeanPropertyRowMapper<ErpEvents> rowMapper = new BeanPropertyRowMapper<ErpEvents>(ErpEvents.class);
		List<ErpEvents> result = this.getJdbcTemplate().query(querySql, rowMapper);
		return result != null && result.size() > 0 ? result.get(0) : null;
	}
	
	/**
	 * 查询当天最大场次号
	 * @param date
	 * @return
	 */
	public String maxNo(String date){
		String eventsNo="HL1512142048000";
		String sql = "select max(events_no) as events_no from Erp_Events where to_char(event_Date,'yyyy-mm-dd')='"+date+"'";
		List list = this.getJdbcTemplate().queryForList(sql);
		Map map = (Map) list.get(0);
		String maxNo = (String) map.get("EVENTS_NO");
		if(StrUtils.isNotNullOrBlank(maxNo)){
			eventsNo=maxNo.toString();
		}
		return eventsNo;
	}

	/**
	 * 根据条件查询相关场次
	 * @param params 传入的条件
	 * @return List
	 * @author DengYouming
	 * @since 2016-5-18 上午11:33:17
	 */
	public List<ErpEvents> listEventsByProps(Map<String,String> params)throws Exception{
		List<ErpEvents> list = null;
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ErpEvents.class);

		if(!CollectionUtils.isEmpty(params)){
			for (String key : params.keySet()) {
				String value = params.get(key);
				if(key.equalsIgnoreCase(ErpEvents.F_ID)){
					String[] idArr;
					if(value.indexOf(",")!=-1){
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpEvents.F_ID, idArr));
				}else if(key.equalsIgnoreCase(ErpEvents.F_ISDELETED)){
					criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
				}else{
					criteria.add(Restrictions.eq(key, value));
				}
			}
			//未删除，按创建日期倒序
			criteria.add(Restrictions.eq(ErpEvents.F_ISDELETED, 0)).addOrder(Order.desc(ErpEvents.F_CREATETIME));
			list = criteria.list();
		}
		return list;
	}
	
	/**
	 * 可以根据 
	 * 1）info: 会员姓名 , infoType: name 
	 * 2）info: 会员条码, infoType: code 
	 * 3）info: 批次号,  infoType: batchNo
	 * 4）info: 场次号,  infoType: eventsNo
	 * 5）info: 团单号 ,  infoType: groupOrderNo
	 * 等信息，查找到相关场次
	 * @param info 传入的信息
	 * @param infoType 信息类型
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-25 下午4:37:14
	 */
	public List<ErpEvents> listEventsByInfo(String info, String infoType)throws Exception{
		List<ErpEvents> list = null;
		Connection conn = null;
		CallableStatement proc = null;
		ResultSet rs = null;
		
		if(StringUtils.isNotEmpty(info)&&StringUtils.isNotEmpty(infoType)){
			try{
				String procName = "{call ERP_COMM_PKG.findEvents(?,?,?)}"; 
				conn = this.getJdbcTemplate().getDataSource().getConnection();
				proc = conn.prepareCall(procName);
				proc.setString(1, info);
				proc.setString(2, infoType);
				proc.registerOutParameter(3, OracleTypes.CURSOR);
				proc.execute();
				rs = (ResultSet) proc.getObject(3);
				if(rs!=null){
					list = this.convertToEvents(rs);
				}
			}finally{
				if(rs!=null){
					rs.close();
				}
				if(proc!=null){
					proc.close();
				}
				if(conn!=null){
					conn.close();
				}
			}
		}
		return list;
	}
	
	private static List<ErpEvents> convertToEvents(ResultSet rs) throws SQLException{
		List<ErpEvents> list = null;
		ErpEvents entity = null;
		if(rs!=null){
		   list = new ArrayList<ErpEvents>();
		   while(rs.next()){
			   entity = new ErpEvents();

				entity.setId(rs.getString("ID"));
				entity.setEventsNo(rs.getString("EVENTS_NO"));
				entity.setBatchNo(rs.getString("BATCHNO"));
				entity.setEventDate(rs.getDate("EVENT_DATE"));
				entity.setAddress(rs.getString("BRANCH_COMPANY"));

				entity.setBranchCompany(rs.getString("BRANCH_COMPANY"));
				entity.setBranchCompanyId(rs.getString("BRANCH_COMPANY_ID"));
				entity.setOwnedCompany(rs.getString("OWNED_COMPANY"));
				entity.setOwnedCompanyId(rs.getString("OWNED_COMPANY_ID"));
				entity.setLevel2(rs.getString("LEVEL2"));
				
				entity.setComboId(rs.getString("COMBO_ID"));
				entity.setComboName(rs.getString("COMBO_NAME"));
				entity.setHeadcount(rs.getInt("HEADCOUNT"));
				entity.setNowHeadcount(rs.getInt("NOW_HEADCOUNT"));
				entity.setIsDeleted(rs.getInt("IS_DELETED"));
				
				entity.setIsExpress(rs.getInt("IS_EXPRESS"));
				entity.setCreateUserName(rs.getString("CREATE_USER_NAME"));
				entity.setCreateTime(rs.getDate("CREATE_TIME"));
				entity.setUpdateUserName(rs.getString("CREATE_USER_NAME"));
				entity.setUpdateTime(rs.getDate("UPDATE_TIME"));
				
				entity.setEname(rs.getString("ENAME"));
				entity.setEtrackingNumber(rs.getString("ETRACKING_NUMBER"));
				entity.setEdate(rs.getDate("EDATE"));
//				entity.setHour(rs.getString("&"));
				entity.setProvice(rs.getString("PROVINCE"));
				
				entity.setCity(rs.getString("PROVINCE"));
				entity.setPdfcount(rs.getInt("PDFCOUNT"));
				entity.setYmSalesman(rs.getString("YM_SALESMAN"));
				entity.setNopdfcount(rs.getInt("NOPDFCOUNT"));
				entity.setSettNumbers(rs.getInt("SETT_NUMBERS"));
				
				entity.setProduceCost(rs.getDouble("PRODUCE_COST"));
				entity.setStatusBX(rs.getInt("STATUS_BX"));
				entity.setGroupOrderNo(rs.getString("GROUP_ORDER_NO"));
				entity.setCustomerRelationShipProId(rs.getString("CUSTOMERRELATIONSHIPPRO_ID"));
				entity.setEventsType(rs.getString("EVENTS_TYPE"));
			   
			   list.add(entity);
		   }
		}
		return list;
	}
	
	private static List convert(ResultSet rs , Class clazz){  
		//返回结果的列表集合  
        List list = new ArrayList();  
        try{
		//结果集的元素对象   
        ResultSetMetaData rsmd = rs.getMetaData();  
        //获取结果集的元素个数  
         int colCount = rsmd.getColumnCount();  
    /*   for(int i = 1;i<=colCount;i++){  
           System.out.println("列名："+rsmd.getColumnName(i));  
           System.out.println("列类型： "+rsmd.getColumnClassName(i));  
           System.out.println("#");  
       } */
        
         //业务对象的属性数组  
         Field[] fields = findPirvateFields(clazz);  
         while(rs.next()){//对每一条记录进行操作  
             Object obj = clazz.newInstance();//构造业务对象实体  
             //将每一个字段取出进行赋值  
             for(int i = 1;i<=colCount;i++){  
                 Object value = rs.getObject(i);  
                 //列名
                 String colName = rsmd.getColumnName(i);
                 colName = Tools.colToField(colName);
                 //寻找该列对应的对象属性  
                 for(int j=0;j<fields.length;j++){  
                     Field f = fields[j];  
                     //如果匹配进行赋值  
                     if(f.getName().equalsIgnoreCase(colName)){  
                         boolean flag = f.isAccessible();  
                         f.setAccessible(true); 
                         //根据字段类型转换
                         value = praseVal(f, value);
                         f.set(obj, value);  
                         f.setAccessible(flag);  
                     }  
                 }  
             }  
             if(obj!=null){
            	 list.add(obj);  
             }
         }  
        }catch(Exception e){
        	e.printStackTrace();
        }
        return list;  
    }  
	
	private static Field[] findPirvateFields(Class clazz){
		Field[] fields = new Field[]{}; 
		Field[] temp = clazz.getDeclaredFields();
		int num = 0;
		for (int i = 0; i < temp.length; i++) {
			String name = temp[i].getName();
			if("serialVersionUID".equalsIgnoreCase(name)||name.startsWith("F_")){
				continue;
			}
			fields = Arrays.copyOf(fields, fields.length+1);
			fields[num] = temp[i];
			num++;
		}
	    return fields;
	}

	private static Object praseVal(Field f, Object val){
		Object otherVal = null;
		String typeName = f.getType().getSimpleName();
		if("String".equalsIgnoreCase(typeName)){
			val = val!=null?val:"";
			otherVal = ""+val;
		}else if("long".equalsIgnoreCase(typeName)){
			val = val!=null?val:0;
			otherVal = Long.parseLong(""+val);
		}else if("int".equalsIgnoreCase(typeName)||"integer".equalsIgnoreCase(typeName)){
			val = val!=null?val:0;
			otherVal = Integer.parseInt(""+val);
		}else if("float".equalsIgnoreCase(typeName)){
			val = val!=null?val:0;
			otherVal = Float.parseFloat(""+val);
		}else if("double".equalsIgnoreCase(typeName)){
			val = val!=null?val:0;
			otherVal = Double.parseDouble(""+val);
		}else if("bigdecimal".equalsIgnoreCase(typeName)){
			val = val!=null?val:0;
			otherVal = new BigDecimal(""+val);
		}else{
			otherVal = val;
		}
		return otherVal;
	}
	
	public static void main(String[] args) {
		findPirvateFields(ErpEvents.class);
	}

	/**
	 * 查询场次二维码生成有效时间;
	 * create by henry.xu 2016年12月5日
	 * @param eventsNo
	 * @return
	 */
	public Map<String, Object> findQRCodeMapByEventsNo(String eventsNo) {
		String sql = "select to_char(EXPIRY_DATE, 'YYYY-MM-DD HH:MI:SS') expiryDate, to_char(EVENTS_DATE, 'YYYY-MM-DD HH:MI:SS') eventsDate from erp_qrcode where EVENTS_NO='"+eventsNo+"' and ISDETELE = '0' ";
		return this.getJdbcTemplate().queryForMap(sql);
	}

	/**
	 * 查询并处理二维码场次对应的批次号算法;
	 * create by henry.xu 2016年12月9日
	 * @return
	 */
	public String findEventsBatchNo() {
		String batchNo = "";
		
		String sql = "select max(to_number(substr(batchNo, 3, length(batchNo)))) batchNo from erp_events where BATCHNO like 'WD%' ";
		Map<String, Object> map = this.getJdbcTemplate().queryForMap(sql);
		if(null != map) {
			batchNo = map.get("batchNo") != null ? map.get("batchNo").toString() : "" ;
		}
		
		return batchNo;
	}
	
	/**
	 * 查询并处理二维码场次对应的批次号算法;
	 * create by henry.xu 2017年01月22日
	 * @return
	 */
	public String findTYEventsBatchNo(String batch) {
		String batchNo = "";
		
		String sql = "select max(to_number(substr(batchNo, "+(batch.length()+1)+", length(batchNo)))) batchNo from erp_events where BATCHNO like '"+batch+"%' ";
		Map<String, Object> map = this.getJdbcTemplate().queryForMap(sql);
		if(null != map) {
			batchNo = map.get("batchNo") != null ? map.get("batchNo").toString() : "" ;
		}
		
		return batchNo;
	}

}
