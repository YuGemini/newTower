package com.vastio.rest.aop;

/**
 * 定义审计日志操作类型
 */
public enum AuditLogType {
    USER_LOGIN("用户登入", "0"), 
    GET_COMMENT("获取报告评论", "1"), 
    ADD_COMMENT("添加评论", "2"), 
    DELETE_COMMENT("删除评论", "4"), 
    GET_USER_COMMENT("获取用户评论", "1"), 
    CHANGE_COMMENT_STATUS("改变评论状态为已读", "3"), 
    ADD_COLLECT("添加收藏", "2"), 
    UNFAVORITE("取消收藏", "4"), 
    IS_COLLECT("是否收藏", "1"), 
    GET_COLLECT_INFO("获取收藏的报告列表", "1"),
    GET_AUTHOR_COLLECT_INFO("获取其他人收藏我报告的信息", "1"), 
    CHANGE_COLLECT_STATUS("改变收藏状态为已读", "3"), 
    ADD_DOC("保存报告信息", "2"), 
    GET_ALL_DOC("获取所有报告信息", "1"), 
    GET_DEPT_DOC("获取部门报告信息", "1"), 
    GET_USER_DOC("获取用户报告信息", "1"), 
    GET_USER_LABELS("获取用户常用标签", "1"), 
    ADD_FILTER("添加筛选条件", "2"), 
    DELETE_FILTER("删除筛选条件", "4"), 
    GET_FILTER("获取筛选条件", "1"), 
    UPDATE_FILTER("更新筛选条件", "3"), 
    UPDATE_FILTER_STATUS("更新筛选条件状态", "3"), 
    GET_PERP_INFOS("获取用户信息", "1"), 
    GET_PERP_BY_ID("获取单个用户详细信息", "1");

    private String _name;
    private String _description;

    private AuditLogType(String description, String name) {
        _name = name;
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public String getName() {
        return _name;
    }
}
