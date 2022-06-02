package com.maozhua.exception;

import com.maozhua.grace.result.ResponseStatusEnum;

/**
 * @author sryzzz
 * @create 2022/6/2 22:33
 * @description 优雅的处理异常，统一封装
 */
public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }
}
