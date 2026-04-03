package com.form_builder.Submission_Service.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
public class FeignConfig {
    private final ObjectProvider<FeignHttpMessageConverters> messageConverters;

    public FeignConfig(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    RequestInterceptor authForwardingInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) {
                return;
            }
            HttpServletRequest request = attrs.getRequest();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.hasText(authorization)) {
                requestTemplate.header("Authorization", authorization);
            }
        };
    }

    @Bean
    Encoder feignEncoder() {
        return new SpringEncoder(messageConverters);
    }

    @Bean
    Decoder feignDecoder() {
        return new SpringDecoder(messageConverters);
    }
}
