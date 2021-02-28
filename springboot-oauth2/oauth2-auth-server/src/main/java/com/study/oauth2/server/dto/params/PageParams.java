package com.study.oauth2.server.dto.params;

import com.study.oauth2.server.dto.common.Page;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PageParams<String ,Object>   extends Page  implements Map {

    private  Map<String , Object >  params  =  new HashMap();


    public PageParams(int pageSize, int pageIndex, Map<String, Object> params) {
        super(pageSize, pageIndex);
        this.params = params;
    }

    public PageParams(int pageSize, int pageIndex) {
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
        return this.params.containsValue( value);
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
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }
}
