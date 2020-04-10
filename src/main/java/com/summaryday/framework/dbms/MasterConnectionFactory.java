package com.summaryday.framework.dbms;

import com.alibaba.druid.pool.DruidDataSource;
import com.summaryday.framework.db.EncryptUtils;
import com.summaryday.framework.db.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MasterConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(MasterConnectionFactory.class);

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
		"‖                    开启数据库主从db模式初始化            ‖\n"+
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
				dd.setRemoveAbandoned(StringUtil.StringToBoolean(BUNDLE.getString(RemoveAbandoned)));
				dd.setRemoveAbandonedTimeout(StringUtil.StringToInteger(BUNDLE.getString(RemoveAbandonedTimeout)));
	
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
	else{logger.info("未开启主db配置参数");}		
}//static

	
	public static synchronized Connection getConnection() {
	        Connection con = null;
	        long s = System.currentTimeMillis();
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
	     
	   
} // end
