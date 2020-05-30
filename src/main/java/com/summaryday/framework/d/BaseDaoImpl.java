package com.summaryday.framework.d;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.summaryday.framework.db.DBHelper;
import com.summaryday.framework.db.DBSetting;

public class BaseDaoImpl implements IBaseDao {
	
	private	DBHelper dbHelper=DBHelper.getInstance();
	
	public boolean save(Object o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.save(o);
		//return ((DBHelper) SpringBeanHelper.getBean("dBHelper")).save(o);
	}

	public boolean update(Object o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.update(o);
		//return ((DBHelper) SpringBeanHelper.getBean("dBHelper")).update(o);
	}


	public List<?> queryTables(Class<?> c, String[] link_name,String sql,boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.queryTables(c, link_name,sql,isCache);
	}


	public boolean remove(Object o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.remove(o);
		//return ((DBHelper) SpringBeanHelper.getBean("dBHelper")).remove(o);
	}


	public boolean delete(Integer id,Class<?> o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.delete(id, o);
		//return ((DBHelper) SpringBeanHelper.getBean("dBHelper")).delete(id, o);
	}


	public Object get(Integer id, Class<?> o,boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.get(id, o,isCache);
		//return ((DBHelper) SpringBeanHelper.getBean("dBHelper")).get(id, o);
	}


