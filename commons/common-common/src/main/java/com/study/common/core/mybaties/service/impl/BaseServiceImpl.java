package com.study.common.core.mybaties.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.study.common.base.dto.PageData;
import com.study.common.base.dto.PageQuery;
import com.study.common.core.id.service.IdGeneratorService;
import com.study.common.core.mybaties.entity.BaseEntity;
import com.study.common.core.mybaties.mapper.BaseMapper;
import com.study.common.core.mybaties.service.BaseService;
import com.study.common.core.mybaties.service.holder.SnowflakeIdGeneratorHolder;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T extends BaseEntity, PK extends Serializable> implements BaseService<T, PK> {

    private static final String SELECT_BY_PAGE_LIST = "selectList";

    private IdGeneratorService idGeneratorService;


    public BaseServiceImpl() {
        this.idGeneratorService = SnowflakeIdGeneratorHolder.getInstance();
    }

    @Autowired
    public SqlSessionTemplate sqlSession;

    public abstract BaseMapper<T, PK> getMapper();

    @Override
    public T findById(PK id) {
        Assert.notNull(id, "通过主键查找时，给出的id不能为空");
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public T insert(T entity) {
        Assert.notNull(entity, "待保存的对象不能为空");
        entity.setId(idGeneratorService.generate());
        getMapper().insert(entity);
        return entity;
    }


    @Override
    public T insertSelective(T entity) {
        Assert.notNull(entity, "待保存的对象不能为空");
        entity.setId(idGeneratorService.generate());
        getMapper().insertSelective(entity);
        return entity;
    }

    @Override
    public Integer update(T entity) {
        Assert.notNull(entity, "待保存的对象不能为空");
        return getMapper().update(entity);
    }

    @Override
    public Integer updateSelective(T entity) {
        Assert.notNull(entity, "待保存的对象不能为空");
        return getMapper().updateSelective(entity);
    }

    @Override
    public void delete(PK id) {
        Assert.notNull(id, "删除对象时，没有给出id");
        getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public List<T> selectList(Map<String, Object> params) {
        List<T> data = getMapper().selectList(params);
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        return data;
    }

    @Override
    public T selectOne(Map<String, Object> params) {
        return sqlSession.selectOne(getMapperName() + SELECT_BY_PAGE_LIST, params);
    }

    /**
     * 自定义sql查询List<EntityMap>
     */
    @Override
    public List<T> selectList(String statement, @Param("map") Map<String, Object> params) {
        List<T> data = sqlSession.selectList(getMapperName() + statement, params);
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        return data;
    }


    @Override
    public Integer selectCount(String statement, Map<String, Object> params) {
        return sqlSession.selectOne(getMapperName() + statement, params);
    }


    /**
     * 获取mapperName
     */
    private String getMapperName() {
        return this.getMapper().getClass().getInterfaces()[0].getName() + ".";
    }


    @Override
    public PageData<T> selectByPage(PageQuery params) {
        Page pageInfo = PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        PageData pageData = new PageData(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageData.setTotalRecord((int) pageInfo.getTotal());
        List<T> data =  getMapper().selectList(params);
        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList<>();
        }
        pageData.setData(data);
        return pageData;

    }


    /**
     * 自定义sql查询List<EntityMap>
     */
    @Override
    public PageData<T> selectByPage(String statement, PageQuery params) {
        Page pageInfo = PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        PageData pageData = new PageData(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageData.setTotalRecord((int) pageInfo.getTotal());
        List<T> data = sqlSession.selectList(getMapperName() + statement, params);
        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList<>();
        }
        pageData.setData(data);
        return pageData;
    }


}
