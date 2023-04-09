package com.platform.wetrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("goods")
public class Goods {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private double price;

    private String description;

    private int status;

    private int category;

    private long sellerId;

    private long buyerId;

    private Date putUpTime;

    public Goods(){}

    public Goods(
            String name,
            double price,
            String description,
            int status,
            int category,
            long sellerId,
            Date putUpTime
    ){
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.category = category;
        this.sellerId = sellerId;
        this.putUpTime = putUpTime;
        this.buyerId = -1;
    }


}
