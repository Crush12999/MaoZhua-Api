package com.maozhua.mo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/10 11:43
 * @description MO：MongoDB Object，消息对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("message")
@ApiModel(value = "MessageMO", description = "消息对象")
public class MessageMO {

    /**
     * 消息主键ID
     */
    @ApiModelProperty(value = "消息主键ID")
    @Id
    private String id;


    /**
     * 消息来自哪个用户的ID
     */
    @ApiModelProperty(value = "消息来自哪个用户的ID")
    @Field("fromUserId")
    private String fromUserId;

    /**
     * 消息来自哪个用户的昵称
     */
    @ApiModelProperty(value = "消息来自哪个用户的昵称")
    @Field("fromNickname")
    private String fromNickname;

    /**
     * 消息来自哪个用户的头像
     */
    @ApiModelProperty(value = "消息来自哪个用户的头像")
    @Field("fromFace")
    private String fromFace;

    /**
     * 消息需要发送到哪个用户
     */
    @ApiModelProperty(value = "消息需要发送到哪个用户")
    @Field("toUserId")
    private String toUserId;

    /**
     * 消息类型，枚举
     */
    @ApiModelProperty(value = "消息类型，枚举")
    @Field("msgType")
    private Integer msgType;

    /**
     * 消息具体内容
     */
    @ApiModelProperty(value = "消息具体内容")
    @Field("msgContent")
    private Map<?, ?> msgContent;

    /**
     * 消息创建时间
     */
    @ApiModelProperty(value = "消息创建时间")
    @Field("createTime")
    private Date createTime;
}
