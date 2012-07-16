package com.neighbor.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataPageForRank<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5261442269274743212L;
	
	/** 数据 */
	private List<T> record;
	/** 整个数据集总记录数 */
	private int totalRecord;
	/** 分页每页大小 */
	private int pageSize;
	/** 当前分页的页码 */
	private int pageNo;
	/** 上次更新时间 */
	private Long lastUpdateTime;
	/** 我的排行 */
	private int myRank;
	
	public DataPageForRank() {
		this.record = new ArrayList<T>();
		this.totalRecord = 0;
		this.pageSize = 1;
		this.pageNo = 1;
		this.lastUpdateTime = (new Date()).getTime();
		this.myRank = 0;
	}

	public DataPageForRank(List<T> record, int totalRecord, int pageSize,
			int pageNo, Long lastUpdateTime, int myRank) {
		this.record = record;
		this.totalRecord = totalRecord;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.lastUpdateTime = lastUpdateTime;
		this.myRank = myRank;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<T> getRecord() {
		return record;
	}

	public void setRecord(List<T> record) {
		this.record = record;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}
	
	
}
