package org.hpin.common.widget.tree;

public class CheckTreeEntity extends TreeEntity {

	private Boolean checked = null;

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public CheckTreeEntity(Object id, String text, Integer isLeaf, String url) {
		this.id = id.toString();
		this.text = text;
		isLeaf = isLeaf == null ? 0 : isLeaf;
		this.leaf = isLeaf == 1 ? true : false;
		this.url = url;
	}

}
