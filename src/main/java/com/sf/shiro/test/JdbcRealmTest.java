package com.sf.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcRealmTest {

	private DruidDataSource dataSource = new DruidDataSource();
	
	@Before
	public void init(){
		// 构建数据源
		dataSource.setUrl("jdbc:mysql://localhost:3306/test-shiro?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	}
	
	@Test
	public void test(){
		// 1.构建SecurityManager环境
		DefaultSecurityManager securityManager = new DefaultSecurityManager();

		
		// 2.创建JdbcRealm
		JdbcRealm jdbcRealm = new JdbcRealm();
		// 设置数据源
		jdbcRealm.setDataSource(dataSource);
		// 设置可以查询权限的开关，这里不设置就无法去验证permissions
		jdbcRealm.setPermissionsLookupEnabled(true);
		
		// JdbcRealm类中有默认的sql语句，可以通过下面的设置进行修改
//		jdbcRealm.setAuthenticationQuery("select password from users where username = ?");
//		jdbcRealm.setUserRolesQuery("select role_name from user_roles where username = ?");
//		jdbcRealm.setPermissionsQuery("select permission from roles_permissions where role_name = ?");
		
		securityManager.setRealm(jdbcRealm);;
		
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
