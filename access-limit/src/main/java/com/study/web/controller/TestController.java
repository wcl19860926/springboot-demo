package com.study.web.controller;

import com.study.annotation.AccessLimit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @AccessLimit(maxCount = 20, seconds = 60)
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String findByParam(@RequestParam(required = false) String q,
                              @RequestParam(required = false, defaultValue = "1") int pageIndex,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String sort,
                              @RequestParam(required = false, defaultValue = "true") Boolean asc) throws Exception {
        System.out.println("我到了，你快来啊。。");
        return "Hello  word!";
    }
}
