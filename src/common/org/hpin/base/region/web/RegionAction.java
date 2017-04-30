package org.hpin.base.region.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hpin.base.region.entity.Region;
import org.hpin.base.region.service.RegionService;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;
import org.hpin.common.widget.pagination.Page;
import org.hpin.common.widget.tree.TreeEntity;
import org.json.simple.JSONArray;

/**
 * 行政区划Action
 * 
 * @author duanguowei
 * @data 2013-4-19
 */
@Namespace("/system")
@Action("region")
@Results( {
		@Result(name = "indexRegion", location = "/WEB-INF/content/system/code/region/indexRegion.jsp"),
		@Result(name = "listRegion", location = "/WEB-INF/content/system/code/region/listRegion.jsp"),
		@Result(name = "lookupCityTree", location = "/WEB-INF/content/system/code/region/lookupCityTree.jsp"),
		@Result(name = "addRegion", location = "/WEB-INF/content/system/code/region/addRegion.jsp"),
		@Result(name = "modifyRegion", location = "/WEB-INF/content/system/code/region/modifyRegion.jsp"),
		@Result(name = "lookupHisTree", location = "/WEB-INF/content/system/code/region/lookupHisTree.jsp"),
		})
public class RegionAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private RegionService regionService = (RegionService) SpringTool.getBean(RegionService.class);
	
	private Region region;

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * 转到行政区划列表页面
	 * @return
	 */
	public String indexRegion() {
		
		return "indexRegion";
	}
	
	/**
	 * 显示行政区划维护管理树
	 */
	public String tree() throws Exception {
		List jsonList = new ArrayList();
		List<Region> list = regionService.findAll("id",true);
		for (int i = 0; i < list.size(); i++) {
			Region region = (Region) list.get(i);
			TreeEntity tree = new TreeEntity(region.getId().toString(), region.getRegionName(), 1, region.getParentId());
			jsonList.add(tree);
		}
		return super.json(jsonList);
	}
	
	/**
	 * 生成模块树JSON
	 * @return
	 * @throws Exception
	 */
	public String treeRegion() throws Exception{
		StringBuffer json = new StringBuffer("[") ;
		List<Region> regionList = regionService.findRegionByParentId("0") ;
		Region region = null ;
		if(regionList.size() > 0){
			for(int i = 0 ; i < regionList.size() ; i ++){
				region = regionList.get(i) ;
				json.append("{\"text\":\"" + region.getRegionName() + "\",\"id\":\"" + region.getId() + "\",\"leaf\":" + false) ;
				json.append("},") ;
			}
		}
		if (json.toString().endsWith(",")) {
			json = json.delete(json.length() - 1, json.length());
		}

		json.append("]");
		this.jsonString = json.toString();
		return "json";
	}
	/**
	 * 生成子模块树JSON
	 * @return
	 * @throws Exception
	 */
	public String treeRegionDispose() throws Exception{
		StringBuffer json = new StringBuffer("[") ;
		String parentId = HttpTool.getParameter("parentId") ;
		//加载叶子节点下的button
		List<Region> regionList =  regionService.findRegionByParentId(parentId)  ;
		Region region = null ;
		if(regionList.size() > 0){
			for(int i = 0 ; i < regionList.size() ; i ++){
				region = regionList.get(i) ;
				json.append("{\"text\":\"" + region.getRegionName() + "\",\"id\":\"" + region.getId() + "\",\"leaf\":" + true) ;
				json.append("},") ;
			}
		}
		if (json.toString().endsWith(",")) {
			json = json.delete(json.length() - 1, json.length());
		}

		json.append("]");
		this.jsonString = json.toString();
		return "json";
	}
	
	/**
	 * 转到行政区划列表页面
	 * @return
	 * @throws ParseException 
	 */
	public String listRegion() throws ParseException {
		Map searchMap = super.buildSearch();
		String parentId = HttpTool.getParameter("parentId");
		if (parentId == null) parentId = "0";
		page = new Page(HttpTool.getPageNum());
		searchMap.put("filter_and_parentId_EQ_S" , parentId);
		searchMap.put("order_id" , "asc") ;
		regionService.findByPage(page, searchMap);
		HttpTool.setAttribute("parentId",parentId);
		return "listRegion";
	}
	
	/**
	 * 转到新增区划页面
	 * @return
	 */
	public String addRegion() {
		String parentId = HttpTool.getParameter("parentId");
		Region parent = null;
		if (parentId != null) {
		    parent = (Region)regionService.findById(parentId);
		}
		HttpTool.setAttribute("parent", parent);
		return "addRegion";
	}
	
	/**
	 * 保存行政区划方法
	 * @return
	 */
	public String saveRegion() {
		if(region.getParentId() == null) region.setParentId("0");
		regionService.save(region);
		return jump("region!listRegion.action?parentId=" + region.getParentId());
	}
	
	/**
	 * 转到修改行政区划页面
	 * @return
	 */
	public String modifyRegion() {
		String id = HttpTool.getParameter("id");
		Region region = (Region)regionService.findById(id);
		Region parent = (Region)regionService.findById(region.getParentId());
		region.setParentName(parent.getParentName());
		HttpTool.setAttribute("region", region);
		return "modifyRegion";
	}
	
	/**
	 * 修改行政区划
	 * @return
	 */
	public String updateRegion() {
		regionService.update(region);
		return jump("region!listRegion.action?parentId=" + region.getParentId());
	}
	
	public String lookupCityTree() {
		
		String areasStr = HttpTool.getParameter("areas") ;
		String[] areas = null;
		if(null!=areasStr){
			areas = areasStr.split(",") ;
		}
		
		List<Region> provinceList = regionService.findRegionByDeep(1);
		List<Region> cityList = regionService.findRegionByDeep(2);
		List<Region> areaList = regionService.findRegionByDeep(3);
		Map<String, List> areaMap = new HashMap<String, List>();
		Map<String, List> cityMap = new HashMap<String, List>();
		//实例化成已市为键的存储区县的Map
		for(Region city : cityList){
			List<Region> list = new LinkedList<Region>();
			areaMap.put(city.getId(), list);
		}
		//为区县Map添加数据
		for(Region area : areaList){
			List<Region> list= areaMap.get(area.getParentId());
			list.add(area);
			areaMap.put(area.getParentId(), list);
		}
		//实例化成已省为键的存储市的Map
		for(Region province : provinceList){
			List<Region> list = new LinkedList<Region>();
			cityMap.put(province.getId(), list);
		}
		//为市Map添加数据
		for(Region city : cityList){
			List<Region> list= cityMap.get(city.getParentId());
			list.add(city);
			cityMap.put(city.getParentId(), list);
		}
		//json封装数据
		JSONArray json = new JSONArray();
		if(provinceList.size() > 0){
			for(Region province : provinceList){
				JSONObject jsonProvince = new JSONObject();
				jsonProvince.accumulate("id",province.getId());
				jsonProvince.accumulate("pId",province.getParentId());
				jsonProvince.accumulate("name",province.getRegionName());
				JSONArray jsonCity = new JSONArray();
				List<Region> lcity = cityMap.get(province.getId());
				if(null!=lcity){
					for(Region city : lcity){
						JSONObject jCity = new JSONObject();
						jCity.accumulate("id",city.getId());
						jCity.accumulate("pId", city.getParentId());
						jCity.accumulate("name", city.getRegionName());
						JSONArray jsonArea = new JSONArray();
						List<Region> larea = areaMap.get(city.getId());
						if(null!=larea){
							for(Region area : larea){
								JSONObject jArea = new JSONObject();
								jArea.accumulate("id", area.getId());
								jArea.accumulate("pId", area.getParentId());
								jArea.accumulate("name", area.getRegionName());
								jArea.accumulate("isCheck", false) ;
								if(null!=areas){
									for(String str : areas){
										if(StringUtils.isNotBlank(str) && str.equals(area.getId())){
											jArea.accumulate("isCheck", true) ;
											break ;
										}
									}
								}
								jsonArea.add(jArea);
							}
						}
						jCity.accumulate("area", jsonArea);
						jsonCity.add(jCity);
					}
				}
				jsonProvince.accumulate("city", jsonCity);
				json.add(jsonProvince);
			}
		}
		HttpTool.setAttribute("data" , json.toString());
		return "lookupCityTree";
	}
	
	
	public String findRegionByParentId() {
		String parentId = HttpTool.getParameter("parentId");
		JSONObject jsonObject = new JSONObject();
		StringBuffer json = new StringBuffer("[") ;
		List<Region> regionList = regionService.findRegionByParentId(parentId);
		if(regionList.size() > 0){
			for(Region region : regionList){
				json.append("{'id':'" + region.getId() + "','pId':'" + region.getParentId() + "','name':'" + region.getRegionName()+"'") ;
				json.append("},");
			}
		}
		if (json.toString().endsWith(",")) {
			json = json.delete(json.length() - 1, json.length());
		}

		json.append("]");
		jsonObject.put("isFree", json.toString());
		renderJson(jsonObject);
		
		return null;
	}
	public String ajaxByParentId(){
		String parentId = HttpTool.getParameter("parentId");
		List<Region> list = regionService.findByParentId(parentId);
		
		JSONArray json = new JSONArray();
		for(Region r:list){
			JSONObject ja = new JSONObject();
			ja.accumulate("id", r.getId());
			ja.accumulate("name", r.getRegionName());
			json.add(ja);
		}
		printObject(json.toString());
		return null;
	}
}
