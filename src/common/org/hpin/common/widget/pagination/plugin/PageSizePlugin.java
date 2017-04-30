package org.hpin.common.widget.pagination.plugin;

import java.util.Map;

import org.hpin.common.widget.pagination.Page;



/**
 *<p>@Desc：创建每页显示条数的分页插件</p> 
 *
 *<p>@author : 胡五音</p>
 *
 *<p>@CreateDate：May 8, 2012 3:51:26 PM</p>
 *<p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>  
 */

public class PageSizePlugin extends BasePlugin {
	
	private final static int DEFAULT_PAGER_INPUT_MAXLENGTH = 5 ;

	private final static int DEFAULT_PAGER_INPUT_SIZE = 1 ;
	
	public PageSizePlugin(Map parsMap) {
		super(parsMap) ;
	}

	public String outputHtml() {
		Page page = (Page)parsMap.get("page") ;
		StringBuffer sb = new StringBuffer() ;
		sb.append(" 每页") ;
		sb.append("<input type = 'text' size = '").append(DEFAULT_PAGER_INPUT_SIZE).append("' maxLength = '") ;
		sb.append(DEFAULT_PAGER_INPUT_MAXLENGTH).append("' onChange = 'setPageSize(this.value);opendataSetPageNum(1);opendataPageSubmit();' value = '")
			.append(page.getPageSize()).append("' />") ;
		sb.append("条记录") ;
		return sb.toString() ;
	}

}
