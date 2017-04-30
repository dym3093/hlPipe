package org.hpin.common.widget.pagination.plugin;

import java.util.Map;

/**
 * 分页插件抽象类
 * 
 * @author thinkpad
 */
@SuppressWarnings("unchecked")
public abstract class BasePlugin implements IPlugin {
	protected Map parsMap = null;

	public BasePlugin(Map parsMap) {
		this.parsMap = parsMap;
	}

	public Map getParsMap() {
		return parsMap;
	}

	public void setParsMap(Map parsMap) {
		this.parsMap = parsMap;
	}
}
