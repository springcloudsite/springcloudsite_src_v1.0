package com.scs.usermanagement.configuration;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.framework.core.exception.InternalServerErrorException;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * 跨服务请求异常再处理
 * 
 *
 * @version ScsDI v1.0
 * @author Sun Yunxu, 2018年5月25日
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    private Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private Marker marker = MarkerFactory.getMarker("FeignErrorDecoder");

    @SuppressWarnings("unchecked")
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 500) {
            String msg = null;
            try {
                String body = IOUtils.toString(response.body().asInputStream(), "utf-8");
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> error = null;
                if (StringUtils.isNotBlank(body)) {
                    error = mapper.readValue(body, Map.class);
                }
                if (error != null) {
                    msg = String.valueOf(error.get("errorCode"));
                }
            } catch (IOException e) {
                logger.error(marker, e.getMessage());
            }
            throw new InternalServerErrorException(msg);
        }
        return feign.FeignException.errorStatus(methodKey, response);
    }
}
