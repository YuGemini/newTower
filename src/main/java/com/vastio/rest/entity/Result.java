package com.vastio.rest.entity;

import java.util.List;

import lombok.Data;

@Data
public class Result {
	private List<StationInfo> rows;
	private int total;

}
