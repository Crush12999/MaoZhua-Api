package com.maozhua.service;

import com.maozhua.bo.VlogBO;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.IndexVlogVO;

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

    /**
     * 通过 视频ID 获取视频详情
     *
     * @param vlogId 视频ID
     * @return 视频
     */
    IndexVlogVO getVlogDetailById(String vlogId);

    /**
     * 用户把视频转为公开/私密
     *
     * @param userId  用户ID
     * @param vlogId  视频ID
     * @param yesOrNo 公开/私密 1：私密 0：公开
     */
    void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo);

    /**
     * 获取我的视频列表
     *
     * @param userId   用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @param yesOrNo  公开/私密 1：私密 0：公开
     * @return 视频列表
     */
    PagedGridResult listMyVlogs(String userId, Integer page, Integer pageSize, Integer yesOrNo);

    /**
     * 用户点赞/喜欢视频
     *
     * @param userId   用户ID
     * @param vlogerId 视频博主ID
     * @param vlogId   视频ID
     */
    void userLikeVlog(String userId, String vlogerId, String vlogId);

    /**
     * 用户取消点赞视频
     *
     * @param userId   用户ID
     * @param vlogerId 视频博主ID
     * @param vlogId   视频ID
     */
    void userUnLikeVlog(String userId, String vlogerId, String vlogId);
}
