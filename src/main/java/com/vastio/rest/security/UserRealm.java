package com.vastio.rest.security;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.vastio.rest.entity.UserLogin;
import com.vastio.rest.service.UserLoginService;

public class UserRealm extends AuthorizingRealm {

	@Resource
	private UserLoginService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		// TODO Auto-generated method stub
		String loginId = (String) principalCollection.fromRealm(getName())
				.iterator().next();
		UserLogin user = userService.findUser(loginId);
		SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
		result.addRole("system");

		return result;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("###【开始认证[SessionId]】"
				+ SecurityUtils.getSubject().getSession().getId());
		String loginName = (String) token.getPrincipal();
		UserLogin user = userService.findUser(loginName);
		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user.getUsername(), // 用户名
				user.getPassword(), // 密码
				// ByteSource.Util.bytes(user.getCredentialsSalt()),//
				// salt=username+salt,采用明文访问时，不需要此句
				getName() // realm name
		);
		return authenticationInfo;
	}

}
