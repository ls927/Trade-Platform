package com.platform.wetrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.wetrade.dao.GoodsImgMapper;
import com.platform.wetrade.dao.GoodsMapper;
import com.platform.wetrade.dao.TradeMapper;
import com.platform.wetrade.dao.UserMapper;
import com.platform.wetrade.dto.goods.GoodsSimpleDTO;
import com.platform.wetrade.dto.goods.SellGoodsDTO;
import com.platform.wetrade.dto.trade.TradeInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.Goods;
import com.platform.wetrade.entity.GoodsImg;
import com.platform.wetrade.entity.TradeAssociation;
import com.platform.wetrade.entity.User;
import com.platform.wetrade.params.TradeOpParams;
import com.platform.wetrade.service.TradeService;
import com.platform.wetrade.utils.BuyerInfoTitle;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.SellerInfoTitle;
import com.platform.wetrade.utils.constant.GoodsStatus;
import com.platform.wetrade.utils.constant.TradeInfoContent;
import com.platform.wetrade.utils.constant.TradeProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;

    @Override
    public List<TradeInfoDTO> getBuying(UserSimpleDTO buyer) {


        List<TradeAssociation> tradeAssociations = tradeMapper.selectList(
                new LambdaQueryWrapper<TradeAssociation>()
                        .eq(TradeAssociation::getBuyerId, buyer.getId())
                        .in(TradeAssociation::getProcess,
                                TradeProcess.OFFER,
                                TradeProcess.INVITE,
                                TradeProcess.AGREEMENT,
                                TradeProcess.TRANSACTION
                        )
        );

        List<TradeInfoDTO> tradeInfoDTOs = new ArrayList<>();
        for (TradeAssociation tradeAss : tradeAssociations) {
            TradeInfoDTO tradeInfoDTO = new TradeInfoDTO(tradeAss);

            Long goodsId = tradeAss.getGoodsId();


            Goods goods = goodsMapper.selectOne
                    (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));
            tradeInfoDTO.setGoods(new GoodsSimpleDTO(goods));


            User sellerSelect = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, goods.getSellerId()));
            tradeInfoDTO.setSeller(new UserSimpleDTO(sellerSelect));

            long buyerId = goods.getBuyerId();
            if(buyerId != -1){
                User buyerSelect = userMapper.selectOne
                        (new LambdaQueryWrapper<User>().eq(User::getId, buyerId));
                tradeInfoDTO.setBuyer(new UserSimpleDTO(buyerSelect));
            }else{
                UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
                userSimpleDTO.setId(buyerId);
                tradeInfoDTO.setBuyer(userSimpleDTO);
            }


            tradeInfoDTO.setTitle(BuyerInfoTitle.in(tradeAss.getProcess()));

            tradeInfoDTOs.add(tradeInfoDTO);
        }

        return tradeInfoDTOs;
    }

    @Override
    public Map<String,Object> getSelling(UserSimpleDTO seller, int currentPage, int pageSize) {
        Map<String,Object> res = new HashMap<>();

        LambdaQueryWrapper<Goods> wrapper =
                new LambdaQueryWrapper<Goods>()
                .eq(Goods::getSellerId, seller.getId())
                .in(Goods::getStatus,
                        GoodsStatus.DISPLAY,
                        GoodsStatus.INTERESTED,
                        GoodsStatus.LOCKED
                );
        Page<Goods> page = new Page<>(currentPage,pageSize);
        Page<Goods> goodsPage = goodsMapper.selectPage(page, wrapper);
        List<Goods> goodsList = goodsPage.getRecords();
        long total = goodsPage.getTotal();

        List<SellGoodsDTO> sellGoodsDTOList = new ArrayList<>();

        for (Goods goods : goodsList) {
            SellGoodsDTO sellGoodsDTO = new SellGoodsDTO(goods);
            Long goodsId = goods.getId();
            List<GoodsImg> goodsImgs = goodsImgMapper.selectList
                    (new LambdaQueryWrapper<GoodsImg>().eq(GoodsImg::getGoodsId, goodsId));
            if(goodsImgs != null && !goodsImgs.isEmpty()){
                sellGoodsDTO.setPreviewImgSrc(goodsImgs.get(0).getSrc());
            }
            Long opsCount = tradeMapper.selectCount
                    (new LambdaQueryWrapper<TradeAssociation>()
                            .eq(TradeAssociation::getGoodsId, goodsId)
                            .in(TradeAssociation::getProcess,
                                    TradeProcess.OFFER,
                                    TradeProcess.AGREEMENT
                            )
                    );
            sellGoodsDTO.setOpsCount(opsCount);

            sellGoodsDTOList.add(sellGoodsDTO);
        }

        res.put("sellGoodsDTOList",sellGoodsDTOList);
        res.put("total",total);

        return res;
    }

    @Override
    public Map<String,Object> getSellingByGoodsId(UserSimpleDTO seller, long goodsId) {

        HashMap<String, Object> res = new HashMap<>();
        if(goodsId < 0){
            res.put("success",false);
            res.put("message", Result.WRONG_GOODS_ID);
            return res;
        }
        Goods goods = goodsMapper.selectOne
                (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));
        if(goods == null){
            res.put("success",false);
            res.put("message", Result.GOODS_NOT_EXITS);
            return res;
        }

        GoodsSimpleDTO goodsSimpleDTO = new GoodsSimpleDTO(goods);
        res.put("success",true);
        res.put("goods",goodsSimpleDTO);
        List<TradeInfoDTO> sellInfos = new ArrayList<>();
        if(goods.getStatus() == GoodsStatus.DISPLAY){
            res.put("sellInfos",sellInfos);
            return res;
        }
        // 当商品状态不为 架上 时
        List<TradeAssociation> associations = tradeMapper.selectList(
                new LambdaQueryWrapper<TradeAssociation>()
                        .eq(TradeAssociation::getGoodsId, goodsId)
                        .in(TradeAssociation::getProcess,
                                TradeProcess.OFFER,
                                TradeProcess.INVITE,
                                TradeProcess.AGREEMENT,
                                TradeProcess.TRANSACTION,
                                TradeProcess.COMPLETION
                        )
        );

        for (TradeAssociation tradeAss : associations){

            TradeInfoDTO tradeInfoDTO = new TradeInfoDTO(tradeAss);

            Long buyerId = tradeAss.getBuyerId();
            User user = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, buyerId));
            tradeInfoDTO.setBuyer(new UserSimpleDTO(user));
            tradeInfoDTO.setTitle(SellerInfoTitle.in(tradeAss.getProcess()));

            sellInfos.add(tradeInfoDTO);
        }

        res.put("sellInfos",sellInfos);
        return res;
    }

    @Override
    public List<TradeInfoDTO> getRecords(UserSimpleDTO user) {

        long userId = user.getId();

        LambdaQueryWrapper<TradeAssociation> wrapper =
                new LambdaQueryWrapper<TradeAssociation>()
                .eq(TradeAssociation::getSellerId, userId)
                .in(
                        TradeAssociation::getProcess,
                        TradeProcess.CANCELLATION,
                        TradeProcess.COMPLETION,
                        TradeProcess.RETURN_COMPLETION
                )
                .or()
                .eq(TradeAssociation::getBuyerId, userId)
                .in(
                        TradeAssociation::getProcess,
                        TradeProcess.CANCELLATION,
                        TradeProcess.COMPLETION,
                        TradeProcess.RETURN_COMPLETION
                );
        List<TradeAssociation> tradeAssociations = tradeMapper.selectList(wrapper);

        List<TradeInfoDTO> tradeInfoDTOs = new ArrayList<>();
        for (TradeAssociation tradeAss : tradeAssociations) {
            TradeInfoDTO tradeInfoDTO = new TradeInfoDTO(tradeAss);

            Long goodsId = tradeAss.getGoodsId();
            Goods goods = goodsMapper.selectOne
                    (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));
            tradeInfoDTO.setGoods(new GoodsSimpleDTO(goods));

            long sellerId = tradeAss.getSellerId();
            long buyerId = tradeAss.getBuyerId();
            User seller = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, sellerId));
            User buyer = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, buyerId));
            tradeInfoDTO.setSeller(new UserSimpleDTO(seller));

            tradeInfoDTO.setBuyer(new UserSimpleDTO(buyer));


            if(sellerId == userId){
                tradeInfoDTO.setTitle(SellerInfoTitle.in(tradeAss.getProcess()));
            }else{
                tradeInfoDTO.setTitle(BuyerInfoTitle.in(tradeAss.getProcess()));
            }

            tradeInfoDTOs.add(tradeInfoDTO);

        }

        return tradeInfoDTOs;
    }

    @Override
    public List<TradeInfoDTO> getReturningRequests(UserSimpleDTO user) {

        long userId = user.getId();

        LambdaQueryWrapper<TradeAssociation> associationWrapper = new LambdaQueryWrapper<TradeAssociation>()
                .eq(TradeAssociation::getBuyerId, userId)
                .in(
                        TradeAssociation::getProcess,
                        TradeProcess.RETURN_REQUEST,
                        TradeProcess.APPROVAL,
                        TradeProcess.RETURN_AGREEMENT
                );
        List<TradeAssociation> tradeAssociations = tradeMapper.selectList(associationWrapper);
        List<TradeInfoDTO> tradeInfoDTOs = new ArrayList<>();
        for (TradeAssociation association : tradeAssociations) {

            TradeInfoDTO tradeInfoDTO = new TradeInfoDTO(association);

            Long goodsId = association.getGoodsId();
            Goods goods = goodsMapper.selectOne
                    (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));
            tradeInfoDTO.setGoods(new GoodsSimpleDTO(goods));

            User seller = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, goods.getSellerId()));
            tradeInfoDTO.setSeller(new UserSimpleDTO(seller));

            tradeInfoDTO.setTitle(BuyerInfoTitle.in(association.getProcess()));

            tradeInfoDTOs.add(tradeInfoDTO);
        }

        return  tradeInfoDTOs;

    }

    @Override
    public List<TradeInfoDTO> getReturningMessages(UserSimpleDTO user) {

        long userId = user.getId();

        LambdaQueryWrapper<TradeAssociation> associationWrapper = new LambdaQueryWrapper<TradeAssociation>()
                .eq(TradeAssociation::getSellerId, userId)
                .in(
                        TradeAssociation::getProcess,
                        TradeProcess.RETURN_REQUEST,
                        TradeProcess.APPROVAL,
                        TradeProcess.RETURN_AGREEMENT
                );
        List<TradeAssociation> tradeAssociations = tradeMapper.selectList(associationWrapper);
        List<TradeInfoDTO> tradeInfoDTOs = new ArrayList<>();
        for (TradeAssociation association : tradeAssociations) {

            TradeInfoDTO tradeInfoDTO = new TradeInfoDTO(association);

            Long goodsId = association.getGoodsId();
            Goods goods = goodsMapper.selectOne
                    (new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId));
            tradeInfoDTO.setGoods(new GoodsSimpleDTO(goods));

            User buyer = userMapper.selectOne
                    (new LambdaQueryWrapper<User>().eq(User::getId, goods.getBuyerId()));
            tradeInfoDTO.setBuyer(new UserSimpleDTO(buyer));

            tradeInfoDTO.setTitle(SellerInfoTitle.in(association.getProcess()));

            tradeInfoDTOs.add(tradeInfoDTO);
        }

        return  tradeInfoDTOs;

    }

    @Override
    public Map<String, Object> trade(TradeOpParams params, long userId) {

        long associationId = params.getAssociationId();
        int from = params.getFrom();
        int to = params.getTo();
        String content = params.getContent();

        Map<String, Object> res = new HashMap<>();

        // 排除异常情况
        if(
                from == TradeProcess.RETURN_COMPLETION ||
                (to != TradeProcess.CANCELLATION && from + 1 != to) ||
                (to == TradeProcess.CANCELLATION &&
                        (
                                from == TradeProcess.CANCELLATION ||
                                        from == TradeProcess.COMPLETION ||
                                        from == TradeProcess.APPROVAL ||
                                        from == TradeProcess.RETURN_AGREEMENT
                        )
                )
        ){
            res.put("success",false);
            res.put("message",Result.ASS_INVALID_OPS);
            return res;
        }

        LambdaQueryWrapper<TradeAssociation> eq =
                new LambdaQueryWrapper<TradeAssociation>()
                        .eq(TradeAssociation::getId, associationId);
        TradeAssociation tradeAssociation = tradeMapper.selectOne(eq);
        if (tradeAssociation == null){
            res.put("success",false);
            res.put("message",Result.ASS_NOT_EXITS);
            return res;
        }

        Long buyerId = tradeAssociation.getBuyerId();
        Long sellerId = tradeAssociation.getSellerId();
        if(userId != buyerId && userId != sellerId){
            res.put("success",false);
            res.put("message",Result.ASS_OPS_NOT_PERMIT);
            return res;
        }

        Long goodsId = tradeAssociation.getGoodsId();
        Goods goods = goodsMapper.selectOne
                (new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getId, goodsId)
                        .notIn(Goods::getStatus,GoodsStatus.RETIRED)
                );
        if(goods == null){
            res.put("success",false);
            res.put("message",Result.GOODS_NOT_EXITS);
            return res;
        }

        int goodsStatus = goods.getStatus();
        if(from == TradeProcess.OFFER &&
                to == TradeProcess.INVITE
                && goodsStatus != GoodsStatus.INTERESTED){

            res.put("success",false);
            res.put("message",Result.GOODS_NOT_IN_INTERESTED);
            return res;
        }


        // 通用修改部分
        if(tradeAssociation.getContent().equals("") || !content.equals("")){
            tradeAssociation.setContent(content);
        }
        tradeAssociation.setDate(new Date());
        tradeAssociation.setProcess(to);
        //

        // 正常情况下的操作
        if(to == TradeProcess.CANCELLATION){

            if(from == TradeProcess.RETURN_REQUEST){
                goods.setStatus(GoodsStatus.SOLD);
            }else{
                // from = 0, 1, 2, 3
                LambdaQueryWrapper<TradeAssociation> tradeAssWrapper = new LambdaQueryWrapper<>();
                tradeAssWrapper.eq(TradeAssociation::getGoodsId,goodsId)
                        .eq(TradeAssociation::getProcess,TradeProcess.OFFER)
                        .notIn(TradeAssociation::getId,associationId);
                List<TradeAssociation> tradeAssociations = tradeMapper.selectList(tradeAssWrapper);
                if(tradeAssociations == null || tradeAssociations.isEmpty()){
                    goods.setStatus(GoodsStatus.DISPLAY);
                }else{
                    goods.setStatus(GoodsStatus.INTERESTED);
                }
                goods.setBuyerId(-1);
            }

        }else{
            if(from == TradeProcess.OFFER){
                goods.setStatus(GoodsStatus.LOCKED);
                goods.setBuyerId(buyerId);
            }else if (from == TradeProcess.TRANSACTION ){
                goods.setStatus(goodsStatus + 1);
                LambdaQueryWrapper<TradeAssociation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TradeAssociation::getGoodsId,goodsId)
                        .notIn(TradeAssociation::getId,associationId);
                List<TradeAssociation> otherAss = tradeMapper.selectList(queryWrapper);
                if (otherAss != null && !otherAss.isEmpty()){
                    for (TradeAssociation ass: otherAss) {
                        ass.setProcess(TradeProcess.CANCELLATION);
                        ass.setContent(TradeInfoContent.GOODS_SOLD);
                        tradeMapper.updateById(ass);
                    }
                }
            }
            else if(from == TradeProcess.COMPLETION ||
                    from == TradeProcess.RETURN_AGREEMENT){
                goods.setStatus(goodsStatus + 1);
            }
            else{
                // from = 1, 2, 5, 6
                // do nothing
            }
        }
        goodsMapper.updateById(goods);
        tradeMapper.updateById(tradeAssociation);

        res.put("success",true);

        return res;
    }


}
