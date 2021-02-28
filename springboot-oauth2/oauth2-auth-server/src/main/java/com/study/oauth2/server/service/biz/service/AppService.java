package com.study.oauth2.server.service.biz.service;


import com.study.oauth2.server.service.base.service.BaseService;
import com.study.oauth2.server.entity.App;

import java.util.List;

public interface AppService extends BaseService<App, Long> {

    List<App> findByNameOrClientId(String name, String clientId);

    App findByClientId(String clientId);

}
