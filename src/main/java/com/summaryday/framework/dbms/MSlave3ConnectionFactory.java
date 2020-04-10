package com.summaryday.framework.dbms;

import com.alibaba.druid.pool.DruidDataSource;
import com.summaryday.framework.db.EncryptUtils;
import com.summaryday.framework.db.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MSlave3ConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(MSlave3ConnectionFactory.class);

	private static DruidDataSource        dd = null;
	
	//private static ResourceBundle     BUNDLE = ResourceBundle.getBundle("db-ms");
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
	
	
	private static final String             URL = "slave03_url";
	private static final String        USERNAME = "slave03_username";
	private static final String        PASSWORD = "slave03_password";
	
	//druid
	private static final String                            INITIALSIZE="slave03_initialSize";
	private static final String                                MINIDLE="slave03_minIdle";
	private static final String                              maxActive="slave03_maxActive";
	private static final String                                maxWait="slave03_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="slave03_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="slave03_minEvictableIdleTimeMillis";
	private static final String  maxPoolPreparedStatementPerConnectionSize="slave03_maxPoolPreparedStatementPerConnectionSize";
	private static final String                        validationQuery="slave03_validationQuery";
	private static final String                                filters="slave03_filters";
	private static final String                      AutoCommitOnClose="slave03_autoCommitOnClose";
	private static final String              maxOpenPreparedStatements="slave03_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="slave03_poolPreparedStatements";
	private static final String                           TestOnBorrow="slave03_testOnBorrow";
	private static final String                           TestOnReturn="slave03_testOnReturn";
	private static final String                        RemoveAbandoned="slave03_removeAbandoned";
	private static final String                 RemoveAbandonedTimeout="slave03_removeAbandonedTimeout";

	public  static  boolean       EHCACHE=false;
	public  static  String       DIALECT =null;
	public  static  boolean    SQL_FORMAT=false;
	public  static  String       PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	private static boolean     Is_MS_OPEN=false;//是否开启主从配置
	public  static  boolean    db_Slave03_R =false;//默认从库为只读方式
	
	MSlave3ConnectionFactory(){}    


	static {
		BUNDLE=LoadPropertiesUtils.BUNDLE;
		if(null!=BUNDLE){
			try {
				Is_MS_OPEN =StringUtil.StringToBoolean(BUNDLE.getString("Is_MS_OPEN"));
			} catch (Exception e) {
				logger.error("警告:获取Is_MS_OPEN");
			}
			try {
				db_Slave03_R=StringUtil.StringToBoolean(BUNDLE.getString("db_Slave03_R"));
			} catch (Exception e) {
				logger.error("警告:获取db_Slave03_R");
			}
	  if(Is_MS_OPEN&&db_Slave03_R){	
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
				EHCACHE   =StringUtil.StringToBoolean(BUNDLE.getString("slave03_ehcache"));
			} catch (Exception e1) {
				logger.error("警告:获取slave03_ehcache异常："+e1.getMessage());
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
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("slave03_testWhileIdle")));
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
	else{logger.info("未开启从3配置参数");}
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

	    private static MSlave3ConnectionFactory instance;
	    
	    public static MSlave3ConnectionFactory getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (MSlave3ConnectionFactory.class) {
	                if(instance == null) {
	                    instance = new MSlave3ConnectionFactory();
	                }
	                logger.info("\n"+
							"=========================================================================\n"+
							"‖                                 		      从03数据源实例化                                                                   ‖\n"+
							"‖   "+instance+"	‖\n"+
							"=========================================================================\n"
							+"\n");
	            }
	        }
	        return instance;
	    }
	     
	   
} // end
