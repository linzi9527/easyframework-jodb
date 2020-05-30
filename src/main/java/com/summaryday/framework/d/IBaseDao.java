package com.summaryday.framework.d;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.summaryday.framework.db.DBSetting;

public interface IBaseDao {
	
	
	/**
	 * 设置实体类自动查询
	 * @param c
	 * @param isCache
	 * @return
	 * @throws Exception
	 */
	public List<?>    query(Class<?> c,boolean isCache) throws Exception;
	
	/**
	 *  设置实体类自动查询
	 * @param c
	 * @param isCache
	 * @return
	 * @throws Exception
	 */
	public List<?>    querys(Class<?> c,boolean isCache) throws Exception;
	
	
	/**
	 * 
	 * @param c 设置实体类自动返回数据类型，重组的vo
	 * @param link_name 查询的多表名称
	 * @param sql  多表查询语句
	 * @param isCache 缓存是否开启
	 * @return
	 * @throws Exception
	 */
	public List<?> queryTables(Class<?> c, String[] link_name,String sql,boolean isCache) throws Exception;
	/**
	 * 根据实体类+条件查询所有记录
	 * @param o :实体对象Obj.class
	 * @param isWhere : where id=id and ...
	 * @param group  : group by ...
	 * @param order  : order by ...
	 * @param isCache : true or false 是否开启查询缓存
	 */
	public List<?> queryList(Class<?> o,String isWhere,boolean isCache) throws Exception;
	
	
	/**
	 * 自定义多表联合查询sql语句
	 * 必须设置那几张表关联setTableLink
	 * String sql="SELECT t0.id,t0.user_id,t0.user_name,t0.address,t1.emp_Name,t1.emp_Pwd,t1.emp_Addr FROM tbl_user AS t0 right JOIN tbl_employee AS t1 ON t0.id=t1.emp_user_no";
	 * postDB.query(UEVO.class,sql);
	 * 
	 * @param o--boolean isCache
	 * @param sql
	 * @return
	 */
	public List<?>    query(Class<?> c,String params,boolean isCache) throws Exception;
	/**
	 * 根据ID获取某个实体
	 * @param id
	 * @param o--
	 * @return
	 * @throws Exception
	 */
	public Object    get(Integer id,Class<?> o,boolean isCache) throws Exception;
	
	public Object    get(String id,Class<?> o,boolean isCache) throws Exception;
	
	/**
	 * 根据普通字段查记录
	 * @param o
	 * @param iswhere
	 * @return
	 * @throws Exception
	 */
	public Object load(Class<?> o,String iswhere,boolean isCache)throws Exception;
	
	public boolean save(Object o) throws Exception;
	
	public boolean saveUpdate(Object o) throws Exception;
	/**
	 * 保存对象，返回新增ID(ID为自增)
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public String saveForKey(Object o) throws Exception;
	
	public boolean update(Object o) throws Exception;
	
	public boolean remove(Object o) throws Exception;
	
	public boolean delete(Integer id,Class<?> o) throws Exception;
	
	public boolean remove(String id,Class<?> o) throws Exception;
	/**
	 * 自定义：创建数据库表、删除表
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	 public boolean CreateTable(String sql) throws Exception;
	 
	 /**
	  * 自定义：创建数据库表、删除表（批量操作）
	  * @param dBSetting
	  * @param sqlList
	  * @return
	  * @throws Exception
	  */
	 public int[] createTables(List<String> sqlList)throws Exception;
	 
	 
	 /**
	  * oracle查询
	  * @param o 查询的类--boolean isCache
	  * @param p1 开始记录
	  * @param p2 结束记录
	  * @param desc 排序
	  * @return
	  * @throws Exception
	  */
	 public List<?> queryByOracle(Class<?> o,int p1,int p2,String desc) throws Exception;
	 
		/**
		 * 自动提交事务多表，执行添加，修改，删除操作
		 */
		public  int  executeAndUpdate(String sql);
	 /**
	     * 增删改
	     * @param sql
	     * @param conn
	     * @param obj
	     * @return
	     */
	 public int executeCUD(String sql,Object...obj);
	  
	 
	 /**
	  * 查询所有
	     * 
	     * maplistClass可以选用list或map返回对应类型数据
	  * @param sql 支持原生sql复杂语句
	  * @param maplistClass 要返回数据类型list或map
	  * @param table  要查的表名
	  * @param isCache 是否启用缓存
	  * @param obj 查询是否动态传参，当isWhere中出现?时采用
	  * @return
	  */
	 public <E> E query(String sql, Class<E> maplistClass,String table,boolean isCache ,Object... obj);
	 
	 
	 /**
	  * 
	  * @param resultClass  返回对象类型
	  * @param isWhere  查询的where开始语句
	  * @param isCache  是否开启缓存
	  * @param obj  查询是否动态传参，当isWhere中出现?时采用
	  * @return
	  * @throws SQLException
	  */
	 public  <T> List<T> queryList(Class<T> resultClass,String isWhere,boolean isCache, Object... obj)  throws SQLException;
	 
	 
	 /**
	  * 
	  * @param resultClass  返回对象类型
	  * @param isWhere  查询的where开始语句
	  * @param isCache  是否开启缓存
	  * @param obj  查询是否动态传参，当isWhere中出现?时采用
	  * @return
	  * @throws SQLException
	  */
	 public  List<?> queryByList(Class<?> resultClass,String isWhere,boolean isCache, Object... obj)  throws SQLException;
	 
