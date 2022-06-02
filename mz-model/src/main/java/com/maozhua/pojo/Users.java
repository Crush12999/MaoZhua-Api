package com.maozhua.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author sryzzz
 * @TableName users
 */
@Data
@ToString
public class Users implements Serializable {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称，媒体号
     */
    private String nickname;

    /**
     * 猫爪号，类似头条号，抖音号，公众号，唯一标识，需要限制修改次数，比如终生1次，每年1次，每半年1次等，可以用于付费修改。
     */
    @Column(name = "maozhua_num")
    private String maozhuaNum;

    /**
     * 头像
     */
    private String face;

    /**
     * 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 简介
     */
    private String description;

    /**
     * 个人介绍的背景图
     */
    @Column(name = "bg_img")
    private String bgImg;

    /**
     * 猫爪号能否被修改，1：默认，可以修改；0，无法修改
     */
    @Column(name = "can_maozhua_num_be_updated")
    private Integer canMaozhuaNumBeUpdated;

    /**
     * 创建时间 创建时间
     */
    @Column(name = "created_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 更新时间 更新时间
     */
    @Column(name = "updated_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;

}