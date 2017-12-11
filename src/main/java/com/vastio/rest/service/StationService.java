package com.vastio.rest.service;

import java.util.List;
import java.util.Map;

import com.vastio.rest.entity.Result;
import com.vastio.rest.entity.StationInfo;

public interface StationService {
	public void insertStation(StationInfo stationInfo);

	public void insertStations(List<StationInfo> stationInfo);

	public void deleteStation(String id);

	public void deleteAllStation();

	public Result findall(Map map);
}