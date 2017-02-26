package app.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import app.type.action.TypingAction;
import app.type.bean.NoRegisterUser;
import app.type.bean.UserInfo;
import app.type.dbc.DatabaseConnection;
import app.type.factory.DaoFactory;
import app.utils.StringBulid;


public class UserService {
	private DatabaseConnection dbc;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Logger log=Logger.getLogger(UserService.class);
	//���ű�userinfo ,noregisiteruser ��ѯ���ҵ��û���Ϣ
	public UserInfo getUserInfoFrom2T(String username){
		UserInfo info=null;
		try {
			String sql="SELECT no_regisiteruser.userid,no_regisiteruser.username  from no_regisiteruser WHERE no_regisiteruser.username=? " 
						+"UNION SELECT userinfo.userid,userinfo.username from userinfo WHERE userinfo.username=?;";
			dbc= new DatabaseConnection();
			pstmt=dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1,username);
			pstmt.setString(2,username);
			rs=pstmt.executeQuery();
			String username2=null,userid=null;
			if(rs.next()){
				info=new UserInfo();
				username2=rs.getString("username");
				userid=rs.getString("userid");
				info.setUserid(userid);
				info.setUsername(username2);
			}
		} catch (Exception e) {
			log.error("�������������ϲ�ѯ�û���ʧ��",e);
		}finally{
			if(pstmt!=null){try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			if(this.dbc!=null){try {
				this.dbc.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
		return info;
	}
	//�õ�δע���ߵ�����
	public  int getAllNoReUserNumbers() {
		int all=0;
		try {
			String sql="SELECT COUNT(*) as 'count' from no_regisiteruser;";
			dbc= new DatabaseConnection();
			pstmt=dbc.getConnection().prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			all=rs.getInt("count");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstmt!=null){try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			if(this.dbc!=null){try {
				this.dbc.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
		return all;
	}
	public NoRegisterUser buildNoReUser() throws Exception{
		String useridS=null;
		boolean flag=false;
		NoRegisterUser tempuser=null;
		useridS=StringBulid.buildUserid();
		int tempUserNum=this.getAllNoReUserNumbers();
		NoRegisterUser user=new NoRegisterUser();
		user.setStatus(1);
		user.setUserid("T"+useridS);
		user.setUsername("�ο�"+(1000+tempUserNum));
		flag=DaoFactory.getNoRegisterUserDaoInstance().addUser(user);
		//System.err.println(flag);
		if(flag==true){
			tempuser=user;
		}
		return tempuser;
	}
}
