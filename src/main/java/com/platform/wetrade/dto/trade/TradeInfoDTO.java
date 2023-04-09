package com.platform.wetrade.dto.trade;

import com.platform.wetrade.dto.goods.GoodsSimpleDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.TradeAssociation;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class TradeInfoDTO {

    private long associationId;

    private String date;

    private String title;

    private GoodsSimpleDTO goods;

    private UserSimpleDTO buyer;

    private UserSimpleDTO seller;

    private double price;

    private String content;

    private int type; // 同 association.process

    public TradeInfoDTO(){}

    public TradeInfoDTO(TradeAssociation association){
        this.associationId = association.getId();
        this.price = association.getPrice();
        this.content = association.getContent();
        this.type = association.getProcess();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = format.format(association.getDate());
    }

}
