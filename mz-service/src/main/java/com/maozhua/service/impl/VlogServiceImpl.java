package com.maozhua.service.impl;

import com.github.pagehelper.PageHelper;
import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.VlogBO;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.MyLikedVlogMapper;
import com.maozhua.mapper.VlogMapper;
import com.maozhua.mapper.VlogMapperCustom;
import com.maozhua.pojo.MyLikedVlog;
import com.maozhua.pojo.Vlog;
import com.maozhua.service.VlogService;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.IndexVlogVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    private MyLikedVlogMapper myLikedVlogMapper;

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

    /**
     * 用户把视频转为公开/私密
     *
     * @param userId  用户ID
     * @param vlogId  视频ID
     * @param yesOrNo 公开/私密 1：私密 0：公开
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {

        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", vlogId);
        criteria.andEqualTo("vlogerId", userId);

        Vlog updatedVlog = new Vlog();
        updatedVlog.setIsPrivate(yesOrNo);

        vlogMapper.updateByExampleSelective(updatedVlog, example);
    }

    /**
     * 获取我的视频列表
     *
     * @param userId   用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @param yesOrNo  公开/私密 1：私密 0：公开
     * @return 视频列表
     */
    @Override
    public PagedGridResult listMyVlogs(String userId, Integer page, Integer pageSize, Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", userId);
        criteria.andEqualTo("isPrivate", yesOrNo);

        PageHelper.startPage(page, pageSize);
        List<Vlog> vlogList = vlogMapper.selectByExample(example);

        return setterPagedGrid(vlogList, page);
    }

    /**
     * 用户点赞/喜欢视频
     *
     * @param userId   用户ID
     * @param vlogerId 视频博主ID
     * @param vlogId   视频ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void userLikeVlog(String userId, String vlogerId, String vlogId) {
        String rid = sid.nextShort();

        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setId(rid);
        myLikedVlog.setUserId(userId);
        myLikedVlog.setVlogId(vlogId);

        myLikedVlogMapper.insert(myLikedVlog);

        // 点赞后，视频和视频发布者的获赞都会 +1
        redisOperator.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        redisOperator.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);

        // 我点赞的视频需要再Redis中保存关联关系
        redisOperator.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, ONE);
    }

}
