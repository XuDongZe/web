package com.xdz.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/8/5 18:47<br/>
 * Version: 1.0<br/>
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @ResponseBody
    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String say() {
        logger.debug("say method debug");
        return "hello controller";
    }

    @ResponseBody
    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String sayV2(@RequestParam String name) {
        logger.debug("say method debug");
        return "hello " + name;
    }
}
