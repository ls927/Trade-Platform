package com.platform.wetrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private Date createTime;


    public User(){

    }

    public User(String username,String password,Date createTime,String salt){
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.salt = salt;
    }


}
