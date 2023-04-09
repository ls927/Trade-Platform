package com.platform.wetrade.interceptor;

import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.utils.Result;
import com.platform.wetrade.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    UserHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserSimpleDTO userSimpleDTO = holder.get();
        if(userSimpleDTO == null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String result = Result.fail(Result.NOT_LOGIN).toString();
            response.getWriter().write(result);
            return false;
        }
        return true;
    }
}
