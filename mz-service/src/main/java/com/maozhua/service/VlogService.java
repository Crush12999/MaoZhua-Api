package com.maozhua.service;

import com.maozhua.bo.VlogBO;
import com.maozhua.utils.PagedGridResult;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 短视频业务逻辑层
 */
public interface VlogService {

    /**
     * 发布短视频
     *
     * @param vlogBO 视频对象
     */
    void createVlog(VlogBO vlogBO);

    /**
     * 获取首页/搜索的视频列表
     *
     * @param search   视频标题
     * @param page     第几页
     * @param pageSize 每页显示多少条视频
     * @return 视频列表
     */
    PagedGridResult listIndexVlogs(String search, Integer page, Integer pageSize);

}
