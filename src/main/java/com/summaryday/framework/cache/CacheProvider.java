package com.summaryday.framework.cache;

import java.util.Map;
import net.sf.ehcache.Cache;

public interface CacheProvider {

	public void put(String key,Object value);
    public Object get(String key);
    public boolean remove(String key);
    public Cache getCache();
    
	public void put(Cache cache,String key,Object value);
	public Object get(String pakage_clazz,String key);
    public Object get(Cache cache,String key);
    public boolean remove(Cache cache,String key);
    public Cache getCache(String pakage_clazz);
    
    public  Map<String, Cache> getMap();
}
