package com.study.user.entity.sys;



import com.study.common.core.mybaties.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "", description = "")
public  class SysRole extends  BaseEntity<String>{


private static final long serialVersionUID=1L;



/**
* 
*/
@ApiModelProperty(value = "")
private   String  code;

/**
* 
*/
@ApiModelProperty(value = "")
private   String  name;

}
