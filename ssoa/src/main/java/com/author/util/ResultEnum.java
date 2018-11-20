package com.author.util;

public enum ResultEnum {

	SUCCESS(100000, "成功"), 
	ERROR(110000, "内部错误"), 
	CODE_ERROR(110099, "代码错误"), 
	SAVE_ROLE_SUCCESS(100001, "保存角色成功"),
	SAVE_ROLE_ERROR(110001, "保存角色失败"), 
	DELETE_ROLE_SUCCESS(100002, "删除角色成功"), 
	DELETE_ROLE_ERROR(110002, "删除角色失败"),
	SAVE_PERMISSION_SUCCESS(100003, "保存权限成功"), 
	SAVE_PERMISSION_ERROR(110003, "保存权限失败"),
	DELETE_PERMISSION_SUCCESS(100004, "删除权限成功"), 
	DELETE_PERMISSION_ERROR(110004, "删除权限失败"),
	USER_NON_EXISTENT(110005, "用户不存在"), 
	USER_ROLE_NON_EXISTENT(110006, "当前登录用户没有角色信息"),
	ACHIEVE_PERMISSION_SUCCESS(100007, "获取资源列表成功"), 
	ACHIEVE_PERMISSION_ERROR(110007, "当前登录用户角色、权限信息有误"),
	SAVE_USER_SUCCESS(100008, "保存用户成功"), 
	SAVE_USER_ERROR(110008, "保存用户失败"), 
	SEARCH_USER_SUCCESS(100009, "获取用户列表成功"),
	SEARCH_USER_ERROR(110009, "获取用户列表失败"), 
	SEARCH_ROLE_SUCCESS(100010, "获取用户列表成功"),
	SEARCH_ROLE_ERROR(110010, "获取用户列表失败"),
	NULL_POINTER_EXCEPTION(110011, "空指针异常"),
	CLASS_CAST_EXCEPTION(110012, "类型转换异常"),
	IO_EXCEPTION(110013, "IO异常"),
	NO_SUCH_METHOD_EXCEPTION(110014, "没有这种方法异常"),
	INDEX_OUT_OF_BOUNDS_EXCEPTION(110015, "数组下标越界异常"),
	HTTP_MESSAGE_NOT_READABLE_EXCEPTION(110016, "HTTP消息不可读异常 "),
	TYPE_MISMATCH_EXCEPTION(110017, "类型异常"),
	MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(110018, "请求参数缺失异常"),
	HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(110019, "不支持HTTP请求方法异常"),
	HTTP_MEDIATYPE_NOT_ACCEPTABLE_EXCEPTION(110020, "不可接受的HTTP请求异常"),
	CONVERSION_NOT_SUPPORTED_EXCEPTION(110021, "转换支持异常"),
	HTTP_MESSAGE_NOT_WRITABLE_EXCEPTION(110022, "不可写的HTTP消息异常"),
	STACK_OVERFLOW_ERROR(110023, "栈溢出异常"),
	DELETE_USER_SUCCESS(100024, "删除用户成功"), 
	DELETE_USER_ERROR(110025, "删除用户失败"),
	EDIT_USER_SUCCESS(100026, "删除用户成功"), 
	EDIT_USER_ERROR(110027, "删除用户失败"),
	DETAIL_USER_SUCCESS(100028, "获取用户成功"), 
	DETAIL_USER_ERROR(110029, "获取用户失败"),
	USER_ADN_PASS_ERROR(110030, "用户名或密码错误"), 
	GENERATE_TOKEN_SUCCESS(100031, "生成token成功"), 
	CANCEL_TOKEN_SUCCESS(100032, "token注销成功"),
	CANCEL_TOKEN_ERROR(110033, "token不存在"),
	USER_ADN_PASS_NULL_ERROR(110034, "用户名或密码不能为空"),
	CHECK_TOKEN_SUCCESS(100035, "token验证成功"), 
	USERID_NULL_ERROR(110036, "用户ID不能为空"),
	SEARCH_AUTHORTY_SUCCESS(100037, "获取权限列表成功"),
	SEARCH_AUTHORTY_ERROR(110037, "获取权限列表失败"),
	
