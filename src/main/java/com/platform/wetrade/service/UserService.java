package com.platform.wetrade.service;

import com.platform.wetrade.dto.user.UserInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;


import java.util.Map;

public interface UserService {

    UserInfoDTO getUserInfo(UserSimpleDTO userSimpleDTO);

    UserSimpleDTO getUserSimpleById(Long userId);

    Map<String,Object> addUser(String username,String password);

    Map<String,Object> checkUser(String username, String password);
}
