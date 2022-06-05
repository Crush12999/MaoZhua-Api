package com.maozhua.service.impl;

import com.maozhua.bo.VlogBO;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.VlogMapper;
import com.maozhua.pojo.Vlog;
import com.maozhua.service.VlogService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 短视频业务逻辑层实现类
 */
@Service
public class VlogServiceImpl implements VlogService {

    @Resource
    private VlogMapper vlogMapper;

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
}
