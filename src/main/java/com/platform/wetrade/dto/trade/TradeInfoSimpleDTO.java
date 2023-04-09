package com.platform.wetrade.dto.trade;

import com.platform.wetrade.entity.TradeAssociation;
import lombok.Data;

@Data
public class TradeInfoSimpleDTO {


    private String buyer;

    private double price;

    public TradeInfoSimpleDTO(){}

    public TradeInfoSimpleDTO(TradeAssociation tradeAssociation){
        this.price = tradeAssociation.getPrice();
    }


}
