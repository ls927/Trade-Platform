package com.platform.wetrade.utils;

import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    private ThreadLocal<UserSimpleDTO> local = new ThreadLocal<>();

    public void set(UserSimpleDTO user){
        local.set(user);
    }

    public UserSimpleDTO get(){
        return local.get();
    }

    public void remove(){
        local.remove();
    }

}
