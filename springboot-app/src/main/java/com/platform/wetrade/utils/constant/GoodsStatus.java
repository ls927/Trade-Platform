package com.platform.wetrade.utils.constant;

// 商品状态
public class GoodsStatus {

    // 架上
    public static final int DISPLAY = 0;

    // 意向：有人出价
    public static final int INTERESTED = 1;

    // 锁定：卖家已指定一名买家与其进行交易
    public static final int LOCKED = 2;

    // 交易完成
    public static final int SOLD = 3;

    // 退货处理中
    public static final int RETURNED = 4;

    // 退货完成
    public static final int REFUNDED = 5;

    // 下架
    public static final int RETIRED = -1;


}
