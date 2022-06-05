package com.maozhua.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/6/5 17:02
 * @description 首页视频VO对象
 */
@ApiModel(value = "IndexVlogVO => 首页视频VO对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IndexVlogVO {

    @ApiModelProperty(value = "视频ID")
    private String vlogId;

    @ApiModelProperty(value = "vlog视频发布者")
    private String vlogerId;

    @ApiModelProperty(value = "视频发布者头像")
    private String vlogerFace;

    @ApiModelProperty(value = "视频发布者名称")
    private String vlogerName;

    @ApiModelProperty(value = "视频播放地址")
    private String url;

    @ApiModelProperty(value = "视频封面")
    private String cover;

    @ApiModelProperty(value = "视频标题，可以为空")
    private String content;

    @ApiModelProperty(value = "视频width")
    private Integer width;

    @ApiModelProperty(value = "视频height")
    private Integer height;

    @ApiModelProperty(value = "点赞总数")
    private Integer likeCounts;

    @ApiModelProperty(value = "评论总数")
    private Integer commentsCounts;

    @ApiModelProperty(value = "是否私密 1：私密 0：公开")
    private Integer isPrivate;

    @ApiModelProperty(value = "所有视频默认不播放")
    private Boolean isPlay = false;

    @ApiModelProperty(value = "是否关注某个Vloger")
    private Boolean doIFollowVloger = false;

    @ApiModelProperty(value = "是否点赞过这个视频")
    private Boolean doILikeThisVlog = false;

}
