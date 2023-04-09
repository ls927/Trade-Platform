package com.platform.wetrade.service;

import com.platform.wetrade.dto.trade.TradeInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.params.TradeOpParams;

import java.util.List;
import java.util.Map;


public interface TradeService {

    List<TradeInfoDTO> getBuying(UserSimpleDTO buyer);


    Map<String,Object> getSelling(UserSimpleDTO seller, int currentPage, int pageSize);

    Map<String,Object> getSellingByGoodsId(UserSimpleDTO seller, long goodsId);

    List<TradeInfoDTO> getRecords(UserSimpleDTO user);

    List<TradeInfoDTO> getReturningRequests(UserSimpleDTO user);

    List<TradeInfoDTO> getReturningMessages(UserSimpleDTO user);

    Map<String,Object> trade(TradeOpParams params, long userId);
}
