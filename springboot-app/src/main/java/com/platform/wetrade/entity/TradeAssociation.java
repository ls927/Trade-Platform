package com.platform.wetrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("association")
public class TradeAssociation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private Long sellerId;

    private Long buyerId;

    private double price;

    private String content = "";//

    private int process;

    private Date date;

    public TradeAssociation(){}

    public TradeAssociation(
            long goodsId,
            long sellerId,
            long buyerId,
            double price,
            int process)
    {
        this.goodsId = goodsId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.price = price;
        this.process = process;
        this.date = new Date();
    }

}
