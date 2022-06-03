package com.maozhua.service.impl;

import com.maozhua.bo.UpdatedUserBO;
import com.maozhua.enums.Sex;
import com.maozhua.enums.UserInfoModifyType;
import com.maozhua.enums.YesOrNo;
import com.maozhua.exception.GraceException;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.mapper.UsersMapper;
import com.maozhua.pojo.Users;
import com.maozhua.service.UserService;
import com.maozhua.utils.DateUtil;
import com.maozhua.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
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

    /**
     * 修改用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Users updateUserInfo(UpdatedUserBO updatedUserBO) {
        Users oldUser = new Users();
        BeanUtils.copyProperties(updatedUserBO, oldUser);
        // 只修改不为空的属性
        int row = usersMapper.updateByPrimaryKeySelective(oldUser);
        if (row != 1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        return getUserById(updatedUserBO.getId());
    }

    /**
     * 修改用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type) {
        if (type.equals(UserInfoModifyType.NICKNAME.type)) {
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("nickname", updatedUserBO.getNickname());
            Users user = usersMapper.selectOneByExample(example);
            if (user != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
        }
        if (type.equals(UserInfoModifyType.MAOZHUANUM.type)) {
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("maozhuaNum", updatedUserBO.getMaozhuaNum());
            Users user = usersMapper.selectOneByExample(example);
            if (user != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_MAOZHUANUM_EXIST_ERROR);
            }

            Users tempUser = getUserById(updatedUserBO.getId());
            if (tempUser.getCanMaozhuaNumBeUpdated().equals(YesOrNo.NO.type)) {
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_MAOZHUANUM_ERROR);
            }

            updatedUserBO.setCanMaozhuaNumBeUpdated(YesOrNo.NO.type);
        }
        return updateUserInfo(updatedUserBO);
    }
}
