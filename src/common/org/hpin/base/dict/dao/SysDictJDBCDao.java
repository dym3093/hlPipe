package org.hpin.base.dict.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hpin.base.dict.service.HpinSystemDictUtil;
import org.hpin.common.core.orm.BaseDao;
import org.springframework.stereotype.Repository;

 
/**
 * 
 * panlong 下午09:02:54
 */
@Repository()
public class SysDictJDBCDao extends BaseDao {
	
	public String getMaxDeptid(String pardictid, int len) {

		
		//修改为循环利用未占用的dictId edit by liqiuye 20080902
		String minDictId = getUsableMinDictId(pardictid, len);
		if ( (minDictId.equals("") || minDictId.equals("null")) ) {
			minDictId = HpinSystemDictUtil.DICTID_DEFULT_VALUE;
		}
		return minDictId;
	}
	
	public String getMaxDeptidDESC(String pardictid,int len) {
		String maxDictId = getUsableMaxDictId(pardictid, len);
		if ( (maxDictId.equals("") || maxDictId.equals("null")) ) {
			maxDictId = HpinSystemDictUtil.DICTID_DEFULT_VALUE;
		}
		return maxDictId;
	}
	
	/**
	 * 根据字典ID删除记录
	 * @param dictid
	 */
	public void removeDictByDictid(String dictid){
		String sql = " delete from sys_dicttype where dictid like '"+dictid+"%'";
		getJdbcTemplate().execute(sql);
	}
	
   /**
     * 更新某字典类型的叶子节点
     * @param dictid
     * @param leaf
     */
    public void updateParentDictLeaf(String dictid,String leaf){
    	String sql = " update sys_dicttype set leaf='"+leaf+"'"+" where dictid='"+dictid+"'";
    	getJdbcTemplate().execute(sql);
    }
    
    /**
     * 生成最小的可用dictId,返回当前最小的未被占用的dictId liqiuye 20080902
     * @param parentDictId
     * @param len
     */
    public String getUsableMinDictId(String parentDictId, int len) {
    	String minUsableDictId = "";
    	String sql = " select distinct(dictid) from sys_dicttype where dictid like '"
			+ parentDictId + "%' and length(dictid)='" + len+"'";
    	List list = getJdbcTemplate().queryForList(sql);
    	HashMap hm = new HashMap();
    	for(Iterator it = list.iterator(); it.hasNext();) {
    		Map dictMap = (Map) it.next();
    		String dictId = dictMap.get("dictid").toString();
    		hm.put(dictId, dictId);
    	}
    	//防止下面的while出现死循环,如果所有Id全部被占用则返回一个空值
    	if (hm.size() >= Integer.parseInt(HpinSystemDictUtil.DICTID_IF_MAXID)) {
    		return minUsableDictId;
    	}
    	long dictIdVar = Long.valueOf(parentDictId + HpinSystemDictUtil.DICTID_NOSON).longValue();
    	while (null != hm.get(String.valueOf(dictIdVar))) {
    		dictIdVar = dictIdVar + 1;
    	}
    	return String.valueOf(dictIdVar);
    }
    
    /**
     * 生成最大的可用dictId,返回当前最大的未被占用的dictId liqiuye 20080916
     * @param parentDictId
     * @param len
     */
    public String getUsableMaxDictId(String parentDictId, int len) {
    	String maxUsableDictId = "";
    	String sql = " select distinct(dictid) from sys_dicttype where dictid like '"
			+ parentDictId + "%' and length(dictid)='" + len+"'";
    	List list = getJdbcTemplate().queryForList(sql);
    	HashMap hm = new HashMap();
    	for(Iterator it = list.iterator(); it.hasNext();) {
    		Map dictMap = (Map) it.next();
    		String dictId = dictMap.get("dictid").toString();
    		hm.put(dictId, dictId);
    	}
    	//防止下面的while出现死循环,如果所有Id全部被占用则返回一个空值
    	if (hm.size() >= Integer.parseInt(HpinSystemDictUtil.DICTID_IF_MAXID)) {
    		return maxUsableDictId;
    	}
    	long dictIdVar = Long.valueOf(parentDictId + HpinSystemDictUtil.DICTID_IF_MAXID).longValue();
    	while (null != hm.get(String.valueOf(dictIdVar))) {
    		dictIdVar = dictIdVar - 1;
    	}
    	return String.valueOf(dictIdVar);
    }
}
