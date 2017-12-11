package com.vastio.rest.entity;

import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;

@Data
@Table(name = "site")
public class StationInfo {
    private String id;
    private String ct;
    private String cm;
    private String cd;
    private String cu;
    private String upkeepIncom;
    private String upkeepExpend;
    private String siteIncom;
    private String siteExpend;
    private String upkeepStatus;
    private String siteStatus;

}
