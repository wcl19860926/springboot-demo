package study.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebResult;


@RestController
@Api(tags = "测试Swage2")
@RequestMapping("/test")
public class TestController {
    Logger logger = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)

    //表示一个http请求的操作
    @ApiOperation(value = "修改指定产品", httpMethod = "PUT", produces = "application/json")
    //@ApiImplicitParams用于方法，包含多个@ApiImplicitParam表示单独的请求参数
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "产品ID", required = true, paramType = "path")})
    public String update(@PathVariable("id") Integer id, @ModelAttribute Product product) {
        logger.debug("修改指定产品接收产品id与产品信息=>%d,{}", id, product);
        if (id == null || "".equals(id)) {
            logger.debug("产品id不能为空");
            return "400";
        }
        return "0000";
    }


    //Model中的注解示例
    //表示对类进行说明，用于参数用实体类接收
    @ApiModel(value = "产品信息")
    static class Product {
        //表示对model属性的说明或者数据操作更改
        @ApiModelProperty(required = true, name = "name", value = "产品名称", dataType = "query")
        private String name;
        @ApiModelProperty(name = "type", value = "产品类型", dataType = "query")
        private String type;
    }
}


