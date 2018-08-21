package cn.kgc.bankonline.dao;

import cn.kgc.bankonline.util.JdbcUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseDAO {
	
	public Connection conn;
	public PreparedStatement pstm;
	public ResultSet rs;
	
	/**
	 * 通用查询(单表)
	 * @param clazz
	 * @param sql
	 * @param obj
	 * @return
	 */
	public <T> List<T> query(Class clazz,String sql,Object... obj){
		List<T> list = new ArrayList<>();
		conn = JdbcUtil.getConn();
		try {
			pstm = conn.prepareStatement(sql);
			for (int i = 1; i <= obj.length; i++) {
				pstm.setObject(i, obj[i-1]);
			}
			rs = pstm.executeQuery();
			//获取列信息
			ResultSetMetaData rsmd = rs.getMetaData();
			//获取列数量
			int count = rsmd.getColumnCount();
//			System.err.println(count);
			//利用反射，设置数据
			while(rs.next()){
				Object o = clazz.newInstance();
				for (int i = 0; i < count; i++) {
					String column = rsmd.getColumnLabel(i+1);
					String setPro = "set"+column.substring(0,1).toUpperCase()+column.substring(1);
					//获取属性信息
					Field field = clazz.getDeclaredField(column);
					Method method = clazz.getMethod(setPro, field.getType());
					method.invoke(o, rs.getObject(column));
				}
				list.add((T)o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.closeAll(rs, pstm, conn);
		}
		return list;
	}
	
	/**
	 * 查询单条记录
	 * @param clazz
	 * @param sql
	 * @param obj
	 * @return
	 */
	public <T> T queryOne(Class clazz,String sql,Object... obj) {
		List<T> list = this.query(clazz, sql, obj);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 通用增删改（事务）
	 * Object...  参数数量可变
	 * @param sql
	 * @param obj
	 * @return
	 */
	public int update(Connection conn,String sql,Object... obj){
		int result = 0;
		try {
			pstm = conn.prepareStatement(sql);
			for (int i = 1; i <= obj.length; i++) {
				pstm.setObject(i, obj[i-1]);
			}
			result = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.closeAll(null, pstm, null);
		}
		return result;
	}
	
	/**
	 * 通用增删改
	 * @param sql
	 * @param obj
	 * @return
	 */
	public int update(String sql,Object... obj){
		if(conn==null) {
			conn = JdbcUtil.getConn();
		}
		int result = this.update(conn, sql, obj);
		JdbcUtil.closeAll(null, null, conn);
		conn = null;
		return result;
	}
	
	/**
	 * 通用查询（多表）
	 * @param sql
	 * @param params
	 * @return List<Map>集合
	 */
	public List<Map<String, Object>> queryMap(String sql,Object... params){
		//创建返回
		List<Map<String, Object>> list = new ArrayList<>();
		//获取连接
		conn = JdbcUtil.getConn();
		try {
			//获取PreparedStatement对象
			pstm = conn.prepareStatement(sql);
			//设置参数
			for (int i = 1; i <= params.length; i++) {
				pstm.setObject(i, params[i-1]);
			}
			//执行sql 获得结果
			rs = pstm.executeQuery();
			//获取列信息
			ResultSetMetaData rsmd =  rs.getMetaData();
			//获取列数量
			int count = rsmd.getColumnCount();
			//循环便利resultSet
			while (rs.next()) {
				//将里面的数据存放到map集合里面
				Map<String, Object> map = new HashMap<>();
				for (int i = 0; i < count; i++) {
					String column = rsmd.getColumnLabel(i+1);
					map.put(column, rs.getObject(column));
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.closeAll(rs, pstm, conn);
		}
		return list;
	}
	
	
}
