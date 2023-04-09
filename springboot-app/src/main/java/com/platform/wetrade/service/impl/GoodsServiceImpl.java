package com.platform.wetrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.wetrade.dao.*;
import com.platform.wetrade.dto.goods.GoodsDetailsDTO;
import com.platform.wetrade.dto.goods.GoodsItemDTO;
import com.platform.wetrade.dto.trade.TradeInfoSimpleDTO;
import com.platform.wetrade.entity.*;
import com.platform.wetrade.service.GoodsService;
import com.platform.wetrade.utils.FileUtil;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.constant.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public Map<String, Object> getShowGoods(
            int currentPage,
            int pageSize,
            String keywords,
            int order,
            int category
    ) {

        HashMap<String,Object> res = new HashMap<>();


        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasLength(keywords), Goods::getName,keywords).or()
                .like(StringUtils.hasLength(keywords), Goods::getDescription,keywords);


        if(order == GoodsOrder.PRICE_ASC){
            wrapper.orderByAsc(Goods::getPrice);
        }else if(order == GoodsOrder.PRICE_DESC){
            wrapper.orderByDesc(Goods::getPrice);
        }else{
            wrapper.orderByDesc(Goods::getPutUpTime);
        }


        wrapper.eq(category != GoodsCategory.ALL,Goods::getCategory,category);
        wrapper.in(Goods::getStatus,
                GoodsStatus.DISPLAY,GoodsStatus.INTERESTED,GoodsStatus.LOCKED);

        Page<Goods> page = new Page<>(currentPage,pageSize);
        Page<Goods> goodsPage = goodsMapper.selectPage(page, wrapper);
        long total = goodsPage.getTotal();

        List<Goods> records = goodsPage.getRecords();
        List<GoodsItemDTO> goodsItemDTOS = toGoodsItemDTO(records);

        res.put("goodsList",goodsItemDTOS);
        res.put("total",total);

        return res;
    }

    @Override
    public List<Category> getGoodsCategories() {
        return categoryMapper.selectList(null);
    }

    @Override
    public Map<String,Object> getGoodsDetails(long goodsId) {

        Map<String,Object> res = new HashMap<>();

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId,goodsId);
        wrapper.in(
                Goods::getStatus,
                GoodsStatus.DISPLAY,
                GoodsStatus.INTERESTED,
                GoodsStatus.LOCKED
        );
        Goods goods = goodsMapper.selectOne(wrapper);
        if(goods == null){
            res.put("success",false);
            res.put("message", Result.GOODS_NOT_EXITS);
            return res;
        }

        GoodsDetailsDTO goodsDetailsDTO = toGoodsDetailsDTO(goods);
        res.put("success",true);
        res.put("goodsDetailsDTO",goodsDetailsDTO);

        return res;
    }

    @Override
    public Map<String, Object> bidGoods(long buyerId, long goodsId, double offerPrice) {

        Map<String, Object> res = new HashMap<>();
        if(offerPrice <= 0){
            res.put("success",false);
            res.put("message",Result.WRONG_PRICE);
        }

        Goods goods = goodsMapper.selectOne
                (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));

        // 检查是否存在该商品
        if(goods == null){
            res.put("success",false);
            res.put("message",Result.GOODS_NOT_EXITS);
            return res;
        }

        // 检查商品是否已下架
        if(goods.getStatus() == GoodsStatus.RETIRED){
            res.put("success",false);
            res.put("message",Result.GOODS_RETIRED);
            return res;
        }

        /**
         * @note 检查商品是否已交易完成（包括售后）。
         *
         * goodsStatus 和 tradeProcess 对应关系：
         *      goodsStatus >= GoodsStatus.SOLD
         *      tradeProcess >= TradeProcess.COMPLETION
         */
        if(goods.getStatus() >= GoodsStatus.SOLD){
            res.put("success",false);
            res.put("message",Result.GOODS_ALREADY_SOLD);
            return res;
        }

        // 检查是否允许购买
        if (goods.getSellerId() == buyerId){
            res.put("success",false);
            res.put("message",Result.BID_NOT_PERMIT);
            return res;
        }


        /**
         * @note 检查商品是否已经处在与 “我”（即 buyerId 对应的 user）的交易中
         * 注：也可以用另一种更简单方法：goods.buyerId == buyId，但是一样要查询
         * TradeAssociation，并作更新
         */
        LambdaQueryWrapper<TradeAssociation>
                associationQueryWrapper = new LambdaQueryWrapper<>();

        associationQueryWrapper.eq(TradeAssociation::getGoodsId,goodsId)
                               .eq(TradeAssociation::getBuyerId,buyerId)
                               .in(
                                       TradeAssociation::getProcess,
                                       TradeProcess.OFFER,
                                       TradeProcess.INVITE,
                                       TradeProcess.AGREEMENT,
                                       TradeProcess.TRANSACTION
                               );
        TradeAssociation association = tradeMapper.selectOne(associationQueryWrapper);

        // 若已处在与“我”的交易中
        if(association != null){

            res.put("success",false);
            res.put("message",Result.ALREADY_IN_TRADE);
            return res;

        }
        // 若该商品未处在与“我”的交易中
        else{

            TradeAssociation tradeAssociation = new TradeAssociation
                    (goodsId, goods.getSellerId(), buyerId, offerPrice, TradeProcess.OFFER);

            // 若在架上，无人意向，那么在建立 *交易关联* 的同时修改 *商品状态*。
            // 注意：此时商品的 buyerId 依然不变。
            tradeMapper.insert(tradeAssociation);

            if(goods.getStatus() == GoodsStatus.DISPLAY){
                goods.setStatus(GoodsStatus.INTERESTED);
                goodsMapper.updateById(goods);
            }
        }

        res.put("success",true);

        return res;
    }

    @Override
    public Map<String, Object> addGoods(
            long userId,
            String name,
            String description,
            int category,
            double price,
            MultipartFile[] images
    ) throws IOException {


        Map<String, Object> res = new HashMap<>();


        if(images.length == 0){
            res.put("success",false);
            res.put("message",Result.NO_IMAGE_FOUND);
            return res;
        }
        for (MultipartFile img : images) {
            if(img.isEmpty()){
                res.put("success",false);
                res.put("message",Result.IMAGE_EMPTY);
                return res;
            }
            if(!fileUtil.isValid(img)){
                res.put("success",false);
                res.put("message",Result.WRONG_IMG_FORMAT);
                return res;
            }
        }

        Goods goods = new Goods(
                name,
                price,
                description,
                GoodsStatus.DISPLAY,
                category,
                userId,
                new Date()
        );

        goodsMapper.insert(goods);

        for (MultipartFile img : images) {
            String imgSrc = fileUtil.upload(img);
            goodsImgMapper.insert(new GoodsImg(imgSrc,goods.getId()));
        }

        res.put("success",true);
        res.put("goodsId",goods.getId());

        return res;
    }

    @Override
    public Map<String, Object> retireGoods(long goodsId,long userId) {

        Map<String,Object> res = new HashMap<>();
        Goods goods = goodsMapper.selectOne
                (new LambdaQueryWrapper<Goods>().eq(Goods::getId,goodsId));

        if(goods == null){
            res.put("success",false);
            res.put("message",Result.GOODS_NOT_EXITS);
            return res;
        }

        if (goods.getSellerId() != userId){
            res.put("success",false);
            res.put("message",Result.RETIRE_NOT_PERMIT);
            return res;
        }

        if(goods.getStatus() >= GoodsStatus.LOCKED){
            res.put("success",false);
            res.put("message",Result.GOODS_IN_TRADE);
            return res;
        }

        //
        goods.setStatus(GoodsStatus.RETIRED);
        goods.setBuyerId(-1);
        goodsMapper.updateById(goods);
        List<TradeAssociation> associations = tradeMapper.selectList(
                new LambdaQueryWrapper<TradeAssociation>()
                        .eq(TradeAssociation::getGoodsId, goodsId)
        );
        for (TradeAssociation association: associations) {

            association.setContent(TradeInfoContent.GOODS_RETIRED);
            association.setProcess(TradeProcess.CANCELLATION);
            association.setDate(new Date());

            tradeMapper.updateById(association);
        }
        //


        res.put("success",true);
        return res;
    }


    private GoodsDetailsDTO toGoodsDetailsDTO(Goods goods) {

        GoodsDetailsDTO goodsDetailsDTO = new GoodsDetailsDTO(goods);

        int goodsStatus = goods.getStatus();
        Long goodsId = goods.getId();
        long sellerId = goods.getSellerId();

        String seller = userMapper.selectOne(new LambdaQueryWrapper<User>()
                                .eq(User::getId,sellerId)).getUsername();

        List<GoodsImg> goodsImgSrcs = goodsImgMapper.selectList(
                new LambdaQueryWrapper<GoodsImg>()
                .eq(GoodsImg::getGoodsId,goodsId));
        List<String> imgSrcs = new ArrayList<>();
        if(imgSrcs != null){
            for (GoodsImg img : goodsImgSrcs) {
                imgSrcs.add(img.getSrc());
            }
        }
        List<TradeInfoSimpleDTO> associations = new ArrayList<>();

        goodsDetailsDTO.setSeller(seller);
        goodsDetailsDTO.setImgSrcs(imgSrcs);
        goodsDetailsDTO.setAssociations(associations);

        if(goodsStatus != GoodsStatus.INTERESTED
                        && goodsStatus != GoodsStatus.LOCKED){
            return  goodsDetailsDTO;
        }

        LambdaQueryWrapper<TradeAssociation> wrapper = new LambdaQueryWrapper<>();

        /**
         * @note 对应关系
         * GoodsStatus.INTERESTED  =>  TradeProcess.OFFER
         * GoodsStatus.LOCKED  =>  TradeProcess.INVITE
         *                     =>  TradeProcess.AGREEMENT
         *                     =>  TradeProcess.TRANSACTION
         */
        if(goodsStatus == GoodsStatus.INTERESTED){

            wrapper.eq(TradeAssociation::getGoodsId, goodsId)
                    .eq(TradeAssociation::getProcess, TradeProcess.OFFER);

        }else if(goodsStatus == GoodsStatus.LOCKED){
            wrapper.eq(TradeAssociation::getGoodsId,goodsId)
                    .in(
                            TradeAssociation::getProcess,
                            TradeProcess.INVITE,
                            TradeProcess.AGREEMENT,
                            TradeProcess.TRANSACTION
                    );
        }
        List<TradeAssociation> tradeAssociations = tradeMapper.selectList(wrapper);
        List<TradeInfoSimpleDTO> simpleAssociations = goodsDetailsDTO.getAssociations();
        for (TradeAssociation tradeAssociation : tradeAssociations) {
            TradeInfoSimpleDTO tradeInfoSimpleDTO = new TradeInfoSimpleDTO(tradeAssociation);
            Long buyerId = tradeAssociation.getBuyerId();
            User user = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, buyerId));
            tradeInfoSimpleDTO.setBuyer(user.getUsername());

            simpleAssociations.add(tradeInfoSimpleDTO);
        }

        return goodsDetailsDTO;

    }

    private List<GoodsItemDTO> toGoodsItemDTO(List<Goods> goodsList) {

        List<GoodsItemDTO> goodsItemDTOS = new ArrayList<>();

        for (Goods goods : goodsList) {
            GoodsItemDTO goodsItemDTO = new GoodsItemDTO(goods);
            LambdaQueryWrapper<GoodsImg> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GoodsImg::getGoodsId,goods.getId());
            List<GoodsImg> goodsImgs = goodsImgMapper.selectList(wrapper);
            if(goodsImgs != null && !goodsImgs.isEmpty()){
                goodsItemDTO.setPreviewImgSrc(goodsImgs.get(0).getSrc());
            }
            goodsItemDTOS.add(goodsItemDTO);
        }

        return goodsItemDTOS;

    }
}
