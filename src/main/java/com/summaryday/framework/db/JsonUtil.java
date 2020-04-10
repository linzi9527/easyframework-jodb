package com.summaryday.framework.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JsonUtil {
	//private static Logger log = Logger.getLogger(JsonUtil.class);
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * 将某个对象转换为json格式
	 * @param obj
	 * @return
	 */
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		if(PropertiesUtils.JSON_LOG){log.info(json.toString());}
		return json.toString();
	}
	
	
	/**
	 * 将某个对象转换为Wex5json格式
	 * @return
	 */
	public static String bean2Wex5json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{").append("\"@type\"").append(":").append("\"table\"").append(",");
		json.append("\"rows\"").append(":");
		json.append("[");
		if (bean != null) {
				json.append(object2Wex5json(bean));
				json.append("]");
		}
		json.append("}");
		if(PropertiesUtils.JSON_LOG){log.info(json.toString());}
		return json.toString();
	}
	
	public static String object2Wex5json(Object obj) {
		StringBuilder json = new StringBuilder();
			json.append("{");
			PropertyDescriptor[] props = null;
			try {
				props = Introspector.getBeanInfo(obj.getClass(), Object.class)
						.getPropertyDescriptors();
			} catch (IntrospectionException e) {
			}
			if (props != null) {
				for (int i = 0; i < props.length; i++) {
					try {
						String name = object2json(props[i].getName());
						String value = object2json(props[i].getReadMethod().invoke(obj));
						json.append(name);
						json.append(":");
						json.append("{").append("\"value\"").append(":");
						json.append(value);
						json.append("}");
						json.append(",");
					} catch (Exception e) {
					}
				}
				json.setCharAt(json.length() - 1, '}');
			} else {
				json.append("}");
			}
		return json.toString();
	}
	/**
	 * 将一个Bean对象转换成json格式
	 * @param bean
	 * @return
	 */
	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个Bean对象转换成json格式:"+json.toString());}
		return json.toString();
	}

	/**
	 * 将一个列表转换为json格式
	 * @param list
	 * @return
	 */
	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个列表转换为json格式:"+json.toString());}
		return json.toString();
	}
	
	
	/**
	 * 将一个列表转换为Wex5json格式
	 * @param list
	 * @return
	 */
	public static String list2Wex5json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("{").append("\"@type\"").append(":").append("\"table\"").append(",");
		json.append("\"rows\"").append(":");
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2Wex5json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		json.append("}");
		if(PropertiesUtils.JSON_LOG){log.info(json.toString());}
		return json.toString();
	}
	
	/**
	 * JsonObject
	 * @param list
	 * @return
	 */
	public static String listJson2Wex5json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("{").append("\"@type\"").append(":").append("\"table\"").append(",");
		json.append("\"rows\"").append(":");
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(obj.toString());
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		json.append("}");
		if(PropertiesUtils.JSON_LOG){log.info(json.toString());}
		return json.toString();
	}
	/**
	 * 将一个数组转换为json格式
	 * @param array
	 * @return
	 */
	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个数组转换为json格式:"+json.toString());}
		return json.toString();
	}

	/**
	 * 将一个map转换为json格式
	 * @param map
	 * @return
	 */
	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个map转换为json格式:"+json.toString());}
		return json.toString();
	}

	/**
	 * 将一个集合转换为json格式
	 * @param set
	 * @return
	 */
	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个集合转换为json格式:"+json.toString());}
		return json.toString();
	}

	/**
	 * 将一个字符串转换为json格式
	 * @param s
	 * @return
	 */
	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		if(PropertiesUtils.JSON_LOG)
		{log.info("将一个字符串转换为json格式:"+sb.toString());}
		return sb.toString();
	}



	//字符串里提取数字
	public static String getIntFromString(String a){
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);

		return  m.replaceAll("").trim();
	}

	/**
	 * 获取指定范围的随机数
	 * @param max
	 * @param min
	 * @return
	 */
	public static int getRandom(int max,int min){
//        int max=100;
//        int min=1;
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	/**
	 * 0~(max-1)
	 * @param max
	 * @return
	 */
	public static int getRandom(int max){
		Random random = new Random();
		int s = random.nextInt(max);
		return s;
	}




	/**
	 * 向前台输出json格式数据
	 * @param response
	 * @param beanObj 是一个pojo或Bean
	 */
	/*public static void returnMsg(HttpServletResponse  response,Object beanObj){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   out.print(JsonUtil.bean2json(beanObj));
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	
	/*
	public static void returnMap(HttpServletResponse  response, Map mapObj){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   out.print(JsonUtil.map2json(mapObj));
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/


	/**
	 * 向前台输出字符串数据
	 * @param response
	 * @param msg
	 */
	/*public static void returnMsg(HttpServletResponse  response,String msg){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   PrintWriter out = response.getWriter();
		   out.print(msg);
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/


	/**
	 * 列表以json格式输出
	 * @param response
	 * @param list
	 */
	/*public static void returnData(HttpServletResponse  response,List<?> list){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   out.print(JsonUtil.list2json(list));
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	/**
	 * 列表以Wex5json格式输出
	 * @param response
	 */
	/*public static void returnDataOfWex5json(HttpServletResponse  response,Object beanObj){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   out.print(JsonUtil.bean2Wex5json(beanObj));
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/


	/**
	 * bean对象以Wex5json格式输出
	 * @param response
	 * @param list
	 */
	/*public static void returnDataOfWex5json(HttpServletResponse  response,List<?> list){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   log.info("===========================Wex5json=======================================");
		   String jp=JsonUtil.list2Wex5json(list);
		   log.info(jp);
		   log.info("==========================================================================");
		   out.print(jp);
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	/**
	 * list中是JsonObject
	 * @param response
	 * @param list
	 */
	/*public static void returnDataOfWex5ListJson(HttpServletResponse  response,List<?> list){
		try{
		   response.setContentType("application/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
		   
		   PrintWriter out = response.getWriter();
		   log.info("===========================Wex5json=======================================");
		   String jsq=JsonUtil.listJson2Wex5json(list);
		   log.info(jsq);
		   log.info("==========================================================================");
		   out.print(jsq);
		   out.flush();
		   out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	

	//在求情的网络流中的缓冲区读取数据
	/*public static String readJSONString(HttpServletRequest request){
			   StringBuffer json = new StringBuffer();
			   String line = null;
			   try {
				   BufferedReader reader = request.getReader();
				   while((line = reader.readLine()) != null) {
					   json.append(line);
				   }
			   }
			   catch(Exception e) {
				   	log.error("缓冲区读取数据-异常："+e);
			   }
			//	log.info("\n缓冲区读取数据："+json.toString());
			   return json.toString();
	}
*/
	 /**
	  * 接收前端发送到后台的数据（json格式为转为对象对应某个bean，可以保存、更新）
	  * @param request
	  * @param clazz
	  *  com.alibaba.fastjson.JSONObject paramsOjbect = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(params);
	  * @return
	  */
	// @SuppressWarnings("unchecked")
	/*public static <T> T beanFromJSONString(HttpServletRequest request,Class<T> clazz){
		String params= readJSONString(request);
		//log.info("\n接收params："+params);
		T t=null;
		if(null!=params&&params.contains("{")&&params.contains("}")){
			 JSONObject jsonObject=JSONObject.fromObject(params.toString());
			 t=(T)JSONObject.toBean(jsonObject, clazz);
		}else{
			log.info("\n================有问题没进去处理==================");
		}
		 return t;
	 }*/
	 
	 /**
	  * 对于各种查询的请求参数json数据处理，一般带有分页和关键字offset、rows以及同时携带了查询条件id、名称、日期、其他等
	  * @param request
	  * @return
	  */
	/* public static JSONObject jsonObjectFromJSONString(HttpServletRequest request){
			String params= readJSONString(request);
		//	log.info("\n接收params："+params);
			 JSONObject jsonObject=null;
			if(null!=params&&params.contains("{")&&params.contains("}")){
				  jsonObject=JSONObject.fromObject(params.toString());
			}else{
				log.info("\n================有问题没进去处理==================");
			}
			 return jsonObject;
	 }*/
}
