package com.example.usermanagement.vo;

import com.example.usermanagement.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseVO {

    private Long id;

    private String username;

    private String name;

    private Integer age;

    private String email;

    private String phone;

    private Integer gender;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static UserResponseVO from(User user) {
        if (user == null) {
            return null;
        }

        UserResponseVO response = new UserResponseVO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setGender(user.getGender());
        response.setCreateTime(user.getCreateTime());
        response.setUpdateTime(user.getUpdateTime());
        return response;
    }
}
