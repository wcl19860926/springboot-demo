package com.study.common.core.mybaties.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity<Pk> implements Serializable {

    private  Pk  id;



}
