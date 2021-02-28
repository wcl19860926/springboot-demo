package com.study.oauth2.server.dto.common;

import lombok.Data;

import java.util.List;


@Data
public class PageData<T>  extends  Page {

    private List<T> data;

    public PageData(int pageSize, int pageIndex) {
        super(pageSize, pageIndex);
    }

    public PageData() {
        super();
    }
}
