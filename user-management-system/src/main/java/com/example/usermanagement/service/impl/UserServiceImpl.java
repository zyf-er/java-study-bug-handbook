package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.usermanagement.common.exception.UsernameAlreadyExistsException;
import com.example.usermanagement.dto.UserAddDTO;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User addUser(UserAddDTO userAddDTO) {
        boolean usernameExists = exists(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userAddDTO.getUsername())
        );
        if (usernameExists) {
            throw new UsernameAlreadyExistsException(userAddDTO.getUsername());
        }

        User user = new User();
        user.setUsername(userAddDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userAddDTO.getPassword()));
        user.setName(userAddDTO.getName());
        user.setAge(userAddDTO.getAge());
        user.setEmail(userAddDTO.getEmail());
        user.setPhone(userAddDTO.getPhone());
        user.setGender(userAddDTO.getGender());
        save(user);
        return user;
    }
}