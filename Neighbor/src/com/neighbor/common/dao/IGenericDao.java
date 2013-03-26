
package com.neighbor.common.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 
* @ClassName: IGenericDao
* @Description: TODO
* @author Melon
* @date 2012-8-25 下午6:12:34
* @param <T>
* @param <ID>
 */
public interface IGenericDao<T, ID extends Serializable> {
	
	  public T get(ID id)throws DataAccessException;   		
	  public boolean save(T t) throws DataAccessException;
	  public boolean saveOrUpdate(T t) throws DataAccessException;
	  public boolean update(T t) throws DataAccessException;
	  public boolean delete(T t) throws DataAccessException;
	  public boolean delete(ID id) throws DataAccessException;
	  public List<T> findByHql(String hql) throws DataAccessException;
	  public List<T> find(String queryString, Object[] values);
	  public void executeHql(String hql);
	  public List<T> findBySql(String sql);
	  public List<T> findBySql(String sql,int top,T object);
	  public void executeSql(String sql);
	  public List<Object>  findObjects(String hql, Object[] values);
}