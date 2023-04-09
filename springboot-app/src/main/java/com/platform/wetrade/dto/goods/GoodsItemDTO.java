package com.platform.wetrade.dto.goods;

import com.platform.wetrade.entity.Goods;
import lombok.Data;

@Data
public class GoodsItemDTO {

    private long id;

    private String name;

    private String description;

    private double price;

    private String previewImgSrc;

    private int status;

    public GoodsItemDTO(){

    }

    public GoodsItemDTO(Goods goods){
        this.id = goods.getId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.status = goods.getStatus();
    }

}
