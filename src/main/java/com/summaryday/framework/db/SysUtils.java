package com.summaryday.framework.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class SysUtils {


	private static final Logger logger = LoggerFactory.getLogger(SysUtils.class);

	public static Properties getProperties() {
		Properties properties = new Properties();
		try {
			properties.load(SysUtils.class.getResourceAsStream("/db.properties"));
		} catch (IOException e) {
			logger.error("获取Properties文件-异常:"+e.getMessage());
		}
		return properties;
	}


/*
	private static WebApplicationContext context = null;

	static {
		if (context == null) {
			context = ContextLoader.getCurrentWebApplicationContext();
		}
	}

	public static ServletContext getServletContext() {

		return context.getServletContext();
	}

	public static HttpSession getSession() {
		  HttpSession session = null; 
		  try { 
		    session = getRequest().getSession(); 
		  } catch (Exception e) {
			  logger.error("获取session-异常:"+e.getMessage());
		  } 
		    return session; 
	} 
		  
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs =null;
		try {
			 attrs =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); 
			 return attrs.getRequest();
		} catch (Exception e) {
			 logger.error("获取Request-异常:"+e.getMessage());
		} 
		return null;
	}
	

	
	public static String getPath(String key) throws UnsupportedEncodingException
	{
		String path = getServletContext().getRealPath(new String(getProperties().getProperty(key).getBytes("ISO8859-1"),"UTF-8"));
		File file = new File(path);
		if(file != null&&file.isFile())
		{
			return file.getAbsolutePath();
		}
		return null;
	}
	*/
}
