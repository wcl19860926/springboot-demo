package com.study.comcom.dto.result;


import com.study.common.error.code.ErrorCode;

import lombok.Data;

/**
 * api返回的统一包装类 <br>
 *
 * @author @author xiquee
 * @date 2018-11-09 10:16:00
 */
@Data
public class ResultDto {
	/**
	 * 日志记录器
	 */
//    protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 返回的错误码，默认为零表示接口正常处理
	 */
	private int errorCode = 0;
	/**
	 * 返回的错误信息，默认为空表示接口正常处理
	 */
	private String msg = "";
	/**
	 * 返回的业务数据
	 */
	private Object data;

	/**
	 * 创建新的结果对象
	 */
	public ResultDto() {

	}

	/**
	 * 创建新的结果对象
	 *
	 * @param data result data
	 */
	public ResultDto(Object data) {
		this.setData(data);
	}

	/**
	 * 创建新的结果对象
	 *
	 * @param result result data
	 *//*
		 * public ResultDto(Result<?> result) { if (result.isSuccess()) { this.data =
		 * result.getValue(); } else { String message =
		 * I18nMessageHelper.getI18nMessage(result.getErrorCode().getMessageKey(),
		 * result.getArgs()); this.data = result.getValue(); this.errorCode =
		 * result.getErrorCode().getCode(); this.msg = message; //
		 * logger.error(errorCode + " : " + message); } this.setData(result.getValue());
		 * }
		 * 
		 * public ResultDto(ErrorCode errorCode) { String message =
		 * I18nMessageHelper.getI18nMessage(errorCode.getMessageKey(), null);
		 * this.errorCode = errorCode.getCode(); this.msg = message; //
		 * logger.error(errorCode + " : " + message); }
		 * 
		 * public ResultDto(ErrorCode errorCode, String... args) { String message =
		 * I18nMessageHelper.getI18nMessage(errorCode.getMessageKey(), args);
		 * this.errorCode = errorCode.getCode(); this.msg = message; }
		 */

}
