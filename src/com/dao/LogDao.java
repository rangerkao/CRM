package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.bean.User;
import com.common.BaseDao;

@Repository
public class LogDao extends BaseDao{

	public LogDao() throws Exception{
		// TODO Auto-generated constructor stub
	}

	
	public User queryUser(String account) throws Exception{
		User user = null;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = getConn3();
		try {
			st = conn.createStatement();
			
			String sql = "SELECT A.ACCOUNT,A.PASSWORD,A.ROLE FROM CRM_USER A WHERE A.ACCOUNT = '"+account+"' ";
			
			rs = st.executeQuery(sql);

			while(rs.next()){
				user = new User();
				user.setAccount(rs.getString("ACCOUNT"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setRole(rs.getString("ROLE"));
			}
			
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return user;
	}
	
	public void actionLog(String account,String params,String function,String result) throws SQLException, Exception{
		String sql = "Insert into CRM_ACTION_LOG (ACCOUNT,PARAMS,FUNCTION,RESULT,CREATEDATE) "
				+ "VALUES('"+account+"','"+params+"','"+function+"','"+result+"',now())";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn3();
		try{
			st = conn.createStatement();
			System.out.println("Action Log sql:"+sql);
			st.execute(sql);
		}finally{
			closeConnection(conn, st, ps, rs);
		}
	}
}
