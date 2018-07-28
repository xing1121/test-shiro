package com.sf.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest {
	
	@Test
	public void testAuthentication(){
		// 1.构建SecurityManager环境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		
		// 2.创建IniRealm读取配置文件
		IniRealm iniRealm = new IniRealm("classpath:realm.ini");
		securityManager.setRealm(iniRealm);;
		
		// 3.主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("Yuri", "123456");
		subject.login(token);
		
		// 4.判断是否登录（认证）
		System.out.println("isAuthenticated : " + subject.isAuthenticated());
		
		// 5.判断是否拥有角色
		subject.checkRole("admin");

		// 6.判断是否有权限
		subject.checkPermission("book:update");
		
	}
}
