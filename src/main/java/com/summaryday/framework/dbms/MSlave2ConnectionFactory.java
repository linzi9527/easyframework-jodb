package com.summaryday.framework.dbms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.summaryday.framework.db.EncryptUtils;
import com.summaryday.framework.db.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSlave2ConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(MSlave2ConnectionFactory.class);

	private static ComboPooledDataSource ds = null;
	private static DruidDataSource        dd=null;
	
	//private static ResourceBundle        BUNDLE = ResourceBundle.getBundle("db-ms");
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
	
	
	private static final String             URL = "slave02_url";
	private static final String        USERNAME = "slave02_username";
	private static final String        PASSWORD = "slave02_password";
	
	//c3p0
	private static final String      MAXPOOLSIZE="slave02_MaxPoolSize";
	private static final String      MINPOOLSIZE="slave02_MinPoolSize";
	private static final String   IDLETESTPERIOD="slave02_idleConnectionTestPeriod";
	private static final String ACQUIREINCREMENT="slave02_acquireIncrement";
	private static final String  INITIALPOOLSIZE="slave02_initialPoolSize";
	private static final String    MAXSTATEMENTS="slave02_maxStatements";
	private static final String     HELPERTHEADS="slave02_numHelperThreads";
	private static final String         IDLETIME="slave02_maxIdleTime";
	private static final String    RetryAttempts="slave02_acquireRetryAttempts";
	private static final String       RetryDelay="slave02_acquireRetryDelay";
	private static final String          TIMEOUT="slave02_checkoutTimeout";
	
	
	//druid
	private static final String                            INITIALSIZE="slave02_initialSize";
	private static final String                                MINIDLE="slave02_minIdle";
	private static final String                              maxActive="slave02_maxActive";
	private static final String                                maxWait="slave02_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="slave02_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="slave02_minEvictableIdleTimeMillis";
	private static final String  maxPoolPreparedStatementPerConnectionSize="slave02_maxPoolPreparedStatementPerConnectionSize";
	private static final String                        validationQuery="slave02_validationQuery";
	private static final String                                filters="slave02_filters";
	private static final String                      AutoCommitOnClose="slave02_autoCommitOnClose";
	private static final String              maxOpenPreparedStatements="slave02_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="slave02_poolPreparedStatements";
	private static final String                           TestOnBorrow="slave02_testOnBorrow";
	private static final String                           TestOnReturn="slave02_testOnReturn";
	private static final String                        RemoveAbandoned="slave02_removeAbandoned";
	private static final String                 RemoveAbandonedTimeout="slave02_removeAbandonedTimeout";
	private static final String                 		  LogAbandoned="slave02_logAbandoned";


	public  static  boolean       EHCACHE=false;
	public  static  String       DIALECT =null;
	public  static  boolean    SQL_FORMAT=false;
	public  static  String       PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	private static boolean     Is_MS_OPEN=false;//是否开启主从配置
	public  static  boolean    db_Slave02_R =false;//默认从库为只读方式
	
	MSlave2ConnectionFactory(){}    


	static {
		BUNDLE=LoadPropertiesUtils.BUNDLE;
		if(null!=BUNDLE){
			try {
				Is_MS_OPEN =StringUtil.StringToBoolean(BUNDLE.getString("Is_MS_OPEN"));
			} catch (Exception e) {
				logger.error("警告:获取Is_MS_OPEN");
			}
			try {
				db_Slave02_R=StringUtil.StringToBoolean(BUNDLE.getString("db_Slave02_R"));
			} catch (Exception e) {
				logger.error("警告:获取db_Slave02_R");
			}
	
		if(Is_MS_OPEN&&db_Slave02_R){
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
			EHCACHE   =StringUtil.StringToBoolean(BUNDLE.getString("slave02_ehcache"));
		} catch (Exception e1) {
			logger.error("警告:获取slave02_ehcache异常："+e1.getMessage());
		}
		if(PoolType!=null&&"c3p0".equals(PoolType.toLowerCase())){
	        try{				
	              ds = new ComboPooledDataSource();
	              ds.setDriverClass(BUNDLE.getString(DRIVER));  
	              ds.setJdbcUrl(BUNDLE.getString(URL));
	              ds.setUser(BUNDLE.getString(USERNAME));
	              ds.setPassword(BUNDLE.getString(PASSWORD));
		          
	            	ds.setAutoCommitOnClose(StringUtil.StringToBoolean(BUNDLE.getString(AutoCommitOnClose)));
	            	ds.setCheckoutTimeout(StringUtil.StringToInteger(BUNDLE.getString(TIMEOUT)));
	            	ds.setAcquireRetryDelay(StringUtil.StringToInteger(BUNDLE.getString(RetryDelay)));
	            	ds.setAcquireRetryAttempts(StringUtil.StringToInteger(BUNDLE.getString(RetryAttempts)));
					ds.setMaxPoolSize(StringUtil.StringToInteger(BUNDLE.getString(MAXPOOLSIZE)));
					ds.setMinPoolSize(StringUtil.StringToInteger(BUNDLE.getString(MINPOOLSIZE)));
					ds.setIdleConnectionTestPeriod(StringUtil.StringToInteger(BUNDLE.getString(IDLETESTPERIOD)));
					ds.setAcquireIncrement(StringUtil.StringToInteger(BUNDLE.getString(ACQUIREINCREMENT)));
					ds.setInitialPoolSize(StringUtil.StringToInteger(BUNDLE.getString(INITIALPOOLSIZE)));
					ds.setMaxStatements(StringUtil.StringToInteger(BUNDLE.getString(MAXSTATEMENTS)));
					ds.setNumHelperThreads(StringUtil.StringToInteger(BUNDLE.getString(HELPERTHEADS)));
					ds.setMaxIdleTime(StringUtil.StringToInteger(BUNDLE.getString(IDLETIME)));
					
					logger.info("\n"+
					"=====================================\n"+
					"‖                         c3p0初始化                               ‖\n"+
					"=====================================\n"
					+"\n");
		} catch (Exception e) {
			logger.error("c3p0连接池参数选择："+e.getMessage());
		}
    }else	if (PoolType!=null&&"druid".equals(PoolType.toLowerCase())) {
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
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("slave02_testWhileIdle")));
				dd.setTestOnReturn(StringUtil.StringToBoolean(BUNDLE.getString(TestOnReturn)));
				dd.setTestOnBorrow(StringUtil.StringToBoolean(BUNDLE.getString(TestOnBorrow)));

				 try {
					 dd.setRemoveAbandoned(StringUtil.StringToBoolean(BUNDLE.getString(RemoveAbandoned)));
					 dd.setRemoveAbandonedTimeout(StringUtil.StringToInteger(BUNDLE.getString(RemoveAbandonedTimeout)));
					 dd.setLogAbandoned(StringUtil.StringToBoolean(BUNDLE.getString(LogAbandoned)));
				 } catch (Exception e) {
					 logger.error("参数[RemoveAbandoned、RemoveAbandonedTimeout、LogAbandoned]配置或转换类型异常：",e.getMessage());
				 }
				logger.info("\n"+
						"=====================================\n"+
						"‖        druid初始化                 ‖\n"+
						"=====================================\n"
						+"\n");
		} catch (Exception e) {
			logger.error("\n druid 数据源连接池初始化异常："+e.getMessage());
		}
      }
    }//if Is_MS_OPEN
	else{logger.info("未开启从2配置参数");}
 }//if	
}//static

	
	public static synchronized Connection getConnection() {
	        Connection con = null;
	        try {
	        	if(EncryptUtils.LOCK){
		        	 if(PoolType!=null&&"c3p0".equals(PoolType.toLowerCase())) 
		            {
						con = ds.getConnection();
		            }else if(PoolType!=null&&"druid".equals(PoolType.toLowerCase())){
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

	    private static MSlave2ConnectionFactory instance;
	    
	    public static MSlave2ConnectionFactory getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (MSlave2ConnectionFactory.class) {
	                if(instance == null) {
	                    instance = new MSlave2ConnectionFactory();
	                }
	                logger.info("\n"+
							"=========================================================================\n"+
							"‖                                                         从02数据源实例化                                                             ‖\n"+
							"‖   "+instance+" 	‖\n"+
							"=========================================================================\n"
							+"\n");
	            }
	        }
	        return instance;
	    }
	     
	   
} // C3P0 end
