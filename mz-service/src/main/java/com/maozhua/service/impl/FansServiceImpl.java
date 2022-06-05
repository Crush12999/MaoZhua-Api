package com.maozhua.service.impl;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.FansMapper;
import com.maozhua.pojo.Fans;
import com.maozhua.service.FansService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

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
        redisOperator.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId, "1");

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
        redisOperator.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redisOperator.decrement(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 我和博主的关联关系，依赖Redis，不要存储数据库
        redisOperator.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId);
    }

    public Fans queryFansRelationship(String fanId, String vlogerId) {
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", vlogerId);
        criteria.andEqualTo("fanId", fanId);
        return fansMapper.selectOneByExample(example);
    }

}
