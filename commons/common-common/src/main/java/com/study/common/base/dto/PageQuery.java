package com.study.common.base.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PageQuery<String, Object> extends Page implements Map {

    private Map<String, Object> params = new HashMap();


    public PageQuery(int pageSize, int pageIndex, Map<String, Object> params) {
        super(pageSize, pageIndex);
        this.params = params;
    }


    public PageQuery(Page page, Map<String, Object> params) {
        super(page.getPageSize(), page.getPageIndex());
        if (params == null) {
            params = new HashMap<>();
        }
        this.params = params;
    }

    public PageQuery(Page page) {
        super(page.getPageSize(), page.getPageIndex());
        this.params = new HashMap();
    }

    /**
     * @param pageSize
     * @param pageIndex
     */
    public PageQuery(int pageSize, int pageIndex) {
        super(pageSize, pageIndex);
        this.params = new HashMap<>();
    }

    @Override
    public int size() {
        return this.params.size();
    }

    @Override
    public boolean isEmpty() {
        return params.isEmpty();
    }

    @Override
    public boolean containsKey(java.lang.Object key) {
        return params.containsKey(key);
    }

    @Override
    public boolean containsValue(java.lang.Object value) {
        return this.params.containsValue(value);
    }

    @Override
    public java.lang.Object get(java.lang.Object key) {
        return this.params.get(key);
    }

    @Override
    public java.lang.Object put(java.lang.Object key, java.lang.Object value) {
        return null;
    }

    @Override
    public java.lang.Object remove(java.lang.Object key) {
        return params.remove(key);
    }

    @Override
    public void putAll(Map m) {
        params.putAll(m);
    }

    @Override
    public void clear() {
        params.clear();
    }

    @Override
    public Set keySet() {
        return params.keySet();
    }

    @Override
    public Collection values() {
        return params.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return params.entrySet();
    }
}
