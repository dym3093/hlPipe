package org.hpin.base.dict.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.hpin.base.dict.service.DictService;
import org.hpin.base.dict.service.IDictItem;
import org.hpin.base.dict.util.Util;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;



/**
 * <p>
 * Title:字典action
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2007-10-30 9:42:21
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 */
public class DictAction extends BaseAction {
    /**
     * xml的下拉框字典关联
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String forXML()
            throws Exception {
        //字典关系id
        String relationId =  HttpTool.getParameter("relationId");
        //字典key值
        String key = HttpTool.getParameter("key");

        //下拉框选择的值
        String value = HttpTool.getParameter( "value");
        if (value == null || "".equals(value.trim())) {
            return null;
        }

        DictService dictService = (DictService) SpringTool.getBean("DictService");
        //取关联结果
        List list = dictService.getRelatedList(value, Util.constituteDictId(
                key, relationId));
        JSONObject j = new JSONObject();
        JSONArray json = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (Iterator it = list.iterator(); it.hasNext();) {
                IDictItem item = (IDictItem) it.next();
                JSONObject jitem = new JSONObject();
                jitem.put("text", item.getItemName());
                jitem.put("value", item.getItemId());
                json.add(jitem);
            }
        }
        j.put("rows", json);
        j.put("results", list.size());
        //转成json
        HttpServletResponse response = ServletActionContext.getResponse() ;
        response.setContentType("text/xml;charset=UTF-8");
        //将对象写入selectId对映的下拉框
        response.getWriter().print(j.toString());
        return null;
    }
}
