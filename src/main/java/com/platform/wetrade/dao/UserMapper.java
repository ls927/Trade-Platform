package com.platform.wetrade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.wetrade.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * @note @Mapper的作用：MyBatis-Plus 会自动帮我们创建对应的实现类，并将其注册为 Spring Bean
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
