package com.vastio.rest.controller;

/**
 * @author 陈晓宇
 * @version 创建时间：2017年12月8日 下午5:18:19 类说明
 */
public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String x = "xxxx无机房";
        if (x.indexOf("无机房")!=-1) {
            System.out.println("111");
        } 
        else if (x.indexOf("机房")!=-1) {
            System.out.println("222");
        }
    }

}
