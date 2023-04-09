package com.platform.wetrade.dto.goods;

import com.platform.wetrade.entity.Goods;
import lombok.Data;

@Data
public class SellGoodsDTO {

    private long id;

    String name;

    private String description;

    private double price;

    private String previewImgSrc;

    private int status;

    private long opsCount;

    public SellGoodsDTO(){

    }

    public SellGoodsDTO(Goods goods){
        this.id = goods.getId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.status = goods.getStatus();
    }

}
