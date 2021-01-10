package com.summaryday.framework.cache;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.beanutils.BeanUtils;

import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Table;

/**
 * 功能：将JDBC ResultSet、Result里的值转化为List
 * 当实例存在时，返回实例集合，当实例不存在时，返回对象数组集合
 * 时间 2011-04-21 上午09:43:35
 * 本次修改2017-06-23 13：09:22
 * 修改人：景林军
 */
public class JDBCBeanUtil {

    //private static Log logger = LogFactory.getLog(JDBCBeanUtil.class);

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    public static Object transTOObject(Result rs, Class<?> instance) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Table t = (Table) instance.newInstance().getClass().getAnnotation(Table.class);
        String[] names = rs.getColumnNames();
        int count = names.length;
        Field[] fields = {};
        Object[] objs = new Object[count];


        if (instance != null) {
            fields = instance.getDeclaredFields();//取得实例对象的属性
        }

        if (fields.length < 1) {//如果传入的对象当中没有属性
            Map row = rs.getRows()[0];//取得行
            for (int i = 1; i <= count; i++) {
                objs[i - 1] = row.get(names[i]);//取得列的值
            }
            return objs;    //返回对象数组
        } else {
            //如果传入的对象当中有属性
            Object obj = instance.newInstance();
            Map row = rs.getRows()[0];//取得行
            for (int i = 0; i < count; i++) {//字段列数遍历
                String _names_ = names[i];
                for (int j = 0; j < fields.length; j++) {//实例对象的属性数量遍历
                    Field field = fields[j];//依次取出实例对象的属性
                    //Object   o=row.get(names[j]);
                    field.setAccessible(true);// 开启私有变量的访问权限
                    Colum colum = field.getAnnotation(Colum.class);
                    String ty = null, colunName = null;
                    if (null != colum) {
                        ty = colum.type().name().toUpperCase();
                        colunName = colum.columName().toUpperCase();
                    }
                    String _names_col = names[i];
                    //当不是TABLE_MODE_COLUMNNAME模式，会将表的字段名称下划线、中划线过滤；当时TABLE_MODE_COLUMNNAMEE将保留字段名称下划线
                    if (null != t && !t.type().toString().equals("TABLE_MODE_COLUMNNAME")) {
                        if (names[i].contains("-")) {
                            _names_col = names[i].replaceAll("-", "");
                        }
                        if (names[i].contains("_")) {
                            _names_col = names[i].replaceAll("_", "");
                        }
                    }
                    if (null != t && t.type().toString().equals("VO")) {
                        if (colunName.contains("-")) {
                            colunName = colunName.toUpperCase().replaceAll("-", "");
                        }
                        if (colunName.contains("_")) {
                            colunName = colunName.toUpperCase().replaceAll("_", "");
                        }
                    }

                    //	logger.info("==>"+fields[j]+"======="+metaData.getColumnName(i).replaceAll("_",""));

                    if (null != t && null != colunName) {
                        if (t.type().toString().equals("TABLE") && field.getName().toUpperCase().equals(_names_col.toUpperCase())) {
                            if (null != ty && (ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                String tmp = row.get(_names_).toString();
                                if (tmp.endsWith(".0")) {
                                    tmp = tmp.substring(0, tmp.indexOf(".0"));
                                }
                                BeanUtils.copyProperty(obj, field.getName(), tmp);
                            } else {
                                BeanUtils.copyProperty(obj, field.getName(), row.get(_names_));
                            }
                            break;
                        } else if (t.type().toString().equals("VO") && colunName.equals(_names_col.toUpperCase())) {
                            if ((ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                String tmp = row.get(_names_).toString();
                                if (tmp.endsWith(".0")) {
                                    tmp = tmp.substring(0, tmp.indexOf(".0"));
                                }
                                BeanUtils.copyProperty(obj, field.getName(), tmp);
                            } else {
                                BeanUtils.copyProperty(obj, field.getName(), row.get(_names_));
                            }
                            break;
                        } else
                            //names[i]没有过滤，VO实体类colunName必须配置和字段名称一致,字母大小忽略
                            if (t.type().toString().equals("TABLE_MODE_COLUMNNAME") && colunName.equals(names[i].toUpperCase())) {
                                if ((ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(names[i])) {
                                    String tmp = row.get(names[i]).toString();
                                    if (tmp.endsWith(".0")) {
                                        tmp = tmp.substring(0, tmp.indexOf(".0"));
                                    }
                                    BeanUtils.copyProperty(obj, field.getName(), tmp);
                                } else if (ty.equals("Date".toUpperCase()) && null != row.get(names[i])) {
                                    BeanUtils.copyProperty(obj, field.getName(), row.get(names[i]));
                                } else {
                                    BeanUtils.copyProperty(obj, field.getName(), row.get(names[i]));
                                }
                                break;
                            } else {
                                //System.out.println("====================================");
                                if (field.getName().toUpperCase().equals(_names_col.toUpperCase())) {
                                   // System.out.println(field.getName()+","+ row.get(_names_));
                                    if (null != ty && (ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                        String tmp = row.get(_names_).toString();
                                        if (tmp.endsWith(".0")) {
                                            tmp = tmp.substring(0, tmp.indexOf(".0"));
                                        }
                                        BeanUtils.copyProperty(obj, field.getName(), tmp);
                                    } else {
                                        BeanUtils.copyProperty(obj, field.getName(), row.get(_names_));
                                    }
                                    break;
                                }
                            }
                    } else {
                      //  System.out.println("====================================");
                        String bsTmp = field.getName(), tmp = names[i];
                        tmp = tmp.replaceAll("-", "");
                        tmp = tmp.replaceAll("_", "");

                        bsTmp = bsTmp.replaceAll("-", "");
                        bsTmp = bsTmp.replaceAll("_", "");
                      //  System.out.println("field.getName:"+bsTmp+",names[i]:"+tmp);
                       // System.out.println("-------------------------------------------");
                        if (bsTmp.toUpperCase().equals(tmp.toUpperCase())) {
                            System.out.println(field.getName()+","+ row.get(names[i]));
                            BeanUtils.copyProperty(obj, field.getName(), row.get(names[i]));
                            break;
                        }
                    }
                }
            }
            return obj;   //返回实例对象
        }

    }

    /**
     * @param r
     * @param instance
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings("rawtypes")
    public static List<Object> transTOList(Result r, Class<?> instance) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Table t = (Table) instance.newInstance().getClass().getAnnotation(Table.class);
        String[] names = r.getColumnNames();
        int c = names.length;//字段列数
        int n = r.getRowCount();//记录数

        Field[] fields = {};
        if (instance != null) {
            fields = instance.getDeclaredFields();//取得实例对象的属性
        }
        //如果传入的对象当中没有属性
        if (fields.length < 1) {
            List<Object> list = new ArrayList<Object>();

            if (r != null && r.getRowCount() != 0) {
                List<Object> arraylist = new ArrayList<Object>();
                for (int i = 0; i <= n; i++) {
                    Map obj = r.getRows()[i];//取得行
                    for (int j = 0; j <= c; j++) {
                        Object o = obj.get(names[j]);    //取得列的值
                        arraylist.add(o);
                    }
                }
                list.add(arraylist.toArray());//把arraylist转化成数组以后，放进list
            }
            return list;
            //返回对象数组集合
        } else {
            //如果传入的对象当中有属性
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < n; i++) {//记录数遍历
                Object newObj = instance.newInstance();//构造业务对象实例
                Map row = r.getRows()[i];//取得行
                //fields.length为实体对象属性数量，c为字段列数;这里是有对象属性数量值
                for (int j = 0; j < c; j++) {//字段列数遍历
                    try {
                        //Object   obj=row.get(names[j]);
                        String _names_ = names[j];
                        for (int s = 0; s < fields.length; s++) {//对象属性数量值
                            Field field = fields[s];//依次取出实例对象的属性
                            field.setAccessible(true);// 开启私有变量的访问权限
                            Colum colum = field.getAnnotation(Colum.class);
                            String ty = null, colunName = null;
                            if (null != colum) {
                                ty = colum.type().name().toUpperCase();
                                colunName = colum.columName().toUpperCase();
                            }
                            String _names_col =names[j];
                            String bs = field.getName();
                            //当不是TABLE_MODE_COLUMNNAME模式，会将表的字段名称下划线、中划线过滤；当时TABLE_MODE_COLUMNNAMEE将保留字段名称下划线
                            if (null != t && !t.type().toString().equals("TABLE_MODE_COLUMNNAME")) {
                                if (names[j].contains("-")) {
                                    _names_col = names[j].replaceAll("-", "");
                                }
                                if (names[j].contains("_")) {
                                    _names_col = names[j].replaceAll("_", "");
                                }
                            }
                            if (null != t && t.type().toString().equals("VO")) {
                                if (colunName.contains("-")) {
                                    colunName = colunName.toUpperCase().replaceAll("-", "");
                                }
                                if (colunName.contains("_")) {
                                    colunName = colunName.toUpperCase().replaceAll("_", "");
                                }
                            }

                            if (null != t && null != colunName) {
                                if (t.type().toString().equals("TABLE") && bs.toUpperCase().equals(_names_col.toUpperCase())) {
                                    if (null != ty && (ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                        String tmp = row.get(_names_).toString();
										if (tmp.endsWith(".0")) {
											tmp = tmp.substring(0, tmp.indexOf(".0"));
										}
                                        BeanUtils.copyProperty(newObj, bs, tmp);
                                    } else {
                                        BeanUtils.copyProperty(newObj, bs, row.get(_names_));
                                    }
                                } else if (t.type().toString().equals("VO") && colunName.equals(_names_col.toUpperCase())) {
                                    if ((ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                        String tmp = row.get(_names_).toString();
										if (tmp.endsWith(".0")) {
											tmp = tmp.substring(0, tmp.indexOf(".0"));
										}
                                        BeanUtils.copyProperty(newObj, bs, tmp);
                                    } else {
                                        BeanUtils.copyProperty(newObj, bs, row.get(_names_));
                                    }
                                } else
                                    //names[i]没有过滤，VO实体类colunName必须配置和字段名称一致,字母大小忽略
                                    if (t.type().toString().equals("TABLE_MODE_COLUMNNAME") && colunName.equals(names[j].toUpperCase())) {
                                        if ((ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(names[j])) {
                                            String tmp = row.get(names[j]).toString();
											if (tmp.endsWith(".0")) {
												tmp = tmp.substring(0, tmp.indexOf(".0"));
											}
                                            BeanUtils.copyProperty(newObj, bs, tmp);
                                        } else {
                                            BeanUtils.copyProperty(newObj, bs, row.get(names[j]));
                                        }
                                        break;
                                    } else {
                                        if (field.getName().toUpperCase().equals(_names_col.toUpperCase())) {
                                            if (null != ty && (ty.equals("Timestamp".toUpperCase()) || ty.equals("DateTime".toUpperCase())) && null != row.get(_names_)) {
                                                String tmp = row.get(_names_).toString();
												if (tmp.endsWith(".0")) {
													tmp = tmp.substring(0, tmp.indexOf(".0"));
												}
                                                BeanUtils.copyProperty(newObj, bs, tmp);
                                            } else {
                                                BeanUtils.copyProperty(newObj, bs, row.get(_names_));
                                            }
                                        }
                                    }
                            } else {
                                String tmp = names[j];
                                tmp = tmp.replaceAll("-", "");
                                tmp = tmp.replaceAll("_", "");

                                bs = bs.replaceAll("-", "");
                                bs = bs.replaceAll("_", "");
                                //System.out.println("field.getName:"+bs+",names[i]:"+tmp);

                                if (bs.toUpperCase().equals(tmp.toUpperCase())) {
                                    BeanUtils.copyProperty(newObj, bs, row.get(_names_));
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        //e.fillInStackTrace();
                        System.out.println("数据表查询结果转换封装对象列表异常："+e.getMessage());
                    }
                }
                list.add(newObj);
            }
            //返回实例对象集合
            return list;
        }
    }


    /**
     *
     * @param rs
     * @param instance
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
	/*public static List<Object> transTurnList(ResultSet rs,Class<?> instance) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException{
		ResultSetMetaData metaData=rs.getMetaData(); //取得结果集的元元素
		Table t = (Table) instance.newInstance().getClass().getAnnotation(Table.class);
        int count=metaData.getColumnCount(); //取得所有列的个数
        Field[] fields={};
        if(instance!=null){
        	fields=instance.getDeclaredFields();//取得实例对象的属性
        }  
        //如果传入的对象当中没有属性
        if(fields.length<1){
        	List<Object> list=new ArrayList<Object>();
        	
          while(rs.next()){
        	  List<Object> arraylist=new ArrayList<Object>();
        	  for(int i=1;i<=count;i++){
        		  Object obj=rs.getObject(i);//取得列的值 
        		  arraylist.add(obj);
        	  }
        	  list.add(arraylist.toArray());//把arraylist转化成数组以后，放进list
          }	
          return list;   
          //返回对象数组集合
        }else{
        	//如果传入的对象当中有属性
        	 List<Object> list=new ArrayList<Object>();
        	while(rs.next()){
        		 Object newInstance=instance.newInstance();//构造业务对象实例
         	   for(int i=1;i<=count;i++){
                	Object obj=rs.getObject(i);//取得列的值           	
                	for(int j=0;j<fields.length;j++){
                		Field field=fields[j];//依次取出实例对象的属性
                		field.setAccessible(true);// 开启私有变量的访问权限
						Colum colum = field.getAnnotation(Colum.class);
						String	colunName=null;
						if(null!=colum){	colunName=  colum.columName().toUpperCase();}
                		//field.getName().equalsIgnoreCase(metaData.getColumnName(i).replaceAll("_",""))
						String metanames =metaData.getColumnName(i);
                		if(metanames.contains("-")){
                			metanames=metanames.replaceAll("-","");
						}
						if(metanames.contains("_")){
							metanames=metanames.replaceAll("_","");
						}
                		if(null!=t&&t.type().toString().equals("TABLE")&&field.getName().equals(metanames.toUpperCase())){
                			BeanUtils.copyProperty(newInstance,field.getName(),obj);
                		}else
                		if(null!=t&&t.type().toString().equals("VO")&&null!=colunName&&colunName.equals(metanames.toUpperCase())){
                    			BeanUtils.copyProperty(newInstance,field.getName(),obj);
                    	}else{
                    		BeanUtils.copyProperty(newInstance,field.getName(),obj);
                    	}
                	}
                }  
         	   list.add(newInstance);
             }
        	//返回实例对象集合
        	return list;   
        }
	}*/
	/*public static Object transTurnObject(ResultSet rs,Class<?> instance) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException{
		Table t = (Table) instance.newInstance().getClass().getAnnotation(Table.class);
		ResultSetMetaData metaData=rs.getMetaData(); //取得结果集的元元素
        int count=metaData.getColumnCount(); //取得所有列的个数
        Field[] fields={};
        Object[] objs=new Object[count];
        
        if(instance!=null){
        	fields=instance.getDeclaredFields();//取得实例对象的属性
        }  
        
        if(fields.length<1){//如果传入的对象当中没有属性
        	  for(int i=1;i<=count;i++){
        		  objs[i-1]=rs.getObject(i);//取得列的值 
        	  }
         return objs;  	//返回对象数组 
        }else{
        	//如果传入的对象当中有属性
        	Object obj=instance.newInstance();
        	while(rs.next()){
         	   for(int i=1;i<=count;i++){
                	for(int j=0;j<fields.length;j++){
                		Field field=fields[j];//依次取出实例对象的属性
                		field.setAccessible(true);// 开启私有变量的访问权限
						Colum colum = field.getAnnotation(Colum.class);
						String	colunName=null;
						if(null!=colum){	colunName=  colum.columName().toUpperCase();}
                		String metanames =metaData.getColumnName(i);
                		if(metanames.contains("-")){
                			metanames=metanames.replaceAll("-","");
						}
						if(metanames.contains("_")){
							metanames=metanames.replaceAll("_","");
						}
						
                	//	logger.info("==>"+fields[j]+"======="+metaData.getColumnName(i).replaceAll("_",""));
                		if(null!=t&&t.type().toString().equals("TABLE")&&field.getName().equals(metanames.toUpperCase())){
                  			BeanUtils.copyProperty(obj,field.getName(),rs.getObject(i));
                  			break;
                		}else
                		if(null!=t&&t.type().toString().equals("VO")&&null!=colunName&&colunName.equals(metanames.toUpperCase())){
                      			BeanUtils.copyProperty(obj,field.getName(),rs.getObject(i));
                      			break;
                    	}else{
                    		BeanUtils.copyProperty(obj,field.getName(),rs.getObject(i));
                  			break;
                    	}
                	}
               } 
        	}
         	  return obj;   //返回实例对象
        }
        	
    }
	*/


}
