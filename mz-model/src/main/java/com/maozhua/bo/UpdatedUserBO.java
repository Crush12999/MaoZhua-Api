package com.maozhua.bo;

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
 * @description 用户BO对象，用于更新用户信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户BO对象")
public class UpdatedUserBO {

    @ApiModelProperty(value = "用户ID")
    private String id;

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

}
