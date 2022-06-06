package com.maozhua.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/6/6 00:00
 * @description 我的粉丝VO对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "我的粉丝VO对象")
public class FansVO {

    @ApiModelProperty(value = "粉丝ID")
    private String fanId;

    @ApiModelProperty(value = "昵称，媒体号")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String face;

    @ApiModelProperty(value = "是否互粉")
    private Boolean isFriend = false;
}
