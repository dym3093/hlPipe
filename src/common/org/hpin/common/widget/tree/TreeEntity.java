package org.hpin.common.widget.tree;

/**
 * 树实体
 * 
 * @author thinkpad
 * @May 7, 2009
 */
public class TreeEntity {

	/**
	 * 树每一个节点编号
	 */
	protected String id;

	/**
	 * 树每一个节点名称
	 */
	protected String text;

	/**
	 * 是否叶子
	 */
	protected boolean leaf = false;

	/**
	 * 链接
	 */
	protected String url;

	/**
	 * 是否展开
	 */
	protected boolean expanded = false;

	/**
	 * 父ID
	 */
	protected String parentId = null;
	
	protected String iconCls = null;

	public TreeEntity() {

	}

	public TreeEntity(Object id, String text, Integer isLeaf) {
		this.id = id.toString();
		this.text = text;
		isLeaf = isLeaf == null ? 0 : isLeaf;
		this.leaf = isLeaf == 1 ? true : false;
	}

	public TreeEntity(Object id, String text, Integer isLeaf, String url) {
		this.id = id.toString();
		this.text = text;
		isLeaf = isLeaf == null ? 0 : isLeaf;
		this.leaf = isLeaf == 1 ? true : false;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
}
