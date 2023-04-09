package com.platform.wetrade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.wetrade.entity.TradeAssociation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeMapper extends BaseMapper<TradeAssociation> {
}
