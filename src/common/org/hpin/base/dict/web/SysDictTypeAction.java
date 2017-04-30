package org.hpin.base.dict.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.JSONUtil;
import org.hpin.base.dict.entity.SysDictType;
import org.hpin.base.dict.service.SysDictTypeService;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.ui.util.HpinCommonsUIListItem;
import org.hpin.common.util.HttpTool;
import org.hpin.common.util.StaticVariable;

@SuppressWarnings("serial")
@Namespace("/hpin")
@Action("sysDictType")
@Results( {
	@Result(name = "getNodes", location = "/WEB-INF/content/system/dict/sysDictType.jsp")
}
)
public final class SysDictTypeAction extends BaseAction {

	SysDictTypeService sysDictTypeService = (SysDictTypeService)SpringTool.getBean(SysDictTypeService.class);
	SysDictType sysDictType;
	// AJAX方式进行搜索请求时的数据处理
	

	public String xsearch()
			throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String dictid = request.getParameter("dictId");
		HttpServletResponse response = ServletActionContext.getResponse() ;
		List _objDictTypeList = new ArrayList();
		_objDictTypeList = sysDictTypeService.getDictSonsByDictid(dictid);
		List itemList = new ArrayList();

		for (Iterator rowIt = _objDictTypeList.iterator(); rowIt.hasNext();) {
			SysDictType dictType = (SysDictType) rowIt.next();
			HpinCommonsUIListItem uiitem = new HpinCommonsUIListItem();
			uiitem.setItemId(String.valueOf(dictType.getId()));
			uiitem.setText(dictType.getDictName());
			uiitem.setValue(dictType.getDictId());
			itemList.add(uiitem);
		}

		response.setContentType("text/xml;charset=UTF-8");

