package com.maozhua.service;

import com.maozhua.utils.PagedGridResult;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 粉丝服务业务逻辑层
 */
public interface FansService {

    /**
     * 关注
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     */
    void doFollow(String myId, String vlogerId);

    /**
     * 取关
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     */
    void doCancel(String myId, String vlogerId);

    /**
     * 查询用户是否关注博主
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     * @return 是否关注博主
     */
    Boolean queryDoMeFollowVloger(String myId, String vlogerId);

    /**
     * 获取我关注的博主列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 我关注的博主列表
     */
    PagedGridResult listMyFollows(String myId, Integer page, Integer pageSize);

    /**
     * 获取我的粉丝列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 我的粉丝列表
     */
    PagedGridResult listMyFans(String myId, Integer page, Integer pageSize);

}
