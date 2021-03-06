package com.maozhua.service.impl;

import com.github.pagehelper.PageHelper;
import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.VlogBO;
import com.maozhua.enums.MessageEnum;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.MyLikedVlogMapper;
import com.maozhua.mapper.VlogMapper;
import com.maozhua.mapper.VlogMapperCustom;
import com.maozhua.pojo.MyLikedVlog;
import com.maozhua.pojo.Vlog;
import com.maozhua.service.FansService;
import com.maozhua.service.MessageService;
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
    private FansService fansService;

    @Resource
    private MessageService messageService;

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
     * @param myId     我的ID
     * @param search   视频标题
     * @param page     第几页
     * @param pageSize 每页显示多少条视频
     * @return 视频列表
     */
    @Override
    public PagedGridResult listIndexVlogs(String myId, String search, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }

        List<IndexVlogVO> list = vlogMapperCustom.listIndexVlogs(map);

        for (IndexVlogVO vlogVO : list) {
            if (StringUtils.isNotBlank(myId)) {
                vlogVO.setLikeCounts(getLikeVlogCount(vlogVO.getVlogId()));
                vlogVO.setDoIFollowVloger(fansService.queryDoMeFollowVloger(myId, vlogVO.getVlogerId()));
                vlogVO.setDoILikeThisVlog(doILikeVlog(myId, vlogVO.getVlogId()));
            }
        }

        return setterPagedGrid(list, page);
    }

    /**
     * 通过 视频ID 获取视频详情
     *
     * @param userId 用户ID
     * @param vlogId 视频ID
     * @return 视频
     */
    @Override
    public IndexVlogVO getVlogDetailById(String userId, String vlogId) {

        IndexVlogVO vlogVO = null;
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isNotBlank(vlogId)) {
            map.put("vlogId", vlogId);
        }

        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);

        if (list != null && !list.isEmpty()) {
            vlogVO = list.get(0);
            return setterVo(vlogVO, userId);
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

        // 点赞后，视频获赞 +1
        redisOperator.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        redisOperator.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);

        // 我点赞的视频需要再Redis中保存关联关系
        redisOperator.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, ONE);

        // 系统消息：点赞短视频
        Vlog vlog = this.getVlogById(vlogId);
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("vlogId", vlogId);
        msgContent.put("vlogCover", vlog.getCover());
        messageService.createMsg(userId, vlog.getVlogerId(), MessageEnum.LIKE_VLOG.type, msgContent);
    }

    /**
     * 根据视频ID获取视频信息
     *
     * @param vlogId 视频ID
     * @return 视频信息
     */
    @Override
    public Vlog getVlogById(String vlogId) {
        return vlogMapper.selectByPrimaryKey(vlogId);
    }

    /**
     * 用户取消点赞视频
     *
     * @param userId   用户ID
     * @param vlogerId 视频博主ID
     * @param vlogId   视频ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void userUnLikeVlog(String userId, String vlogerId, String vlogId) {
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setUserId(userId);
        myLikedVlog.setVlogId(vlogId);
        myLikedVlogMapper.delete(myLikedVlog);

        String vlogLikeCount = redisOperator.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
        String vlogerLikeCount = redisOperator.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId);
        if (!ZERO.equals(vlogLikeCount)) {
            redisOperator.decrement(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        }
        if (!ZERO.equals(vlogerLikeCount)) {
            redisOperator.decrement(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        }

        // 我点赞的视频需要再Redis中保存关联关系
        redisOperator.del(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);
    }

    /**
     * 判断我是否喜欢这个视频
     *
     * @param myId   用户ID
     * @param vlogId 视频ID
     * @return 是否喜欢这个视频
     */
    private boolean doILikeVlog(String myId, String vlogId) {
        String doILike = redisOperator.get(REDIS_USER_LIKE_VLOG + ":" + myId + ":" + vlogId);
        if (StringUtils.isNotBlank(doILike) && ONE.equalsIgnoreCase(doILike)) {
            return true;
        }
        return false;
    }


    /**
     * 获取视频点赞总数
     *
     * @param vlogId 视频ID
     * @return 视频点赞总数
     */
    @Override
    public Integer getLikeVlogCount(String vlogId) {
        String likeCountStr = redisOperator.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(likeCountStr)) {
            return 0;
        }
        return Integer.parseInt(likeCountStr);
    }

    /**
     * 获取我点赞过的视频列表
     *
     * @param userId   用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 视频列表
     */
    @Override
    public PagedGridResult listMyLikedVlogs(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<IndexVlogVO> voList = vlogMapperCustom.listMyLikedVlogs(map);

        return setterPagedGrid(voList, page);
    }

    /**
     * 获取我关注的视频博主已发布的视频列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 视频列表
     */
    @Override
    public PagedGridResult listMyFollowVlogs(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<IndexVlogVO> voList = vlogMapperCustom.listMyFollowVlogs(map);

        for (IndexVlogVO vlogVO : voList) {
            if (StringUtils.isNotBlank(myId)) {
                vlogVO.setLikeCounts(getLikeVlogCount(vlogVO.getVlogId()));
                vlogVO.setDoIFollowVloger(true);
                vlogVO.setDoILikeThisVlog(doILikeVlog(myId, vlogVO.getVlogId()));
            }
        }

        return setterPagedGrid(voList, page);
    }

    /**
     * 获取朋友（互关）发布的视频列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 朋友（互关）发布的视频列表
     */
    @Override
    public PagedGridResult listMyFriendVlogs(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<IndexVlogVO> voList = vlogMapperCustom.listMyFriendVlogs(map);

        for (IndexVlogVO vlogVO : voList) {
            if (StringUtils.isNotBlank(myId)) {
                vlogVO.setLikeCounts(getLikeVlogCount(vlogVO.getVlogId()));
                vlogVO.setDoIFollowVloger(true);
                vlogVO.setDoILikeThisVlog(doILikeVlog(myId, vlogVO.getVlogId()));
            }
        }

        return setterPagedGrid(voList, page);
    }

    private IndexVlogVO setterVo(IndexVlogVO vlogVO, String userId) {
        if (StringUtils.isNotBlank(userId)) {
            vlogVO.setLikeCounts(getLikeVlogCount(vlogVO.getVlogId()));
            vlogVO.setDoIFollowVloger(fansService.queryDoMeFollowVloger(userId, vlogVO.getVlogerId()));
            vlogVO.setDoILikeThisVlog(doILikeVlog(userId, vlogVO.getVlogId()));
        }
        return vlogVO;
    }

}
