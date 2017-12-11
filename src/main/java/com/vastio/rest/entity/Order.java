package com.vastio.rest.entity;

import lombok.Data;

/**
 * @author 陈晓宇
 * @version 创建时间：2017年12月7日 下午5:38:39
 * 类说明
 */

@Data
public class Order {
    private String orderStatus;
    private String orderId;
    private String mno;
    private String province;
    private String city;
    private String county;
    private String siteName;
    private String towerName;
    private String siteId;
    private String unitNum;
    private String towerStyle;
    private String macRoom;
    private String upkeep;
    private String upkeepDiscount;
    private String site;
    private String siteDiscount;
    private String upkeepCost;
    private String siteCost;
}
