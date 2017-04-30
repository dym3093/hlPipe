package org.hpin.common.widget.pagination;

import java.util.List;

import org.hpin.common.util.HttpTool;

/**
 * 分页类
 * 
 * @author thinkpad
 * 
 * @param <Entity>
 */
public class Page<Entity> {

	private int pageNum = 1; // 当前页开始

	private int pageSize = 10; // 记录每页的数据个数

	private long totalCount; // 总记录数

	private List results;

	private Long offset = null;

	public Page() {

	}

	/**
	 * @param pageNum
	 *            页号
	 * @param pageSize
	 *            页长度
	 */
	public Page(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/**
	 * 
	 * @param pageNum
	 *            页号
	 * @param pageSize
	 *            页长度
	 * @param totalCount
	 *            总记录
	 * @param results
	 *            对象集合
	 */
	public Page(int pageNum, int pageSize, long totalCount, List results) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.results = results;
	}

	public Page(Integer pageNum) {
		if (HttpTool.getBooleanParameter("portal", false)) {
			pageSize = 5;
		}
		this.pageNum = pageNum;
	}

	/**
	 * 是否有第一页
	 * 
	 * @return
	 */
	public boolean isHasFirst() {
		if (getPageNum() <= 1)
			return false;
		return true;
	}

	/**
	 * 是否有最后一页
	 * 
	 * @return
	 */
	public boolean isHasLast() {
		if (getPageNum() >= getTotalPageCount())
			return false;
		return true;
	}

	/**
	 * 是否有下一页
	 * 
	 * @return
	 */
	public boolean isHasNext() {
		return (this.pageNum < this.getTotalPageCount());
	}

	/**
	 * 是否有前一页
	 * 
	 * @return
	 */
	public boolean isHasPrevious() {
		return (this.pageNum > 1);
	}

	/**
	 * 获取最大记录数
	 * 
	 * @return
	 */
	public int getTotalPageCount() {
		Long maxPage = 0l;
		maxPage = getTotalCount() / getPageSize();
		if (getTotalCount() % getPageSize() != 0)
			maxPage++;
		return maxPage.intValue();

	}

	/**
	 * 获取当前页
	 * 
	 * @return
	 */
	public int getPageNum() {
		if (pageNum < 1) {
			pageNum = 1;
		} else if (pageNum > this.getTotalPageCount()
				&& this.getTotalPageCount() >= 1) {
			pageNum = this.getTotalPageCount();
		}
		return pageNum;
	}

	/**
	 * 设置页号
	 * 
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 获取页长度
	 * 
	 * @return
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 设置页长度
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获取对象集合
	 * 
	 * @return
	 */
	public List getResults() {
		return results;
	}

	/**
	 * 设置对象集合
	 * 
	 * @param results
	 */
	public void setResults(List results) {
		this.results = results;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}
}
