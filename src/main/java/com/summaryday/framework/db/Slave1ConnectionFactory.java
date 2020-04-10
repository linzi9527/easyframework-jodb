package com.summaryday.framework.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Slave1ConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(Slave1ConnectionFactory.class);

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
	
	private static final String        POOLTYPE ="s1_PoolType";
	private static final String          DRIVER = "s1_driver";
	private static final String             URL = "s1_url";
	private static final String        USERNAME = "s1_username";
	private static final String        PASSWORD = "s1_password";
	

	//druid
	private static final String                            INITIALSIZE="s1_initialSize";
	private static final String                                MINIDLE="s1_minIdle";
	private static final String                              maxActive="s1_maxActive";
	private static final String                                maxWait="s1_maxWait";
	private static final String          timeBetweenEvictionRunsMillis="s1_timeBetweenEvictionRunsMillis";
	private static final String             minEvictableIdleTimeMillis="s1_minEvictableIdleTimeMillis";
	private static final String  maxPoolPreparedStatementPerConnectionSize="s1_maxPoolPreparedStatementPerConnectionSize";
	private static final String                        validationQuery="s1_validationQuery";
	private static final String                                filters="s1_filters";
	private static final String                      AutoCommitOnClose="s1_autoCommitOnClose";
	private static final String              MAXOPENPREPAREDSTATEMENTS="s1_maxOpenPreparedStatements";
	private static final String                 PoolPreparedStatements="s1_poolPreparedStatements";
	private static final String                           TestOnBorrow="s1_testOnBorrow";
	private static final String                           TestOnReturn="s1_testOnReturn";
	private static final String                        RemoveAbandoned="removeAbandoned";
	private static final String                 RemoveAbandonedTimeout="removeAbandonedTimeout";

	
	public  static  boolean     EHCACHE=false;
	public  static  String     DIALECT =null;
	public  static  boolean SQL_FORMAT =false;
	public  static  String     PoolType=null;
	public  static  boolean  Develop_Mode=true;//管理开发过程的打印信息，部署时不需求打印信息的控制
	
	private Slave1ConnectionFactory(){}    


	static {
		if(null!=BUNDLE){
			try {
				PoolType = BUNDLE.getString(POOLTYPE);
			} catch (Exception e) {
				logger.error("警告:获取POOLTYPE异常："+e.getMessage());
			}
		 if(null!=PoolType){	
			try {
				EncryptUtils.isGOTO();
			} catch (Exception e) {
				System.out.println("注册失效，服务会受影响！");
			}
			try {
				DIALECT=BUNDLE.getString("s1_dialect");
			} catch (Exception e) {
				logger.error("获取s1_dialect异常："+e.getMessage());
			}
			try {
				Develop_Mode=StringUtil.StringToBoolean(BUNDLE.getString("s1_develop_mode"));
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
				dd.setTestWhileIdle(StringUtil.StringToBoolean(BUNDLE.getString("s1_testWhileIdle")));
				dd.setTestOnReturn(StringUtil.StringToBoolean(BUNDLE.getString(TestOnReturn)));
				dd.setTestOnBorrow(StringUtil.StringToBoolean(BUNDLE.getString(TestOnBorrow)));
				try {
					dd.setRemoveAbandoned(StringUtil.StringToBoolean(BUNDLE.getString(RemoveAbandoned)));
					dd.setRemoveAbandonedTimeout(StringUtil.StringToInteger(BUNDLE.getString(RemoveAbandonedTimeout)));
				} catch (Exception e) {
				}
	
				Slave1ConnectionFactory.DIALECT    = BUNDLE.getString("s1_dialect");
				Slave1ConnectionFactory.SQL_FORMAT = StringUtil.StringToBoolean(BUNDLE.getString("s1_sql_format"));
				Slave1ConnectionFactory.EHCACHE    = StringUtil.StringToBoolean(BUNDLE.getString("s1_ehcache"));
				logger.info("\n"+
						"=====================================\n"+
						"‖                         druid初始化从库（01）                  ‖\n"+
						"=====================================\n"
						+"\n");
		} catch (Exception e) {
			logger.error("\n druid 数据源连接池初始化异常："+e.getMessage());
		}
      }
	}//if PoolType
	else{logger.info("没有开启Slave第2个实例配置");}	
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

	    private static Slave1ConnectionFactory instance;
	    
	    public static Slave1ConnectionFactory getInstance() {
	        if (instance == null&&EncryptUtils.LOCK) {
	            synchronized (Slave1ConnectionFactory.class) {
	                if(instance == null) {
	                    instance = new Slave1ConnectionFactory();
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
