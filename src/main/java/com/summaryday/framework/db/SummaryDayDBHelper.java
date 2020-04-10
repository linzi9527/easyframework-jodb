package com.summaryday.framework.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;

import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import com.summaryday.framework.cache.Command;
import com.summaryday.framework.cache.JDBCBeanUtil;
import com.summaryday.framework.cache.SqlCommonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class SummaryDayDBHelper {
	private static final Logger log = LoggerFactory.getLogger(SummaryDayDBHelper.class);
	 private static final SummaryDayDBHelper postDB = new SummaryDayDBHelper();
	 private Connection connection = null;
	 private String isWhere="",order="",group="",leftAndRightJoin="",columName="",on="";
	 private String[] link_name=null;
	 


	public static SummaryDayDBHelper getInstance() {
	  return postDB;
	 }

	 /**
	  * 设置关联表名称即表名
	  */
	public SummaryDayDBHelper setTableLink(String[] tbl_name){
			if(!StringUtil.isNull(tbl_name)){
				link_name=tbl_name;
			}
			return this;
	}
	/**
	 * 设置关联所需要显示的字段名称
	 * @return
	 */
	public SummaryDayDBHelper setColumName(String columName){
		if(!StringUtil.isNull(columName)){
			this.columName=columName;
		}
		return this;
	}
	/**
	 * 设置左连接、右连接	
	 * @param leftOrRight
	 * @return
	 */
	public SummaryDayDBHelper setJoin(String leftOrRight){
		if(!StringUtil.isNull(leftOrRight)){
			leftAndRightJoin=leftOrRight;
		}
		return this;
	}
	/**
	 * 设置条件
	 * @param isString
	 * @return
	 */
	public SummaryDayDBHelper setWhere(String isString){
		if(!StringUtil.isNull(isString)){
			isWhere+=" WHERE "+isString;
		}
		return this;
	}
    /**
     * 设置顺序
     * @param isOrder
     * @return
     */
	public SummaryDayDBHelper setOrder(String isOrder){
		if(!StringUtil.isNull(isOrder)){
			order+=" ORDER BY "+isOrder;
		}
		return this;
	}
	/**
	 * 设置群组
	 * @param isGroup
	 * @return
	 */
	public SummaryDayDBHelper setGroup(String isGroup){
		if(!StringUtil.isNull(isGroup)){
			group+=" GROUP BY "+isGroup;
		}
		return this;
	}
	
	public SummaryDayDBHelper setON(String on){
		if(!StringUtil.isNull(on)){
			this.on=on;
		}
		return this;
	}
		/**
		 * 查询数据
		 * @param sql
		 * @return
		 */
		public ResultSet executeQuery(String sql,String SID) {
			   ResultSet result = null;
			   PreparedStatement preparedStatement;
			   try {
					if(connection==null||connection.isClosed()) {
							connection=SummaryDayConnectionFactory.getConnection(SID);
						}
			    preparedStatement = connection.prepareStatement(sql);
			    result = preparedStatement.executeQuery();
			   
			   } catch (SQLException e) {
			    log.error("executeQuery-->"+e);
			   }
			   return result;
			}
	 
	 /**
	  * 设置实体类自动查询
	  * @param o
	  * @return
	  */
		public List queryList(Class<?> o,String SID){
			SqlCommonDAO  helper=new SqlCommonDAO();
			 StringBuffer sb=new StringBuffer();
			 List	list=null;
			 sb.append("SELECT * FROM ");
			 String table="";
			 try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
				boolean tbl=o.newInstance().getClass().isAnnotationPresent(Table.class);
				if(tbl){
					  Table	t=(Table) o.newInstance().getClass().getAnnotation(Table.class);
					  	 table=connection.getCatalog()+"."+t.name();
					  	 sb.append(table);
					  }
				if(!StringUtil.isNull(isWhere)){ sb.append(isWhere);}
				if(!StringUtil.isNull(group)){ sb.append(group);}
				if(!StringUtil.isNull(order)){ sb.append(order);}
				//System.out.println("====>"+connection.getCatalog());
				 helper.setConnection(connection);
				 Command c=new Command();
				 helper.setCommand(c);
				 if(ConnectionFactory.EHCACHE){
					//log.info("...开启"+SummaryDayConnectionFactory.getSID()+"---"+connection.getCatalog()+"查询模式缓存...");
					 c.setCache(true);
				 }
				 c.setTables(new String[]{table});
				 c.setSql(sb.toString());
				// System.out.println(sb.toString());
				Result rs=helper.executeQuery();
				list=JDBCBeanUtil.transTOList(rs, o);
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				helper.closeConnection(connection);
			}
			 return list;
		 }
		
		/**
		 * 根据参数来查数据
		 *  SELECT * FROM tbl_user AS t
			 LEFT JOIN
			 tbl_employee AS s
			 ON t.id=s.emp_user_no
			 
			 postDB.setWhere("t0.id=12")
				 					 	.setGroup("t0.sex")
				 						 .setOrder("t0.create_date DESC")
				 						 .setTableLink(new String[]{"tbl_user","tbl_employee"})
				 						 .setJoin("right")
				 						 .setON("t0.id=t1.emp_user_no")
				 						 .query(UserLinkEmployeeVO.class);
		 * @param o
		 * @return
		 */
		public List list(Class<?> o,String SID){
			 StringBuffer sb=new StringBuffer();
			 List	list=null;
			 SqlCommonDAO  helper=new SqlCommonDAO();
			 try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
				if(!StringUtil.isNull(columName)){
					sb.append("SELECT "+columName+" FROM ");
				}else{
					sb.append("SELECT * FROM ");
				}
				if(!StringUtil.isNull(link_name)){ 
					for (int i = 0; i < link_name.length; i++) {
						    sb.append(connection.getCatalog()+"."+link_name[i]).append(" AS t"+i);
						    if(!StringUtil.isNull(leftAndRightJoin)&& i<link_name.length-1) {sb.append(" "+leftAndRightJoin.toUpperCase()+" JOIN ");}
					     }
				}
				if(!StringUtil.isNull(on)) {sb.append(" ON "+on);}
				if(!StringUtil.isNull(isWhere)){ sb.append(isWhere);}
				if(!StringUtil.isNull(group)){ sb.append(group);}
				if(!StringUtil.isNull(order)){ sb.append(order);}
				
				 helper.setConnection(connection);
				 Command c=new Command();
				 helper.setCommand(c);
				 if(ConnectionFactory.EHCACHE){
						//log.info("...开启"+SummaryDayConnectionFactory.getSID()+"---"+connection.getCatalog()+"查询模式缓存...");
						 c.setCache(true);
					 }
				 c.setTables(link_name);
				 c.setSql(sb.toString());
			System.out.println(sb.toString());
				Result rs=helper.executeQuery();
				list=	JDBCBeanUtil.transTOList(rs, o);
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				helper.closeConnection(connection);
			}
			 return list;
		 }
	/**
	 * 自定义多表联合查询sql语句
	 * 必须设置那几张表关联setTableLink
	 * String sql="SELECT t0.id,t0.user_id,t0.user_name,t0.address,t1.emp_Name,t1.emp_Pwd,t1.emp_Addr FROM tbl_user AS t0 right JOIN tbl_employee AS t1 ON t0.id=t1.emp_user_no";
	 * postDB.query(UEVO.class,sql);
	 * 
	 * @param o
	 * @param sql
	 * @return
	 */
		public List query(Class<?> o,String sql,String SID){
			 List	list=null;
			 SqlCommonDAO  helper=new SqlCommonDAO();
			 try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
				if(!StringUtil.isNull(sql)){
					 helper.setConnection(connection);
					 Command c=new Command();
					 helper.setCommand(c);
					 if(ConnectionFactory.EHCACHE){
							//log.info("...开启"+SummaryDayConnectionFactory.getSID()+"---"+connection.getCatalog()+"查询模式缓存...");
							 c.setCache(true);
						 }
					 c.setTables(link_name);
					 c.setSql(sql);
			
					Result rs=helper.executeQuery();
					list=	JDBCBeanUtil.transTOList(rs, o);
				}
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				helper.closeConnection(connection);
			}
			 return list;
		 }
			 

		
	/**
	 * 通用添加实体对象	
	 * @throws SQLException 
	 */
	public boolean	save(Object obj,String SID){
		 Key k=null;
		boolean flag=false;
		boolean flag_isAuto=false;
		int i=0;
		String tbl_name="";
		String tbl_name_seq="";
		String name ="";//记录字段名
		String value ="";//记录值名称
		String name_auto ="";//记录字段名
		String value_auto ="";//记录值名称
		StringBuffer sb=new StringBuffer();
		try {
			try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Field[] fields=	obj.getClass().getDeclaredFields();
			boolean tbl=obj.getClass().isAnnotationPresent(Table.class);
			sb.append("INSERT INTO ");
			
			if(tbl){
					Table	t=(Table) obj.getClass().getAnnotation(Table.class);
					try {
						tbl_name=connection.getCatalog()+"."+t.name();
						tbl_name_seq=t.name();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  	    sb.append(tbl_name);
			  }
			for (Field f : fields) {
				f.setAccessible(true);//开启私有变量的访问权限
				  Object v=f.get(obj);
				  flag=f.isAnnotationPresent(Key.class);
				  if(flag)   k= f.getAnnotation(Key.class);
				  Colum c=  f.getAnnotation(Colum.class);
				 //System.out.println(f.getName()+"|"+v);
					 if(v!=null&&k!=null){
						if(c!=null&& f.getName().toUpperCase().equals(c.columName().toUpperCase())) 
						 name+=f.getName()+",";
						else
						 name+=	c.columName()+",";
						if( f.getType().getName().equals("java.lang.String"))
						    value+="'"+v.toString()+"',";
						else
							value+=v.toString()+","; 
							
							//System.out.println("==>"+name+"|"+value);

					}else{
						if(!flag_isAuto&&k.isPrimary()&&k.isAuto()){
							name_auto=f.getName()+",";
							value_auto="SEQ_"+tbl_name_seq.toUpperCase()+".NEXTVAL,";
							flag_isAuto=true;
						}
					}
				  
			}//end
			
			  if(name.length()>0&&value.length()>0){
					  name=name.substring(0, name.length()-1);
					 value=value.substring(0, value.length()-1);

				  //判断方言是否为mysql，不用关心自增序列
				  //判断方言Oracle，必须加上自增序列
				  if(SummaryDayConnectionFactory.getDialect().equalsIgnoreCase("MySQLDialect")){
				     sb.append("(").append(name).append(")").append("  VALUES").append("(").append(value).append(")");
				  }else if(SummaryDayConnectionFactory.getDialect().equalsIgnoreCase("OracleDialect")){
					 sb.append("(").append(name_auto+name).append(")").append("  VALUES").append("(").append(value_auto+value).append(")");
				  }
				  SqlCommonDAO  helper=new SqlCommonDAO();
					 try {
						helper.setConnection(connection);
						 Command c=new Command();
						 helper.setCommand(c);
						 c.setCache(false);
						 c.setTables(new String[]{tbl_name});
						 c.setSql(sb.toString());
						// log.info("====SAVE:"+sb.toString());
					     i=  helper.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						 helper.closeConnection(connection);
					 }
					
				
			  }
			  
		}  catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i>0?true:false;
		
	}//end
		
		
		
	 /**
	  * 更新实体对象
	  * @param obj
	  * @return
	  * @throws IllegalArgumentException
	  * @throws IllegalAccessException
	  */
	 public boolean update(Object obj,String SID){
			String wh ="";//记录字段名
			String k="";
		 boolean bl=false;//记录主键是否为空
		 boolean flag=false;
		 int i=0;
		 Key isKey=null;
		 String tbl_name="";
		String sql="UPDATE ";
		 try {
				try {
					if(connection==null||connection.isClosed()) {
						connection=SummaryDayConnectionFactory.getConnection(SID);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(obj!=null){
					Field[] fiels=obj.getClass().getDeclaredFields();//获得反射对象集合
					boolean t=obj.getClass().isAnnotationPresent(Table.class);//获得类是否有注解
					if(t){
						Table tab=obj.getClass().getAnnotation(Table.class);
						try {
							tbl_name=connection.getCatalog()+"."+tab.name();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						sql+=tab.name()+"  SET  ";//获得表名
						
						for(Field fl:fiels){//循环组装
							fl.setAccessible(true);//开启支私有变量的访问权限
							Object	tobj = fl.get(obj);
							  Colum c=  fl.getAnnotation(Colum.class);
							flag=fl.isAnnotationPresent(Key.class);
						    if(flag) isKey=fl.getAnnotation(Key.class);
						    
							if(tobj!=null&&isKey!=null){
								if(!bl&&isKey.isPrimary()&&isKey.isAuto()){//判断是否存在主键
									bl=true;
									if( fl.getType().getName().equals("java.lang.String"))
										k="  WHERE  "+fl.getName()+"='"+tobj.toString()+"'  ";
									else
										k="  WHERE  "+fl.getName()+"="+tobj.toString()+"  ";
									
								}else{
									if(c!=null&& fl.getName().toUpperCase().equals(c.columName().toUpperCase())){
										if( fl.getType().getName().equals("java.lang.String"))
											wh+=fl.getName()+"='"+tobj.toString()+"',";
										else
											wh+=fl.getName()+"="+tobj.toString()+",";
									}else{
										if( fl.getType().getName().equals("java.lang.String"))
											wh+=c.columName()+"='"+tobj.toString()+"',";
										else
											wh+=c.columName()+"="+tobj.toString()+",";	
									}
								}
							}
						}
							if(wh.length()>0&&k.length()>0){
								wh=wh.substring(0,wh.length()-1);
								k=k.substring(0,k.length()-1);
							    sql+=wh+k;
							    
								  SqlCommonDAO  helper=new SqlCommonDAO();
									 try {
										helper.setConnection(connection);
										 Command c=new Command();
										 helper.setCommand(c);
										 c.setCache(false);
										 c.setTables(new String[]{tbl_name});
										 c.setSql(sql.toString());
										 i=  helper.executeUpdate();
									 } catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
									 }finally{
										 helper.closeConnection(connection);
									 }
							}
						//log.info("\n"+sql);
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		return i>0?true:false;
	 }
     
	 
	 /**
	  * 根据实体Object删除记录
	  * @return
	  */
	 public boolean remove(Object obj,String SID){
		 boolean flag=false;
		 boolean flag_is=false;
		 Key isKey=null;
		 String tbl_name="";
		 String sql="DELETE FROM  ";
		 int i=0;
		 try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	  try {
			       if(obj!=null){
					   Field[] fiels=obj.getClass().getDeclaredFields();//获得反射对象集合
						boolean t=obj.getClass().isAnnotationPresent(Table.class);//获得类是否有注解
						if(t){
							Table tab=obj.getClass().getAnnotation(Table.class);
							try {
								tbl_name=connection.getCatalog()+"."+tab.name();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sql+=tbl_name;
						}
						for(Field fl:fiels){//循环组装
							fl.setAccessible(true);//开启支私有变量的访问权限
							Object	tobj = fl.get(obj);
							flag=fl.isAnnotationPresent(Key.class);
						    if(flag) isKey=fl.getAnnotation(Key.class);
						    if(tobj!=null&&isKey!=null){
							    if(!flag_is&&isKey.isPrimary()&&isKey.isAuto()){
							    	flag_is=true;
							    	sql+="  WHERE  "+fl.getName()+"="+tobj.toString()+"  ";
							    	break;
							    }
						    }
						}
						  SqlCommonDAO  helper=new SqlCommonDAO();
						 try {
							 Command c=new Command();
						   	 helper.setConnection(connection);
							 helper.setCommand(c);
							 c.setCache(false);
							 c.setTables(new String[]{tbl_name});
							 c.setSql(sql.toString());
							 i=  helper.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							 helper.closeConnection(connection);
						 }
			       }
			 } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return  i>0?true:false;
	 }
	 
	 /**
	  * 通过ID删除实体对象
	  * @param id
	  * @param o
	  * @return
	  */
	 public boolean delete(Integer id,Class<?> o,String SID){
			String tbl_name="";
		 boolean flag=false,flag_is=false;
		 int i=0;
		 Key isKey=null;
		 String sql="DELETE FROM ";
		 
		 try {
				if(connection==null||connection.isClosed()) {
					connection=SummaryDayConnectionFactory.getConnection(SID);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
		try {
			 if(o!=null){
				   Field[] fiels=o.newInstance().getClass().getDeclaredFields();//获得反射对象集合
				   boolean	t = o.newInstance().getClass().isAnnotationPresent(Table.class);
			//获得类是否有注解
			if(t){
				Table tab=o.newInstance().getClass().getAnnotation(Table.class);
				try {
					sql+=connection.getCatalog()+"."+tab.name();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			for(Field fl:fiels){//循环组装
				fl.setAccessible(true);//开启支私有变量的访问权限
				flag=fl.isAnnotationPresent(Key.class);
			    if(flag) isKey=fl.getAnnotation(Key.class);
			    if(isKey!=null){
				    if(!flag_is&&isKey.isPrimary()&&isKey.isAuto()){
				    	flag_is=true;
				    	sql+="  WHERE  "+fl.getName()+"="+id+"  ";
				    	break;
				    }
			    }
			}
			
			  SqlCommonDAO  helper=new SqlCommonDAO();
				 try {
					 Command c=new Command();
				   	 helper.setConnection(connection);
					 helper.setCommand(c);
					 c.setCache(false);
					 c.setTables(new String[]{tbl_name});
					 c.setSql(sql.toString());
					// System.out.println(sql);
					 i=  helper.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					 helper.closeConnection(connection);
				 }
		  }
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return  i>0?true:false;
	 }
	 /**
	  * 获取实体对象
	  * @param -instance
	  * @return
	  */
	 public Object get(Integer id,Class<?> o,String SID){
		 Object obj=null;
		 Key isKey=null;
		 boolean flag=false,flag_is=false;
		 StringBuffer sb=new StringBuffer();
		
		 try {
			if(connection==null||connection.isClosed()) {
				connection=SummaryDayConnectionFactory.getConnection(SID);
			}
			sb.append("SELECT * FROM ");
			String table="";
			   Field[] fiels=o.newInstance().getClass().getDeclaredFields();//获得反射对象集合
				boolean tbl=o.newInstance().getClass().isAnnotationPresent(Table.class);//获得类是否有注解

			if(tbl){
			    Table	t=(Table) o.newInstance().getClass().getAnnotation(Table.class);
			  	 table=connection.getCatalog()+"."+t.name();
			  	 sb.append(table);
			  }
			for(Field fl:fiels){//循环组装
				fl.setAccessible(true);//开启支私有变量的访问权限
				flag=fl.isAnnotationPresent(Key.class);
			    if(flag) isKey=fl.getAnnotation(Key.class);
			    if(isKey!=null){
				    if(!flag_is&&isKey.isPrimary()&&isKey.isAuto()){
				    	flag_is=true;
				    	sb.append("  WHERE  "+fl.getName()+"="+id+"  ");
				    	break;
				    }
			    }
			}
			//System.out.println(sb.toString());
			 SqlCommonDAO  helper=new SqlCommonDAO();
				 try {
					helper.setConnection(connection);
					Command c = new Command();
					helper.setCommand(c);
					c.setCache(true);
					c.setTables(new String[] { table });
					c.setSql(sb.toString());
					Result rs = helper.executeQuery();
					obj = JDBCBeanUtil.transTOObject(rs, o);
				} catch (Exception e) {
					// TODO: handle exception
				}finally{
					 helper.closeConnection(connection);
				 }
		 }catch (Exception e) {
			// TODO: handle exception
			 e.fillInStackTrace();
		 }
		 return obj;
	 }
	 

	
}
