package com.maozhua.service.impl;

import com.github.pagehelper.PageHelper;
import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.VlogBO;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.VlogMapper;
import com.maozhua.mapper.VlogMapperCustom;
import com.maozhua.pojo.Vlog;
import com.maozhua.service.VlogService;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.IndexVlogVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 短视频业务逻辑层实现类
 */
@Service
public class VlogServiceImpl extends BaseInfoProperties implements VlogService {

    @Resource
    private VlogMapper vlogMapper;

    @Resource
    private VlogMapperCustom vlogMapperCustom;

    @Resource
    private Sid sid;

    /**
     * 发布短视频
     *
     * @param vlogBO 视频对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createVlog(VlogBO vlogBO) {

        String vid = sid.nextShort();

        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);
        vlog.setId(vid);

        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);
        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        vlogMapper.insert(vlog);

    }

    /**
     * 获取首页/搜索的视频列表
     *
     * @param search   视频标题
     * @param page     第几页
     * @param pageSize 每页显示多少条视频
     * @return 视频列表
     */
    @Override
    public PagedGridResult listIndexVlogs(String search, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }

        List<IndexVlogVO> list = vlogMapperCustom.listIndexVlogs(map);

        return setterPagedGrid(list, page);
    }

    /**
     * 通过 视频ID 获取视频详情
     *
     * @param vlogId 视频ID
     * @return 视频
     */
    @Override
    public IndexVlogVO getVlogDetailById(String vlogId) {

        IndexVlogVO vlogVO = null;
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isNotBlank(vlogId)) {
            map.put("vlogId", vlogId);
        }

        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);

        if (list != null && !list.isEmpty()) {
            vlogVO = list.get(0);
        }
        return vlogVO;
    }

}
