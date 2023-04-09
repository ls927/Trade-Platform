package com.platform.wetrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.wetrade.dao.TradeMapper;
import com.platform.wetrade.dao.UserMapper;
import com.platform.wetrade.dto.user.UserInfoDTO;
import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.entity.TradeAssociation;
import com.platform.wetrade.entity.User;
import com.platform.wetrade.service.UserService;
import com.platform.wetrade.utils.CookieUtil;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.UserHolder;
import com.platform.wetrade.utils.constant.PasswordUtils;
import com.platform.wetrade.utils.constant.TradeProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TradeMapper tradeMapper;



    @Override
    public UserSimpleDTO getUserSimpleById(Long userId) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null)
            return null;
        return new UserSimpleDTO(user);

    }


    @Override
    public UserInfoDTO getUserInfo(UserSimpleDTO userSimpleDTO) {

        long id = userSimpleDTO.getId();
        UserInfoDTO userInfoDTO = new UserInfoDTO(userSimpleDTO);

        LambdaQueryWrapper<TradeAssociation> tradeQueryWrapper = new LambdaQueryWrapper<>();
        tradeQueryWrapper.eq(TradeAssociation::getBuyerId,id)
                .in(TradeAssociation::getProcess,TradeProcess.INVITE,TradeProcess.TRANSACTION);
        Long buyingOpsCount = tradeMapper.selectCount(tradeQueryWrapper);
        tradeQueryWrapper.clear();

        tradeQueryWrapper.eq(TradeAssociation::getSellerId,id)
                .in(TradeAssociation::getProcess,TradeProcess.OFFER,TradeProcess.AGREEMENT);
        Long sellingOpsCount = tradeMapper.selectCount(tradeQueryWrapper);
        tradeQueryWrapper.clear();

        tradeQueryWrapper.eq(TradeAssociation::getBuyerId,id)
                .in(TradeAssociation::getProcess,TradeProcess.APPROVAL,TradeProcess.RETURN_AGREEMENT);
        Long returningReqsCount = tradeMapper.selectCount(tradeQueryWrapper);
        tradeQueryWrapper.clear();

        tradeQueryWrapper.eq(TradeAssociation::getSellerId,id)
                .eq(TradeAssociation::getProcess,TradeProcess.RETURN_REQUEST);
        Long returningMsgsCount = tradeMapper.selectCount(tradeQueryWrapper);

        userInfoDTO.setOpsCount(buyingOpsCount,sellingOpsCount,returningReqsCount,returningMsgsCount);

        return userInfoDTO;
    }

    @Override
    public Map<String,Object> addUser(String username, String password) {

        Map<String,Object> res = new HashMap<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        Long count = userMapper.selectCount(wrapper);
        if(count > 0){
             res.put("success",false);
             res.put("message",Result.USERNAME_ALREADY_EXIT);
             return  res;
        }

        String salt = PasswordUtils.getSalt();
        String passwordCoded = PasswordUtils.encryption(password, salt);
        userMapper.insert(new User(username, passwordCoded, new Date(), salt));

        res.put("success",true);
        return res;
    }


    @Override
    public Map<String, Object> checkUser(String username, String password) {

        Map<String,Object> res = new HashMap<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);
        if(user == null){
            res.put("success",false);
            res.put("message",Result.WRONG_USERNAME);
            return res;
        }
        String passwordCoded = PasswordUtils.encryption(password, user.getSalt());
        if(!passwordCoded.equals(user.getPassword())){
            res.put("success",false);
            res.put("message",Result.WRONG_PASSWORD);
            return res;
        }

        res.put("success",true);
        res.put("userId",user.getId());
        return res;
    }

}
