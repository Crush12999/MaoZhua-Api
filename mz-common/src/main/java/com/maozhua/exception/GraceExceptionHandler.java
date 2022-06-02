package com.maozhua.exception;

import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.grace.result.ResponseStatusEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 22:33
 * @description 统一异常拦截处理
 * 可以针对异常的类型进行捕获，然后返回json信息到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJsonResult returnMyException(MyCustomException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJsonResult returnMethodArgumentNotValid(MethodArgumentNotValidException e) {
        // 判断 BindingResult 是否保存了错误的验证信息，如果有，需要返回给前端
        BindingResult result = e.getBindingResult();
        Map<String, String> map = getErrors(result);
        return GraceJsonResult.errorMap(map);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJsonResult returnMaxUploadSize(MaxUploadSizeExceededException e) {
        return GraceJsonResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_2MB_ERROR);
    }

    /**
     * 获取数据校验的错误信息
     *
     * @param bindingResult BindingResult
     * @return 数据校验的错误信息
     */
    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError error : errorList) {
            // 错误所对应的属性字段名
            String field = error.getField();
            // 错误的信息
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }

}
