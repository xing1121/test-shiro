package com.sf.shiro.realm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 描述：自定义Realm
 * @author 80002888
 * @date   2018年7月17日
 */
public class MyRealm extends AuthorizingRealm{

	private Map<String, String> userMap;
	
	private Map<String, Set<String>> userRoleMap;
	
	private Map<String, Set<String>> rolePermissionMap;
	
	private String salt;
	
	private boolean saltStatus;
	
	public MyRealm() {
	}

	{
		userMap = new HashMap<>();
//		userMap.put("Yuri", "123456");
//		userMap.put("Yuri", "e10adc3949ba59abbe56e057f20f883e");
		userMap.put("Yuri", "1785067ca6f96590d4e11b1385ab9dc5");
		
		userRoleMap = new HashMap<>();
		Set<String> roleSet = new HashSet<>();
		roleSet.add("admin");
		roleSet.add("user");
		userRoleMap.put("Yuri", roleSet);
		
		rolePermissionMap = new HashMap<>();
		Set<String> permissionSet = new HashSet<>();
		permissionSet.add("book:insert");
		permissionSet.add("book:delete");
		rolePermissionMap.put("admin", permissionSet);
	}
	
	/**
	 * 获取角色、权限的方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取用户名
		String userName = (String) principals.getPrimaryPrincipal();
		// 根据用户名从数据库获取角色
		Set<String> roles = getRolesByUserName(userName);
		if (roles == null || roles.size() == 0) {
			return null;
		}
		// 构建凭证
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setRoles(roles);
		// 根据角色从数据库获取权限
		Set<String> permissions = new HashSet<>();
		for (String role : roles) {
			Set<String> ps = getPermissionsByRole(role);
			if (ps == null || ps.size() == 0) {
				continue;
			}
			permissions.addAll(ps);
		}
		if (permissions.size() != 0) {
			simpleAuthorizationInfo.setStringPermissions(permissions);
		}
		return simpleAuthorizationInfo;
	}
	
	/**
	 * 认证的方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 从主体传过来的认证信息中获取用户名
		String userName = (String) token.getPrincipal();
		// 根据用户名从数据库获取密码
		String password = getPasswordByUserName(userName);
		if (password == null) {
			return null;
		}
		// 构造凭证
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, password, "MR");
		// 判断是否加盐
		if (saltStatus) {
			simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(salt));
		}
		return simpleAuthenticationInfo;
	}

	/**
	 * 模拟从数据库根据用户名获取密码
	 *	@ReturnType	String 
	 *	@Date	2018年7月17日	下午2:18:18
	 *  @Param  @return
	 */
	private String getPasswordByUserName(String userName) {
		return userMap.get(userName);
	}
	
	/**
	 * 模拟从数据库根据用户名获取所拥有的角色
	 *	@ReturnType	Set<String> 
	 *	@Date	2018年7月17日	下午2:27:55
	 *  @Param  @param userName
	 *  @Param  @return
	 */
	private Set<String> getRolesByUserName(String userName) {
		return userRoleMap.get(userName);
	}
	
	/**
	 * 模拟从数据库根据角色获取所拥有的权限
	 *	@ReturnType	Set<String> 
	 *	@Date	2018年7月17日	下午2:27:55
	 *  @Param  @param userName
	 *  @Param  @return
	 */
	private Set<String> getPermissionsByRole(String role) {
		return rolePermissionMap.get(role);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isSaltStatus() {
		return saltStatus;
	}

	public void setSaltStatus(boolean saltStatus) {
		this.saltStatus = saltStatus;
	}
	
}
