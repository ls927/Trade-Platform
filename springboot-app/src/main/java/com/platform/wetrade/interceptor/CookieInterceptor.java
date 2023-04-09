package com.platform.wetrade.interceptor;

import com.platform.wetrade.dto.user.UserSimpleDTO;
import com.platform.wetrade.service.UserService;
import com.platform.wetrade.utils.CookieUtil;
import com.platform.wetrade.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieInterceptor implements HandlerInterceptor {


    @Autowired
    private UserHolder holder;
    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String value = CookieUtil.getCookieValue(request, CookieUtil.TOKEN);
        if(value != null && value != ""){
            Long userId = Long.valueOf(value);
            UserSimpleDTO userSimpleDTO = userService.getUserSimpleById(userId);
            holder.set(userSimpleDTO);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(holder.get() != null)
            holder.remove();
    }
}
