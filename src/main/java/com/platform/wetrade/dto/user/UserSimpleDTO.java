package com.platform.wetrade.dto.user;

import com.platform.wetrade.entity.User;
import lombok.Data;

@Data
public class UserSimpleDTO {

    private long id;

    private String username;

    private String noName = "暂无";


    public UserSimpleDTO(){
        this.username = noName;
    }

    public UserSimpleDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
    }



}
