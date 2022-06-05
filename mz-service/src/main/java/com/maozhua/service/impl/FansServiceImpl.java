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
        Fans vloger = queryFansRelationship(myId, vlogerId);
        if (vloger != null) {
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type);
            fansMapper.updateByPrimaryKeySelective(vloger);
        } else {
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }
        fansMapper.insert(fans);


    }

    public Fans queryFansRelationship(String fanId, String vlogerId) {
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", fanId);
        criteria.andEqualTo("fanId", vlogerId);
        return fansMapper.selectOneByExample(example);
    }

}
