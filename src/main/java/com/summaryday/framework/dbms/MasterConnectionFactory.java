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

public class MasterConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(MasterConnectionFactory.class);

	private static ComboPooledDataSource ds = null;
	private static DruidDataSource        dd=null;
	
	//private static ResourceBundle  BUNDLE       = ResourceBundle.getBundle("db-ms");
	private static  ResourceBundle BUNDLE =null;
	/*static {
		try {
			BUNDLE = ResourceBundle.getBundle("db-ms");
			} catch (Exception e) {
				logger.error("主从db-ms配置文件加载异常:"+e.getMessage());
			}
	}*/
	private static final String MSATER_SLAVE_TYPE = "master_slave_type";
	private static final String        POOLTYPE ="PoolType";
	private static final String          DRIVER = "driver";
	
	
	private static final String             URL = "master_url";
	private static final String        USERNAME = "master_username";
	private static final String        PASSWORD = "master_password";
	
	//c3p0
	private static final String      MAXPOOLSIZE="master_MaxPoolSize";
	private static final String      MINPOOLSIZE="master_MinPoolSize";
	private static final String   IDLETESTPERIOD="master_idleConnectionTestPeriod";
	private static final String ACQUIREINCREMENT="master_acquireIncrement";
	private static final String  INITIALPOOLSIZE="master_initialPoolSize";
	private static final String    MAXSTATEMENTS="master_maxStatements";
	private static final String     HELPERTHEADS="master_numHelperThreads";
	private static final String         IDLETIME="master_maxIdleTime";
	private static final String    RetryAttempts="master_acquireRetryAttempts";
	private static final String       RetryDelay="master_acquireRetryDelay";
	private static final String          TIMEOUT="master_checkoutTimeout";
	
	
	//druid
	private static final String                            INITIALSIZE="master_initialSize";
	private static final String                                MINIDLE="master_minIdle";
	private static final String                              maxActive="master_maxActive";
	private static final String                                maxWait="master_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="master_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="master_minEvictableIdleTimeMillis";
	private static final String                        validationQuery="master_validationQuery";
	private static final String                                filters="master_filters";
	private static final String                      AutoCommitOnClose="master_autoCommitOnClose";
	private static final String              maxOpenPreparedStatements="master_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="master_poolPreparedStatements";
	private static final String  maxPoolPreparedStatementPerConnectionSize="master_maxPoolPreparedStatementPerConnectionSize";
	private static final String                           TestOnBorrow="master_testOnBorrow";
	private static final String                           TestOnReturn="master_testOnReturn";
	private static final String                        RemoveAbandoned="master_removeAbandoned";
	private static final String                 RemoveAbandonedTimeout="master_removeAbandonedTimeout";
	private static final String                 		  LogAbandoned="master_logAbandoned";
	
	public  static  boolean       EHCACHE=false;
	public  static  String       DIALECT =null;
	public  static  boolean    SQL_FORMAT=true;
	public  static  String       PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	public  static  boolean     Is_MS_OPEN=false;//是否开启主从配置
	public  static  boolean    db_Master_W =false;//默认主库为只写方式
	public  static  int         MS_TYPE=0;
	MasterConnectionFactory(){}    


	static {
		BUNDLE=LoadPropertiesUtils.BUNDLE;
	if(LoadPropertiesUtils.Is_MS_OPEN&&LoadPropertiesUtils.db_Master_W){	
		logger.info("\n"+
		"=====================================\n"+
		"‖      开启数据库主从db模式初始化       ‖\n"+
		"=====================================\n"
		+"\n");
		try {
			EncryptUtils.isGOTO();
		} catch (Exception e) {
			System.out.println("安全检查-注册失效，服务会受影响！");
		}
		try {
			PoolType = BUNDLE.getString(POOLTYPE);
			DIALECT  = BUNDLE.getString("dialect");
		} catch (Exception e) {
			logger.error("获取PoolType异常："+e.getMessage());
		}
		
	
		try {
			EHCACHE   =StringUtil.StringToBoolean(BUNDLE.getString("master_ehcache"));
		} catch (Exception e1) {
			logger.error("警告:获取master_ehcache异常："+e1.getMessage());
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
					"‖             c3p0初始化             ‖\n"+
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
						//dd.setMaxOpenPreparedStatements(StringUtil.StringToInteger(BUNDLE.getString(maxOpenPreparedStatements)));
					}
				
				dd.setFilters(BUNDLE.getString(filters));
				dd.setDefaultAutoCommit(StringUtil.StringToBoolean(BUNDLE.getString(AutoCommitOnClose)));
				dd.setValidationQuery(BUNDLE.getString(validationQuery));
				dd.setMinEvictableIdleTimeMillis(StringUtil.StringToInteger(BUNDLE.getString(minEvictableIdleTimeMillis)));
				dd.setTimeBetweenEvictionRunsMillis(StringUtil.StringToInteger(BUNDLE.getString(timeBetweenEvictionRunsMillis)));
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("master_testWhileIdle")));
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
						"‖            druid初始化             ‖\n"+
						"=====================================\n"
						+"\n");
		} catch (Exception e) {
			logger.error("\n druid 数据源连接池初始化异常："+e.getMessage());
		}
    }
	
   }//if Is_MS_OPEN
	else{logger.info("未开启主db配置参数");}		
}//static

	
	public static synchronized Connection getConnection() {
	        Connection con = null;
	        long s = System.currentTimeMillis();
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
			long e = System.currentTimeMillis();
			logger.info("耗时:" + (e - s));
	        return con;
	    }

		public static String getDialect() {
			return DIALECT;
		}
	    
	    public static Boolean getSqlFormat() {
			return SQL_FORMAT;
		}

	    private static MasterConnectionFactory instance;
	    
	    public static MasterConnectionFactory getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (MasterConnectionFactory.class) {
	                if(instance == null) {
	                    instance = new MasterConnectionFactory();
	                }
	                logger.info("\n"+
							"=========================================================================\n"+
							"‖                                                           主数据源实例化                                                                   ‖\n"+
							"‖                   "+instance+"	 	‖\n"+
							"=========================================================================\n"
							+"\n");
	            }
	        }
	        return instance;
	    }
	     
	   
} // C3P0 end
