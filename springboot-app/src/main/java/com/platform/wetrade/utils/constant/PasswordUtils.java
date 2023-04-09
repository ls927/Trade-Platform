package com.platform.wetrade.utils.constant;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class PasswordUtils {

    public static String getSalt(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0,5);
    }


    public static String encryption(String rawPassword,String salt){
        return DigestUtils.md5DigestAsHex((rawPassword + salt).getBytes());
    }




}
