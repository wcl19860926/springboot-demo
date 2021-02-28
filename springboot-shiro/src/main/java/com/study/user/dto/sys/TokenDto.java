package com.study.user.dto.sys;

import lombok.Data;

import java.util.Date;
@Data
public class TokenDto {
    private String token;
    private String username;
    private String password;
    private long expire;
    private Date created;
}
