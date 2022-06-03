package com.maozhua.service;

import com.maozhua.bo.UpdatedUserBO;
import com.maozhua.pojo.Users;

/**
 * @author sryzzz
 * @create 2022/6/2 23:27
 * @description 用户业务逻辑层
 */
public interface UserService {

    /**
     * 判断用户是否存在，如果存在则返回用户信息
     *
     * @param mobile 手机号
     * @return 用户
     */
    Users queryMobileIsExist(String mobile);

    /**
     * 创建用户信息，返回用户信息对象
     *
     * @param mobile 手机号
     * @return 用户
     */
    Users createUser(String mobile);

    /**
     * 根据用户ID获取用户信息，返回用户信息对象
     *
     * @param userId 用户ID
     * @return 用户
     */
    Users getUserById(String userId);

    /**
     * 修改用户信息
     *
     * @param updatedUserBO 要修改的用户信息
     * @return 用户信息
     */
    Users updateUserInfo(UpdatedUserBO updatedUserBO);

    /**
     * 修改用户信息
     */
    Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type);

}
