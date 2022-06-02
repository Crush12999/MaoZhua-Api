package com.maozhua.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 点赞短视频关联表
 *
 * @author sryzzz
 * @TableName my_liked_vlog
 */
@Data
@ToString
@Table(name = "my_liked_vlog")
public class MyLikedVlog implements Serializable {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 喜欢的短视频id
     */
    @Column(name = "vlog_id")
    private String vlogId;

    private static final long serialVersionUID = 1L;

}