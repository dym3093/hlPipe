package org.hpin.base.region.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hpin.base.dict.service.ID2NameIService;
import org.hpin.base.region.dao.RegionDao;
import org.hpin.base.region.entity.Region;
import org.hpin.common.core.orm.BaseEntity;
import org.hpin.common.core.orm.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "org.hpin.base.region.service.RegionService")
@Transactional()
public class RegionService extends BaseService  implements ID2NameIService {

	@SuppressWarnings("unused")
	@Autowired
	private RegionDao regionDao;

	/**
	 * 根据父节点，找到子行政区划
	 * @return
	 */
	public List<Region> findRegionByParentId(String parentId) {
		return regionDao.findRegionByParentId(parentId);
	}
	
	public List<Region> findRegionByDeep(int deep) {
		return regionDao.findRegionByDeep(deep);
	}
	
	public String id2Name(String id, String beanId) {
		// TODO Auto-generated method stub
		Region  region = (Region) regionDao.findById(Region.class, id);
		return region.getRegionName();
	}
	
	public List<Region> findRegionsByParams(boolean isProvince){
		return regionDao.findRegionsByParams(isProvince) ;
	}
	
	public List<Region> findByProvinceForTrees() {
		StringBuffer hql = new StringBuffer();
		hql.append("from Region se where se.parentId='0' order by se.id asc");
		return regionDao.findByHql(hql);
	}
	public Map findByCityForTrees() {
		StringBuffer hql = new StringBuffer();
		Map cityMap = new HashMap();
		hql.append("from Region se where substr(se.parentId,3,6)='0000' order by se.id asc");
		List<Region> list = regionDao.findByHql(hql);
		if (list != null && list.size() > 0) {
			for (Region region : list) {
				cityMap.put(region.getParentId(), region.getId()+","+region.getRegionName());
			}
		}
		return cityMap;
	}
	public List findByCityForTree() {
		StringBuffer hql = new StringBuffer();
		Map cityMap = new HashMap();
		hql.append("from Region se where substr(se.parentId,3,6)='0000' order by se.id asc");
		List<Region> list = regionDao.findByHql(hql);
		return list;
	}
	public Map findByDistrictForTrees() {
		StringBuffer hql = new StringBuffer();
		Map districtMap = new HashMap();
		hql.append("from Region se where substr(se.parentId,5,6)='00' order by se.id asc");
		List<Region> list = regionDao.findByHql(hql);
		if (list != null && list.size() > 0) {
			for (Region region : list) {
				districtMap.put(region.getParentId(), region.getId()+","+region.getRegionName());
			}
		}
		return districtMap;
	}
	
	public List<Region> findbyAllRegion(){
		List<Region> list = regionDao.findAll(Region.class, "id", true);
		return list;
	}

	public List<Region> findByDeepAndIds(String ids, int i) {
		StringBuffer hql = new StringBuffer();
		hql.append("from Region se where se.id in("+ids+") and se.deep = ? order by se.id asc");
		return  regionDao.findByHql(hql,new Object[]{i});
	}
	public List<Region> findByParentId(String parentId) {
		return regionDao.findByProperty(Region.class, "parentId", parentId, "regionName", true);
	}
	
	@SuppressWarnings("unchecked")
	public List<Region> findByDeep(int deep){
		return regionDao.findByProperty(Region.class, "deep", deep, "id", true);
	}
	
	@SuppressWarnings("unchecked")
	public BaseEntity findById(String id){
		return regionDao.findById(Region.class,id);
	}
}
