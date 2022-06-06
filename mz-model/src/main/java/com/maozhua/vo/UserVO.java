package com.maozhua.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/6/3 00:00
 * @description 用户VO对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户VO对象")
public class UserVO {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "昵称，媒体号")
    private String nickname;

    @ApiModelProperty(value = "猫爪号，唯一标识")
    private String maozhuaNum;

    @ApiModelProperty(value = "头像")
    private String face;

    @ApiModelProperty(value = "性别 1:男  0:女  2:保密")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String district;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "个人介绍的背景图")
    private String bgImg;

    @ApiModelProperty(value = "猫爪号能否被修改，1：默认，可以修改；0，无法修改")
    private Integer canMaozhuaNumBeUpdated;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @ApiModelProperty(value = "用户登录令牌")
    private String userToken;

    @ApiModelProperty(value = "我的关注总数")
    private Integer myFollowsCounts;

    @ApiModelProperty(value = "我的粉丝总数")
    private Integer myFansCounts;

    // @ApiModelProperty(value = "喜欢我的视频总数")
    // private Integer myLikedVlogCounts;

    @ApiModelProperty(value = "喜欢我的（获赞）总数")
    private Integer totalLikeMeCounts;
}
