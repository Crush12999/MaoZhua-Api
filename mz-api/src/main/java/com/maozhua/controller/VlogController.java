package com.maozhua.controller;

import com.maozhua.bo.VlogBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.service.VlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/6/5 16:40
 * @description 短视频接口层
 */
@Api(tags = "VlogController => 短视频接口层")
@RestController
@RequestMapping("vlog")
public class VlogController {

    @Resource
    private VlogService vlogService;

    /**
     * 视频发布接口
     * FIXME 对 VlogBO 添加数据校验
     *
     * @param vlogBO 视频信息
     * @return 发布结果
     */
    @ApiOperation(value = "视频发布")
    @PostMapping("publish")
    public GraceJsonResult publish(@RequestBody VlogBO vlogBO) {
        vlogService.createVlog(vlogBO);
        return GraceJsonResult.ok();
    }
}
