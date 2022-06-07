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

    public static final String ZERO = "0";
    public static final String ONE = "1";

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
     * 短视频的评论总数
     */
    public static final String REDIS_VLOG_COMMENT_COUNTS = "redis_vlog_comment_counts";
    /**
     * 短视频的评论喜欢数量
     */
    public static final String REDIS_VLOG_COMMENT_LIKED_COUNTS = "redis_vlog_comment_liked_counts";
    /**
     * 用户点赞评论
     */
    public static final String REDIS_USER_LIKE_COMMENT = "redis_user_like_comment";

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

    /**
     * 用户是否喜欢/点赞视频，取代数据库关联关系，1：喜欢，2：不喜欢（默认）
     */
    public static final String REDIS_USER_LIKE_VLOG = "redis_user_like_vlog";

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
