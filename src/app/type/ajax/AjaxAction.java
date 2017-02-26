package app.type.ajax;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import app.model.UserService;
import app.type.action.TypingAction;
import app.type.bean.Article;
import app.type.bean.NoRegisterUser;
import app.type.bean.UserInfo;
import app.type.factory.DaoFactory;
import app.utils.CookieUtil;
import app.utils.StringCheck;

import com.opensymphony.xwork2.ActionSupport;

public class AjaxAction extends ActionSupport {
	private String username;
	private String password;
	//��������
	private String option_type;
	private String msg;
	private JSONArray articleJsonArray;
	private Logger log=Logger.getLogger(AjaxAction.class);
	private String usertype;
	
	public String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}
	public JSONArray getArticleJsonArray() {
		return articleJsonArray;
	}
	public void setArticleJsonArray(JSONArray articleJsonArray) {
		this.articleJsonArray = articleJsonArray;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/*
	 * �û���¼��
	 * username,passwprd
	 * usertype:tempuser,user;
	 * */
	public String login(){
		this.msg="false";
		if(StringCheck.isNull(username,usertype)==false){return this.ERROR;}
		UserInfo info=null;
		NoRegisterUser userInfo=null;
		try {
			HttpSession session=ServletActionContext.getRequest().getSession();
			if(usertype.equals("user")&&password!=null){//�������ʽ�û�
				info=DaoFactory.getUserInfoDaoInstance().getUserByName(username);
				if(info.getPassword().equals(password)){//������ȷ
					msg="true";
					session.setAttribute("username",info.getUsername());
					session.setAttribute("userid",info.getUserid());
					log.info("�û���¼�ɹ���"+username);
				}
			}else if(usertype.equals("tempuser")){//����Ƿÿ�
				userInfo=DaoFactory.getNoRegisterUserDaoInstance().getUser(username);
				if(userInfo!=null){//������ȷ
					msg="true";
					session.setAttribute("username",userInfo.getUsername());
					session.setAttribute("userid",userInfo.getUserid());
					log.info("�ο͵�¼�ɹ���"+username);
				}
				
			}else{
				System.out.println("��¼ʧ��");
				msg="wrong"; 
			}
//			info=DaoFactory.getUserInfoDaoInstance().getUserByName(username);
//			HttpSession session=ServletActionContext.getRequest().getSession();
//			session.setAttribute("username",info.getUsername());
//			session.setAttribute("userid",info.getUserid());
//			session.setAttribute("email",info.getEmail());
			
			
//			 HttpServletResponse response = ServletActionContext.getResponse();
//			 Cookie cookie=new Cookie("username", URLEncoder.encode(this.username, "utf-8"));
//			// System.out.println("test:"+cookie.getValue());
//			 cookie.setMaxAge(3600*24*30);
//			 response.addCookie(cookie);
		} catch (Exception e) {
			log.error("�û���¼ʧ��",e);
		}
		return "login_res";
	}
	
	public String logOut(){
		msg="ע��ʧ��";
		try {
			HttpSession session =ServletActionContext.getRequest().getSession();
			if(session==null||session.getAttribute("username")==null||session.getAttribute("userid")==null){
				//ʧ��
			}else{
				msg="ע���ɹ�";
				log.info("�û�ע���ɹ���"+session.getAttribute("username"));
				session.removeAttribute("username");
				session.removeAttribute("userid");
			}
		} catch (Exception e) {
			log.error("�û�ע��ʧ��",e);
		}
		return "logout";
	}
	
	/*
	 * ��֤�û������鿴�����û������Ƿ��и��û�����Ϣ
	 * param username
	 * return {"msg":msg} msg:false,tempuser,user;
	 * */
	public String verifyUserName(){
		this.msg="false";
		if(username==null){return ERROR;}
		try {
			UserService service=new UserService();
			UserInfo userInfo=service.getUserInfoFrom2T(username);
			if(userInfo!=null&&userInfo.getUsername().equals(username)){//�û�����ȷ
				if(userInfo.getUserid().charAt(0)!='T'){//�����ע����û�
					msg="user";
				}else{
					msg="tempuser";
				}
			}else{
				
			}
			//System.out.println(msg);
		} catch (Exception e) {
			log.error("�û�����֤ʧ��",e);
		}
		return "verifyRes";
	}
	public String getTempUser(){
		msg="";
		try {
			HttpSession session=ServletActionContext.getRequest().getSession();
			if(!session.getAttribute("token").equals(token)){
				msg="wrong";
				return "error";
			}
			//��ȡcookie,����ο��û������򷵻ظ��û�������û��������򴴽�һ���ο���ݣ��ٷ���
			HttpServletRequest request = ServletActionContext.getRequest();
			String usernameString=CookieUtil.getCookieValue(request, "username");
			if(usernameString!=null&&!usernameString.equals("")){
				this.username=usernameString;
				this.msg=usernameString;
				log.info("��ȡ�û�cookie�ɹ���"+usernameString);
			}//����û��� �����û����� Ϊ��
			else if(usernameString==null||usernameString.equals("")) {//����û��������ڣ���Ϊ֮����һ���û���
				NoRegisterUser user=null;
				user=new UserService().buildNoReUser();
				if(user!=null){
					log.info("�����ο��û��ɹ���"+user.getUsername());
					this.username=user.getUsername();
					this.msg=user.getUsername();
				}
				else{
					log.info("�����ο��û�ʧ��");
				}
			}
			if(username!=null){
				//���cookie
				 HttpServletResponse response = ServletActionContext.getResponse();
				 Cookie cookie=new Cookie("username", URLEncoder.encode(this.username, "utf-8"));
				 cookie.setMaxAge(3600*24*30);
				 response.addCookie(cookie);
			}
		} catch (Exception e) {
			log.error("�����ο���ݻ������cookieʧ��");
		}
		return "noreUser";
	}
	public String loadAllArticle(){
		JSONObject articleJsonObj=null;
		List<Article> articleList=null;
		if(option_type==null){msg="request reject";return this.ERROR;}
		try {
			articleJsonArray=new JSONArray();
			articleList=DaoFactory.getArticleInfoDaoInstance().getArticleByType(option_type);
			for(Article article:articleList){
				articleJsonObj=new JSONObject();
				String ar=article.getContent().replaceAll("\n", "");
				ar=ar.trim();
				article.setContent(ar);
				//System.out.println(article.getContent());
				articleJsonObj.put("title", article.getTitle()+"("+article.getContent().length()+"��)");
				articleJsonObj.put("ID", article.getID());
				articleJsonArray.add(articleJsonObj);
			}
		} catch (Exception e) {
			System.out.println("��ȡ���������б�ʧ��");
			e.printStackTrace();
		}
		return "allArticle";
	}
}
