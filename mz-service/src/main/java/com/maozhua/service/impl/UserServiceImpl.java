package com.maozhua.service.impl;

import com.maozhua.enums.Sex;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.UsersMapper;
import com.maozhua.pojo.Users;
import com.maozhua.service.UserService;
import com.maozhua.utils.DateUtil;
import com.maozhua.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 用户业务逻辑层实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private Sid sid;

    private static final String USER_FACE1 = "http://cdn.chuchen.sharesweets.top/images/IMG_3101.JPG";

    /**
     * 判断用户是否存在，如果存在则返回用户信息
     *
     * @param mobile 手机号
     * @return 用户
     */
    @Override
    public Users queryMobileIsExist(String mobile) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("mobile", mobile);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    /**
     * 创建用户信息，返回用户信息对象
     *
     * @param mobile 手机号
     * @return 用户
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Users createUser(String mobile) {

        // 获取全局唯一主键
        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);

        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setMaozhuaNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);

        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");
        user.setCanMaozhuaNumBeUpdated(YesOrNo.YES.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    /**
     * 根据用户ID获取用户信息，返回用户信息对象
     */
    @Override
    public Users getUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }
}
