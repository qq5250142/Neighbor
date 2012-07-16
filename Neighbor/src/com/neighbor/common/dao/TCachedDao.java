package com.neighbor.common.dao;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.qq.DCache.WapGame.DcacheValueHolder;
import com.qq.cp.cache.TCacheClient;
import com.qq.cp.cache.TDCacheException;

/**
 * 
 * @author benlin
 * 
 * @param <T>
 * @param <ID>
 */
@SuppressWarnings("unchecked")
public class TCachedDao<T, ID extends Serializable> implements IGenericCacheDao<T, ID> {
	protected Logger logger=Logger.getLogger("developLogger");
	protected Class<T> entityClass;
	protected TCacheClient c = TCacheClient.getInstance();

	
	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityClass;
	}

	private String getKey(T t)
	{
		return this.getBussiId()+"|"+this.getId(t);
	}
	
	private String getKeyById(ID id)
	{
		return this.getBussiId()+"|"+id;
	}	

	public String getId(T t)
	{
		return "";
	}
	public String getBussiId()
	{
		return "";
	}	
	public boolean add(T t) {
		String key=this.getKey(t);
		if(key.isEmpty())
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("error:key is empty.");
			return false;
		}		
		String value = JSONObject.fromObject(t).toString();
		
		int iRet=0;
		try
		{			
			iRet = c.setCache(key, value.getBytes("GBK")); 
			if (iRet == 0||iRet==7){
				return true;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->add(T)出现错误,错误代码:"+iRet);
				return false;
			}
		}
		catch(Exception e)
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("testAdd catch exp:"+e);
			return false;
		}
	}

	public boolean delete(T t) {
		String key=this.getKey(t);
		if(key.isEmpty())
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("error:key is empty.");
			return false;
		}
		int iRet=0;
		try
		{			
			iRet = c.delCache(key); 
			if (iRet == 0){
				return true;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->delete(T)出现错误,错误代码:"+iRet);
				return false;
			}
		}
		catch(Exception e)
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->delete(T)出现错误:"+e.getMessage());
			return false;
		}
	}
	
	public boolean delete(ID id) {
		String key=this.getKeyById(id);
		if(key.isEmpty())
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("error:key is empty.");
			return false;
		}
		int iRet=0;
		try
		{			
			iRet = c.delCache(key); 
			if (iRet == 0){
				return true;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->delete("+id+")出现错误,错误代码:"+iRet);
				return false;
			}
		}
		catch(Exception e)
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->delete("+id+")出现错误:"+e.getMessage());
			return false;
		}
	}	

	public T get(ID id){
		String key=this.getBussiId()+"|"+id.toString();
		DcacheValueHolder valueHodler = new DcacheValueHolder();
		int iRet=0;
		try {
			iRet=c.getCache(key, valueHodler);
			if(iRet==0)
			{
				String valueItem = new String(valueHodler.value,"GBK");
				JSONObject jsonObject=JSONObject.fromObject(valueItem);
				
				T result=(T)JSONObject.toBean(jsonObject, this.getEntityClass());
				return result;
			}
			else if(iRet == 1||iRet==-1)
			{
				//if(logger.isEnabledFor(Priority.INFO)) logger.info("getCache("+id+") Succ ,but result is empty.iRet="+iRet);
				return null;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get("+id+")出现错误,错误代码:"+iRet);					
			}			
		} catch (TDCacheException e) {
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get("+id+")出现错误:"+e.getMessage());
		}catch(UnsupportedEncodingException ue){
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get("+id+")出现错误:"+ue.getMessage());		
			//ue.printStackTrace();
		}

		String errMsg="dcache error";
		return (T) errMsg;
	}

	static String map2Json(Map<String, Object> map) { 
	    if (map.isEmpty()) 
	        return "{}"; 
	    StringBuilder sb = new StringBuilder(map.size() << 4); 
	    sb.append('{'); 
	    Set<String> keys = map.keySet(); 
	    for (String key : keys) { 
	        Object value = map.get(key); 
	        sb.append('\"'); 
	        sb.append(key); 
	        sb.append('\"'); 
	        sb.append(':'); 
	        sb.append(value); 
	        sb.append(','); 
	    } 
	    // 将最后的 ',' 变为 '}': 
	    sb.setCharAt(sb.length()-1, '}'); 
	    return sb.toString(); 
	 } 

	
	public boolean update(T t) {
		String key=this.getKey(t);
		if(key.isEmpty())
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("error:key is empty.");
			return false;
		}
		String value=JSONObject.fromObject(t).toString();
