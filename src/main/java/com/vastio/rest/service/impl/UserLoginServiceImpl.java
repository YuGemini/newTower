package com.vastio.rest.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.beetl.sql.core.SQLManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vastio.rest.entity.UserLogin;
import com.vastio.rest.service.UserLoginService;



@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    SQLManager sqlManager;

    public UserLogin findUser(String name) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("name", name);
        UserLogin user = sqlManager.selectSingle("account.findUserByName", paras, UserLogin.class);
        return user;
    }

    @Override
    public void saveUser(UserLogin user) {
        sqlManager.insert(user);
    }


}
