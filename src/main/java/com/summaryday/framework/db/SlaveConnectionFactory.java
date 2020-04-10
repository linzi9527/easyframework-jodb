package com.summaryday.framework.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaveConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(SlaveConnectionFactory.class);

	private static DruidDataSource dd=null;
	//private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("db");
	private static  ResourceBundle BUNDLE =null;
	static {
		try {
			BUNDLE = ResourceBundle.getBundle("db");
			} catch (Exception e) {
				logger.error("配置文件db加载异常:"+e.getMessage());
			}
	}
	
	private static final String        POOLTYPE ="s_PoolType";
	private static final String          DRIVER = "s_driver";
	private static final String             URL = "s_url";
	private static final String        USERNAME = "s_username";
	private static final String        PASSWORD = "s_password";
	

	//druid
	private static final String                            INITIALSIZE="s_initialSize";
	private static final String                                MINIDLE="s_minIdle";
	private static final String                              maxActive="s_maxActive";
	private static final String                                maxWait="s_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="s_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="s_minEvictableIdleTimeMillis";
	private static final String  maxPoolPreparedStatementPerConnectionSize="s_maxPoolPreparedStatementPerConnectionSize";
	private static final String                        validationQuery="s_validationQuery";
	private static final String                                filters="s_filters";
	private static final String                      AutoCommitOnClose="s_autoCommitOnClose";
	private static final String              MAXOPENPREPAREDSTATEMENTS="s_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="s_poolPreparedStatements";
	private static final String                           TestOnBorrow="s_testOnBorrow";
	private static final String                           TestOnReturn="s_testOnReturn";
	private static final String                        RemoveAbandoned="s_removeAbandoned";
	private static final String                 RemoveAbandonedTimeout="s_removeAbandonedTimeout";

	
	public  static  boolean     EHCACHE=false;
	public  static  String     DIALECT =null;
	public  static  boolean SQL_FORMAT =false;
	public  static  String     PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	private SlaveConnectionFactory(){}    


	static {
		if(null!=BUNDLE){
			try {
				PoolType = BUNDLE.getString(POOLTYPE);
			} catch (Exception e) {
				logger.error("警告:获取PoolType异常："+e.getMessage());
			}
		 if(null!=PoolType){	
			try {
				EncryptUtils.isGOTO();
			} catch (Exception e) {
				System.out.println("注册失效，服务会受影响！");
			}
			try {
				DIALECT=BUNDLE.getString("s_dialect");
			} catch (Exception e) {
				logger.error("获取s_dialect异常："+e.getMessage());
			}
			try {
				Develop_Mode=StringUtil.StringToBoolean(BUNDLE.getString("s_develop_mode"));
			} catch (Exception e1) {
				logger.error("警告:获取develop_mode异常："+e1.getMessage());
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
						dd.setMaxOpenPreparedStatements(StringUtil.StringToInteger(BUNDLE.getString(MAXOPENPREPAREDSTATEMENTS)));
					}
				
				dd.setFilters(BUNDLE.getString(filters));
				dd.setDefaultAutoCommit(StringUtil.StringToBoolean(BUNDLE.getString(AutoCommitOnClose)));
				dd.setValidationQuery(BUNDLE.getString(validationQuery));
				dd.setMinEvictableIdleTimeMillis(StringUtil.StringToInteger(BUNDLE.getString(minEvictableIdleTimeMillis)));
				dd.setTimeBetweenEvictionRunsMillis(StringUtil.StringToInteger(BUNDLE.getString(timeBetweenEvictionRunsMillis)));
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("s_testWhileIdle")));
				dd.setTestOnReturn(StringUtil.StringToBoolean(BUNDLE.getString(TestOnReturn)));
				dd.setTestOnBorrow(StringUtil.StringToBoolean(BUNDLE.getString(TestOnBorrow)));
				try {
					dd.setRemoveAbandoned(StringUtil.StringToBoolean(BUNDLE.getString(RemoveAbandoned)));
					dd.setRemoveAbandonedTimeout(StringUtil.StringToInteger(BUNDLE.getString(RemoveAbandonedTimeout)));
				} catch (Exception e) {
				}
	
				SlaveConnectionFactory.DIALECT    = BUNDLE.getString("s_dialect");
				SlaveConnectionFactory.SQL_FORMAT = StringUtil.StringToBoolean(BUNDLE.getString("s_sql_format"));
				SlaveConnectionFactory.EHCACHE    = StringUtil.StringToBoolean(BUNDLE.getString("s_ehcache"));
				logger.info("\n"+
						"=====================================\n"+
						"‖                         druid初始化从库（00）                 ‖\n"+
						"=====================================\n"
						+"\n");
		} catch (Exception e) {
			logger.error("\n druid 数据源连接池初始化异常："+e.getMessage());
		}
      } 
    }//ifPoolType
    else{logger.info("没有开启Slave第1个实例配置");}	
  }//if	
}//static

	
	public static synchronized Connection getConnection() {
	        Connection con = null;
	        long s=System.currentTimeMillis();
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
	        long e=System.currentTimeMillis();
	        logger.info("耗时:"+(e-s));
	        return con;
	    }

		public static String getDialect() {
			return DIALECT;
		}
	    
	    public static Boolean getSqlFormat() {
			return SQL_FORMAT;
		}

	    private static SlaveConnectionFactory instance;
	    
	    public static SlaveConnectionFactory getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (SlaveConnectionFactory.class) {
	                if(instance == null) {
	                    instance = new SlaveConnectionFactory();
	                }
	                logger.info("\n"+
							"=========================================================================\n"+
							"‖                                                            数据源实例化                                                                   ‖\n"+
							"‖                   "+instance+"	 	‖\n"+
							"=========================================================================\n"
							+"\n");
	            }
	        }
	        return instance;
	    }
	     
	   
} // end
