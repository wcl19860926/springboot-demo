package com.study.oauth2.server.i18n;

import com.study.oauth2.server.util.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * 国际化工具类
 */
public final class I18nMessageHelper {

    private static volatile MessageSource messageSource;


    private I18nMessageHelper() {
    }


    private static MessageSource getMessageSource() {
        if (messageSource == null) {
            synchronized (I18nMessageHelper.class) {
                if (messageSource == null) {
                    messageSource = (MessageSource) BeanUtils.getBean(MessageSource.class);
                }
            }
        }
        return messageSource;
    }


    /**
     * 获取国际化消息
     * @param key
     * @param args
     * @return
     */
    public static String getMsg(String key, Object[] args) {
        try {
            return getMessageSource().getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception var4) {
            String defaultMessage = "message key [" + key + "] not exist";
            return defaultMessage;
        }
    }

    /**
     * 获取国际化消息
     * @param key
     * @return
     */
    public static String getMsg(String key) {
        try {
            return getMessageSource().getMessage(key, (Object[]) null, LocaleContextHolder.getLocale());
        } catch (Exception var3) {
            String defaultMessage = "message key [" + key + "] not exist";
            return defaultMessage;
        }
    }


}