	public boolean CreateTable(String sql) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.CreateTable(sql);
	}


	public List<?> queryByOracle(Class<?> o, int p1, int p2, String desc)
			throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.queryByOracle(o, p1, p2, desc);
	}


	public List<?> query(Class<?> o,boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.find(o,isCache);
	}

	@SuppressWarnings("static-access")
	public List<?> querys(Class<?> o,boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.querys(o,isCache);
	}

	public List<?> queryList(Class<?> o, String isWhere, boolean isCache)
			throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.queryList(o, isWhere, isCache);
	}

	public Object load(Class<?> o, String iswhere,boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.load(o, iswhere, isCache);
	}

	public boolean saveByTransaction(Object[] o) throws Exception {
		// TODO Auto-generated method stub
		
		return dbHelper.saveByTransaction(o);
	}

	public Object get(String id, Class<?> o, boolean isCache) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.get(id, o, isCache);
	}
	public boolean remove(String id,Class<?> o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.delete(id, o);
	}

	public List<?> query(Class<?> c, String sql, boolean isCache)
			throws Exception {
		// TODO Auto-generated method stub
		return  dbHelper.query(c, sql, isCache);
	}

	public int executeCUD(String sql, Object... obj) {
		// TODO Auto-generated method stub
		return dbHelper.executeUpdate(sql, obj);
	}

	public <E> E query(String sql, Class<E> maplistClass,String table,boolean isCache ,Object... obj) {
		// TODO Auto-generated method stub
		return dbHelper.query(sql, maplistClass,table,isCache, obj);
	}

	public <T> List<T> queryList(Class<T> resultClass,
			String isWhere,boolean isCache, Object... obj) throws SQLException {
		// TODO Auto-generated method stub
		return dbHelper.queryList(resultClass, isWhere, isCache,obj);
	}

	public <T> void sortList(List<T> list, String sortField, String sortMode) {
		// TODO Auto-generated method stub
		dbHelper.sortList(list, sortField, sortMode);
	}

	public List<?> queryByList(Class<?> resultClass,
			String isWhere, boolean isCache, Object... obj) throws SQLException {
		// TODO Auto-generated method stub
		return dbHelper.queryByList(resultClass, isWhere, isCache, obj);
	}

	public List<?> queryForList(Class<?> Clazz, String sql, Object... obj) throws SQLException  {
		// TODO Auto-generated method stub
		return dbHelper.queryForList(Clazz, sql, obj);
	}

	public String saveForKey(Object o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.saveForKey(o);
	}

	//@Override
	public boolean saveUpdate(Object o) throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.saveUpdate(o);
	}

	public Map<String, Object> queryMap(String sql) {
		// TODO Auto-generated method stub
		return dbHelper.queryMap(sql);
	}

	public List<Map<String, Object>> queryListMap(String sql) {
		// TODO Auto-generated method stub
		return dbHelper.queryListMap(sql);
	}

	public boolean deleteByTransaction(String id, Class<?> o, String sid,
			Class<?> so) {
		// TODO Auto-generated method stub
		return dbHelper.deleteByTransaction(id, o, sid, so);
	}

	public boolean deleteByTransaction(String id, Class<?> o, Integer sid,
			Class<?> so) {
		// TODO Auto-generated method stub
		return dbHelper.deleteByTransaction(id, o, sid, so);
	}

	public boolean deleteByTransaction(Integer id, Class<?> o, String sid,
			Class<?> so) {
		// TODO Auto-generated method stub
		return dbHelper.deleteByTransaction(id, o, sid, so);
	}

	public boolean deleteByTransaction(Integer id, Class<?> o, Integer sid,
			Class<?> so) {
		// TODO Auto-generated method stub
		return dbHelper.deleteByTransaction(id, o, sid, so);
	}

	public boolean removeByTransaction(Object[] obj) {
		// TODO Auto-generated method stub
		return dbHelper.removeByTransaction(obj);
	}

	public boolean removeByTransaction(Object[] ids, String filed_id, String tablename) {
		return dbHelper.removeByTransaction(ids,  filed_id,  tablename);
	}

	public boolean updateByTransaction(Object[] obj) {
		// TODO Auto-generated method stub
		return dbHelper.updateByTransaction(obj);
	}

	public boolean deleteAfterSaveByTransaction(Object delObj, Object saveObj) {
		// TODO Auto-generated method stub
		return dbHelper.deleteAfterSaveByTransaction(delObj, saveObj);
	}

	public boolean deleteAfterUpdateByTransaction(Object delObj,
			Object updateObj) {
		// TODO Auto-generated method stub
		return dbHelper.deleteAfterUpdateByTransaction(delObj, updateObj);
	}

	public boolean saveAfterDeleteByTransaction(Object saveObj, Object delObj) {
		// TODO Auto-generated method stub
		return dbHelper.saveAfterDeleteByTransaction(saveObj, delObj);
	}

	public boolean saveAfterUpdateByTransaction(Object saveObj, Object updateObj) {
		// TODO Auto-generated method stub
		return dbHelper.saveAfterUpdateByTransaction(saveObj, updateObj);
	}

	public boolean updateAfterDeleteByTransaction(Object updateObj,
			Object delObj) {
		// TODO Auto-generated method stub
		return dbHelper.updateAfterDeleteByTransaction(updateObj, delObj);
	}

	public boolean updateAfterSaveByTransaction(Object updateObj, Object saveObj) {
		// TODO Auto-generated method stub
		return dbHelper.updateAfterSaveByTransaction(updateObj, saveObj);
	}

	public int QueryCount(String from_sql) {
		// TODO Auto-generated method stub
		return dbHelper.QueryCount(from_sql);
	}

	@Override
	public boolean multiSQLByTransaction(Map<String, Object[]> multiSQL) {
		// TODO Auto-generated method stub
		return dbHelper.multiSQLByTransaction(multiSQL);
	}

	@Override
	public boolean exeCUDByTransaction(Object addObj, Object updateObj,
			Object delObj) {
		// TODO Auto-generated method stub
		return dbHelper.exeCUDByTransaction(addObj, updateObj, delObj);
	}

	@Override
	public boolean removeByTransaction(String[] ids, Class<?> o) {
		// TODO Auto-generated method stub
		return dbHelper.removeByTransaction(ids, o);
	}

	@Override
	public boolean saveByTransaction(Object[] o, String tableName)
			throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.saveByTransaction(o, tableName);
	}

	@Override
	public int executeAndUpdate(String sql) {
		// TODO Auto-generated method stub
		return dbHelper.executeAndUpdate(sql);
	}

	@Override
	public int[] createTables(List<String> sqlList)
			throws Exception {
		// TODO Auto-generated method stub
		return dbHelper.createTables(sqlList);
	}

	@Override
	public boolean exeCUDByTransaction(Object[] objMap) {
		// TODO Auto-generated method stub
		return dbHelper.exeCUDByTransaction(objMap);
	}



}
