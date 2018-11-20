package com.lt.base.outrest.handler;

import java.io.IOException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.base.common.ResultEnum;
import com.lt.base.contants.Result;
import com.lt.base.util.ResultUtils;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
	
	//运行时异常  
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException ex) {
    	log.error("运行时异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
	//空指针异常  
    @ExceptionHandler(NullPointerException.class)
    public Result<?> nullPointerExceptionHandler(NullPointerException ex) {
    	log.error("空指针异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.NULL_POINTER_EXCEPTION);
    }
 
    //类型转换异常  
    @ExceptionHandler(ClassCastException.class)
    public Result<?> classCastExceptionHandler(ClassCastException ex) {
    	log.error("类型转换异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.CLASS_CAST_EXCEPTION);
    }

	//IO异常  
    @ExceptionHandler(IOException.class)
    public Result<?> iOExceptionHandler(IOException ex) {
    	log.error("IO异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.IO_EXCEPTION);
    }
 
    //未知方法异常  
    @ExceptionHandler(NoSuchMethodException.class)
    public Result<?> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
    	log.error("未知方法异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.NO_SUCH_METHOD_EXCEPTION);
    }
 
    //数组越界异常  
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Result<?> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
    	log.error("数组越界异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.INDEX_OUT_OF_BOUNDS_EXCEPTION);
    }
 
    //400错误  
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result<?> requestNotReadable(HttpMessageNotReadableException ex) {
        log.error("400异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //400错误  
    @ExceptionHandler({TypeMismatchException.class})
    public Result<?> requestTypeMismatch(TypeMismatchException ex) {
        log.error("400异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //400错误  
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result<?> requestMissingServletRequest(MissingServletRequestParameterException ex) {
        log.error("400异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //405错误  
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<?> request405(HttpRequestMethodNotSupportedException ex) {
    	log.error("405异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //406错误  
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Result<?> request406(HttpMediaTypeNotAcceptableException ex) {
        log.error("406异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //500错误  
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Result<?> server500(RuntimeException ex) {
        log.error("500异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }
 
    //栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public Result<?> requestStackOverflow(StackOverflowError ex) {
        return null;
    }
 
    //其他错误
    @ExceptionHandler({Exception.class})
    public Result<?> exception(Exception ex) {
    	log.error("异常：" + ex.getMessage(), ex);
        return ResultUtils.error(ResultEnum.ERROR);
    }

}
