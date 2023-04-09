package com.platform.wetrade.controller;

import com.platform.wetrade.dto.goods.GoodsDetailsDTO;
import com.platform.wetrade.dto.goods.GoodsItemDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.Category;
import com.platform.wetrade.params.BidGoodsParams;
import com.platform.wetrade.service.GoodsService;
import com.platform.wetrade.utils.FileUtil;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserHolder holder;
    @Autowired
    private FileUtil fileUtil;
    @Value("${img.upload.path}")
    private String uploadPath;

    @GetMapping("/goods/show")
    public Result showGoods(
            @RequestParam("currentPage")int currentPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam(value = "keywords",defaultValue = "")String keywords,
            @RequestParam("order")int order,
            @RequestParam("category")int category
    ){


        Map<String, Object> resMap =
                goodsService.getShowGoods(
                        currentPage,
                        pageSize,
                        keywords,
                        order,
                        category
                );
        List<GoodsItemDTO> goodsList = (List<GoodsItemDTO>) resMap.get("goodsList");
        Long total = (Long) resMap.get("total");

        Result<List<GoodsItemDTO>> result = Result.success(goodsList);
        result.setOther("total",total);

        return result;

    }

    @GetMapping("/goods/categories")
    public Result goodsCategories(){
        List<Category> categories = goodsService.getGoodsCategories();
        return Result.success(categories);
    }

    @GetMapping("/goods/details/{goodsId}")
    public Result goodsDetails(@PathVariable("goodsId")long goodsId){

        if(goodsId < 0) return Result.fail(Result.WRONG_GOODS_ID);

        Map<String, Object> resMap = goodsService.getGoodsDetails(goodsId);
        if(!(boolean)resMap.get("success")){
            return Result.fail((String)resMap.get("message"));
        }

        GoodsDetailsDTO goodsDetails = (GoodsDetailsDTO) resMap.get("goodsDetailsDTO");
        return Result.success(goodsDetails);

    }

    @PostMapping("/goods/bid")
    public Result purchaseGoods(@RequestBody BidGoodsParams params){

        UserSimpleDTO userSimpleDTO = holder.get();
        if (userSimpleDTO == null){
            return Result.fail(Result.NOT_LOGIN);
        }

        Map<String, Object> resMap = goodsService.bidGoods(
                userSimpleDTO.getId(),
                params.getGoodsId(),
                params.getOfferPrice()
        );

        if(!(boolean)resMap.get("success")){
            return Result.fail((String)resMap.get("message"));
        }

        return Result.success(null,Result.BID_SUCCESS);


    }


    @PostMapping("/goods/putup")
    public Result putUpGoods(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") int category,
            @RequestParam("price") double price,
            @RequestParam("images") MultipartFile[] images
    ){

        UserSimpleDTO userSimpleDTO = holder.get();
        if(userSimpleDTO == null){
            return Result.fail(Result.NOT_LOGIN);
        }

        Map<String, Object> resMap = null;

        try {
            resMap = goodsService.addGoods(
                    userSimpleDTO.getId(),
                    name,
                    description,
                    category,
                    price,
                    images
            );
        } catch (IOException e) {
            return Result.fail(Result.UPLOAD_IMG_FAIL);
        }

        if(!(boolean)resMap.get("success")){
            return Result.fail((String)resMap.get("message"));
        }

        long goodsId = (long) resMap.get("goodsId");

        return Result.success(goodsId,Result.ADD_GOODS_SUCCESS);

    }


    @PostMapping("/goods/takedown/{goodsId}")
    public Result takeDownGoods(@PathVariable("goodsId")long goodsId){

        if(goodsId < 0){
            return Result.fail(Result.WRONG_GOODS_ID);
        }

        UserSimpleDTO userSimpleDTO = holder.get();
        if(userSimpleDTO == null){
            return Result.fail(Result.NOT_LOGIN);
        }

        Map<String, Object> resMap = goodsService.retireGoods(goodsId,userSimpleDTO.getId());
        if(!(boolean)resMap.get("success")){
            return Result.fail((String)resMap.get("message"));
        }

        return Result.success(null,Result.RETIRE_GOODS_SUCCESS);

    }


    @GetMapping("/img/{imgName}")
    public Result getImg(@PathVariable("imgName")String imgName, HttpServletResponse response){
        log.info("==========getImg==========");
        boolean success = fileUtil.getFile(imgName, response);
        if(!success){
            return Result.fail(Result.GET_IMG_FAIL);
        }
        return Result.success();
    }



}
