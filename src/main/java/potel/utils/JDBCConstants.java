package potel.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class JDBCConstants {
	private static DataSource ds;
	public static final String URL = "jdbc:mysql://114.32.203.170:3306/potel";
//	public static final String URL = "jdbc:mysql://localhost:3306/example";
	public static final String USER = "root";
	public static final String PASSWORD = "TIP102_25541859101";
//	public static final String PASSWORD = "abcd1234";
	
	public static synchronized DataSource getDataSource2() {
		if(ds == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			HikariDataSource ds1 = new HikariDataSource();
			ds1.setJdbcUrl(URL);
			ds1.setUsername(USER);
			ds1.setPassword(PASSWORD);
			// 最小空閒連線數
			ds1.setMinimumIdle(5);
			// 最大連線數
			ds1.setMaximumPoolSize(10);
			// 啟⽤預編譯敘述快取
			ds1.addDataSourceProperty("cachePrepStmts", true);
			// 設定最多可保存的預編譯敘述數量
			ds1.addDataSourceProperty("prepStmtCacheSize", 250);
			// 設定預編譯敘述⻑度上限
			ds1.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
			
			ds = (DataSource)ds1;
		}
		return ds;
	}
	
	public static synchronized DataSource getDataSource() throws NamingException {
		if(ds == null) {
			ds = InitialContext.doLookup("java:comp/env/jdbc/potel");
		}
		return ds;
	}
}
