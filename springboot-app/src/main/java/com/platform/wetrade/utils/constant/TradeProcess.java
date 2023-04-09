package com.platform.wetrade.utils.constant;

// 交易进度
public class TradeProcess {

    // 1. 买家出价
    public static final int OFFER = 0;

    // 2. 卖家邀约
    public static final int INVITE = 1;

    // 3. 买家同意，约定达成
    public static final int AGREEMENT = 2;

    // 4. 交易中
    public static final int TRANSACTION = 3;

    // 5. 交易完成
    public static final int COMPLETION = 4;

    // 6. 买家发出退货申请
    public static final int RETURN_REQUEST = 5;

    // 7. 卖家同意退货申请
    public static final int APPROVAL = 6;

    // 8. 退货约定达成，退货中
    public static final int RETURN_AGREEMENT = 7;

    // 9. 退货完成
    public static final int RETURN_COMPLETION = 8;

    // 0. 交易取消
    public static final int CANCELLATION = -1;

}
