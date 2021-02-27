package com.study.export.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportField {
    /**
     * 导出文件字段的名称，notEmpty
     *
     * @return
     */
    String title() default "";

    /**
     * 导出字段的顺序， 从1开始，最大到1000 ，不充许重复
     *
     * @return
     */
    int cols() default 0;


    /**
     * 导出默认的时间格试
     *
     * @return
     */
    String dataFormat() default "";


    /**
     * 枚举。
     *
     * @return
     */
    Class<? extends IEnum> enumValue() default IEnum.class;

}