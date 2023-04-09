package com.platform.wetrade.dto.goods;

import com.platform.wetrade.dto.trade.TradeInfoSimpleDTO;
import com.platform.wetrade.entity.Goods;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;


@Data
public class GoodsDetailsDTO {

    private long id;

    private List<String> imgSrcs;//

    private String name;

    private double price;

    private String putUpTime;

    private long sellerId;

    private String seller;//

    private int status;

    private List<TradeInfoSimpleDTO> associations;//

    private String description;

    public GoodsDetailsDTO(){}

    public GoodsDetailsDTO(Goods goods){
        this.id = goods.getId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.status = goods.getStatus();
        this.sellerId = goods.getSellerId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.putUpTime = format.format(goods.getPutUpTime());
    }

}
