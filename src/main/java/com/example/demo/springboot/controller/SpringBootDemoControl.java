package com.example.demo.springboot.controller;

import com.example.demo.util.WeChatUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/test")
public class SpringBootDemoControl {

    @RequestMapping(value = "/demo1", method = RequestMethod.GET)
    public Map test(String code) throws Exception {
        return WeChatUtil.getWeChatUserInfo(code);
    }
}
