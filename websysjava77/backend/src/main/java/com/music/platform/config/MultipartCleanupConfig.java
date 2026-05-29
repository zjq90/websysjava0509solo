package com.music.platform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class MultipartCleanupConfig {

    @Bean
    public FilterRegistrationBean<MultipartCleanupFilter> multipartCleanupFilter() {
        FilterRegistrationBean<MultipartCleanupFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MultipartCleanupFilter());
        registration.addUrlPatterns("/api/music/*");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

    static class MultipartCleanupFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            try {
                filterChain.doFilter(request, response);
            } finally {
                cleanupMultipart(request);
            }
        }

        private void cleanupMultipart(HttpServletRequest request) {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Map<String, List<MultipartFile>> fileMap = multipartRequest.getMultiFileMap();
                for (Map.Entry<String, List<MultipartFile>> entry : fileMap.entrySet()) {
                    for (MultipartFile file : entry.getValue()) {
                        if (file != null && !file.isEmpty()) {
                            try {
                                file.getInputStream().close();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }
}
