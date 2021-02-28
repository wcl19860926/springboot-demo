package com.study.common.i18n;

import com.study.common.core.util.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class I18nMessageHelper {
    private static MessageSource messageSource = null;

    public I18nMessageHelper() {
    }

    public static String getI18nMessage(String key, Object[] args) {
        try {
            return getMessageSource().getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception var4) {
            String defaultMessage = "message key [" + key + "] not exist";
            return defaultMessage;
        }
    }

    public static String getI18nMessage(String key) {
        try {
            return getMessageSource().getMessage(key, (Object[])null, LocaleContextHolder.getLocale());
        } catch (Exception var3) {
            String defaultMessage = "message key [" + key + "] not exist";
            return defaultMessage;
        }
    }

    private static MessageSource getMessageSource() {
        if (messageSource == null) {
            messageSource = (MessageSource) BeanUtils.getBean(MessageSource.class);
        }

        return messageSource;
    }
}