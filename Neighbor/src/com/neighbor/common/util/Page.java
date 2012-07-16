package com.neighbor.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.caucho.server.http.HttpServletRequestImpl;




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

	/*鍒濆鍖栧垎椤�
	 * */
	public Page() {
		this(new ArrayList<T>(),0,0);
	}
	
	/*鍒濆鍖栧垎椤�
	 * @data 鏁版嵁
	 * @pageSize 涓�〉澶氬皯琛屾暟
	 * @total	鎬昏鏁�
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
	
	/*鍒濆鍖栧垎椤�
	 * @data 鏁版嵁
	 * @curPageNo 褰撳墠椤电爜
	 * @pageSize 涓�〉澶氬皯琛屾暟
	 * @total	鎬昏鏁�
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
	/*鑾峰彇鍒嗛〉椤佃剼
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
				this.footer="棣栭〉&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo+1)+"'>涓嬩竴椤�/a>&nbsp;&nbsp;<a href='"+url+"pageno="+this.pageCount+"'>鏈〉</a></div>";
			}
			else if(this.curPageNo==this.pageCount)
			{
				this.footer="<a href='"+url+"pageno=1'>棣栭〉</a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo-1)+"'>涓婁竴椤�/a>&nbsp;&nbsp;鏈〉";
			}
			else
			{		
				this.footer="<a href='"+url+"pageno=1'>棣栭〉</a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo-1)+"'>涓婁竴椤�/a>&nbsp;&nbsp;<a href='"+url+"pageno="+(this.curPageNo+1)+"'>涓嬩竴椤�/a>&nbsp;&nbsp;<a href='"+url+"pageno="+this.pageCount+"'>鏈〉</a>";
			}			
		}
			
			
		
		return footer;
	}	
}
