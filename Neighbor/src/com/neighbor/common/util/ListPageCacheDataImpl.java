package com.neighbor.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class ListPageCacheDataImpl<E>
implements Serializable
{
private ArrayList<E> ls;
private int topIdx;
private int size;
private int totalRecordCount;
private boolean autoChangeTotalRecordCount;

public ListPageCacheDataImpl(List<E> data, int totalRecordCount, boolean autoChangeTotalRecordCount)
{
  init(data, totalRecordCount, autoChangeTotalRecordCount);
}

private void init(List<E> data, int totalRecordCount, boolean autoChangeTotalRecordCount)
{
  this.autoChangeTotalRecordCount = autoChangeTotalRecordCount;
  this.totalRecordCount = totalRecordCount;
  this.size = data.size();
  if (this.size > 0)
    this.topIdx = (this.size - 1);
  this.ls = new ArrayList(this.size);

  for (int i = data.size() - 1; i >= 0; --i)
    this.ls.add(data.get(i));
}

public ListPageCacheDataImpl(List<E> data, int maxSize, int totalRecordCount, boolean autoChangeTotalRecordCount)
{
  if (data.size() > maxSize)
  {
    List l = data.subList(data.size() - maxSize, data.size());
    init(l, totalRecordCount, autoChangeTotalRecordCount);
  }
  else if (data.size() == maxSize)
  {
    init(data, totalRecordCount, autoChangeTotalRecordCount);
  }
  else
  {
    this.autoChangeTotalRecordCount = autoChangeTotalRecordCount;
    this.totalRecordCount = totalRecordCount;
    this.size = data.size();
    if (this.size > 0)
      this.topIdx = (this.size - 1);
    this.ls = new ArrayList(maxSize);

    for (int i = data.size() - 1; i >= 0; --i)
      this.ls.add(data.get(i));
    for (int i = 0; i < maxSize - this.size; ++i)
    {
      this.ls.add(null);
    }
  }
}

public int size()
{
  return this.size;
}

public int maxSize()
{
  return this.ls.size();
}

public E get(int n)
{
  if ((n < 0) || (n >= this.size)) {
    throw new IndexOutOfBoundsException(new StringBuilder().append("current index: ").append(n).append(", size: ").append(this.size).toString());
  }
  int idx = getIdx(n);
  return this.ls.get(idx);
}

public E add(E e)
{
  if (this.autoChangeTotalRecordCount)
    this.totalRecordCount += 1;
  this.topIdx = ((this.topIdx + 1) % this.ls.size());
  if (this.size < this.ls.size())
    this.size += 1;
  return this.ls.set(this.topIdx, e);
}

public E add(int n, E e)
{
  if (this.autoChangeTotalRecordCount)
    this.totalRecordCount += 1;
  this.topIdx = ((this.topIdx + 1) % this.ls.size());
  if (this.size < this.ls.size())
    this.size += 1;
  int i = 1;
  for (; i <= n; ++i)
  {
    Object o = get(i);
    int idx = getIdx(i - 1);
    this.ls.set(idx, (E) o);
  }
  int idx = getIdx(i - 1);
  return this.ls.set(idx, e);
}

public boolean isEmpty()
{
  return (this.size == 0);
}

public boolean contains(E e)
{
  for (int i = 0; i < this.size; ++i)
  {
    Object o = get(i);
    if (((e == null) && (o == null)) || ((e != null) && (e.equals(o))))
      return true;
  }
  return false;
}

public E remove(int n)
{
  if ((n < 0) || (n >= this.size)) {
    throw new IndexOutOfBoundsException(new StringBuilder().append("current index: ").append(n).append(", size: ").append(this.size).toString());
  }
  if (this.autoChangeTotalRecordCount)
    this.totalRecordCount -= 1;
  Object o = get(n);
  int i = n + 1;
  for (; i < this.size; ++i)
  {
    int idx1 = getIdx(i - 1);
    int idx2 = getIdx(i);
    this.ls.set(idx1, this.ls.get(idx2));
  }
  int idx = getIdx(--this.size);
  this.ls.set(idx, null);

  return (E) o;
}

public E remove(E e)
{
  for (int i = 0; i < this.size; ++i)
  {
    Object o = get(i);
    if (((e == null) && (o == null)) || ((e != null) && (e.equals(o))))
    {
      return remove(i);
    }
  }
  return null;
}

private int getIdx(int i)
{
  int idx = this.topIdx - i;
  idx = (idx < 0) ? idx + this.ls.size() : idx;
  return idx;
}

public void moveToTop(int n)
{
  E o = get(n);
  remove(n);
  add(o);
}

public E moveToTop(E e)
{
  for (int i = 0; i < this.size; ++i)
  {
    Object o = get(i);
    if ((((e != null) || (o != null))) && (((e == null) || (!(e.equals(o))))))
      continue;
    remove(i);
    add((E) o);
    return null;
  }

  return add(e);
}

public void moveTo(int srcPos, int destPos)
{
  if ((srcPos <= destPos) && (destPos > 0))
    --destPos;
  E o = remove(srcPos);
  if (o != null)
    add(destPos, o);
}

public void moveTo(E src, int destPos)
{
  int srcPos = find(src);
  if (srcPos != -1)
    moveTo(srcPos, destPos);
  else
    add(destPos, src);
}

public void setTotalRecordCount(int totalRecordCount)
{
  this.totalRecordCount = totalRecordCount;
}

public int getTotalRecordCount()
{
  return this.totalRecordCount;
}

public void clear()
{
  this.size = 0;
}

public DataPage<E> getPage(int pageSize, int pageNo)
{
  ArrayList l = new ArrayList();
  pageSize = (pageSize <= 0) ? 10 : pageSize;
  pageNo = (pageNo <= 0) ? 1 : pageNo;
  int begin = pageSize * (pageNo - 1);
  int end = begin + pageSize;

  if (end > this.size)
  {
    if ((this.size < this.totalRecordCount) && (this.autoChangeTotalRecordCount))
      this.totalRecordCount = this.size;
    end = this.size;
  }
  for (int i = begin; i < end; ++i)
  {
    Object e = get(i);
    l.add(e);
  }
  return new DataPage(l, this.totalRecordCount, pageSize, pageNo);
}

public ArrayList<E> toArrayList()
{
  ArrayList l = new ArrayList();
  l.ensureCapacity(this.size);
  for (int i = 0; i < this.size; ++i)
    l.add(get(i));
  return l;
}

public String toString()
{
  StringBuilder sb = new StringBuilder();
  sb.append("{ data: [");
  for (int i = 0; i < this.size; ++i)
  {
    sb.append(String.valueOf(get(i)));
    if (i != this.size)
      sb.append(", ");
  }
  sb.append("], size: ").append(this.size).append(", totalRecordCount: ").append(this.totalRecordCount).append(" }");

  return sb.toString();
}

private int find(E e)
{
  for (int i = 0; i < this.size; ++i)
  {
    Object o = get(i);
    if (((e == null) && (o == null)) || ((e != null) && (e.equals(o))))
    {
      return i;
    }
  }
  return -1;
}

public E findElement(E e)
{
  for (int i = 0; i < this.size; ++i)
  {
    E o = get(i);
    if (((e == null) && (o == null)) || ((e != null) && (e.equals(o))))
    {
      return o;
    }
  }
  return null;
}

public static void main(String[] argv)
{
  ArrayList ll = new ArrayList();
  ll.add("0");
  ll.add("1");
  ll.add("2");
  ll.add("3");
  ll.add("4");
  ll.add("5");
  ll.add("6");
  ll.add("7");
  ll.add("8");
  ListPageCacheDataImpl l = new ListPageCacheDataImpl(ll, 100, 100, true);

  for (int i = 0; i < l.size(); ++i) {
    //System.out.println((String)l.get(i));
  }
  l.moveTo(7, 6);
  System.out.println(l);
}
}
