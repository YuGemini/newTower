package com.vastio.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vastio.rest.beans.SearchParam;
import com.vastio.rest.entity.Order;
import com.vastio.rest.entity.Result;
import com.vastio.rest.entity.StationInfo;
import com.vastio.rest.service.StationService;
import com.vastio.rest.utils.ExcelExportUtil;
import com.vastio.rest.utils.ExcelImportUtil;
import com.vastio.rest.utils.ViewExcel;

@Controller
public class AtmController {
    public static double[][] cost = {
            {2626.42, 1683.77, 210.47, 2104.72, 1683.77, 210.47, 2104.72, 1683.77, 210.47},
            {2626.42, 1683.77, 210.47, 2104.72, 1683.77, 210.47, 2104.72, 1683.77, 210.47},
            {2594.34, 1698.11, 212.26, 2122.64, 1698.11, 212.26, 2122.64, 1698.11, 212.26},
            {2594.34, 1698.11, 212.26, 2122.64, 1698.11, 212.26, 2122.64, 1698.11, 212.26}};
    @Resource
    private StationService stationService;

    @RequestMapping(value = "/station/list.action", method = RequestMethod.POST)
    @ResponseBody
    public Result userList(@RequestBody SearchParam param) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curRow", param.getCurPage());
        map.put("pageSize", param.getPageSize());
        map.put("id", param.getId());
        map.put("ct", param.getTtmc());
        map.put("upkeepStatus", param.getUpkeepStatus());
        map.put("siteStatus", param.getSiteStatus());
        Result list = this.stationService.findall(map);
        return list;
    }

    @RequestMapping(value = "/station/delete")
    @ResponseBody
    public void delete(@RequestParam(value = "id", required = true) String row) {
        stationService.deleteStation(row);
    }

    @RequestMapping(value = "/station/deleteAll")
    @ResponseBody
    public void deleteAll() {
        stationService.deleteAllStation();
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String toStation(Integer id, Model model) {
        return "index";
    }

    @RequestMapping(value = "/station/import.action")
    @ResponseBody
    public String importExcel(@RequestParam(value = "fileUpload") MultipartFile excelFile,
            HttpServletRequest request) throws IOException {
        InputStream fis = excelFile.getInputStream();
        List<Map<String, String>> data = ExcelImportUtil.parseExcel(fis);
        List<Order> orders = new ArrayList<Order>();
        List<String> siteIds = new ArrayList<String>();
        for (Map item : data) {
            if ("已起租".equals(item.get("订单状态").toString())) {
                Order order = new Order();
                order.setOrderStatus(item.get("订单状态").toString());
                order.setOrderId(item.get("订单号").toString());
                order.setMno(item.get("运营商").toString());
                order.setProvince(item.get("省份").toString());
                order.setCity(item.get("地市").toString());
                order.setCounty(item.get("区县").toString());
                order.setSiteName(item.get("需求方站点名称").toString());
                order.setTowerName(item.get("铁塔站址名称").toString());
                order.setSiteId(item.get("站址编码").toString());
                siteIds.add(item.get("站址编码").toString());
                order.setUnitNum(item.get("产品单元数").toString());
                order.setTowerStyle(item.get("铁塔类型").toString());
                order.setMacRoom(item.get("机房类型").toString());
                order.setUpkeep(item.get("[维护费原始值]").toString());
                order.setUpkeepDiscount(item.get("维护费共享折扣").toString());
                order.setSite(item.get("[场地费原始值]").toString());
                order.setSiteDiscount(item.get("场地费共享折扣").toString());
                order.setUpkeepCost(item.get("维保费用").toString());
                order.setSiteCost(item.get("物业系统场地费数据").toString());
                orders.add(order);
            }
        }

        Set<String> siteSet = new HashSet<String>(siteIds);
        List<StationInfo> stationInfos = new ArrayList<StationInfo>();
        siteSet.forEach(e -> {
            StationInfo stationInfo = new StationInfo();
            stationInfo.setId(e);
            changeStationInfo(orders, e, stationInfo);
            stationInfos.add(stationInfo);
        });

        stationService.insertStations(stationInfos);

        return "success";
    }

    private void changeStationInfo(List<Order> orders, String id, StationInfo stationInfo) {
        DecimalFormat df = new DecimalFormat("#.00");
        String ctName = "";
        Double upkeepIncom = 0.0;
        Double upkeepExpend1 = 0.0;
        Double upkeepExpend2 = 0.0;
        Double siteIncom = 0.0;
        Double siteExpend = 0.0;
        for (Order order : orders) {
            if (order.getSiteId().equals(id)) {
                ctName = order.getTowerName();
                int x = 0;
                int y = 0;
                if (order.getMno().equals("移动")) {
                    stationInfo.setCm(order.getSiteName());
                    if (order.getMacRoom().indexOf("无机房") != -1) {
                        y = 2;
                    } else if (order.getMacRoom().indexOf("机房") != -1) {
                        y = 0;
                    } else if (order.getMacRoom().indexOf("一体化机柜") != -1
                            || order.getMacRoom().indexOf("RRU拉远") != -1) {
                        y = 1;
                    }
                } else if (order.getMno().equals("电信")) {
                    stationInfo.setCd(order.getSiteName());
                    if (order.getMacRoom().indexOf("无机房") != -1) {
                        y = 5;
                    } else if (order.getMacRoom().indexOf("机房") != -1) {
                        y = 3;
                    } else if (order.getMacRoom().indexOf("一体化机柜") != -1
                            || order.getMacRoom().indexOf("RRU拉远") != -1) {
                        y = 4;
                    }
                } else if (order.getMno().equals("联通")) {
                    stationInfo.setCu(order.getSiteName());
                    if (order.getMacRoom().indexOf("无机房") != -1) {
                        y = 8;
                    } else if (order.getMacRoom().indexOf("机房") != -1) {
                        y = 6;
                    } else if (order.getMacRoom().indexOf("一体化机柜") != -1
                            || order.getMacRoom().indexOf("RRU拉远") != -1) {
                        y = 7;
                    }
                }

                if (!order.getUpkeep().equals("")) {
                    upkeepIncom +=
                            (Double.valueOf(order.getUpkeep()))
                                    * (Double.valueOf(order.getUnitNum()))
                                    * (Double.valueOf(order.getUpkeepDiscount()));
                }
                upkeepExpend1 = Double.valueOf(order.getUpkeepCost());

                if (order.getCounty().equals("乐平市")) {
                    x = 0;
                } else if (order.getCounty().equals("昌江区")) {
                    x = 1;
                } else if (order.getCounty().equals("浮梁县")) {
                    x = 2;
                } else if (order.getCounty().equals("珠山区")) {
                    x = 3;
                }
                double c = cost[x][y];
                if (c > upkeepExpend2) {
                    upkeepExpend2 = c;
                }

                String[] str = order.getSite().split("|");
                if (Double.valueOf(str[0]) == 0 && Double.valueOf(str[2]) == 0) {
                    siteIncom += 0;
                } else {
                    siteIncom +=
                            (Double.valueOf(str[0]) + Double.valueOf(str[2]) / 10)
                                    * Double.valueOf(order.getSiteDiscount());
                }

                siteExpend = Double.valueOf(order.getSiteCost());

            }
        }

        stationInfo.setCt(ctName);
        if (upkeepIncom == 0) {
            stationInfo.setUpkeepIncom(String.valueOf(upkeepIncom / 12));
        } else {
            stationInfo.setUpkeepIncom(String.valueOf(df.format(upkeepIncom / 12)));
        }
        if ((upkeepExpend1 + upkeepExpend2) == 0) {
            stationInfo.setUpkeepExpend(String.valueOf((upkeepExpend1 + upkeepExpend2) / 12));
        } else {
            stationInfo.setUpkeepExpend(String.valueOf(df
                    .format((upkeepExpend1 + upkeepExpend2) / 12)));
        }
        if (upkeepIncom / (upkeepExpend1 + upkeepExpend2) > 1.15) {
            stationInfo.setUpkeepStatus("达标");
        } else {
            stationInfo.setUpkeepStatus("未达标");
        }
        if (siteIncom == 0) {
            stationInfo.setSiteIncom(String.valueOf(siteIncom));
        } else {
            stationInfo.setSiteIncom(String.valueOf(df.format(siteIncom)));
        }
        if (siteExpend == 0) {
            stationInfo.setSiteExpend(String.valueOf(siteExpend));
        } else {
            stationInfo.setSiteExpend(String.valueOf(df.format(siteExpend)));
        }

        if (siteIncom > siteExpend) {
            stationInfo.setSiteStatus("达标");
        } else {
            stationInfo.setSiteStatus("未达标");
        }

    }

    @RequestMapping(value = "/station/export.action")
    public void report(@RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "ttmc", required = false) String ttmc,
            @RequestParam(value = "upkeep", required = false) String upkeep,
            @RequestParam(value = "site", required = false) String site, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curRow", 1);
        map.put("pageSize", 30000);
        map.put("id", id);
        map.put("ct", ttmc);
        map.put("upkeepStatus", upkeep);
        map.put("siteStatus", site);
        ViewExcel viewExcel = new ViewExcel();

        Map obj = null;
        System.out.println("response:" + response);
        // 获取数据库表生成的workbook
        Map condition = new HashMap();
        // 这里是从数据库里查数据并组装成我们想要的数据结构的过程，略。。
        Result list = this.stationService.findall(map);
        List<StationInfo> infos = list.getRows();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (StationInfo item : infos) {
            Map<String, String> station = new LinkedHashMap<String, String>();
            station.put("站址编号", item.getId());
            station.put("铁塔名称", item.getCt());
            station.put("移动名称", item.getCm());
            station.put("电信名称", item.getCd());
            station.put("联通名称", item.getCu());
            station.put("维护费出账收入", item.getUpkeepIncom());
            station.put("维护费支出计提", item.getUpkeepExpend());
            station.put("场地费出账收入", item.getSiteIncom());
            station.put("场地费支出计提", item.getSiteExpend());
            station.put("维护费收支是否达标", item.getUpkeepStatus());
            station.put("场地费收支是否达标", item.getSiteStatus());
            data.add(station);

        }
        XSSFWorkbook workbook = ExcelExportUtil.generateExcel(data, "站点信息表");
        try {
            viewExcel.buildExcelDocument(obj, workbook, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/station/template.action")
    public void template(ModelMap model, HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        ViewExcel viewExcel = new ViewExcel();
        Map obj = null;
        System.out.println("response:" + response);
        // 获取数据库表生成的workbook
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> station = new LinkedHashMap<String, String>();
        station.put("序号", "");
        station.put("订单状态", "");
        station.put("订单号", "");
        station.put("运营商", "");
        station.put("省份", "");
        station.put("地市", "");
        station.put("区县", "");
        station.put("需求方站点名称", "");
        station.put("铁塔站址名称", "");
        station.put("站址编码", "");
        station.put("产品单元数", "");
        station.put("铁塔类型", "");
        station.put("机房类型", "");
        station.put("[维护费原始值]", "");
        station.put("维护费共享折扣", "");
        station.put("[场地费原始值]", "");
        station.put("场地费共享折扣", "");
        station.put("维保费用", "");
        station.put("物业系统场地费数据", "");

        data.add(station);

        XSSFWorkbook workbook = ExcelExportUtil.generateExcel(data, "订单信息导入模板表");
        try {
            viewExcel.buildExcelDocument(obj, workbook, request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/towerlist", method = RequestMethod.GET)
    public String toTowerList() {
        return "towerlist";
    }

    @RequestMapping(value = "/addtower", method = RequestMethod.GET)
    public String toAddTower() {
        return "addtower";
    }

    @RequestMapping(value = "/uploadtower", method = RequestMethod.GET)
    public String toUploadTower() {
        return "uploadtower";
    }

}