		// 返回JSON对象 
		response.getWriter().print(JSONUtil.serialize(itemList));
		return null;
	}
	
	public String execute()
			throws Exception {
			return "getNodes";
	}

	//**
	//根据父节点的id得到所有子节点的JSON数据 070723
	//*
	public String getNodes()
			throws Exception {
		String nodeId = HttpTool.getParameter("node");
		ArrayList list = new ArrayList();

		
		list = sysDictTypeService.getDictSonsByDictid(nodeId);
		
		JSONArray jsonRoot = new JSONArray();
		HttpServletResponse response = ServletActionContext.getResponse() ;
		for (Iterator rowIt = list.iterator(); rowIt.hasNext();) {
			SysDictType _objDictType = (SysDictType) rowIt.next();

			JSONObject jitem = new JSONObject();
			jitem.put("id", _objDictType.getDictId());
			jitem.put("dictId", _objDictType.getDictId());
			jitem.put("parentDictId", _objDictType.getParentDictId());
			jitem.put("text", _objDictType.getDictName());
			jitem.put("allowChild", "true");
			jitem.put("allowDelete", "true");
			jitem.put("qtip", "代码:" + _objDictType.getDictId() + "<br \\/>取值:"
					+ _objDictType.getDictCode() + "<br \\/>备注:"
					+ _objDictType.getDictRemark());
			jitem.put("qtipTitle", _objDictType.getDictName());
			jitem.put("leaf", _objDictType.getLeaf());
	
			jsonRoot.add(jitem);
		}

		response.setContentType("text/xml;charset=UTF-8");
		response.getWriter().print(jsonRoot.toString());
		return null;
	} 

	public String xsave()
			throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse() ;
		boolean isNew = ("".equals(id) || id == null);
		
		if (isNew) {

			if (null==sysDictType.getParentDictId() || "".equals(sysDictType.getParentDictId())
					|| sysDictType.getParentDictId().equals("-1")) {
				String firstdictid = sysDictTypeService.getMaxDictid("1");
				sysDictType.setParentDictId("-1");
				sysDictType.setLeaf(Integer.valueOf(StaticVariable.LEAF));
				sysDictType.setDictId(firstdictid);
				sysDictType.setSysType(new Integer(0));
				sysDictType.setDictCode(firstdictid);
			} else {
				String newdictid = sysDictTypeService.getMaxDictid(sysDictType.getParentDictId());
				SysDictType dictType = new SysDictType();
				dictType = sysDictTypeService.getDictTypeByDictid(sysDictType.getParentDictId());
				if (sysDictType.getDictId() == null
						|| sysDictType.getDictId().equals("")) {
					sysDictType.setDictId(newdictid);
				}

				// 如果不填dictCode就赋dictId的值
				if (null == sysDictType.getDictCode()
						|| "".equals(sysDictType.getDictCode())) {
					sysDictType
							.setDictCode(sysDictType.getDictId());
				} else {
					// 判断用户填写的code是否存在
					if (sysDictTypeService.isCodeExist(sysDictType.getDictCode(),
							sysDictType.getDictId())) {
						return "字典类型值为【"
						+ sysDictType.getDictCode()
						+ "】的字典项已经存在";
					}
				}

				dictType.setLeaf(Integer.valueOf(StaticVariable.NOTLEAF));
				sysDictType.setSysType(new Integer(dictType.getSysType()
						.intValue() + 1));
				sysDictType.setLeaf(Integer.valueOf(StaticVariable.LEAF));
				sysDictTypeService.saveHpinSystemDictType(dictType);
			}
			sysDictTypeService.saveHpinSystemDictType(sysDictType);
			HttpTool.setAttribute("lastNewId", sysDictType.getParentDictId());
		} else {
			// 保存编辑的部门id，使转向后可刷新树图上的相应节点
			HttpTool.setAttribute("lastEditId", sysDictType.getDictId());
		}
		return null;
	}

	//**
	 //* ajax请求获取某节点的详细信息。
	 //*
	public String xget()
			throws Exception {
		String _strId = HttpTool.getParameter("id");
		SysDictType _objOne = (SysDictType)sysDictTypeService
				.getDictTypeByDictid(_strId);

		JSONObject jsonRoot = new JSONObject();
		jsonRoot = JSONObject.fromObject(_objOne);
		HttpServletResponse response = ServletActionContext.getResponse() ;
		response.setContentType("text/xml;charset=UTF-8");
		response.getWriter().print(jsonRoot.toString());
		return null;
	}

	//**
	 // ajax请求修改某节点的详细信息。 
	 //*
	public String xedit()
			throws Exception {


		if (sysDictType.getId() != null) {
			// 如果不填dictCode就赋dictId的值 edit by 
			if (null == sysDictType.getDictCode()
					|| "".equals(sysDictType.getDictCode())) {
				sysDictType.setDictCode(sysDictType.getDictId());
			} else {
				// 判断用户填写的code是否存在
				if (sysDictTypeService.isCodeExist(sysDictType.getDictCode(),
						sysDictType.getDictId())) {
					return  "字典类型值为【"
					+ sysDictType.getDictCode() + "】的字典项已经存在";
				}
			}
			//将修改过的字典放到缓存中
			sysDictTypeService.saveHpinSystemDictType(sysDictType);
		}

		return null;
	}

	//**
	 //根据模块或功能的编码，删除该对象。
	// * 页面访问：http://hostname:port/webappname/hpinSystemDictTypes.html?method=xdelete&id=inputvalue
	// *//*
	public String xdelete()
			throws Exception {
		String _strId = HttpTool.getParameter("id");
		SysDictType dicttype = new SysDictType();
		dicttype = sysDictTypeService.getDictTypeByDictid(_strId);
		String parentdictid = dicttype.getParentDictId();
		//将该字典在缓存中删除
		sysDictTypeService.removeDictByDictid(_strId);
		boolean flag = sysDictTypeService.isHaveSameLevel(parentdictid, String
				.valueOf(dicttype.getSysType()));
		if (!flag) {
			sysDictTypeService.updateParentDictLeaf(parentdictid, StaticVariable.STRLEAF);
		}
		return null;
	}

	/**
	 * 字段树ON
	 * @return
	 * @throws Exception
	 */
	public String treeRegion() throws Exception{
		StringBuffer json = new StringBuffer("[") ;
		String parentdictid = HttpTool.getParameter("defaultID");
		List<SysDictType> hpinSystemDictTypeList = sysDictTypeService.getDictSonsByDictid(parentdictid) ;
		if(hpinSystemDictTypeList.size() > 0){
			for(int i = 0 ; i < hpinSystemDictTypeList.size() ; i ++){
				SysDictType hpinSystemDictType = new SysDictType();
				hpinSystemDictType = hpinSystemDictTypeList.get(i) ;
				json.append("{\"text\":\"" + hpinSystemDictType.getDictName() + "\",\"id\":\"" + hpinSystemDictType.getDictId()+ "\",\"leaf\":" + false) ;
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
		List<SysDictType> dictList =  sysDictTypeService.getDictSonsByDictid(parentId)  ;
		SysDictType hpinSystemDictType = null ;
		if(dictList.size() > 0){
			for(int i = 0 ; i < dictList.size() ; i ++){
				hpinSystemDictType = dictList.get(i) ;
				json.append("{\"text\":\"" + hpinSystemDictType.getDictName() + "\",\"id\":\"" + hpinSystemDictType.getDictId() + "\",\"leaf\":" + true) ;
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
	
	public SysDictType getSysDictType() {
		return sysDictType;
	}

	public void setSysDictType(SysDictType sysDictType) {
		this.sysDictType = sysDictType;
	}

}
