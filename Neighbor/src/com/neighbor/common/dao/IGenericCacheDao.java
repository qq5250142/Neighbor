
package com.neighbor.common.dao;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author benlin
 * @created 2010-5-18
 */

public interface IGenericCacheDao<T, ID extends Serializable> {

	public T get(ID id);
	
	public T get(ID id,Class object,String name);
	
	public T get(ID id,List<String> name,List<Class> object);

	public boolean add(T t) ;

	public boolean update(T t);

    public boolean delete(T t);
    
    public boolean delete(ID id);
}