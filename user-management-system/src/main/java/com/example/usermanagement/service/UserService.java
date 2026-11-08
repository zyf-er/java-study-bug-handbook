package com.example.usermanagement.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.example.usermanagement.dto.UserAddDTO;
import com.example.usermanagement.entity.User;

public interface UserService extends IService<User> {

    User addUser(UserAddDTO userAddDTO);
}