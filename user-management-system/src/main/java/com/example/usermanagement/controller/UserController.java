package com.example.usermanagement.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 查询全部用户
    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    // 查询用户详情
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    // 新增用户（此处仅作接口结构示范，正式实现需接入 DTO 和密码哈希）
    @PostMapping
    public Result<Boolean> testSave(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    // 修改用户
    @PutMapping("/{id}")
    public Result<Boolean> update(
            @PathVariable Long id,
            @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.updateById(user));
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }


    // 分页查询用户
    @GetMapping("/page")
    public Result<?> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {

        Page<User> page = userService.page(
                new Page<>(current, size)
        );

        return Result.success(page);
    }

    // 条件查询用户（name 模糊匹配、age 精确匹配，两个参数均可选，未传则不参与条件拼接）
    @GetMapping("/search")
    public Result<List<User>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age) {

        // 构建 Lambda 查询条件包装器
        LambdaQueryWrapper<User> wrapper =
                new LambdaQueryWrapper<>();

        // 第一个参数为条件开关：仅当 name 非空时才拼接 LIKE 条件
        wrapper.like(name != null && !name.isBlank(),
                User::getName, name);

        // 仅当 age 非空时才拼接等值条件
        wrapper.eq(age != null,
                User::getAge, age);

        // 执行查询并统一包装返回
        return Result.success(userService.list(wrapper));
    }

    // 统计用户数量
    @GetMapping("/count")
    public Result<Long> count() {
        return Result.success(userService.count());
    }

}