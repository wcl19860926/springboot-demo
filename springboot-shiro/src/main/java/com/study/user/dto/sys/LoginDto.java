package com.study.user.dto.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data

public class LoginDto {
    @NotEmpty
    String username;
    @NotEmpty
    String password;
    String validateCode;
    String deviceId;
    String deviceType;
}
