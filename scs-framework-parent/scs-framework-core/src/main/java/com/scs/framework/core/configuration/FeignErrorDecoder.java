/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.configuration;

import java.io.IOException;

import com.scs.framework.core.exception.BadRequestException;
import com.scs.framework.core.exception.ErrorMessage;

import feign.Response;
import feign.codec.ErrorDecoder;
//import feign.gson.GsonDecoder;
import feign.gson.GsonDecoder;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月22日
 */
public class FeignErrorDecoder implements ErrorDecoder {

    /**
     * @param arg0
     * @param arg1
     * @return
     * @see feign.codec.ErrorDecoder#decode(java.lang.String, feign.Response)
     */
    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {

            try {
                return new BadRequestException(
                        ((ErrorMessage) new GsonDecoder().decode(response, ErrorMessage.class))
                                .getErrorCode());
                
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return new BadRequestException();

        }
        return new RuntimeException(response.body().toString());
    }

}
