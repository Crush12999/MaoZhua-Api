package com.maozhua.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author sryzzz
 * @create 2022/6/2 22:47
 * @description 登录注册信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterLoginBO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度不正确")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

}
