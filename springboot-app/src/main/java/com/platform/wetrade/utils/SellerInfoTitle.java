package com.platform.wetrade.utils;

import com.platform.wetrade.utils.constant.TradeProcess;

public class SellerInfoTitle {

    public final static String in(int tradeProcess){

        String title;
        switch (tradeProcess){
            case TradeProcess.OFFER:
                title = "买家出价!";
                break;
            case TradeProcess.INVITE:
                title = "交易已邀约!等待买家同意";
                break;
            case TradeProcess.AGREEMENT:
                title = "交易约定达成!待交易";
                break;
            case TradeProcess.TRANSACTION:
                title = "交易中，等待买家同意";
                break;
            case TradeProcess.COMPLETION:
                title = "交易完成!";
                break;
            case TradeProcess.RETURN_REQUEST:
                title = "买家申请退货!";
                break;
            case TradeProcess.APPROVAL:
                title = "已发送退货邀约!等待买家同意";
                break;
            case TradeProcess.RETURN_AGREEMENT:
                title = "退货约定达成!待退货";
                break;
            case TradeProcess.CANCELLATION:
                title = "交易已取消";
                break;
            case TradeProcess.RETURN_COMPLETION:
                title = "退货完成";
                break;
            default:
                title = "";
                break;
        }

        return title;
    }


}
