package com.vastio.rest.entity;

import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;

@Data
@Table(name = "loginuser")
public class UserLogin {
    private String id;
    private String username;
    private String password;
    private String code;

}
