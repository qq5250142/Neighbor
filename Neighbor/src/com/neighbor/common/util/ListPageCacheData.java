package com.neighbor.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ListPageCacheData<E>
implements Serializable
{
private ListPageCacheDataImpl<E> ls;
private final ReentrantReadWriteLock rwLock;
private final Lock r;
private final Lock w;

public ListPageCacheData(List<E> data, int totalRecordCount, boolean autoChangeTotalRecordCount)
{
  this.rwLock = new ReentrantReadWriteLock();

  this.r = this.rwLock.readLock();

  this.w = this.rwLock.writeLock();

  this.w.lock();
  try
  {
    this.ls = new ListPageCacheDataImpl(data, totalRecordCount, autoChangeTotalRecordCount);
  }
  finally
  {
    this.w.unlock();
  }
}

public ListPageCacheData(List<E> data, int maxSize, int totalRecordCount, boolean autoChangeTotalRecordCount)
{
  this.rwLock = new ReentrantReadWriteLock();

  this.r = this.rwLock.readLock();

  this.w = this.rwLock.writeLock();

  this.w.lock();
  try
  {
    this.ls = new ListPageCacheDataImpl(data, maxSize, totalRecordCount, autoChangeTotalRecordCount);
  }
  finally
  {
    this.w.unlock();
  }
}

public ListPageCacheData(List<E> data, int totalRecordCount)
{
  this(data, totalRecordCount, true);
}

public ListPageCacheData(List<E> data, int maxSize, int totalRecordCount)
{
  this(data, maxSize, totalRecordCount, true);
}

public E add(E e)
{
  this.w.lock();
  try
  {
    E localObject1 = this.ls.add(e);

    return localObject1; } finally { this.w.unlock();
  }
}

public E add(int n, E e)
{
  this.w.lock();
  try
  {
    E localObject1 = this.ls.add(n, e);

    return localObject1; } finally { this.w.unlock();
  }
}

public void clear()
{
  this.w.lock();
  try
  {
    this.ls.clear();
  }
  finally
  {
    this.w.unlock();
  }
}

public void moveTo(E src, int destPos)
{
  this.w.lock();
  try
  {
    this.ls.moveTo(src, destPos);
  }
  finally
  {
    this.w.unlock();
  }
}

public void moveTo(int srcPos, int destPos)
{
  this.w.lock();
  try
  {
    this.ls.moveTo(srcPos, destPos);
  }
  finally
  {
    this.w.unlock();
  }
}

public E moveToTop(E e)
{
  this.w.lock();
  try
  {
    E localObject1 = this.ls.moveToTop(e);

    return localObject1; } finally { this.w.unlock();
  }
}

public void moveToTop(int n)
{
  this.w.lock();
  try
  {
    this.ls.moveToTop(n);
  }
  finally
  {
    this.w.unlock();
  }
}

public E remove(E e)
{
  this.w.lock();
  try
  {
    E localObject1 = this.ls.remove(e);

    return localObject1; } finally { this.w.unlock();
  }
}

public E remove(int n)
{
  this.w.lock();
  try
  {
    E localObject1 = this.ls.remove(n);

    return localObject1; } finally { this.w.unlock();
  }
}

public void setTotalRecordCount(int totalRecordCount)
{
  this.w.lock();
  try
  {
    this.ls.setTotalRecordCount(totalRecordCount);
  }
  finally
  {
    this.w.unlock();
  }
}

public int getTotalRecordCount()
{
  this.r.lock();
  try
  {
    int i = this.ls.getTotalRecordCount();

    return i; } finally { this.r.unlock();
  }
}

public boolean contains(E e)
{
  this.r.lock();
  try
  {
    boolean bool = this.ls.contains(e);

    return bool; } finally { this.r.unlock();
  }
}

public E get(int n)
{
  this.r.lock();
  try
  {
    E localObject1 = this.ls.get(n);

    return localObject1; } finally { this.r.unlock();
  }
}

public DataPage<E> getPage(int pageSize, int pageNo)
{
  this.r.lock();
  try
  {
    DataPage localDataPage = this.ls.getPage(pageSize, pageNo);

    return localDataPage; } finally { this.r.unlock();
  }
}

public boolean isEmpty()
{
  this.r.lock();
  try
  {
    boolean bool = this.ls.isEmpty();

    return bool; } finally { this.r.unlock();
  }
}

public int size()
{
  this.r.lock();
  try
  {
    int i = this.ls.size();

    return i; } finally { this.r.unlock();
  }
}

public int maxSize()
{
  this.r.lock();
  try
  {
    int i = this.ls.maxSize();

    return i; } finally { this.r.unlock();
  }
}

public E findElement(E e)
{
  this.r.lock();
  try
  {
    E localObject1 = this.ls.findElement(e);

    return localObject1; } finally { this.r.unlock();
  }
}

public ArrayList<E> toArrayList()
{
  this.r.lock();
  try
  {
    ArrayList<E> localArrayList = this.ls.toArrayList();

    return localArrayList; } finally { this.r.unlock();
  }
}

public String toString()
{
  this.r.lock();
  try
  {
    String str = this.ls.toString();

    return str; } finally { this.r.unlock();
  }
}

public static void main(String[] argv)
{
  ListPageCacheData l = new ListPageCacheData(new ArrayList(), 10, 6);

  for (int i = 0; i < 6; ++i)
  {
    l.add(i + "");
  }
  DataPage pg = l.getPage(5, 2);
  List list = pg.getRecord();
  for (Object s : list)
  {
   // System.out.println(s);
  }
}
}