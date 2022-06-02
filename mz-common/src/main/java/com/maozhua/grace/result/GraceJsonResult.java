package com.maozhua.grace.result;

import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 12:24
 * @description 自定义响应数据类型枚举升级版本
 */
public class GraceJsonResult {

    /**
     * 响应业务状态码
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 响应数据，可以是Object，也可以是List或Map等
     */
    private Object data;

    /**
     * 成功返回，带有数据的，直接往OK方法丢data数据即可
     *
     * @param data 数据
     * @return 成功返回
     */
    public static GraceJsonResult ok(Object data) {
        return new GraceJsonResult(data);
    }

    /**
     * 成功返回，不带有数据的，直接调用ok方法，data无须传入（其实就是null）
     *
     * @return 成功返回
     */
    public static GraceJsonResult ok() {
        return new GraceJsonResult(ResponseStatusEnum.SUCCESS);
    }

    public GraceJsonResult(Object data) {
        this.status = ResponseStatusEnum.SUCCESS.status();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }


    /**
     * 错误返回，直接调用error方法即可，当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     *
     * @return 错误返回
     */
    public static GraceJsonResult error() {
        return new GraceJsonResult(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回，map中包含了多条错误信息，可以用于表单验证，把错误统一的全部返回出去
     *
     * @param map map
     * @return 错误返回
     */
    public static GraceJsonResult errorMap(Map map) {
        return new GraceJsonResult(ResponseStatusEnum.FAILED, map);
    }

    /**
     * 错误返回，直接返回错误的消息
     *
     * @param msg 错误信息
     * @return 错误返回
     */
    public static GraceJsonResult errorMsg(String msg) {
        return new GraceJsonResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 错误返回，token异常，一些通用的可以在这里统一定义
     *
     * @return 错误返回
     */
    public static GraceJsonResult errorTicket() {
        return new GraceJsonResult(ResponseStatusEnum.TICKET_INVALID);
    }

    /**
     * 自定义错误范围，需要传入一个自定义的枚举，可以到[ResponseStatusEnum.java]中自定义后再传入
     *
     * @param responseStatus 自定义枚举
     * @return 自定义错误范围
     */
    public static GraceJsonResult errorCustom(ResponseStatusEnum responseStatus) {
        return new GraceJsonResult(responseStatus);
    }

    public static GraceJsonResult exception(ResponseStatusEnum responseStatus) {
        return new GraceJsonResult(responseStatus);
    }

    public GraceJsonResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }

    public GraceJsonResult(ResponseStatusEnum responseStatus, Object data) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }

    public GraceJsonResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    public GraceJsonResult() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
