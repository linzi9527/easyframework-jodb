package com.summaryday.framework.dbms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class ConnUtils2 {

	private static final Logger log = LoggerFactory.getLogger(ConnUtils2.class);
	

	
	/**
	 * 从1连接
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Connection slaveConnection(){
		Connection connection =null;
		try {
			if(LoadPropertiesUtils2.db02_db_Slave_R)
			connection =MSlaveConnectionFactory_2db2.getInstance().getConnection();
		} catch (Exception e) {
			log.error("MSlaveConnectionFactory_2db2继续流转至2中..."+e.getMessage());
		}
		return connection;
	}
	
	@SuppressWarnings("static-access")
	public static Connection slave2Connection(){
		Connection connection =null;
		try {
			if(LoadPropertiesUtils2.db02_db_Slave02_R)
				connection =MSlave2ConnectionFactory_2db2.getInstance().getConnection();
		} catch (Exception e) {
			log.error("MSlave2ConnectionFactory_2db2继续流转至3中..."+e.getMessage());
		}
		return connection;
	}
	
	@SuppressWarnings("static-access")
	public static Connection slave3Connection(){
		Connection connection =null;
		try {
			if(LoadPropertiesUtils2.db02_db_Slave03_R)
			connection =MSlave3ConnectionFactory_2db2.getInstance().getConnection();
		} catch (Exception e) {
			log.error("MSlave3ConnectionFactory_2db2继续流转至4中..."+e.getMessage());
		}
		return connection;
	}
	
	
	@SuppressWarnings("static-access")
	public static Connection slave4Connection(){
		Connection connection =null;
		try {
			if(LoadPropertiesUtils2.db02_db_Slave04_R)
			connection =MSlave4ConnectionFactory_2db2.getInstance().getConnection();
		} catch (Exception e) {
			log.error("MSlave4ConnectionFactory_2db2继续流转至5中..."+e.getMessage());
		}
		return connection;
	}
	
	@SuppressWarnings("static-access")
	public static Connection slave5Connection(){
		Connection connection =null;
		try {
			if(LoadPropertiesUtils2.db02_db_Slave05_R)
			connection =MSlave5ConnectionFactory_2db2.getInstance().getConnection();
		} catch (Exception e) {
			log.error("MSlave5ConnectionFactory_2db2继续流转至1中..."+e.getMessage());
		}
		return connection;
	}
	
	
/*	public static Connection initConnection(){
		//主从开启后，系统随机在多个从库之间连接查询
		long s=System.currentTimeMillis();
		int i=0;
		int ms=1;
		Connection connection =null;
		java.util.Random random=new java.util.Random();// 定义随机类
		
		try {
			 ms=LoadPropertiesUtils2.ms;
			 i=random.nextInt(ms)+1;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error("获取MS_TYPE异常:"+e1.getMessage());
		}
		
		if(1==i&&ms>=i){
			connection=slaveConnection();
			if(null==connection){
				connection=slave2Connection();
				if(null==connection){
					connection=slave3Connection();
					if(null==connection){
						connection=slave4Connection();
						if(null==connection){
							connection=slave5Connection();
						}
					}
				}
			}
		}
		else
		if(2==i&&ms>=i){
			connection=slave2Connection();
			if(null==connection){
				connection=slave3Connection();
				if(null==connection){
					connection=slave4Connection();
					if(null==connection){
						connection=slave5Connection();
						if(null==connection){
							connection=slaveConnection();
						}
					}
				}
			}
		}
		else
		if(3==i&&ms>=i){
			connection=slave3Connection();
			if(null==connection){
				connection=slave4Connection();
				if(null==connection){
					connection=slave5Connection();
					if(null==connection){
						connection=slaveConnection();
						if(null==connection){
							connection=slave2Connection();
						}
					}
				}
			}
		}
		else
		if(4==i&&ms>=i){
			connection=slave4Connection();
			if(null==connection){
				connection=slave5Connection();
				if(null==connection){
					connection=slaveConnection();
					if(null==connection){
						connection=slave2Connection();
						if(null==connection){
							connection=slave3Connection();
						}
					}
				}
			}
		}
		else  
		if(5==i&&ms>=i){
			connection=slave5Connection();
			if(null==connection){
				connection=slaveConnection();
				if(null==connection){
					connection=slave2Connection();
					if(null==connection){
						connection=slave3Connection();
						if(null==connection){
							connection=slave4Connection();
						}
					}
				}
			}
		}
		
		if(null==connection){
			log.info("======内部命中-继续流转========");
			connection=slaveConnection();
			if(null==connection){
				connection=slave2Connection();
				if(null==connection){
					connection=slave3Connection();
					if(null==connection){
						connection=slave4Connection();
						if(null==connection){
							connection=slave5Connection();
						}
					}
				}
			}
		}
		long e=System.currentTimeMillis();
		log.info("耗时:"+(e-s));
		return connection;
	}*/
	
	public static Connection initConnection() {
		long s = System.currentTimeMillis();
		Connection connection = null;

		// 主从开启后，系统随机在多个从库之间连接查询
		int i = 0;
		int ms = 1;
		java.util.Random random = new java.util.Random();// 定义随机类
		try {
			ms = LoadPropertiesUtils.ms;
		} catch (Exception e1) {
			log.error("获取MS_TYPE异常:" + e1.getMessage());
		}
		// ///////////////////////////////////////////////////////////

		while (true) {
			i = random.nextInt(ms) + 1;
			connection=CreateConnection(i);
			if(null!=connection){break;}
		}

		// ///////////////////////////////////////////////////////////
		long e = System.currentTimeMillis();
		log.info("耗时:" + (e - s));
		return connection;
	}

	public static Connection CreateConnection(int i) {
		Connection connection = null;
		switch (i) {
		case 1:
			//connection = slaveConnection();
			connection = MSlaveConnectionFactory.getInstance().getConnection();
			break;
		case 2:
			//connection = slave2Connection();
			connection = MSlave2ConnectionFactory.getInstance().getConnection();
			break;
		case 3:
			//connection = slave3Connection();
			connection = MSlave3ConnectionFactory.getInstance().getConnection();
			break;
		case 4:
			//connection = slave4Connection();
			connection = MSlave4ConnectionFactory.getInstance().getConnection();
			break;
		case 5:
			//connection = slave5Connection();
			connection = MSlave5ConnectionFactory.getInstance().getConnection();
			break;
		default:
			//connection = slaveConnection();
			connection = MSlaveConnectionFactory.getInstance().getConnection();
			break;
		}
		return connection;
	}
}
