package com.tm.auth.api;

import com.tm.auth.po.OAuthClient;
import com.tm.auth.service.ClientDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tangming
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/auth/client")
@Slf4j
public class AppManagementController {

    @Resource
    ClientDetailsServiceImpl oAuthClientService;

    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody OAuthClient oAuthClient) {
        oAuthClientService.create(oAuthClient);
        return "添加成功";
    }
}
