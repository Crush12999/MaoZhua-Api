package com.maozhua.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 短视频表
 *
 * @author sryzzz
 * @TableName vlog
 */
@Data
@ToString
public class Vlog implements Serializable {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 对应用户表id，vlog视频发布者
     */
    @Column(name = "vloger_id")
    private String vlogerId;

    /**
     * 视频播放地址
     */
    private String url;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 视频标题，可以为空
     */
    private String title;

    /**
     * 视频width
     */
    private Integer width;

    /**
     * 视频height
     */
    private Integer height;

    /**
     * 点赞总数
     */
    @Column(name = "like_counts")
    private Integer likeCounts;

    /**
     * 评论总数
     */
    @Column(name = "comments_counts")
    private Integer commentsCounts;

    /**
     * 是否私密，用户可以设置私密，如此可以不公开给比人看
     */
    @Column(name = "is_private")
    private Integer isPrivate;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;

}