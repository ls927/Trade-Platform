package com.platform.wetrade.service;

import com.platform.wetrade.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface GoodsService {

    Map<String,Object> getShowGoods(
            int currentPage,
            int pageSize,
            String keywords,
            int order,
            int category
    );

    List<Category> getGoodsCategories();

    Map<String,Object> getGoodsDetails(long goodsId);

    Map<String,Object> bidGoods(long buyerId, long goodsId, double offerPrice);

    Map<String,Object> addGoods(long userId,
                                String name,
                                String description,
                                int category,
                                double price,
                                MultipartFile[] images
    ) throws IOException;

    Map<String,Object> retireGoods(long goodsId,long userId);
}
