/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scs.framework.core.exception.BadRequestException;
import com.scs.framework.core.exception.ErrorMessage;
import com.scs.framework.core.exception.ForbiddenException;
import com.scs.framework.core.exception.GoneException;
import com.scs.framework.core.exception.InternalServerErrorException;
import com.scs.framework.core.exception.NotFoundException;
import com.scs.framework.core.exception.UnprocesableEntityException;

/**
 * restful api统一异常处理
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
@ControllerAdvice
public class RestfulApiExceptionHandler {

    @ExceptionHandler(GoneException.class)
    public ResponseEntity<ErrorMessage> handleGone(GoneException ex) {
        return createGoneResponse(ex);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorMessage> handleInternalServerError(InternalServerErrorException ex) {
        return createInternalServerErrorResponse(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequest(BadRequestException ex) {
        return createBadRequestResponse(ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex) {
        return createNotFoundResponse(ex);
    }

    @ExceptionHandler(UnprocesableEntityException.class)
    public ResponseEntity<ErrorMessage> handleUnprocesable(UnprocesableEntityException ex) {
        return createUnprocesableEntityResponse(ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(ForbiddenException ex) {
        return createForbiddenResponse(ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleUnexpectedServerError(RuntimeException ex) {
        return createRuntimeExceptionResponse(ex);
    }

    protected ResponseEntity<ErrorMessage> createGoneResponse(GoneException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.GONE);
    }

    protected ResponseEntity<ErrorMessage> createInternalServerErrorResponse(InternalServerErrorException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ErrorMessage> createBadRequestResponse(BadRequestException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorMessage> createNotFoundResponse(NotFoundException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<ErrorMessage> createForbiddenResponse(ForbiddenException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    protected ResponseEntity<ErrorMessage> createUnprocesableEntityResponse(UnprocesableEntityException ex) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(ex.getMessage(), ex.getMessage(), ex.getMap()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    protected ResponseEntity<ErrorMessage> createRuntimeExceptionResponse(RuntimeException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
