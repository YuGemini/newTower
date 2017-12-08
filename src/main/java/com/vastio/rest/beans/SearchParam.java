package com.vastio.rest.beans;

import lombok.Data;

@Data
public class SearchParam {
	private int pageSize;
	private int curPage;
	private String id;
	private String ttmc;
	private String upkeepStatus;
	private String siteStatus;
}
