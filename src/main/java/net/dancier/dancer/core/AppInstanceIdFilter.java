package net.dancier.dancer.core;

import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppInstanceIdFilter extends OncePerRequestFilter {

    private final String APP_INSTANCE_ID_HEADER_NAME = "X-App-Instance-Id";

    private final String APP_INSTANCE_ID_CONTEXT_FIELD = "AppInstanceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            setAppInstanceId(request);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("CorrelationId");
        }
    }
    private void setAppInstanceId(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getHeader(APP_INSTANCE_ID_HEADER_NAME)!=null) {
            MDC.put(APP_INSTANCE_ID_CONTEXT_FIELD, httpServletRequest.getHeader(APP_INSTANCE_ID_HEADER_NAME));
        }
    }
}
