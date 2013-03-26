package com.neighbor.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

public class Page<T> {

	public static int DEFAULT_PAGE_SIZE = 10;
	private static String DEFAULT_CURRENT_PAGE_NO_KEY="pageno";
	private String footer;
	private List<T> data;	
	private Integer curPageNo=1;
	private Integer pageSize=DEFAULT_PAGE_SIZE;
	private Integer pageCount;
	private Integer total;
	
	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<T> getData() {
		pageSize = (pageSize <= 0 ? 10 : pageSize);
		curPageNo = (curPageNo <= 0 ? 1 : curPageNo);	
		int begin = (pageSize * (curPageNo - 1) > data.size() ? data.size(): pageSize * (curPageNo - 1));
		int end = (pageSize * curPageNo > data.size() ? data.size() : pageSize * curPageNo);		
		return data.subList(begin,end);
	}

	public void setCurPageNo(Integer curPageNo) {
		this.curPageNo = curPageNo;
	}

	public static Integer getCurPageNo() {
		if(ServletActionContext.getRequest().getParameter(DEFAULT_CURRENT_PAGE_NO_KEY)==null)
			return 1;
		else
		{
			return Integer.parseInt(ServletActionContext.getRequest().getParameter(DEFAULT_CURRENT_PAGE_NO_KEY));
		}
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotal() {
		return total;
	}

	/*初始化分页 
	 * */
	public Page() {
		this(new ArrayList<T>(),0,0);
	}
	
	/*初始化分页
	 * @data 数据
	 * @pageSize 一页多少行数
	 * @total	总行数 
	 * */
	public Page(List<T> data,Integer pageSize,Integer total)
	{
		this.data=data;
		this.curPageNo=getCurPageNo();
		if(pageSize>0)
			this.pageSize=pageSize;
		this.total=total;
		if(this.total<0) this.pageCount=1;
		else
		{
			if (this.total % this.pageSize == 0) {
				this.pageCount= this.total/this.pageSize;
			} else{
				this.pageCount=this.total/this.pageSize + 1;
			}
		}
	}
	
	/*初始化分页
	 * @data 数据
	 * @curPageNo 当前页码
	 * @pageSize 一页多少行数
	 * @total	总行数 
	 * */
	public Page(List<T> data,Integer curPageno,Integer pageSize,Integer total)
	{
		this.data=data;
		if(curPageno>0)
			this.curPageNo=curPageno;
		if(pageSize>0)
			this.pageSize=pageSize;
		this.total=total;
		if(total<0) this.pageCount=1;
		else
		{
			if (total % pageSize == 0) {
				this.pageCount= total/pageSize;
			} else{
				this.pageCount=total/pageSize + 1;
			}
		}
	}
	/*获取分页页脚
	 * */
	public String getFooter() {
		String url=ServletActionContext.getRequest().getContextPath();
		if(ServletActionContext.getActionMapping().getNamespace().endsWith("/"))
		{
			url=url+ServletActionContext.getActionMapping().getNamespace()+ServletActionContext.getActionMapping().getName()+"."+ServletActionContext.getActionMapping().getExtension();
		}
		else
			url=url+ServletActionContext.getActionMapping().getNamespace()+"/"+ServletActionContext.getActionMapping().getName()+"."+ServletActionContext.getActionMapping().getExtension();
		Map params=ServletActionContext.getRequest().getParameterMap();
		url=url+"?";
		if(params.size()>0)
		{
			String queryString="";
			for(Object key:params.keySet())
			{
				if(!key.equals("pageno"))
				{
					String value=((String[])params.get(key))[0];
					queryString+=key+"="+value+"&";
				}
			}
			url=url+queryString;
		}
		if(this.pageCount<=1)
		{
			this.footer="";
		}
		else if(pageCount==2)
		{
			if(this.curPageNo==1)
			{
				this.footer="1&nbsp;&nbsp;<a href='"+url+"pageno=2'>2</a>";
			}
			else
			this.footer="<a href='"+url+"pageno=1'>1</a>&nbsp;&nbsp;";
		}
		else 
		{
			//first page
			if(this.curPageNo==1)
			{
				this.footer="首页&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo+1)+"'>下一页</a>&nbsp;&nbsp;<a href='"+url+"pageno="+this.pageCount+"'>末页</a></div>";
			}
			else if(this.curPageNo==this.pageCount)
			{
				this.footer="<a href='"+url+"pageno=1'>首页</a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo-1)+"'>上一页</a>&nbsp;&nbsp;末页";
			}
			else
			{		
				this.footer="<a href='"+url+"pageno=1'>首页</a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo-1)+"'>上一页</a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo+1)+"'>下一页</a>&nbsp;&nbsp;<a href='"+url+"pageno="+this.pageCount+"'>末页</a>";
			}			
		}
			
			
		
		return footer;
	}	
}
