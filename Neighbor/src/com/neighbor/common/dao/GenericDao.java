
package com.neighbor.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.neighbor.common.util.Page;
/**
 * 
* @ClassName: GenericDao
* @Description: TODO
* @author Melon
* @date 2012-8-25 下午6:12:11
* @param <T>
* @param <ID>
 */
@SuppressWarnings("unchecked")
public class GenericDao<T, ID extends Serializable> extends HibernateDaoSupport
		implements IGenericDao<T, ID> {
	
	protected Logger logger=Logger.getLogger("developLogger");

	protected Class<T> entityClass;

	public GenericDao() {

	}

	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
			logger.debug("T class = " + entityClass.getName());
		}
		return entityClass;
	}


	  /**
	   * 閫氶敓鏂ゆ嫹涓�敓鏂ゆ嫹ID get涓�敓鏂ゆ嫹瀹為敓鏂ゆ嫹
	   */
	public T get(ID id) throws DataAccessException {
		T load = (T) getHibernateTemplate().get(getEntityClass(), id);
		return load;
	}


	public void delete(T t, LockMode lockMode) throws DataAccessException {
		getHibernateTemplate().delete(t, lockMode);
	}
    
	
	public boolean delete(ID id) {
		return this.delete(this.get(id));
	}
	
	public boolean delete(T t){
		boolean result=false;
		try
		{
			getHibernateTemplate().delete(t);
			result=true;
		}
		catch(DataAccessException ex)
		{
			ex.printStackTrace();
		}
		return result;

	}

	public void deleteAll(Collection<T> entities) throws DataAccessException {
		getHibernateTemplate().deleteAll(entities);
	}

	public List<T> findByHql(String hql)
			throws DataAccessException {
		List<T> find = (List<T>) getHibernateTemplate()
				.find(hql);
		return find;
	}

	public List<T> find(String queryString, Object[] values)
			throws DataAccessException {
		List<T> find = (List<T>) getHibernateTemplate().find(queryString,
				values);
		return find;
	}

	public List<T> find(String queryString) throws DataAccessException {
		return (List<T>) getHibernateTemplate().find(queryString);
	}

	public void refresh(T t, LockMode lockMode) throws DataAccessException {
		getHibernateTemplate().refresh(t, lockMode);
	}

	public void refresh(T t) throws DataAccessException {
		getHibernateTemplate().refresh(t);
	}


	public boolean save(T t)  {
		 boolean result=false;
		 try
		 {
			 getHibernateTemplate().save(t);
			 result=true;
		 }
		 catch(DataAccessException ex)
		 {
			 ex.printStackTrace();
		 }
		 return result;
	}


	public void update(T t, LockMode lockMode) throws DataAccessException {
		getHibernateTemplate().update(t, lockMode);
	}

	public boolean update(T t){
		 boolean result=false;
		 try
		 {
			 getHibernateTemplate().update(t);
			 //System.out.print(t.toString());
			 result=true;
		 }
		 catch(DataAccessException ex)
		 {
			 ex.printStackTrace();
		 }
		 return result;
		
	}


	public List<T> list() throws DataAccessException {
		return getHibernateTemplate().loadAll(getEntityClass());

	}

	public List<T> findByNamedQuery(String queryName)
			throws DataAccessException {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	public List<T> findByNamedQuery(String queryName, Object value)
			throws DataAccessException {
		return getHibernateTemplate().findByNamedQuery(queryName, value);
	}

	public List<T> findByNamedQuery(String queryName, Object[] values)
			throws DataAccessException {
		return getHibernateTemplate().findByNamedQuery(queryName, values);
	}
    
	 

	 public Page<T>  findPageByQuery(int pageCurrentNo,String hql,int pageSize,List values,String order){
		 String countQueryString="select count (*)"+hql;
		 List  conutList;
		 if (values!=null&& values.size()>0) {
			conutList=getHibernateTemplate().find(countQueryString, values.toArray());
		}else {
			conutList=getHibernateTemplate().find(countQueryString);
		}
		 
		 long totalCount=0;
		Integer totalCountint=0;
		 if (conutList.size()<1) {
			return new Page();
		}else {
		
			totalCount=((Long)conutList.get(0)).longValue();
		
		}
		 if (totalCount<1) {
			return new Page();
		}
		 
		 		 
		 if (order!=null) {
			hql=hql+" "+order;
		}
		 Session session=getSession();
		 
		 Query query=session.createQuery(hql);
		 if (values!=null) {
			 setQueryParamter(query, values);
		  }
		 
		List<T> list=null;
		try {
			if (pageSize==-100) {
				list=query.list();//閿熸枻鎷风ず鍏ㄩ敓鏂ゆ嫹
			}
				else {
					if (totalCount<pageSize) {
						 list=query.list();
					}else {
						//list=query.setFirstResult(startIndex).setMaxResults(pageSize).list();
					}					
				}
			
		} catch (HibernateException e) {
		}
		finally{
			releaseSession(session);
		}
		 return null; 
	 }
	 

	 public Page  findPageByJoinQuery(int pageCurrentNo,String hql,int pageSize,List values,String order){
		 String countQueryString= hql;
		 List  conutList;
		 if (values!=null&& values.size()>0) {
			conutList=getHibernateTemplate().find(countQueryString, values.toArray());
		}else {
			conutList=getHibernateTemplate().find(countQueryString);
		}
		 
		 long totalCount=0;
		Integer totalCountint=0;
		 if (conutList.size()<1) {
			return new Page();
		}else {
		
			totalCount=(new Long(conutList.size())).longValue();
//			if (totalCount<pageSize) {
//				totalCountint=(Integer)conutList.get(0);
//			}
		
		}
		 if (totalCount<1) {
			return new Page();
		}
		 
		 		 
		 if (order!=null) {
			hql=hql+" "+order;
		}
		 Session session=getSession();
		 
		 Query query=session.createQuery(hql);
		 if (values!=null) {
			 setQueryParamter(query, values);
		  }
		 /*
		 long totalPageCount=Page.getTotalPageCount(totalCount, pageSize);
		 Number num=totalPageCount;
		 if (pageCurrentNo>totalPageCount) {
			 pageCurrentNo=num.intValue();			
		}
		if (pageCurrentNo<1) {
		}
		
		int startIndex=Page.getStartOfPage(pageCurrentNo, pageSize);
		List<T> list=null;
		try {
			if (pageSize==-100) {
				list=query.list();//閿熸枻鎷风ず鍏ㄩ敓鏂ゆ嫹
			}
				else {
					if (totalCount<pageSize) {
						 list=query.list();
					}else {
						list=query.setFirstResult(startIndex).setMaxResults(pageSize).list();
					}
					
				}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		finally{
			releaseSession(session);
		}
		  
		 return new Page(startIndex,totalCount,pageSize,list);*/
		 return null;
	 }

	public void setQueryParamter(Query query,List values){
		
		 for(int i=0;i<values.size();i++){
			 query.setParameter(i, values.get(i));
		 }
		 
	 }
	 
	
	public List<T> findBySql(String sql)
	{
		 Session session=null;			 
		 Transaction tx=null;
		try{  
			session=this.getSession();
			tx=session.beginTransaction();
			SQLQuery sqlQuery=session.createSQLQuery(sql);	 
			sqlQuery.addEntity(this.getEntityClass());
			tx.commit();
			return sqlQuery.list();		
		}catch (Exception e) {
			e.printStackTrace();
			try{
				if(tx!=null)
					tx.rollback();
			}catch (Exception en) {
				en.printStackTrace();
			}
			return null;
		}finally{
			if(session!=null)
				session.close();
				releaseSession(session);
		}
	}
	
    public void executeSql2(String sql){
    	Session session=null;
    	Transaction tx=null;
    	try {
			session=this.getSession();
			tx=session.beginTransaction();
			SQLQuery qurey=session.createSQLQuery(sql);
			qurey.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			if (session!=null) {
				releaseSession(session);
			}
		}finally{
			if (session!=null) {
				session.close();
				releaseSession(session);
			}
		}
    }
	
	 public List<T> findBySql(String sql,T object){
		 
		 Session session=null;
		 
		 Transaction tx=null;
		try{  
		  session=this.getSession();
		   tx=session.beginTransaction();
		 SQLQuery sqlQuery=session.createSQLQuery(sql);
		 if(object != null)	 
			 sqlQuery.addEntity(object.getClass());
		 tx.commit();
		  return sqlQuery.list();
		
		}catch (Exception e) {
			try{
			tx.rollback();
			session.close();
			if (session!=null) {
				session=null;
			}
			}catch (Exception en) {
			}
			return null;
		}finally{
			releaseSession(session);
		}
		
	 }

	public void executeHql(String hql) {
		
		Session session=null;
		Transaction tx=null;
	
		try{  
		   session=this.getSession();
		   tx=session.beginTransaction();
		   Query query=session.createQuery(hql);
		   query.executeUpdate();
		  session.flush();  
		 tx.commit();
		 
		
		}catch (Exception e) {
			try{
			tx.rollback();
			session.close();
			if (session!=null) {
				session=null;
			}
			}catch (Exception en) {
			}
			
		}finally{
			releaseSession(session);
		}
	}

	public List<T> executeHql(String hql, int maxResults) {
		Session session=null;
		List list = null;
		 
		 Transaction tx=null;
		try{  
		  session=this.getSession();
		   tx=session.beginTransaction();
		  Query query = session.createQuery(hql);
		  
		  query.setMaxResults(maxResults);
		  
		  list = query.list();
		  
		 tx.commit();
		
		}catch (Exception e) {
			try{
			tx.rollback();
			session.close();
			if (session!=null) {
				session=null;
			}
			}catch (Exception en) {
			}
			
		}finally{
			releaseSession(session);
		}
		return list;
	}
    
	
	@SuppressWarnings("unchecked")
	 public void executeSql(String sql){
		Session session=null;
		List list = null;
		 
		 Transaction tx=null;
		try{  
		  session=this.getSession();
		   tx=session.beginTransaction();
		  Query query = session.createSQLQuery(sql);
		  query.executeUpdate();
		  //query.setMaxResults(maxResults);
		  
		  //list = query.list();
		  
		 tx.commit();
		
		}catch (Exception e) {
			try{
			tx.rollback();
			session.close();
			if (session!=null) {
				session=null;
			}
			}catch (Exception en) {
			}
			
		}finally{
			releaseSession(session);
		}		
	}

	public List<T> findBySql(String sql, int top, T object) {

		Session session = null;

		Transaction tx = null;
		try {
			session = this.getSession();
			tx = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			if (top != 0) {
				sqlQuery.setFirstResult(0); 
				sqlQuery.setMaxResults(top);
				//sqlQuery.setFetchSize(top);
			}
			if (object != null)
				sqlQuery.addEntity(object.getClass());
			tx.commit();
			return sqlQuery.list();

		} catch (Exception e) {
			try {
				tx.rollback();
				session.close();
				if (session != null) {
					session = null;
				}
			} catch (Exception en) {
			}
			return null;
		} finally {
			releaseSession(session);
		}

	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public List<Object> findObjects(String hql,Object[] values) throws DataAccessException{
		List<Object> list = getHibernateTemplate().find(hql, values);
		return list;
	}

	@SuppressWarnings("deprecation")
	public boolean saveOrUpdate(T t) throws DataAccessException {
		
		boolean result=false;
		try
		{
			getHibernateTemplate().saveOrUpdate(t);
			result=true;
		}
		catch(DataAccessException ex)
		{
			 ex.printStackTrace();
		}
		
//		 boolean result=false;
//		 try
//		 {
//			 getHibernateTemplate().update(t);
//			 result=true;
//		 }
//		 catch(DataAccessException ex)
//		 {
//			 try
//			 {
//				 getHibernateTemplate().save(t);
//				 result=true;
//			 }
//			 catch(DataAccessException saveEx)
//			 {
//				 saveEx.printStackTrace();
//				 if(logger.isEnabledFor(Priority.ERROR)) logger.error("璋冪敤GenericDao.saveOrUpdate,save("+t.toString()+")鍑虹幇閿欒锛岄敊璇唬鐮�+saveEx.getMessage());
//			 }
//			 ex.printStackTrace();
//			 if(logger.isEnabledFor(Priority.DEBUG)) logger.debug("璋冪敤GenericDao.saveOrUpdate,update"+t.toString()+"鍑虹幇閿欒锛岄敊璇唬鐮�+ex.getMessage());
//		 }
		 return result;
	}	
}
