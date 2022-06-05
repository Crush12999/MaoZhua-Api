package com.maozhua.service;

import com.maozhua.bo.VlogBO;

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

}
