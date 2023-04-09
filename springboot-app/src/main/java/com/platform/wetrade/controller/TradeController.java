package com.platform.wetrade.controller;

import com.platform.wetrade.dao.UserMapper;
import com.platform.wetrade.dto.goods.GoodsSimpleDTO;
import com.platform.wetrade.dto.goods.SellGoodsDTO;
import com.platform.wetrade.dto.trade.TradeInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.Goods;
import com.platform.wetrade.params.TradeOpParams;
import com.platform.wetrade.service.TradeService;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private UserHolder userHolder;

    @GetMapping("/trade/buying")
    public Result tradeBuying(){

        UserSimpleDTO userSimpleDTO = userHolder.get();

        List<TradeInfoDTO> buyInfos = tradeService.getBuying(userSimpleDTO);

        return Result.success(buyInfos);

    }


    @GetMapping("/trade/selling")
    public Result tradeSelling(@RequestParam("currentPage") int currentPage,
                               @RequestParam("pageSize") int pageSize
    ){

        UserSimpleDTO userSimpleDTO = userHolder.get();
        Map<String, Object> resMap = tradeService.getSelling(userSimpleDTO, currentPage, pageSize);
        List<SellGoodsDTO> sellGoodsDTOList = (List<SellGoodsDTO>) resMap.get("sellGoodsDTOList");
        Result<List<SellGoodsDTO>> result = Result.success(sellGoodsDTOList);
        result.setOther("total",(long)resMap.get("total"));
        return result;
    }


    @GetMapping("/trade/selling/{goodsId}")
    public Result tradeSellingGoods(@PathVariable("goodsId")long goodsId){

        UserSimpleDTO userSimpleDTO = userHolder.get();
        Map<String, Object> resMap = tradeService.getSellingByGoodsId(userSimpleDTO, goodsId);

        if(!(boolean)resMap.get("success")){
            return Result.fail((String)resMap.get("message"));
        }

        GoodsSimpleDTO goods = (GoodsSimpleDTO) resMap.get("goods");
        Result<GoodsSimpleDTO> result = Result.success(goods);
        result.setOther("sellInfos",(List)resMap.get("sellInfos"));

        return result;
    }


    @GetMapping("/trade/records")
    public Result tradeRecords(){
        UserSimpleDTO userSimpleDTO = userHolder.get();
        List<TradeInfoDTO> records = tradeService.getRecords(userSimpleDTO);
        return Result.success(records);
    }


    @GetMapping("/trade/returning/requests")
    public Result tradeReturningRequests(){
        UserSimpleDTO userSimpleDTO = userHolder.get();
        List<TradeInfoDTO> returningRequests = tradeService.getReturningRequests(userSimpleDTO);
        return Result.success(returningRequests);
    }


    @GetMapping("/trade/returning/messages")
    public Result tradeReturningMessages(){
        UserSimpleDTO userSimpleDTO = userHolder.get();
        List<TradeInfoDTO> returningMessages = tradeService.getReturningMessages(userSimpleDTO);
        return Result.success(returningMessages);
    }

    @PostMapping("/trade")
    public Result trade(@RequestBody TradeOpParams params){

        UserSimpleDTO userSimpleDTO = userHolder.get();
        Map<String, Object> resMap = tradeService.trade(params,userSimpleDTO.getId());

        if(!(boolean)resMap.get("success")){
            return Result.fail((String) resMap.get("message"));
        }

        return Result.success();
    }


}
