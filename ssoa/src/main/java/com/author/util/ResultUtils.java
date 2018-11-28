package com.author.util;

public class ResultUtils {
	
	private static final Integer SUCCESS = 200;
	private static final String SUCCESS_MESSAGE = "接口访问成功";
	
	private static final Integer ERROR = 500;
	private static final String ERROR_MESSAGE = "业务执行失败";
	
	private static final String PARAM_ERROR_MESSAGE = "参数错误";
	
	public static <T> Result<T> success(ResultEnum e) {
		return success(e, null);
	}
	
	public static <T> Result<T> success() {
		return success(ResultEnum.SUCCESS);
	}
	
	public static <T> Result<T> error(ResultEnum e) {
		return new Result<>(ERROR,ERROR_MESSAGE, e.getCode(), e.getMessage());
	}
	
	public static <T> Result<T> error(ResultEnum e, String message) {
		return new Result<>(ERROR,ERROR_MESSAGE, e.getCode(), e.getMessage() + message);
	}
	
	public static <T> Result<T> paramError(ResultEnum e) {
		return new Result<>(ERROR,PARAM_ERROR_MESSAGE, e.getCode(), e.getMessage());
	}
	
	public static <T> Result<T> success(ResultEnum e, T data) {
		return new Result<>(SUCCESS,SUCCESS_MESSAGE, e.getCode(), e.getMessage(), data);
	}
	
	public static <T> Result<T> success(ResultEnum e, String message, T data) {
		return new Result<>(SUCCESS,SUCCESS_MESSAGE, e.getCode(), e.getMessage() + message);
	}
	

}
