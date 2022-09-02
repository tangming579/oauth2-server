package com.tm.auth.api;

import com.tm.auth.common.api.CommonPage;
import com.tm.auth.common.api.AipResult;
import com.tm.auth.dto.AuthoritiesRequest;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.dto.AuthClientRequest;
import com.tm.auth.service.OAuthAuthorityService;
import com.tm.auth.service.OAuthClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author tangming
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/oauth/client")
@Api("应用管理")
@Slf4j
public class ClientController {
    @Autowired
    private OAuthClientService clientService;
    @Autowired
    private OAuthAuthorityService authorityService;

    @ApiOperation("创建应用")
    @PostMapping("/create")
    @ResponseBody
    public AipResult create(@Valid @RequestBody AuthClientRequest authClientRequest) {
        return AipResult.success(clientService.createClient(authClientRequest));
    }

    @ApiOperation("删除应用")
    @PostMapping("/delete")
    @ResponseBody
    public AipResult delete(String clientId) {
        return AipResult.success(clientService.deleteClient(clientId));
    }

    @ApiOperation("修改应用")
    @PostMapping("/update")
    @ResponseBody
    public AipResult update(@Valid @RequestBody OauthClientDetails authClientRequest) {
        return AipResult.success(clientService.updateClient(authClientRequest));
    }

    @ApiOperation("应用列表")
    @PostMapping("/list")
    @ResponseBody
    public CommonPage list(int pageNum, int pageSize) {
        return clientService.listPageClient(pageNum, pageSize);
    }

    @ApiOperation("修改应用权限")
    @PostMapping("/authorities")
    @ResponseBody
    public AipResult authorities(@Valid @RequestBody AuthoritiesRequest authoritiesRequest) {
        return AipResult.success(authorityService.authorities(authoritiesRequest));
    }
}