	CONTINUE(100, "继续"),
	SWITCHING_PROTOCOLS(101, "交换协议"),
	PROCESSING(102, "处理"),
	CHECKPOINT(103, "检查点"),
	OK(200, "成功"),
	CREATED(201, "创建"),
	ACCEPTED(202, "认可的"),
	NON_AUTHORITATIVE_INFORMATION(203, "非授权信息"),
	NO_CONTENT(204, "无内容"),
	RESET_CONTENT(205, "重置内容"),
	PARTIAL_CONTENT(206, "部分内容"),
	MULTI_STATUS(207, "多状态"),
	ALREADY_REPORTED(208, "已经报道"),
	IM_USED(226, "我用过"),
	MULTIPLE_CHOICES(300, "多种选择"),
	MOVED_PERMANENTLY(301, "永久移除"),
	FOUND(302, "发现"),
	MOVED_TEMPORARILY(302, "临时搬家"),
	SEE_OTHER(303, "看到其他"),
	NOT_MODIFIED(304, "未修改"),
	USE_PROXY(305, "使用代理"),
	TEMPORARY_REDIRECT(307, "暂时重定向"),
	PERMANENT_REDIRECT(308, "永久重定向"),
	BAD_REQUEST(400, "不良要求"),
	UNAUTHORIZED(401, "未经授权的"),
	PAYMENT_REQUIRED(402, "要求付款"),
	FORBIDDEN(403, "被禁止的"),
	NOT_FOUND(404, "找不到"),
	METHOD_NOT_ALLOWED(405, "方法不允许"),
	NOT_ACCEPTABLE(406, "不可接受"),
	PROXY_AUTHENTICATION_REQUIRED(407, "需要代理身份验证"),
	REQUEST_TIMEOUT(408, "请求超时"),
	CONFLICT(409, "冲突"),
	GONE(410, "跑了"),
	LENGTH_REQUIRED(411, "长度要求"),
	PRECONDITION_FAILED(412, "先决条件失败"),
	PAYLOAD_TOO_LARGE(413, "有效载荷过大"),
	REQUEST_ENTITY_TOO_LARGE(413, "请求实体过大"),
	URI_TOO_LONG(414, "太长了"),
	REQUEST_URI_TOO_LONG(414, "请求URI太长"),
	UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
	REQUESTED_RANGE_NOT_SATISFIABLE(416, "请求范围不符合要求"),
	EXPECTATION_FAILED(417, "期望失败"),
	I_AM_A_TEAPOT(418, "我是茶壶"),
	INSUFFICIENT_SPACE_ON_RESOURCE(419, "资源空间不足"),
	METHOD_FAILURE(420, "方法失效"),
	DESTINATION_LOCKED(421, "目的地锁定"),
	UNPROCESSABLE_ENTITY(422, "无法处理的实体"),
	LOCKED(423, "锁定的"),
	FAILED_DEPENDENCY(424, "依赖失败"),
	UPGRADE_REQUIRED(426, "需要升级"),
	PRECONDITION_REQUIRED(428, "要求先决条件"),
	TOO_MANY_REQUESTS(429, "请求太多"),
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, "请求头字段太大"),
	UNAVAILABLE_FOR_LEGAL_REASONS(451, "因法律原因无法获得"),
	INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
	NOT_IMPLEMENTED(501, "未实施"),
	BAD_GATEWAY(502, "坏网关"),
	SERVICE_UNAVAILABLE(503, "服务不可用"),
	GATEWAY_TIMEOUT(504, "网关超时"),
	HTTP_VERSION_NOT_SUPPORTED(505, "版本未被支持"),
	VARIANT_ALSO_NEGOTIATES(506, "变型也谈判"),
	INSUFFICIENT_STORAGE(507, "存储不足"),
	LOOP_DETECTED(508, "回路检测"),
	BANDWIDTH_LIMIT_EXCEEDED(509, "超出带宽限制"),
	NOT_EXTENDED(510, "未扩展");

	private Integer code;

	private String message;

	ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
