package com.platform.wetrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods_img")
public class GoodsImg {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String src;

    private Long goodsId;


    public GoodsImg(){

    }

    public GoodsImg(String src,Long goodsId){
        this.src = src;
        this.goodsId = goodsId;
    }
}