	 /**
	  * 
	  * @param Clazz 返回对象类型
	  * @param sql 原生查询语句
	  * @param obj 查询是否动态传参，当where中出现?时采用
	  * @return
	  * @throws SQLException
	  */
	 public  List<?> queryForList(Class<?> Clazz,String sql, Object... obj) throws SQLException  ;
	 
	    /**
	     * 对List对象按照某个成员变量进行排序
	     * @param list       List对象
	     * @param sortField  排序的属性名称
	     * @param sortMode   排序方式：ASC，DESC 任选其一
	     */
	 public  <T> void sortList(List<T> list, final String sortField, final String sortMode);
	 
	
	 
	 
	  /**
	     * 动态bean使用map<k,n>来代替
	     * @param sql
	     * @param isCache
	     * @return
	     */
	public Map<String,Object> queryMap(String sql);
	
		/**
	     * 动态bean使用map<k,n>来代替,返回list<bean>由list<map<k,o>>代替
	     * @param sql
	     * @param isCache
	     * @return
	     */
	 public List<Map<String,Object>> queryListMap(String sql); 
	 
	 
	 ////////////////////////////////////////////////////////////////////////////////
	 
		/**
		 * 多表保存操作，手动提交事务，和回滚事务
		 * 
		====================================================
		e.g:
				Lee  le=new Lee();
				Jojo jj=new Jojo();
				le.setBirthday(DateUtil.getCurDateTime());
				le.setFlag("1");
				le.setId("lee-0001");
				le.setUsername("Lee大哥更新成功");
				
				jj.setBirthday(DateUtil.getCurDateTime());
				jj.setFlag("1");
				jj.setId("jj-0001");
				jj.setUsername("JOJO大哥更新成功");
				Object[] obj=new Object[2];
				obj[0]=le;
				obj[1]=jj;
				boolean key=dbHelper.saveByTransaction(obj);
		=====================================================
		 * 
		 * @param o
		 * @return
		 * @throws Exception
		 */
		public   boolean    saveByTransaction(Object[] o ) throws Exception;
		
		
		//主要解决批量数据入库表名字不固定，动态变化的情况,如按日期生成的表名
		public   boolean    saveByTransaction(Object[] o, String tableName) throws Exception;
		
		/**
		 * 事物操作，删除第1个对象成功后，在删除第2个对象
		 * 
		 e.g:
		 boolean key= dbHelper.deleteByTransaction("6", Lee.class, "j0002", Jojo.class);
		 
		 * @param id
		 * @param o
		 * @param sid
		 * @param so
		 * @return
		 */
		public boolean deleteByTransaction(String id, Class<?> o,String sid, Class<?> so);
	 
		/**
		 * 事物操作，删除第1个对象成功后，在删除第2个对象
		 * 
		 e.g:
		 boolean key= dbHelper.deleteByTransaction("j0002", Lee.class, 2, Jojo.class);
		 
		 * @param id
		 * @param o
		 * @param sid
		 * @param so
		 * @return
		 */
		public boolean deleteByTransaction(String id, Class<?> o,Integer sid, Class<?> so);
		
		/**
		 * 事物操作，删除第1个对象成功后，在删除第2个对象
		 * 
		 e.g:
		  boolean key= dbHelper.deleteByTransaction(3, Lee.class, "j0001", Jojo.class);
		 
		 * 
		 * @param id
		 * @param o
		 * @param sid
		 * @param so
		 * @return
		 */
		public boolean deleteByTransaction(Integer id, Class<?> o,String sid, Class<?> so);
		
