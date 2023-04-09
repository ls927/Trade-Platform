package com.platform.wetrade.dto.goods;

import com.platform.wetrade.entity.Goods;
import lombok.Data;

@Data
public class GoodsSimpleDTO {

    private long id;

    private String name;

    private int status;

    public GoodsSimpleDTO(Goods goods){
        this.id = goods.getId();
        this.name = goods.getName();
        this.status = goods.getStatus();
    }

}
