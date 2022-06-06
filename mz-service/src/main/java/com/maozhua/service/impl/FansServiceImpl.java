package com.maozhua.service.impl;

import com.github.pagehelper.PageHelper;
import com.maozhua.base.BaseInfoProperties;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.FansMapper;
import com.maozhua.mapper.FansMapperCustom;
import com.maozhua.pojo.Fans;
import com.maozhua.service.FansService;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.FansVO;
import com.maozhua.vo.VlogerVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/5 23:20
 * @description 粉丝服务接口实现
 */
@Service
public class FansServiceImpl extends BaseInfoProperties implements FansService {

    @Resource
    private FansMapper fansMapper;

    @Resource
    private FansMapperCustom fansMapperCustom;

    @Resource
    private Sid sid;

    /**
     * 关注
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doFollow(String myId, String vlogerId) {
        String fid = sid.nextShort();

        Fans fans = new Fans();
        fans.setId(fid);
        fans.setFanId(myId);
        fans.setVlogerId(vlogerId);

        // 判断对方是否关注我，如果关注我，那么双方都要互为好友关系
        Fans vloger = queryFansRelationship(vlogerId, myId);
        if (vloger != null) {
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type);
            fansMapper.updateByPrimaryKeySelective(vloger);
        } else {
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }
        fansMapper.insert(fans);

        // 博主的粉丝 + 1，我的关注 + 1。
        redisOperator.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redisOperator.increment(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 我和博主的关联关系，依赖Redis，不要存储数据库
        redisOperator.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId, ONE);

    }

    /**
     * 取关
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doCancel(String myId, String vlogerId) {
        // 判断我们是否朋友关系，如果是，则需要取消双方关系
        Fans fan = queryFansRelationship(myId, vlogerId);
        if (fan != null && fan.getIsFanFriendOfMine().equals(YesOrNo.YES.type)) {
            // 抹除双方的朋友关系，自己的关系删除即可
            Fans pendingFan = queryFansRelationship(vlogerId, myId);
            pendingFan.setIsFanFriendOfMine(YesOrNo.NO.type);
            fansMapper.updateByPrimaryKeySelective(pendingFan);
        }

        // 删除自己的关注关联表记录
        fansMapper.delete(fan);

        // 博主的粉丝 - 1，我的关注 - 1。
        if (!ZERO.equals(redisOperator.get(REDIS_MY_FANS_COUNTS + ":" + vlogerId))) {
            redisOperator.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        }

        if (!ZERO.equals(redisOperator.get(REDIS_MY_FANS_COUNTS + ":" + vlogerId))) {
            redisOperator.decrement(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);
        }

        // 我和博主的关联关系，依赖Redis，不要存储数据库
        redisOperator.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId);
    }

    /**
     * 查询用户是否关注博主
     *
     * @param myId     我的ID
     * @param vlogerId 视频博主ID
     * @return 是否关注博主
     */
    @Override
    public Boolean queryDoMeFollowVloger(String myId, String vlogerId) {
        if (redisOperator.keyIsExist(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId)) {
            return true;
        }
        Fans fan = queryFansRelationship(myId, vlogerId);
        if (fan != null) {
            redisOperator.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId, ONE);
            return true;
        }
        return false;
    }

    /**
     * 获取我关注的博主列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 我关注的博主列表
     */
    @Override
    public PagedGridResult listMyFollows(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<VlogerVO> myFollows = fansMapperCustom.listMyFollows(map);
        return setterPagedGrid(myFollows, page);
    }

    /**
     * 获取我的粉丝列表
     *
     * @param myId     用户ID
     * @param page     当前页
     * @param pageSize 每页显示视频条数
     * @return 我的粉丝列表
     */
    @Override
    public PagedGridResult listMyFans(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<FansVO> myFans = fansMapperCustom.listMyFans(map);

        for (FansVO fansVO : myFans) {
            String relationship = redisOperator.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + fansVO.getFanId());
            if (StringUtils.isNotBlank(relationship) && ONE.equalsIgnoreCase(relationship)) {
                fansVO.setFriend(true);
            }
        }

        return setterPagedGrid(myFans, page);
    }

    public Fans queryFansRelationship(String fanId, String vlogerId) {
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", vlogerId);
        criteria.andEqualTo("fanId", fanId);
        return fansMapper.selectOneByExample(example);
    }

}
