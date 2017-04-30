package org.hpin.webservice.dao;

import java.util.List;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.ErpPrintTaskContent;
import org.springframework.stereotype.Repository;

import sun.util.logging.resources.logging;

@Repository
public class ErpPrintTaskContentDao extends BaseDao {
	
	public void save(ErpPrintTaskContent content){
		this.getHibernateTemplate().save(content);
	}
	
	public void save(List<ErpPrintTaskContent> contentList){
		this.getHibernateTemplate().saveOrUpdateAll(contentList);
	}
	
	public int saveList(List<ErpPrintTaskContent> list){
		Integer count = 0;
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				this.getHibernateTemplate().save(list.get(i));
				count++;
			}
		}
		return count;
	}

	public List<ErpCustomer> getCustomerInfoByCode(String code) {
		String sql = "from ErpCustomer where is_deleted=0 and code=?";
		return this.getHibernateTemplate().find(sql,code);
	}

	public CustomerRelationShipPro getProjectCodeByEvent(String eventsNo) {
		String sql1 = "from ErpEvents where is_deleted=0 and events_no=?";
		List<ErpEvents> eventList = this.getHibernateTemplate().find(sql1,eventsNo);
		CustomerRelationShipPro shipProList = null;
		if(eventList.size()!=0){
			shipProList =  this.getHibernateTemplate().get(CustomerRelationShipPro.class,eventList.get(0).getCustomerRelationShipProId());
		}
		return shipProList;
	}

	public List<ErpPrintTaskContent> getContentByPdfId(String pdfId) {
		String sql = "from ErpPrintTaskContent where pdfcontentid=?";
		return this.getHibernateTemplate().find(sql,pdfId);
	}
	
}
