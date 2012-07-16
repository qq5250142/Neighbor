package com.neighbor.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.danga.MemCached.MemCachedClient;
import com.dx.cache.common.memcached.MemCachedCacheManager;

public class GenericCacheDao<T, ID extends Serializable> extends HibernateDaoSupport {

	protected Date defaultExpire = new Date(1000 * 60 * 30);

	// get the memcached client instance
	public MemCachedClient getMemCachedClient() {
		MemCachedCacheManager manager = MemCachedCacheManager.getInstance();
		MemCachedClient cache = manager.getCache();
		return cache;
	}

	protected Class<T> entityClass;

	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			logger.debug("T class = " + entityClass.getName());
		}
		return entityClass;
	}

	protected String getEntityClassName() {
		return this.getEntityClass().getName();
	}

	public void delete(T t, ID id) throws DataAccessException {
		// TODO Auto-generated method stub
		String key = this.getEntityClassName() + ":" + id;
		boolean success = getMemCachedClient().delete(key);
		if (!success) {
			logger.debug("memcachedclient delete failed: key=" + key);
		}

		getHibernateTemplate().delete(t);
	}

	public void delete(ID id) throws DataAccessException {
		// TODO Auto-generated method stub
		String key = this.getEntityClassName() + ":" + id;
		boolean success = getMemCachedClient().delete(key);
		if (!success) {
			logger.debug("memcachedclient delete failed: key=" + key);
		}

		getHibernateTemplate().delete(this.get(id));
	}

	public T get(ID id) throws DataAccessException {
		// TODO Auto-generated method stub
		String key = this.getEntityClassName() + ":" + id;
		T t = (T) getMemCachedClient().get(key);
		if (t == null) {
			logger.debug("memcachedclient get failed: key=" + key);
			t = (T) getHibernateTemplate().get(getEntityClass(), id);

			boolean success = getMemCachedClient().set(key, t, defaultExpire);
			if (!success) {
				logger.debug("memcachedclient get set failed: key=" + key);
			}
		}

		return t;
	}

	public void save(T t) throws DataAccessException {
		// TODO Auto-generated method stub
		ID id = (ID) getHibernateTemplate().save(t);
		String key = this.getEntityClassName() + ":" + id;
		boolean success = getMemCachedClient().add(key, t, defaultExpire);
		if (!success) {
			logger.debug("memcachedclient save failed: key=" + key);
		}
	}

	public void update(T t, ID id) throws DataAccessException {
		// TODO Auto-generated method stub
		String key = this.getEntityClassName() + ":" + id;
		boolean success = getMemCachedClient().set(key, t, defaultExpire);
		if (!success) {
			logger.debug("memcachedclient update failed: key=" + key);
		}
		getHibernateTemplate().update(t);
	}
}
