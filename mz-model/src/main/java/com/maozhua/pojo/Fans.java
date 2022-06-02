package com.maozhua.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 粉丝表
 *
 * @author sryzzz
 * @TableName fans
 */
@Data
@ToString
public class Fans implements Serializable {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 作家用户id
     */
    @Column(name = "vloger_id")
    private String vlogerId;

    /**
     * 粉丝用户id
     */
    @Column(name = "fan_id")
    private String fanId;

    /**
     * 粉丝是否是vloger的朋友，如果成为朋友，则本表的双方此字段都需要设置为1，
     * 如果有一人取关，则两边都需要设置为0
     */
    @Column(name = "is_fan_friend_of_mine")
    private Integer isFanFriendOfMine;

    private static final long serialVersionUID = 1L;

}