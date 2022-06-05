package com.maozhua.base;

import com.github.pagehelper.PageInfo;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.utils.RedisOperator;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sryzzz
 * @create 2022/6/2 21:39
 * @description 公共接口
 */
public class BaseInfoProperties {

    /**
     * 开始分页的页码数
     */
    public static final Integer COMMON_START_PAGE = 1;

    /**
     * 每页显示 10 条
     */
    public static final Integer COMMON_PAGE_SIZE = 10;

    /**
     * Redis验证码key
     */
    public static final String MOBILE_SMS_CODE = "mobile:sms_code";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";

    /**
     * 我的关注总数
     */
    public static final String REDIS_MY_FOLLOWS_COUNTS = "redis_my_follows_counts";
    /**
     * 我的粉丝总数
     */
    public static final String REDIS_MY_FANS_COUNTS = "redis_my_fans_counts";

    /**
     * 博主和粉丝的关联关系，用于判断他们是否互粉
     */
    public static final String REDIS_FANS_AND_VLOGGER_RELATIONSHIP = "redis_fans_and_vlogger_relationship";

    /**
     * 视频和发布者获赞数
     */
    public static final String REDIS_VLOG_BE_LIKED_COUNTS = "redis_vlog_be_liked_counts";
    public static final String REDIS_VLOGER_BE_LIKED_COUNTS = "redis_vloger_be_liked_counts";

    @Resource
    public RedisOperator redisOperator;

    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }

}
