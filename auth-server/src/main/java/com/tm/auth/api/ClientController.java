package com.tm.auth.api;

import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.po.OAuthClient;
import com.tm.auth.service.ClientDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tangming
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/auth/client")
@Api(value = "client")
@Slf4j
public class ClientController {

    @ApiOperation("创建App")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody OauthClientDetails oauthClientDetails) {
        //clientService.createClient(oauthClientDetails);
        return "添加成功";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(String clientId) {
        //clientService.deleteClient(clientId);
        return "添加成功";
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(@RequestBody OAuthClient oAuthClient) {

    }

    @PostMapping("/list")
    @ResponseBody
    public void list(@RequestBody OAuthClient oAuthClient) {

    }

    public void authorities(){

    }
}
