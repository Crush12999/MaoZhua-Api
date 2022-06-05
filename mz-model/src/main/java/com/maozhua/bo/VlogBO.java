package com.maozhua.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author sryzzz
 * @description VlogBO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "VlogBO")
public class VlogBO implements Serializable {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "vlog视频发布者")
    private String vlogerId;

    @ApiModelProperty(value = "视频播放地址")
    private String url;

    @ApiModelProperty(value = "视频封面")
    private String cover;

    @ApiModelProperty(value = "视频标题，可以为空")
    private String title;

    @ApiModelProperty(value = "视频width")
    private Integer width;

    @ApiModelProperty(value = "视频height")
    private Integer height;

    @ApiModelProperty(value = "点赞总数")
    private Integer likeCounts;

    @ApiModelProperty(value = "评论总数")
    private Integer commentsCounts;

}