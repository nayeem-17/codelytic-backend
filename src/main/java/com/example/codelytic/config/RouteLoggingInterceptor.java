package com.example.codelytic.config;

import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        request.setAttribute("stopWatch", stopWatch);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        StopWatch stopWatch = (StopWatch) request.getAttribute("stopWatch");
        stopWatch.stop();

        String route = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        if (route.startsWith("/swagger-ui")
                ||
                route.startsWith("/api-docs"))
            return;
        long executionTime = stopWatch.getTotalTimeMillis();
        String logMessage = String.format("{'route': '%s', 'method': '%s', 'status': %d, 'executionTime': %d}",
                route, method, status, executionTime);
        log.trace(logMessage);
        // System.out.println(logMessage);
    }
}
