package com.vastio.rest.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vastio.rest.entity.Result;
import com.vastio.rest.entity.StationInfo;
import com.vastio.rest.service.StationService;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    SQLManager sqlManager;

    public void insertStation(StationInfo stationInfo) {
        sqlManager.insert(stationInfo);
    }

    public void deleteStation(String id) {
        sqlManager.deleteById(StationInfo.class, id);
    }

    public Result findall(Map map) {
        PageQuery<StationInfo> query = new PageQuery<StationInfo>();
        query.setPageNumber(((Number) map.get("curRow")).longValue());
        query.setPageSize(((Number) map.get("pageSize")).longValue());
        query.setParas(map);
        query = sqlManager.pageQuery("site.findAll", StationInfo.class, query);
        Result result = new Result();
        result.setRows(query.getList());
        result.setTotal((int) query.getTotalRow());
        return result;
    }

    public void insertStations(List<StationInfo> stationInfos) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("stationInfos", stationInfos);
        sqlManager.update("site.insertStations", paras);
    }

    public void deleteAllStation() {
        sqlManager.update("site.deleteAllStations", null);
    }

}
