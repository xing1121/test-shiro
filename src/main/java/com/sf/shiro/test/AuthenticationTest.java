package com.sf.shiro.test;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

	private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
	
	@Before
	public void init(){
		// 添加账户Yuri，密码123456，角色admin（授权）
		simpleAccountRealm.addAccount("Yuri", "123456", "admin", "user");
	}
	
	@Test
	public void testAuthentication(){
		// 1.构建SecurityManager环境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(simpleAccountRealm);
		
		// 2.主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		// 用户名不存在会报org.apache.shiro.authc.UnknownAccountException
		// 密码错误会报org.apache.shiro.authc.IncorrectCredentialsException
		UsernamePasswordToken token = new UsernamePasswordToken("Yuri", "123456");
		subject.login(token);
		
		// 3.判断是否登录（认证）
		System.out.println("isAuthenticated : " + subject.isAuthenticated());
		
		// 4.检查角色，若该主体没有该角色则报异常：org.apache.shiro.authz.UnauthorizedException
		subject.checkRoles("admin", "user");

	}
	
}
