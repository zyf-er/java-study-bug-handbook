package com.example.usermanagement.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * 用于注册 MyBatis-Plus 的核心插件拦截器
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 注册 MyBatis-Plus 主拦截器
     * 所有内置插件都需要通过该拦截器统一加入生效
     *
     * @return MybatisPlusInterceptor 拦截器实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor =
                new MybatisPlusInterceptor();

        // 分页插件：开启分页查询支持，指定数据库方言为 MySQL
        // 注意：需保证多租户、性能分析等插件在其之前添加（如有）
        interceptor.addInnerInterceptor(
                new PaginationInnerInterceptor(DbType.MYSQL)
        );
        // 乐观锁插件：配合实体类中的 @Version 注解字段，
        // 在 updateById 时自动基于版本号进行并发更新控制
        interceptor.addInnerInterceptor(
                new OptimisticLockerInnerInterceptor()
        );
        return interceptor;
    }
}