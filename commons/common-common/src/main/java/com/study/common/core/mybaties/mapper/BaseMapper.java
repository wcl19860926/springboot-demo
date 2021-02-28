package com.study.common.core.mybaties.mapper;

import com.study.common.core.mybaties.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T extends BaseEntity, PK extends Serializable> {

    T insert(T entity);

    Integer deleteByPrimaryKey(PK id);


    T insertSelective(T entity);

    Integer update(T entity);


    Integer updateSelective(T entity);


    T selectByPrimaryKey(PK id);


    List<T> selectList(Map<String, Object> params);

}
