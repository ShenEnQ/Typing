package app.type.action;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import app.model.UserService;
import app.type.bean.Article;
import app.type.bean.NoRegisterUser;
import app.type.factory.DaoFactory;
import app.utils.CookieUtil;
import app.utils.StringBulid;
import app.utils.StringCheck;

import com.opensymphony.xwork2.ActionSupport;
@SuppressWarnings({ "serial", "deprecation" })
public class TypingAction extends ActionSupport  {
	private String username;
	//�û�ѡ�����������
	private String option_type;
	private String articleSelect;
	private String password;
	private List<String>sentenceList;
	private Integer article_length;
	private Logger log=Logger.getLogger(TypingAction.class);
	private Boolean isNeedLogin;
	private String token;
	
	//�û����ύ����������
	private String timeuse;
	private String rightwords;
	private String wrongwords;
	private String backwords;
	private String typespeed;
	private String keyspeed;
	private Float rightPercent;
	
	

	public Float getRightPercent() {
		return rightPercent;
	}

	public void setRightPercent(Float rightPercent) {
		this.rightPercent = rightPercent;
	}

	public String getTimeuse() {
		return timeuse;
	}

	public void setTimeuse(String timeuse) {
		this.timeuse = timeuse;
	}

	public String getRightwords() {
		return rightwords;
	}

	public void setRightwords(String rightwords) {
		this.rightwords = rightwords;
	}

	public String getWrongwords() {
		return wrongwords;
	}

	public void setWrongwords(String wrongwords) {
		this.wrongwords = wrongwords;
	}

	public String getBackwords() {
		return backwords;
	}

	public void setBackwords(String backwords) {
		this.backwords = backwords;
	}

	public String getTypespeed() {
		return typespeed;
	}

	public void setTypespeed(String typespeed) {
		this.typespeed = typespeed;
	}

	public String getKeyspeed() {
		return keyspeed;
	}

	public void setKeyspeed(String keyspeed) {
		this.keyspeed = keyspeed;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getIsNeedLogin() {
		return isNeedLogin;
	}

	public void setIsNeedLogin(Boolean isNeedLogin) {
		this.isNeedLogin = isNeedLogin;
	}

	public Integer getArticle_length() {
		return article_length;
	}

	public void setArticle_length(Integer article_length) {
		this.article_length = article_length;
	}

	public List<String> getSentenceList() {
		return sentenceList;
	}

	public void setSentenceList(List<String> sentenceList) {
		this.sentenceList = sentenceList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}
	public String getArticleSelect() {
		return articleSelect;
	}
	public void setArticleSelect(String articleSelect) {
		this.articleSelect = articleSelect;
	}

	//end of get and set
	//start of jsp interface
	//index.jsp
	public String index(){
		try {
//			isNeedLogin=false;
//			//�ж��û���Ϣ�Ƿ���cookie��
//			HttpServletRequest request = ServletActionContext.getRequest();
//			String usernameString=CookieUtil.getCookieValue(request, "username");
//			String usertype=CookieUtil.getCookieValue(request, "usertype");
//			//System.out.println("usernameString"+usernameString);
//			//����ɹ���ȡ�û��� ���û�����
//			if(usernameString!=null&&usertype!=null&&!usertype.equals("")&&!usernameString.equals("")){
//				this.username=usernameString;
//				if(usertype.equals("B"))//����û��������Ѿ�ע�����
//				{
//					isNeedLogin=true;
//				}
//				log.info("��ȡ�û�cookie�ɹ���"+usernameString);
//			}//����û��� �����û����� Ϊ��
//			else if(usernameString==null||usernameString.equals("")||usertype==null||usertype.equals("")) {//����û��������ڣ���Ϊ֮����һ���û���
//				NoRegisterUser user=null;
//				user=new UserService().buildNoReUser();
//				usertype="A";
//				if(user!=null){
//					log.info("�����ο��û��ɹ���"+user.getUsername());
//					this.username=user.getUsername();
//				}
//				else{
//					log.info("�����ο��û�ʧ��");
//				}
//			}
//			
//			//���cookie
//			 HttpServletResponse response = ServletActionContext.getResponse();
//			 Cookie cookie=new Cookie("username", URLEncoder.encode(this.username, "utf-8"));
//			 cookie.setMaxAge(3600*24*30);
//			 Cookie userTypeCookie=new Cookie("usertype", URLEncoder.encode(usertype, "utf-8"));
//			 userTypeCookie.setMaxAge(3600*24*30);
//			 response.addCookie(cookie);
//			 response.addCookie(userTypeCookie);
			/* ���ϴ��룬����û�ʹ�ø���������˺ŵ�¼�����cookie�е�username�滻Ϊ���˺ţ�
			 * ���´��룬ֻ���ɷÿ͵�cookie��������ʽ�û���������cookie��   
			 *    */
			token=StringBulid.bulidToken();
			HttpSession session=ServletActionContext.getRequest().getSession();
			session.setAttribute("token", token);
		} catch (Exception e) {
			log.error("����indexҳ��ʧ�ܣ�Ԥ��ʼ��ʧ��",e);
		}
		return "index";
	}
	//speedTest.jsp
	public String test(){
		HttpSession session=ServletActionContext.getRequest().getSession();
		if(session==null||session.getAttribute("username")==null||
				StringCheck.isNull(option_type,articleSelect)==false){
			return "reIndex";
		}
		Article article=null;
		try {
			//��������
			token=StringBulid.bulidToken();
			session.setAttribute("token", token);
			//��ȡ����
			article=DaoFactory.getArticleInfoDaoInstance().getArticleByID(Integer.parseInt(articleSelect));
			article_length=0;
			if(article!=null){
				String str=article.getContent();
				str=str.trim();
				str=str.replaceAll("\n", "");
				article_length=str.length();//���µĳ��ȣ���ǰ��ʹ��
				//System.out.println(str);
				article.setContent(str);
				sentenceList=StringCheck.splitArticle(str,option_type);
				//System.out.println(sentenceList.get(sentenceList.size()-1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "speedTest";
	}
	//�û���ɴ��֣��ϴ�����
	public String finishType(){
		HttpSession session=ServletActionContext.getRequest().getSession();
		if(session==null||session.getAttribute("username")==null||
				StringCheck.isNull(timeuse,rightwords,wrongwords,
						backwords,typespeed,keyspeed,token,articleSelect)==false){
			return "reIndex";
		}
//		System.out.println(timeuse);
//		System.out.println(rightwords);
//		System.out.println(wrongwords);
//		System.out.println(backwords);
//		System.out.println(typespeed);
//		System.out.println(keyspeed);
//		System.out.println(token);
		if(!session.getAttribute("token").equals(token)){
			log.info("�û�"+session.getAttribute("username")+"�ύ���Գɼ�ʧ�ܣ�token����ȷ");
			return "reIndex";
		}
		rightPercent=Float.parseFloat(rightwords)*100/(Float.parseFloat(rightwords)+Float.parseFloat(wrongwords));
		
		try {
			String useridString=session.getAttribute("userid").toString();
			
		} catch (Exception e) {
			log.error("�洢���ֳɼ���¼�����ݿ�ʧ�ܡ�");
		}
		return "typeresult";
	}
	
}
