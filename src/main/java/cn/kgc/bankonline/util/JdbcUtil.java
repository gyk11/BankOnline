package cn.kgc.bankonline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://127.0.0.1:3306/bankonline";
	public static String username = "root";
	public static String password = "ok";
	static{
		try {
			//加载数据库驱动
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//获取连接
	public static Connection getConn(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	//释放资源
	public static void closeAll(ResultSet rs,PreparedStatement pstm,Connection conn){
		try {
			if(rs!=null){
				rs.close();
			}
			if(pstm!=null){
				pstm.close();
			}
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
