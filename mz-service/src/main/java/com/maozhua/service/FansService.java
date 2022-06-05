package com.maozhua.service;

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

}
