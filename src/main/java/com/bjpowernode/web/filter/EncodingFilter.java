package com.bjpowernode.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中文参数乱码
        req.setCharacterEncoding("utf-8");

        //处理响应的参数乱码
        resp.setContentType("text/html;charset=utf-8");

        chain.doFilter(req, resp);
    }

}
