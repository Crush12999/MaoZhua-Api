package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.VlogBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.service.VlogService;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.IndexVlogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/6/5 16:40
 * @description 短视频接口层
 */
@Api(tags = "VlogController => 短视频接口层")
@RestController
@RequestMapping("vlog")
public class VlogController extends BaseInfoProperties {

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

    /**
     * 获取首页/搜索的视频列表
     *
     * @return 视频列表
     */
    @ApiOperation(value = "获取首页/搜索的视频列表")
    @GetMapping("indexList")
    public GraceJsonResult indexList(@RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {

        PagedGridResult gridResult = vlogService.listIndexVlogs(search, page, pageSize);
        return GraceJsonResult.ok(gridResult);
    }

    /**
     * 通过视频ID获取视频信息
     *
     * @return 视频信息
     */
    @ApiOperation(value = "通过视频ID获取视频信息")
    @GetMapping("detail")
    public GraceJsonResult detail(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam String vlogId) {

        return GraceJsonResult.ok(vlogService.getVlogDetailById(vlogId));
    }
}