//		JSONObject.fromObject(value, jsonConfig)

		int iRet=0;
		try
		{			
			iRet = c.setCache(key, value.getBytes("GBK")); 
			if (iRet == 0||iRet==7){
				return true;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->update("+t+")出现错误,错误代码:"+iRet);
				return false;
			}
		}
		catch(Exception e)
		{
			if(logger.isEnabledFor(Priority.ERROR)) logger.error(e);
			return false;
		}
	}

	public T get(ID id, Class object,String name) {
		String key=this.getBussiId()+"|"+id.toString();
		DcacheValueHolder valueHodler = new DcacheValueHolder();
		int iRet=0;
		try {
			iRet=c.getCache(key, valueHodler);
			if(iRet==0)
			{
				String valueItem = new String(valueHodler.value,"GBK");
				JSONObject jsonObject=JSONObject.fromObject(valueItem);
				
				
				 Map<String, Class> m = new HashMap<String, Class>();
	             m.put(name, object);
				
				T result=(T)JSONObject.toBean(jsonObject, this.getEntityClass(),m);

	             return result;
			}
			else if(iRet == 1 || iRet ==-1)
			{
				//if(logger.isEnabledFor(Priority.INFO)) logger.info("getCache Succ ,but result is empty");
				return null;
			}
			else
			{
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get("+id+","+object+","+name+")出现错误,错误代码:"+iRet);
			}
		} catch (TDCacheException e) {
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get(ID id, Class object,String name)出现错误:"+e.getMessage());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String errMsg="dcache error";
		return (T) errMsg;
	}
	
	public T get(ID id, List<String> name , List<Class> object) {
		
		String key=this.getBussiId()+"|"+id.toString();
		DcacheValueHolder valueHodler = new DcacheValueHolder();
		int iRet=0;
		try {
			System.out.println("-------key-------"+key);
			iRet=c.getCache(key, valueHodler);
			System.out.println("-------iRet-------"+iRet);
			if(iRet==0)
			{
				String valueItem = new String(valueHodler.value,"GBK");
				JSONObject jsonObject=JSONObject.fromObject(valueItem);
				System.out.println(jsonObject);
				
				 Map<String, Class> m = new HashMap<String, Class>();
	           
				 
				 for(int i = 0; i < name.size(); i++)
				 {
					 m.put((String)name.get(i), (Class)object.get(i));
				 }
				
				T result=(T)JSONObject.toBean(jsonObject, this.getEntityClass(),m);

	             return result;
			}
			else if(iRet == 1 || iRet ==-1)
			{
				//if(logger.isEnabledFor(Priority.INFO)) logger.info("getCache Succ ,but result is empty.iRet="+iRet);
				return null;
			}
			else {
				if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用Dcache->get("+id+","+object+","+name+")出现错误,错误代码:"+iRet);
			}			
		} catch (TDCacheException e) {
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用get(ID id, List<String> name , List<Class> object)出现错误,错误代码:"+iRet);
		} catch (UnsupportedEncodingException e) {
			if(logger.isEnabledFor(Priority.ERROR)) logger.error("调用get(ID id, List<String> name , List<Class> object)出现错误,错误代码:"+iRet);
		}

		String errMsg="dcache error";
		return (T) errMsg;
	}

}
