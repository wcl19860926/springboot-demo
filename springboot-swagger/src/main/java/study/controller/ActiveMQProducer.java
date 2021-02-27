package study.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "生产者进程API接口")
@RestController
@RequestMapping("/producer")
public class ActiveMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProducer.class);


    @ApiOperation(value = "发送解析文本", notes = "发送解析文本", produces = "application/json")
    @RequestMapping(value = "/sendText", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, method = RequestMethod.POST)
    public String sendText(@RequestBody String text) {
        logger.info("发送的文本内容：{}", text);

        try {
            logger.info("发送的文本内容：{}", text);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return "SUCESS";
    }

    @ApiOperation(value = "查询单词计数", notes = "查询单词计数", produces = "application/json")
    @ApiImplicitParam(name = "word", value = "单词", paramType = "query", required = true, dataType = "String")
    @RequestMapping(value = "/queryWordCount", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, method = RequestMethod.POST)
    public String queryWordCount(@RequestBody String word) {
        logger.info("查询单词计数：{}", word);

        try {
            logger.info("发送的文本内容：{}", word);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return "SUCESS";
    }
}