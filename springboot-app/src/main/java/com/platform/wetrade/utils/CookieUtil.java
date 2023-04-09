package com.platform.wetrade.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static final String TOKEN = "token";

    public static String getCookieValue(HttpServletRequest request,String name){

        String value = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)){
                    value = cookie.getValue();
                }
            }
        }


        return  value;
    }


}
