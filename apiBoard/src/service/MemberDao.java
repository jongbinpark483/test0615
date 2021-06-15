package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconn.DBconn;

public class MemberDao {
	
	private PreparedStatement pstmt;
	private Connection conn;
	
	public MemberDao(){
		DBconn dbconn = new DBconn();
		this.conn = dbconn.getConnection();
	}	
	
	public int memberInsert(String memberName,String memberId,String memberPwd,String memberJumin,String memberAddr,String memberEmail,String memberPhone,String memberGender,String ip) {
		int value = 0;
		
		try {
		String sql = "insert into Api_member(MIDX,MEMBERNAME,MEMBERID,MEMBERPWD,MEMBERJUMIN,MEMBERADDR,MEMBEREMAIL,MEMBERPHONE,MEMBERGENDER,WRITEDAY,DELYN,IP) values(midx_seq.nextval,?,?,?,?,?,?,?,?,sysdate,?,?)";
	    pstmt = conn.prepareStatement(sql);
	  //  pstmt.setInt(1, 11);
		pstmt.setString(1,memberName);
		pstmt.setString(2,memberId);
		pstmt.setString(3,memberPwd);
		pstmt.setString(4,memberJumin);
		pstmt.setString(5,memberAddr);
		pstmt.setString(6,memberEmail);
		pstmt.setString(7,memberPhone);
		pstmt.setString(8,memberGender);
		//pstmt.setString(9, "21/04/20");
		pstmt.setString(9, "N");
		pstmt.setString(10, ip);
		pstmt.execute();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
						
		}				
		
		return value;
	}
	
	public ArrayList<MemberVo> memberSelectAll(){
		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
		
		String sql="select * from api_member where delyn='N' order by midx desc";
		
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs  = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberVo mv = new MemberVo();
				mv.setMidx(rs.getInt("midx"));
				mv.setMemberName(rs.getString("memberName"));
				mv.setMemberId(rs.getString("memberId"));
				mv.setWriteday(rs.getString("writeday"));
				alist.add(mv);				
			}			
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
		
		return alist;
	}
	
	
	public int memberLoginCheck(String memberId, String memberPwd){
		int midx=0;
		
		String sql ="select midx"
				+ " from api_member where memberId=? and memberPwd=?";
		
		try {
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				midx = rs.getInt("midx");
			}			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		
		return midx;
	}
	
	
	
	
	
	
	
		
}