		/**
		 * 事物操作，删除第1个对象成功后，在删除第2个对象
		 * 
		  e.g:
		  boolean key= dbHelper.deleteByTransaction(3, Lee.class, 12, Jojo.class);
		  
		 * @param id
		 * @param o
		 * @param sid
		 * @param so
		 * @return
		 */
		public boolean deleteByTransaction(Integer id, Class<?> o,Integer sid, Class<?> so);
		
		
		/**
		 * 多表记录删除，手动提交事务
		 * 
		====================================================
		e.g:
				Lee  le=new Lee();
				Jojo jj=new Jojo();
				le.setBirthday(DateUtil.getCurDateTime());
				le.setFlag("1");
				le.setId("lee-0001");
				le.setUsername("Lee大哥更新成功");
				
				jj.setBirthday(DateUtil.getCurDateTime());
				jj.setFlag("1");
				jj.setId("jj-0001");
				jj.setUsername("JOJO大哥更新成功");
				Object[] obj=new Object[2];
				obj[0]=le;
				obj[1]=jj;
				boolean key=dbHelper.removeByTransaction(obj);
		=====================================================
		 * @param obj
		 * @return
		 */
		public boolean removeByTransaction(Object[] obj);

	/**
	 * 批量删除根据数组id
	 * @param ids   filed_id:id字段名称
	 * @param tablename
	 * @return
	 */
		public boolean removeByTransaction(Object[] ids,String filed_id,String tablename);
		
		/**
		 * 多表记录更新，手动提交事务
		 * 
	    ====================================================
		e.g:
				Lee  le=new Lee();
				Jojo jj=new Jojo();
				le.setBirthday(DateUtil.getCurDateTime());
				le.setFlag("1");
				le.setId("lee-0001");
				le.setUsername("Lee大哥更新成功");
				
				jj.setBirthday(DateUtil.getCurDateTime());
				jj.setFlag("1");
				jj.setId("jj-0001");
				jj.setUsername("JOJO大哥更新成功");
				Object[] obj=new Object[2];
				obj[0]=le;
				obj[1]=jj;
				boolean key=dbHelper.updateByTransaction(obj);
		=====================================================
		 * 
		 * @param obj
		 * @return
		 */
		public boolean updateByTransaction(Object[] obj);
		
		

		/**
		 * 开启事物操作，先删除，后保存
		 * @param saveObj
		 * @param delObj
		 * @return
		 */
		public boolean deleteAfterSaveByTransaction(Object delObj,Object saveObj) ;
		
		
		/**
		 * 事物操作，先删除，后更新
		 * @param delObj
		 * @param updateObj
		 * @return
		 */
		public boolean deleteAfterUpdateByTransaction(Object delObj,Object updateObj);
		
		
		/**
		 * 开启事物操作，先保存，后删除
		 * @param saveObj
		 * @param delObj
		 * @return
		 */
		public boolean saveAfterDeleteByTransaction(Object saveObj,Object delObj) ;
		
		/**
		 * 不同类型件事物操作和回滚,先保存后更新
		 * @param saveObj
		 * @param updateObj
		 * @return
		 */
		public boolean saveAfterUpdateByTransaction(Object saveObj,Object updateObj);
		
		/**
		 * 事物操作，先更新，后删除
		 * @param delObj
		 * @param updateObj
		 * @return
		 */
		public boolean updateAfterDeleteByTransaction(Object  updateObj,Object delObj);
		
		/**
		 * 事物操作，先更新，后保存
		 * @param updateObj
		 * @param saveObj
		 * @return
		 */
		public boolean updateAfterSaveByTransaction(Object updateObj,Object saveObj);
		
		/**
		 * 根据from tbl_name查表中记录数
		 * @param from_sql
		 * @return
		 */
		public int QueryCount(String from_sql);
		
		/**
		 * 同一事物下，执行多条sql语句后一起提交事物
		 * @param multiSQL:Map<String,Object[]> 
		 * @return
		 */
		public boolean multiSQLByTransaction(Map<String,Object[]> multiSQL);
		
		/**
		 * 新增、更新、删除在同意事物下执行
		 * @param addObj
		 * @param updateObj
		 * @param delObj
		 * @return
		 */
		public boolean exeCUDByTransaction(Object addObj,Object updateObj,Object delObj); 
		
		/**
		 * 新增、更新、删除在同意事物下执行
		 * 无限多个操作事物处理
		 * map.put("C",javaBean)
		 * map.put("U",javaBean)
		 * map.put("D",javaBean)
		 * map.put("C",javaBean)
		 * Object[] objMap对象是一个map对象
		 * @param objMap
		 * @return
		 */
		public boolean exeCUDByTransaction(Object[] objMap);
		/**
		 * 批量删除
		 * @param ids 所有要删除的字符串数组id
		 * @return
		 */
		public  boolean removeByTransaction(String[] ids,Class<?> o);
		
		
}
