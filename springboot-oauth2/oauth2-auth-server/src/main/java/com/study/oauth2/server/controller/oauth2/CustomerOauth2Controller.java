package com.study.oauth2.server.controller.oauth2;

import com.study.oauth2.server.dto.common.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 覆盖WhitelabelApprovalEndPoint#getAccessConfirmation
 */
@Slf4j
@Controller
@SessionAttributes({"authorizationRequest"})
@RequestMapping("/oauth")
public class CustomerOauth2Controller {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;


    @RequestMapping("/approval_page")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {

        if (request.getAttribute("_csrf") != null) {
            CsrfToken csrfToken = (CsrfToken) (model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf"));
            model.put("_csrf_token_name", HtmlUtils.htmlEscape(csrfToken.getParameterName()));
            model.put("_csrf_token_value", HtmlUtils.htmlEscape(csrfToken.getToken()));
        }
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        if (authorizationRequest != null) {
            Set<String> scopeList = authorizationRequest.getScope();
            model.put("scopeList", scopeList);
        }
        return new ModelAndView("oauth/confirmAccess", model);
    }

    @RequestMapping("/error_page")
    public ModelAndView authError(Map<String, Object> model, HttpServletRequest request) throws Exception {
        return new ModelAndView("oauth/error", model);
    }


    public ResultDto revokeToken(@RequestParam("token") String access_token) {
        if (consumerTokenServices.revokeToken(access_token)) {
            return ResultDto.sucess(null);
        } else {
            return ResultDto.fail(-1);
        }
    }

}
