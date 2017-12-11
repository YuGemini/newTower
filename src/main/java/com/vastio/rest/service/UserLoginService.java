package com.vastio.rest.service;

import com.vastio.rest.entity.UserLogin;

public interface UserLoginService {
	public UserLogin findUser(String name);
	
	public void saveUser(UserLogin user);
}