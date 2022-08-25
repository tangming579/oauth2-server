package com.tm.auth.api;

import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.dto.AuthClientRequest;
import com.tm.auth.service.AuthClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tangming
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/oauth/client")
@Api(value = "client")
@Slf4j
public class ClientController {
    @Autowired
    private AuthClientService clientService;

    @ApiOperation("创建App")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody OauthClientDetails oauthClientDetails) {
        clientService.createClient(oauthClientDetails);
        return "添加成功";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(String clientId) {
        clientService.deleteClient(clientId);
        return "添加成功";
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(@RequestBody AuthClientRequest authClientRequest) {

    }

    @PostMapping("/list")
    @ResponseBody
    public List<OauthClientDetails> list() {
        return clientService.getList();
    }

    public void authorities() {

    }
}
