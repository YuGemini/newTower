package com.vastio.rest.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vastio.rest.entity.SignUp;
import com.vastio.rest.entity.UserLogin;
import com.vastio.rest.service.UserLoginService;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserLoginController {
	@Autowired
	private UserLoginService userLoginService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin() {
		return "login";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String tosignup() {
		return "signup";
	}
	
	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "login";
	}

	@ApiOperation(value = "用户登入", notes = "")
	@RequestMapping(value = "/tologin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> login(UserLogin user, HttpSession session,
			HttpServletResponse response, Model m) {
		response.setCharacterEncoding("UTF-8");

		Map<String, String> result = new HashMap<String, String>();
		if (!(user.getCode().equalsIgnoreCase(session.getAttribute("code")
				.toString()))) {
			result.put("msg", "验证码错误");
			result.put("status", "1");
			m.addAttribute("message", "验证码错误");
			return result;
		} else {
			String username = user.getUsername();
			System.out.println(username);
			UsernamePasswordToken token = new UsernamePasswordToken(
					user.getUsername(), user.getPassword());
			
			//Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");  
			// 创建SecurityManager (根据配置创建SecurityManager实例)  
			//SecurityManager security = factory.getInstance();  
			//SecurityUtils.setSecurityManager(security);
			
			// 获取当前的Subject
			Subject currentUser = SecurityUtils.getSubject();
			try {
				// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
				// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
				// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
				currentUser.login(token);
				result.put("status", "0");
			} catch (UnknownAccountException uae) {
				m.addAttribute("message", "未知账户");
				result.put("msg", "未知账户");
				result.put("status", "1");
			} catch (IncorrectCredentialsException ice) {
				m.addAttribute("message", "密码不正确");
				result.put("msg", "密码不正确");
				result.put("status", "1");
			} catch (LockedAccountException lae) {
				m.addAttribute("message", "账户已锁定");
				result.put("msg", "账户已锁定");
				result.put("status", "1");
			} catch (ExcessiveAttemptsException eae) {
				m.addAttribute("message", "用户名或密码错误次数过多");
				result.put("msg", "用户名或密码错误次数过多");
				result.put("status", "1");
			} catch (AuthenticationException ae) {
				// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
				ae.printStackTrace();
				m.addAttribute("message", "用户名或密码不正确");
				result.put("msg", "用户名或密码不正确");
				result.put("status", "1");
			}
			// 验证是否登录成功
			if (currentUser.isAuthenticated()) {
				//return "/index";
			} else {
				token.clear();
				//return "redirect:/login";
			}
		}
		return result;

	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> sign(SignUp user, HttpSession session,
			HttpServletResponse response, Model m) {
		response.setCharacterEncoding("UTF-8");

		Map<String, String> result = new HashMap<String, String>();
		if(user.getUsername().equals("")||user.getPsd1().equals("")){
			result.put("msg", "输入不能为空");
			result.put("status", "1");
			return result;
		}
		
		if(!user.getPsd1().equals(user.getPsd2())){
			result.put("msg", "两次密码输入不同");
			result.put("status", "1");
			return result;
		}
		
		if(userLoginService.findUser(user.getUsername())!=null){
			result.put("msg", "用户已存在");
			result.put("status", "1");
			return result;
		}
		
		UserLogin data = new UserLogin();
		data.setUsername(user.getUsername());
		data.setPassword(new Md5Hash(new Md5Hash(user.getPsd1())).toHex());
		data.setId("1");
		userLoginService.saveUser(data);
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPsd1());
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(token);
		result.put("status", "0");
		
		return result;

	}

}
