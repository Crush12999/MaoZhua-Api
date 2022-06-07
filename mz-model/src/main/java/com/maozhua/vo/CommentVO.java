package com.maozhua.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/6/7 23:46
 * @description 评论展示对象
 */
@ApiModel(value = "CommentVO", description = "评论展示对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentVO {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "评论ID")
    private String commentId;

    @ApiModelProperty(value = "视频发布者ID")
    private String vlogerId;

    @ApiModelProperty(value = "父评论ID")
    private String fatherCommentId;

    @ApiModelProperty(value = "视频ID")
    private String vlogId;

    @ApiModelProperty(value = "评论用户ID")
    private String commentUserId;

    @ApiModelProperty(value = "评论用户昵称")
    private String commentUserNickname;

    @ApiModelProperty(value = "评论用户头像")
    private String commentUserFace;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论点赞数")
    private Integer likeCounts;

    @ApiModelProperty(value = "所回复用户的昵称")
    private String replyedUserNickname;

    @ApiModelProperty(value = "评论创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否点赞此评论")
    private Integer isLike = 0;
}
