package com.summaryday.framework.db;

import java.sql.Connection;

/**
 * 
 * 
 * 动态连接多个数据库，通过传来访问的数据库SID
 * @author Administrator
 *
 */
public class SummaryDayConnectionFactory {
    private static  String DIALECT =null;
	/*private static final Logger logger = LoggerFactory.getLogger(SummaryDayConnectionFactory.class);
	
	private static ComboPooledDataSource ds = null;
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("db");
	private static  String DIALECT =null;
	private static  String SQL_FORMAT =null;
	static  String EHCACHE="false";
	private static String SID="";
	private static String[] city=null;
	
	private static Map<String,Object> map=new HashMap<String, Object>();
	
	static{
		
	        try {
	        	city=BUNDLE.getString("citySID").split(":");
	        	//System.out.println("当前地市数:"+city.length);
	        	for(int i=0;i<city.length;i++){
	        			SID=city[i];
		              ds = new ComboPooledDataSource();
		              ds.setDriverClass(BUNDLE.getString("driver_"+SID));  
		              ds.setJdbcUrl(BUNDLE.getString("url_"+SID));
		              ds.setUser(BUNDLE.getString("username_"+SID));
		              ds.setPassword(BUNDLE.getString("password_"+SID));
		              try {
						ds.setMaxPoolSize(StringUtil.StringToInteger(BUNDLE
								.getString("MaxPoolSize_"+SID)));
						ds.setMinPoolSize(StringUtil.StringToInteger(BUNDLE
								.getString("MinPoolSize_"+SID)));
						ds.setIdleConnectionTestPeriod(StringUtil
								.StringToInteger(BUNDLE.getString("idleConnectionTestPeriod_"+SID)));
						ds.setAcquireIncrement(StringUtil.StringToInteger(BUNDLE
								.getString("acquireIncrement_"+SID)));
						ds.setInitialPoolSize(StringUtil.StringToInteger(BUNDLE
								.getString("initialPoolSize_"+SID)));
						ds.setMaxStatements(StringUtil.StringToInteger(BUNDLE
								.getString("maxStatements_"+SID)));
						ds.setNumHelperThreads(StringUtil.StringToInteger(BUNDLE
								.getString("numHelperThreads_"+SID)));
						ds.setMaxIdleTime(StringUtil.StringToInteger(BUNDLE
								.getString("maxIdleTime_"+SID)));
						SummaryDayConnectionFactory.DIALECT=BUNDLE.getString("dialect_"+SID);
						SummaryDayConnectionFactory.SQL_FORMAT=BUNDLE.getString("sql_format_"+SID);
						SummaryDayConnectionFactory.EHCACHE=BUNDLE.getString("ehcache_"+SID);
					} catch (Exception e) {
						// TODO: handle exception
						logger.error("SID:"+SID+"连接池参数选择："+e);
					}
					
					map.put(city[i], ds);
					logger.info("\n\n--------已经动态创建数据源:"+map.size()+"-----------\n");
					if("true".equals(SummaryDayConnectionFactory.SQL_FORMAT)){
						logger.info("  \n ///////////////////////////SID:"+SID+"连接池相关参数：/////////////////////////////////" +
			              			  "\n //\tAcquireIncrement:"+ds.getAcquireIncrement()+
			            		  	  "\n //\tAcquireRetryAttempts:"+ds.getAcquireRetryAttempts()+
			            		  	  "\n //\tAcquireRetryDelay:"+ds.getAcquireRetryDelay()+
			            		  	  "\n //\tCheckoutTimeout:"+ds.getCheckoutTimeout()+
			            		  	  "\n //\tAllUsers:"+ds.getAllUsers()+
			            		  	  "\n //\tIdentityToken:"+ds.getIdentityToken()+
			            		  	  "\n //\tLoginTimeout:"+ds.getLoginTimeout()+
			            		  	  "\n //\tMaxIdleTime:"+ds.getMaxIdleTime()+
			            		  	  "\n //\tNumBusyConnections:"+ds.getNumBusyConnections()+
			            		  	  "\n //\tThreadPoolNumActiveThreads:"+ds.getThreadPoolNumActiveThreads()+
			            		  	  "\n //\tThreadPoolSize:"+ds.getThreadPoolSize()+
			            		  	  "\n /////////////////////////////////////////////////////////////////////////" );
						}
	        	}
	        }  catch (Exception e) {
	           // e.printStackTrace();
	        	logger.error("\n数据源连接池初始化异常："+e);
	        }

	    	
	    }
	  
	    public static synchronized Connection getConnection() {
	        Connection con = null;
	        try {
	            con = ds.getConnection();
	        } catch (SQLException e1) {
	          //  e1.printStackTrace();
	        	logger.error("\n c3p0连接池--获取数据库连接异常："+e1);
	        }
	        return con;
	    }
	    public static synchronized Connection getConnection(String SID){
	        Connection con = null;
	        try {
	        	ComboPooledDataSource ds_=(ComboPooledDataSource) map.get(SID);
	            con = ds_.getConnection();
	        } catch (SQLException e1) {
	          //  e1.printStackTrace();
	        	logger.error("\n c3p0连接池--获取数据库连接异常："+e1);
	        }
	        return con;
	    }
		public static String getDialect() {
			return DIALECT;
		}
	    
	    public static String getSqlFormat() {
			return SQL_FORMAT;
		}

		public static String getSID() {
			return SID;
		}

		public static void setSID(String sID) {
			SID = sID;
		}
*/
    public static synchronized Connection getConnection() {
        return null;
    }
    public static synchronized Connection getConnection(String SID) {
        return null;
    }
    public static String getDialect() {
        return DIALECT;
    }
} // C3P0 end
