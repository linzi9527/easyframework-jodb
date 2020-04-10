package com.summaryday.framework.dbms;

import com.alibaba.druid.pool.DruidDataSource;
import com.summaryday.framework.db.EncryptUtils;
import com.summaryday.framework.db.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MSlave5ConnectionFactory_3db2 {
	private static final Logger logger = LoggerFactory.getLogger(MSlave5ConnectionFactory_3db2.class);

	private static DruidDataSource        dd=null;
	
	//private static ResourceBundle    BUNDLE = ResourceBundle.getBundle("db-ms");
	private static  ResourceBundle BUNDLE =null;
/*	static {
		try {
			BUNDLE = ResourceBundle.getBundle("db-ms");
			} catch (Exception e) {
				logger.error("主从db-ms配置文件加载异常:"+e.getMessage());
			}
	}*/
	
	private static final String        POOLTYPE ="PoolType";
	private static final String          DRIVER = "driver";
	
	
	private static final String             URL = "db03_slave05_url";
	private static final String        USERNAME = "db03_slave05_username";
	private static final String        PASSWORD = "db03_slave05_password";
	

	//druid
	private static final String                            INITIALSIZE="db03_slave05_initialSize";
	private static final String                                MINIDLE="db03_slave05_minIdle";
	private static final String                              maxActive="db03_slave05_maxActive";
	private static final String                                maxWait="db03_slave05_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="db03_slave05_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="db03_slave05_minEvictableIdleTimeMillis";
	private static final String  maxPoolPreparedStatementPerConnectionSize="db03_slave05_maxPoolPreparedStatementPerConnectionSize";
	private static final String                        validationQuery="db03_slave05_validationQuery";
	private static final String                                filters="db03_slave05_filters";
	private static final String                      AutoCommitOnClose="db03_slave05_autoCommitOnClose";
	private static final String              maxOpenPreparedStatements="db03_slave05_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="db03_slave05_poolPreparedStatements";
	private static final String                           TestOnBorrow="db03_slave05_testOnBorrow";
	private static final String                           TestOnReturn="db03_slave05_testOnReturn";
	
	public  static  boolean       EHCACHE=false;
	public  static  String       DIALECT =null;
	public  static  boolean    SQL_FORMAT=false;
	public  static  String       PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	private static boolean     Is_MS_OPEN=false;//是否开启主从配置
	public  static  boolean    db03_db_Slave05_R =false;//默认从库为只读方式
	
	MSlave5ConnectionFactory_3db2(){}    


	static {
		BUNDLE=LoadPropertiesUtils.BUNDLE;
		if(null!=BUNDLE){	
			try {
				Is_MS_OPEN =StringUtil.StringToBoolean(BUNDLE.getString("Is_MS_OPEN"));
			} catch (Exception e) {
				logger.error("警告:获取Is_MS_OPEN");
			}
			try {
				db03_db_Slave05_R=StringUtil.StringToBoolean(BUNDLE.getString("db03_db_Slave05_R"));
			} catch (Exception e) {
				logger.error("警告:获取db03_db_Slave05_R");
			}
	  if(Is_MS_OPEN&&db03_db_Slave05_R){	
			try {
				EncryptUtils.isGOTO();
			} catch (Exception e) {
				System.out.println("安全检查-注册失效，服务会受影响！");
			}
			try {
				PoolType = BUNDLE.getString(POOLTYPE);
				DIALECT=BUNDLE.getString("dialect");
			} catch (Exception e) {
				logger.error("获取PoolType异常："+e.getMessage());
			}
			
			try {
				EHCACHE   =StringUtil.StringToBoolean(BUNDLE.getString("db03_slave05_ehcache"));
			} catch (Exception e1) {
				logger.error("警告:获取db03_slave05_ehcache异常："+e1.getMessage());
			}
		if (PoolType!=null&&"druid".equals(PoolType.toLowerCase())) {
		//driud
		 try{
				
				dd = new DruidDataSource();
				dd.setDriverClassName(BUNDLE.getString(DRIVER));
				dd.setUrl(BUNDLE.getString(URL));
				dd.setUsername(BUNDLE.getString(USERNAME));
				dd.setPassword(BUNDLE.getString(PASSWORD));
				
				
				dd.setInitialSize(StringUtil.StringToInteger(BUNDLE.getString(INITIALSIZE)));
				dd.setMaxActive(StringUtil.StringToInteger(BUNDLE.getString(maxActive)));
				dd.setMinIdle(StringUtil.StringToInteger(BUNDLE.getString(MINIDLE)));
				dd.setMaxWait(StringUtil.StringToInteger(BUNDLE.getString(maxWait)));

				// 启用监控统计功能
				if (DIALECT!=null&&DIALECT.toLowerCase().indexOf("mysql") > -1){
					// for mysql
						dd.setValidationQuery(BUNDLE.getString(validationQuery));
					}else{
						dd.setPoolPreparedStatements(StringUtil.StringToBoolean(BUNDLE.getString(PoolPreparedStatements)));
						dd.setMaxPoolPreparedStatementPerConnectionSize(StringUtil.StringToInteger(BUNDLE.getString(maxPoolPreparedStatementPerConnectionSize)));
						//dd.setMaxOpenPreparedStatements(StringUtil.StringToInteger(BUNDLE.getString(MAXOPENPREPAREDSTATEMENTS)));
					}
				
				dd.setFilters(BUNDLE.getString(filters));
				dd.setDefaultAutoCommit(StringUtil.StringToBoolean(BUNDLE.getString(AutoCommitOnClose)));
				dd.setValidationQuery(BUNDLE.getString(validationQuery));
				dd.setMinEvictableIdleTimeMillis(StringUtil.StringToInteger(BUNDLE.getString(minEvictableIdleTimeMillis)));
				dd.setTimeBetweenEvictionRunsMillis(StringUtil.StringToInteger(BUNDLE.getString(timeBetweenEvictionRunsMillis)));
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("db03_slave05_testWhileIdle")));
				dd.setTestOnReturn(StringUtil.StringToBoolean(BUNDLE.getString(TestOnReturn)));
				dd.setTestOnBorrow(StringUtil.StringToBoolean(BUNDLE.getString(TestOnBorrow)));
				
				logger.info("\n"+
						"=====================================\n"+
						"‖                         druid初始化                               ‖\n"+
						"=====================================\n"
						+"\n");
		} catch (Exception e) {
			logger.error("\n druid 数据源连接池初始化异常："+e.getMessage());
		}
      }
	}//if Is_MS_OPEN
	else{logger.info("未开启从5配置参数");}
 }//if		
	  
}//static

	
	public static synchronized Connection getConnection() {
	        Connection con = null;
	        try {
	        	if(EncryptUtils.LOCK){
		        	if(PoolType!=null&&"druid".equals(PoolType.toLowerCase())){
		            	con=dd.getConnection();
		            }
	        	}else{
	        		System.out.println("系统为了系统安全，服务暂停！");
	        	}
	        } catch (SQLException e1) {
	        	logger.error("\n 连接池--获取数据库连接异常："+e1.getMessage());
	        }
	        return con;
	    }

		public static String getDialect() {
			return DIALECT;
		}
	    
	    public static Boolean getSqlFormat() {
			return SQL_FORMAT;
		}

	    private static MSlave5ConnectionFactory_3db2 instance;
	    
	    public static MSlave5ConnectionFactory_3db2 getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (MSlave5ConnectionFactory_3db2.class) {
	                if(instance == null) {
	                    instance = new MSlave5ConnectionFactory_3db2();
	                }
	                logger.info("\n"+
							"=========================================================================\n"+
							"‖                                   从05-db03数据源实例化                                                                   ‖\n"+
							"‖     "+instance+"	 ‖\n"+
							"=========================================================================\n"
							+"\n");
	            }
	        }
	        return instance;
	    }
	     
	   
} // end
