package cn.com.eship.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("userInfo");
        if (user == null) {
            // 获取request返回页面到登录页
            response.sendRedirect("/system/loginPage");
            return false;
        } else {
            // 已登录，放行
            return true;
        }
    }
}
