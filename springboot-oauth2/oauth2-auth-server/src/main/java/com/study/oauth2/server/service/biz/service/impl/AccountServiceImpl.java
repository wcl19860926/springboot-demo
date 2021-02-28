package com.study.oauth2.server.service.biz.service.impl;

import com.study.oauth2.server.service.base.impl.BaseServiceImpl;
import com.study.oauth2.server.entity.Account;
import com.study.oauth2.server.mapper.AccountMapper;
import com.study.oauth2.server.mybaties.mapper.BaseMapper;

import com.study.oauth2.server.service.biz.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService {

    @Resource
    AccountMapper mapper;

    @Override
    public BaseMapper<Account, Long> getMapper() {
        return mapper;
    }

}
