package com.platform.wetrade.utils;

import com.platform.wetrade.utils.constant.TradeProcess;

public class BuyerInfoTitle {

    public final static String in(int tradeProcess){

        String title;
        switch (tradeProcess){
            case TradeProcess.OFFER:
                title = "已出价!等待卖家确认";
                break;
            case TradeProcess.INVITE:
                title = "卖方发起交易邀约!";
                break;
            case TradeProcess.AGREEMENT:
                title = "交易约定达成!待交易";
                break;
            case TradeProcess.TRANSACTION:
                title = "交易中";
                break;
            case TradeProcess.RETURN_REQUEST:
                title = "已申请退货!待卖家确认";
                break;
            case TradeProcess.APPROVAL:
                title = "卖家发送退货邀约!";
                break;
            case TradeProcess.RETURN_AGREEMENT:
                title = "退货约定达成!待退货";
                break;
            case TradeProcess.RETURN_COMPLETION:
                title = "退货完成";
                break;
            case TradeProcess.CANCELLATION:
                title = "交易已取消";
                break;
            default:
                title = "";
                break;
        }

        return title;
    }

}
