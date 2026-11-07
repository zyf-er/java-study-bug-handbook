package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {

}