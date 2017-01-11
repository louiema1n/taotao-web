package com.taotao.web.intercepters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.util.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserLoginService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginIntercepter implements HandlerInterceptor {
    
    @Autowired
    private UserLoginService userLoginService;
    
    private static final String COOKIE_NAME = "TT_COOKIE";
    
    @Value("${TAOTAO_SSO_URL}")
    private String TAOTAO_SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        // 获取token
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            // 未登录,跳转到登陆页面
            response.sendRedirect(TAOTAO_SSO_URL + "/user/login.html");
            return false;
        }
        // token存在,验证redis中是否存在
        User user = this.userLoginService.queryByToken(token);
        if (user == null) {
            // 登录超时,跳转至登录页面
            response.sendRedirect(TAOTAO_SSO_URL + "/user/login.html");
            return false;
        }
        // 登录成功,将user放入ThreadLocal(方便controller和service获取)
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // 渲染完成后,清空本地线程中的user
        UserThreadLocal.set(null);
    }

}
