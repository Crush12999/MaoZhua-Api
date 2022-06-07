package com.maozhua.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author sryzzz
 * @create 2022/6/7 23:33
 * @description 评论数据传输对象
 */
@ApiModel(value = "CommentBO", description = "评论数据传输对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentBO {

    @NotBlank(message = "留言信息不完整")
    @ApiModelProperty(value = "视频博主ID")
    private String vlogerId;

    @ApiModelProperty(value = "父级评论ID")
    @NotBlank(message = "留言信息不完整")
    private String fatherCommentId;

    @ApiModelProperty(value = "视频ID")
    @NotBlank(message = "留言信息不完整")
    private String vlogId;

    @ApiModelProperty(value = "评论用户（发布者）ID")
    @NotBlank(message = "当前用户信息不正确，请尝试重新登录")
    private String commentUserId;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    @Length(max = 50, message = "评论内容长度不能超过50")
    private String content;
}