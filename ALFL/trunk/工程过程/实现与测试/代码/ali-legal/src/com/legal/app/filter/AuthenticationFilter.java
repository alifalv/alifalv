package com.legal.app.filter;

import com.legal.app.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    SessionUtils sessionUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        sessionUtils = (SessionUtils) wac.getBean("sessionUtils");
    }

    @SuppressWarnings("unused")
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String resourceId = (String) httpRequest.getParameter("resourceId");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //empty implement
    }
}

