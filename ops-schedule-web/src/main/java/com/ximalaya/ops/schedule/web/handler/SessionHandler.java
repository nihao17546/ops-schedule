package com.ximalaya.ops.schedule.web.handler;

import com.alibaba.fastjson.JSON;
import com.ximalaya.ops.schedule.web.model.result.JSONResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nihao on 17/8/17.
 */
public class SessionHandler implements HandlerInterceptor {
    /**
     * 以JSON格式输出
     * @param response
     */
    protected void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSON.toJSONString(responseObject));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object obj = request.getSession().getAttribute("user");
        if(obj == null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                JSONResult jsonResult=new JSONResult();
                jsonResult.setCode(500);
                jsonResult.setMessage("未登录或登录已超时");
                responseOutWithJson(response, jsonResult);
            }
            else{
                response.sendRedirect("/ops-schedule-web/page/login");
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
