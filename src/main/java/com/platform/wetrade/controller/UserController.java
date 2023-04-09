package com.platform.wetrade.controller;

import com.platform.wetrade.params.LoginParams;
import com.platform.wetrade.dto.user.UserInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.service.UserService;
import com.platform.wetrade.utils.CookieUtil;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserHolder holder;

    @GetMapping("/user/info")
    public Result<UserInfoDTO> getInfo(HttpServletResponse response){

        UserSimpleDTO userSimpleDTO = holder.get();
        if(userSimpleDTO == null) return Result.fail();

        UserInfoDTO userInfo = userService.getUserInfo(userSimpleDTO);
        if(userInfo == null) return Result.fail();
        return Result.success(userInfo);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody LoginParams loginParams){

        String username = loginParams.getUsername();
        String password = loginParams.getPassword();
        if( !StringUtils.hasLength(username)
                || !StringUtils.hasLength(password)){
            return Result.fail(Result.INVALID_INPUT);
        }

        Map<String, Object> resMap = userService.addUser(username, password);
        if(!(boolean)resMap.get("success")){
            return Result.fail((String) resMap.get("message"));
        }

        return Result.success(null,Result.REGISTER_SUCCESS);

    }


    @PostMapping("/user/login")
    public Result login(@RequestBody LoginParams loginParams, HttpServletResponse response){

        String username = loginParams.getUsername();
        String password = loginParams.getPassword();


        if( !StringUtils.hasLength(username)
                || !StringUtils.hasLength(password)){

            return Result.fail(Result.INVALID_INPUT);
        }

        Map<String, Object> resMap = userService.checkUser(username, password);
        if(!(boolean)resMap.get("success")){
            return Result.fail((String) resMap.get("message"));
        }

        Long userId = (Long) resMap.get("userId");
        Cookie cookie = new Cookie(CookieUtil.TOKEN, String.valueOf(userId));
        cookie.setPath("/");
        cookie.setMaxAge(3*24*3600);
        response.addCookie(cookie);

        return Result.success();
    }


    @GetMapping("/user/logout")
    public Result logout(@CookieValue("token")String token,HttpServletResponse response){
        Cookie cookie = new Cookie(CookieUtil.TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return Result.success(null,Result.LOGOUT_SUCCESS);
    }



}

