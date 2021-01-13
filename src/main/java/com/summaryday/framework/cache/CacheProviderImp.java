package com.summaryday.framework.cache;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheProviderImp implements CacheProvider {

	private  Cache cache;
	private static CacheManager manager ;
	private static Map<String,Cache> map=new HashMap<String, Cache>();
	
	
	
	public Map<String, Cache> getMap() {
		return map;
	}


	public CacheProviderImp() {
		InputStream in = getClass().getResourceAsStream("ehcache.xml");
		CacheManager manager = new CacheManager(in); // 解析缓存配置文件
		 String[] names=manager.getCacheNames();
		 if(names.length==1&&names[0].equals("jdbc_cache")){
			  this.cache = manager.getCache("jdbc_cache");// 创建缓存
			System.out.println("\n默认缓存jdbc_cache创建成功...");
		 }else{
			 for(String cacheName:names){
			 	if(cacheName.contains(".")) {
					cacheName = cacheName.toLowerCase().replaceAll(".", "_");
				}
				 Cache cache= manager.getCache(cacheName);
				 if(!cacheName.equals("jdbc_cache")){
					 map.put(cacheName,cache);// 创建缓存
					 System.out.println("\n自定义缓存："+cacheName+"\n"+cache+"\n");
				 }
			 }
		 }
	}
	/*public CacheProviderImp(String path) {
		InputStream in = getClass().getResourceAsStream(path);
		CacheManager manager = new CacheManager(in); // 解析缓存配置文件
		//String []names=manager.getCacheNames();
		   this.cache = manager.getCache("jdbc_cache");// 创建缓存
	}*/
	
	
	/*public  CacheManager CreateCache(String path){
		URL url=getClass().getResource(path);
		CacheManager manager=CacheManager.create(url);
		return manager;
	}*/
	public Object get(String key) {
		// TODO Auto-generated method stub
		Element element = this.cache.get(key);// 取出缓存中的element
		return element == null ? null : element.getObjectValue();// 取出缓存的对象
	}

	public Object get(String pakage_clazz,String key) {
		// TODO Auto-generated method stub
		Element element = null;
		Cache cache=map.get(pakage_clazz);
		if(cache!=null)	element = cache.get(key);// 取出缓存中的element
		return element == null ? null : element.getObjectValue();// 取出缓存的对象
	}
	public Object get(Cache cache,String key) {
		// TODO Auto-generated method stub
		Element element = cache.get(key);// 取出缓存中的element
		return element == null ? null : element.getObjectValue();// 取出缓存的对象
	}
	public Cache getCache() {
		// TODO Auto-generated method stub
		return this.cache;
	}
	//@Override
	public Cache getCache(String pakage_clazz) {
		// TODO Auto-generated method stub
		return manager.getCache(pakage_clazz);// 创建缓存
	}
	public void put(Cache cache,String key, Object value) {
		// TODO Auto-generated method stub
		Element element = new Element(key, value);// 构建一个element对象
		cache.put(element);// 将element放入缓存
	}

	public void put(String key, Object value) {
		// TODO Auto-generated method stub
		Element element = new Element(key, value);// 构建一个element对象
		this.cache.put(element);// 将element放入缓存
	}
	
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return this.cache.remove(key);// 将对象从缓存的中移除
	}
	
	public boolean remove(Cache cache,String key) {
		// TODO Auto-generated method stub
		return cache.remove(key);// 将对象从缓存的中移除
	}
	
	/*public static void main(String[] args) {
		CacheProviderImp cp=new CacheProviderImp();
		CacheManager m=cp.CreateCache("/ehcache.xml");
		String []names=m.getCacheNames();
		for(String n:names){
			System.out.println(n);
		}
		
	}*/



}
