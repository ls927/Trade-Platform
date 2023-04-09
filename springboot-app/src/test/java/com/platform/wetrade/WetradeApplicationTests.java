package com.platform.wetrade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.wetrade.dao.UserMapper;
import com.platform.wetrade.entity.User;
import com.platform.wetrade.service.UserService;
import com.platform.wetrade.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Slf4j
class WetradeApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    FileUtil fileUtil;

    @Test
    void contextLoads() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("=======================");
        log.info(format.format(new Date()));
    }

    @Test
    void testPagination(){

        Page<User> page = new Page<>(1,2);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername,2);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        userPage.getRecords().forEach(user -> {log.info(user.getSalt());});

    }

    @Test
    void testInsert(){
        User user = new User("HHH", "FFF", new Date(),"12");
        user.setSalt("NNN");
        int i = userMapper.insert(user);
        if(i > 0) log.info("return ========>" + user.getId());
    }

    @Test
    void testUpdate(){

        User user = new User();
        user.setUsername("IXIX");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,7L);
        int update = userMapper.update(user, queryWrapper);

        log.info("update ========>" + update);


    }


    @Test
    void testMD5(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String salt = uuid.substring(0,5);
        String key = "ffff";
        String digest = DigestUtils.md5DigestAsHex((key + salt).getBytes());
        log.info(digest);
    }




}
