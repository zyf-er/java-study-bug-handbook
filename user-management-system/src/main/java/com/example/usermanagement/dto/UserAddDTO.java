package com.example.usermanagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserAddDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @Min(value = 1, message = "年龄不能小于1")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private Integer gender;
}