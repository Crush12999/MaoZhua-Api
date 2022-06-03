package com.maozhua.enums;


import com.maozhua.exception.GraceException;
import com.maozhua.grace.result.ResponseStatusEnum;

/**
 * @author sryzzz
 * @Desc: 修改用户信息类型 枚举
 */
public enum UserInfoModifyType {
    NICKNAME(1, "昵称"),
    MAOZHUANUM(2, "猫爪号"),
    SEX(3, "性别"),
    BIRTHDAY(4, "生日"),
    LOCATION(5, "所在地"),
    DESC(6, "简介");

    public final Integer type;
    public final String value;

    UserInfoModifyType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public static void checkUserInfoTypeIsRight(Integer type) {
        if (!type.equals(UserInfoModifyType.NICKNAME.type) &&
                !type.equals(UserInfoModifyType.MAOZHUANUM.type) &&
                !type.equals(UserInfoModifyType.SEX.type) &&
                !type.equals(UserInfoModifyType.BIRTHDAY.type) &&
                !type.equals(UserInfoModifyType.LOCATION.type) &&
                !type.equals(UserInfoModifyType.DESC.type)) {
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
        }
    }
}
