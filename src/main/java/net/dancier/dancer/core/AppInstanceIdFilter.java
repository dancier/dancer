package net.dancier.dancer.core;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppInstanceIdFilter extends OncePerRequestFilter {

    private final static String APP_INSTANCE_ID_HEADER_NAME = "X-App-Instance-Id";

    public final static String APP_INSTANCE_ID_CONTEXT_FIELD = "AppInstanceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            setAppInstanceId(request);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(APP_INSTANCE_ID_CONTEXT_FIELD);
        }
    }
    private void setAppInstanceId(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getHeader(APP_INSTANCE_ID_HEADER_NAME)!=null) {
            MDC.put(APP_INSTANCE_ID_CONTEXT_FIELD, httpServletRequest.getHeader(APP_INSTANCE_ID_HEADER_NAME));
        }
    }
}
