package com.study.core.exception;

import com.study.core.util.ExceptionUtil;
import com.study.core.util.StrUtil;

/**
 * Bean异常
 * @author xiaoleilu
 */
public class ConverterException extends RuntimeException{
	private static final long serialVersionUID = -8096998667745023423L;

	public ConverterException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ConverterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ConverterException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
