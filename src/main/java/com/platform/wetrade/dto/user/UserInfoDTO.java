package com.platform.wetrade.dto.user;

import com.platform.wetrade.entity.User;
import lombok.Data;

@Data
public class UserInfoDTO {

    long id;

    String username;

    long buyingOpsCount;

    long sellingOpsCount;

    long returningReqsCount;

    long returningMsgsCount;

    public UserInfoDTO(){

    }

    public UserInfoDTO(UserSimpleDTO userSimpleDTO){
        this.id = userSimpleDTO.getId();
        this.username = userSimpleDTO.getUsername();
    }

    public void setOpsCount(long buyingOpsCount,long sellingOpsCount,long returningReqsCount,long returningMsgsCount){
        this.buyingOpsCount = buyingOpsCount;
        this.sellingOpsCount = sellingOpsCount;
        this.returningReqsCount = returningReqsCount;
        this.returningMsgsCount = returningMsgsCount;
    }

}
