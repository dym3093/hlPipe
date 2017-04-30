/**
 * @author DengYouming
 * @since 2016-8-19 下午9:54:17
 */
package org.hpin.webservice.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.hpin.base.region.dao.RegionDao;
import org.hpin.base.region.entity.Region;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.ErpQRCode;
import org.hpin.webservice.dao.CustomerRelationshipDao;
import org.hpin.webservice.dao.ErpQRCodeDao;
import org.hpin.webservice.foreign.DownloadThread;
import org.hpin.webservice.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DengYouming
 * @since 2016-8-19 下午9:54:17
 */
@Service(value = "org.hpin.webservice.service.ErpQRCodeService")
@Transactional
public class ErpQRCodeService extends BaseService {

	@Autowired
	private ErpQRCodeDao dao;

	@Autowired
	private ErpEventsService eventsService;

	Logger quCodeInfoLog= Logger.getLogger("getEventQRCodeInfo");

	public void update(String fileAddr,String viewPath,String eventsNo){
		String sql = "update erp_qrcode set QRCODE_PATH= ?, QRCODE_LOCALPATH = ? where events_no=? ";
		dao.getJdbcTemplate().update(sql,new Object[]{fileAddr,viewPath,eventsNo});
	}
	
	public void update (String sql) {
		dao.getJdbcTemplate().update(sql);
	}

	/**
	 * 保存优宝过来的二维码数据
	 * @param result
	 * @return
	 */
	public boolean saveFromUB(Map<String,String> result) throws Exception{
		boolean flag = false;
		if(result!=null&&result.size()>0){
			SimpleDateFormat sdf = new SimpleDateFormat();
			ErpQRCode qrCode;
			List<ErpEvents> eventsList;

			String eventsNo = result.get("eventsNo");
			String eventKey = result.get("eventKey");
//					String qrCodePic = result.get("qrCodePic");
			String qrCodeUrl = result.get("qrCodeUrl");

			//根据场次号获取场次信息
			Map<String,String> params = new HashMap<String, String>();
			params.put(ErpEvents.F_EVENTSNO, eventsNo);
			eventsList = eventsService.listEventsByProps(params);
			ErpEvents events = eventsList.get(0);

			//保存二维码图片
			sdf.applyPattern("yyyyMMdd");
			String dateStr = sdf.format(Calendar.getInstance().getTime());
			String fileName = dateStr+events.getBranchCompany()+eventsNo+".png";

			String savePath = PropertiesUtils.getString("foreign","disk.no")+ File.separator+"qrCode"+dateStr;
			//保存后返回实际保存位置
			Map<String, String> resp = new DownloadThread(qrCodeUrl, fileName, savePath).call();
			String fileAddr = resp.get(fileName);
			//数据入库
			if(fileAddr!=null&&fileAddr.length()>0){
				String subAddr = fileAddr.substring(fileAddr.indexOf(File.separator));
				quCodeInfoLog.info("subAddr: "+subAddr);
				//预览地址
				String viewPath = PropertiesUtils.getString("foreign","viewPath_head")+subAddr;
				quCodeInfoLog.info("viewPath: "+viewPath);

				//新建对象
				qrCode = new ErpQRCode();

				qrCode.setEventsNo(eventsNo);
				qrCode.setEventsDate(events.getEventDate());

				qrCode.setQRCodeName(fileName);
				qrCode.setQRCodePath(fileAddr);//物理地址
				qrCode.setQRCodeLocalPath(viewPath);

				//根据支公司名称获取 HL_CUSTOMER_RELATIONSHIP的中省，市信息
				String company = events.getBranchCompany();
				CustomerRelationshipDao shipDao = (CustomerRelationshipDao) SpringTool.getBean(CustomerRelationshipDao.class);
				CustomerRelationShip ship = shipDao.findByCompanyName(company);

				RegionDao regionDao = (RegionDao) SpringTool.getBean(RegionDao.class);
				Map<String,String> props = new HashMap<String, String>();
				List<Region> regionList;
				Region region;
				//根据代码查找城市
				if(ship!=null){
					props.put("id", ship.getProvince());
					regionList = regionDao.listByProps(props);
					region = regionList.get(0);

					qrCode.setProvinceName(region.getRegionName());
					qrCode.setProvinceId(ship.getProvince());

					props.clear();
					props.put("id", ship.getCity());
					regionList = regionDao.listByProps(props);
					region = regionList.get(0);

					qrCode.setCityName("");
					qrCode.setCityId(ship.getCity());
				}

				qrCode.setBanchCompanyName(events.getBranchCompany());
				qrCode.setBanchCompanyId(events.getBranchCompanyId()==null?"":events.getBranchCompanyId());
				qrCode.setOwnedCompanyName(events.getOwnedCompany());
				qrCode.setOwnedCompanyId(events.getOwnedCompanyId()==null?"":events.getOwnedCompanyId());

				qrCode.setCombo(events.getComboName());//套餐名
				qrCode.setLevel(events.getLevel2());
				qrCode.setQRCodeStatus("2");//已生成
				qrCode.setKeyword(eventKey);
				qrCode.setExpectNum(""+events.getHeadcount());//预计人数
				//qrCode.setExpiryDate(new Date());//预计时间
				qrCode.setCreateUserName(events.getCreateUserName());
				qrCode.setCreateUserId(events.getCreateUserId()==null?"":events.getCreateUserId());
				qrCode.setIsDelete("0");

				//保存到数据库
				dao.save(qrCode);
				flag = true;
			}
		}

		return flag;
	}

}
