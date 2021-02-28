package com.study.oauth2.server.controller.code;

import com.study.oauth2.server.constants.Constant;
import com.study.oauth2.server.service.cache.RedisCacheService;
import com.study.oauth2.server.service.code.ImageCodeGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class ValidationCodeController {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ImageCodeGeneratorService imageCodeGeneratorService;


    /**
     * 生成一个有效期为60秒的图形验证码，
     * 并将此验证码和需要保护的数据进行绑定（如:账号、手机号）
     */
    @ResponseBody
    @RequestMapping("/code/image")
    public void image(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String formToken = request.getParameter(Constant.REQUEST_KEY_FORM_TOKEN);
        ImageCodeGeneratorService.ImageCode imageCode = imageCodeGeneratorService.generate();
        redisCacheService.set(Constant.CACHE_KEY_IMAGE_YZM + ":" + formToken, imageCode.getCode(), 60);
        logger.debug("生成图形验证码：{}, 有效期:{}妙", imageCode.getCode(), 60);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }


}
