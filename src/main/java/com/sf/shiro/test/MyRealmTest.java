package com.sf.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.sf.shiro.realm.MyRealm;

public class MyRealmTest {

	@Test
	public void testAuthentication(){
		// 1.构建SecurityManager环境，使用自定义的Realm
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		MyRealm myRealm = new MyRealm();
		securityManager.setRealm(myRealm);
		
		// 使用MD5加密，加密次数1，盐"HAHA"
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("MD5");
		matcher.setHashIterations(1);
		myRealm.setCredentialsMatcher(matcher);
		myRealm.setSaltStatus(true);
		myRealm.setSalt("HAHA");
		
		// 2.主体提交认证请求
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("Yuri", "123456");
		subject.login(token);
		
		// 3.判断是否登录（认证）
		System.out.println("isAuthenticated : " + subject.isAuthenticated());
		
		// 4.检查角色，若该主体没有该角色则报异常：org.apache.shiro.authz.UnauthorizedException
		subject.checkRoles("admin", "user");
		
		// 5.检查权限
		subject.checkPermissions("book:delete");
	}
	
	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456");
		System.out.println(md5Hash.toString());//e10adc3949ba59abbe56e057f20f883e
		md5Hash = new Md5Hash("123456", "HAHA");
		System.out.println(md5Hash.toString());//1785067ca6f96590d4e11b1385ab9dc5

	}
	
}
