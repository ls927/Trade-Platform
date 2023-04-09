package com.platform.wetrade.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {

    public static final String INVALID_INPUT = "非法输入！用户名和密码不能为空";
    public static final String USERNAME_ALREADY_EXIT = "用户名已存在！";
    public static final String WRONG_USERNAME = "用户不存在！";
    public static final String WRONG_PASSWORD = "密码错误！";
    public static final String WRONG_GOODS_ID = "商品 id 错误！";
    public static final String GOODS_NOT_EXITS = "商品不存在！";
    public static final String NOT_LOGIN = "请先登录！";
    public static final String WRONG_PRICE = "价格错误！";
    public static final String GOODS_RETIRED= "商品已下架！";
    public static final String GOODS_ALREADY_SOLD = "商品已被交易！";
    public static final String GOODS_IN_TRADE = "商品正在交易中！请取消交易后下架商品！";
    public static final String GOODS_NOT_IN_INTERESTED = "商品正在交易中！请取消交易后接受报价！";
    public static final String ALREADY_IN_TRADE = "您正处在交易中，不能重复出价！请先取消交易后再重新出价";
    public static final String NO_IMAGE_FOUND = "请上传商品图片！";
    public static final String IMAGE_EMPTY = "商品图片不能为空文件！";
    public static final String WRONG_IMG_FORMAT = "商品图片格式错误！";
    public static final String UPLOAD_IMG_FAIL = "商品图片上传失败！";
    public static final String GET_IMG_FAIL = "商品图片获取失败！";
    public static final String BID_NOT_PERMIT = "不能购买自己的商品！";
    public static final String RETIRE_NOT_PERMIT = "没有权限下架此商品！";
    public static final String ASS_NOT_EXITS = "交易不存在！";
    public static final String ASS_ALREADY_CANCEL = "交易已取消！";
    public static final String ASS_INVALID_OPS = "非法交易操作！";
    public static final String ASS_OPS_NOT_PERMIT = "无交易操作权限！";

    public static final String REGISTER_SUCCESS = "注册成功！跳转至登录界面";
    public static final String BID_SUCCESS = "出价成功！跳转至我的购买";
    public static final String LOGOUT_SUCCESS = "已退出！";
    public static final String ADD_GOODS_SUCCESS = "上架成功！跳转至我的出售";
    public static final String RETIRE_GOODS_SUCCESS = "下架成功！";

    private boolean success;
    private T data;
    private String message;
    private Map<String,Object> map = new HashMap<>();


    public static <T> Result<T> success(){
        Result result = new Result<>();
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> success(T data){
        Result result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data, String message){
        Result result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail(){
        Result result = new Result<>();
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> fail(String message){
        Result result = new Result<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public void setOther(String name,Object value){
        map.put(name,value);
    }


    public String toJSONString(Result result){
        return JSON.toJSONString(result);
    }






}
