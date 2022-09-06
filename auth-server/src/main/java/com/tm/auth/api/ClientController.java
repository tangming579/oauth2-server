package com.tm.auth.api;

import com.tm.auth.common.api.ApiResult;
import com.tm.auth.dto.AuthoritiesReq;
import com.tm.auth.dto.ClientUpdateReq;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.dto.ClientCreateReq;
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
@RequestMapping("/api/oauth/client")
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
    public ApiResult create(@Valid @RequestBody ClientCreateReq clientCreateReq) {
        return ApiResult.success(clientService.createClient(clientCreateReq));
    }

    @ApiOperation("删除应用")
    @PostMapping("/delete")
    @ResponseBody
    public ApiResult delete(String clientId) {
        return ApiResult.success(clientService.deleteClient(clientId));
    }

    @ApiOperation("修改应用")
    @PostMapping("/update")
    @ResponseBody
    public ApiResult update(@Valid @RequestBody ClientUpdateReq clientDetailsRequest) {
        return ApiResult.success(clientService.updateClient(clientDetailsRequest));
    }

    @ApiOperation("应用列表")
    @PostMapping("/list")
    @ResponseBody
    public ApiResult list(int pageNum, int pageSize) {
        return ApiResult.success(clientService.listPageClient(pageNum, pageSize));
    }

    @ApiOperation("修改应用权限")
    @PostMapping("/allocAuthorities")
    @ResponseBody
    public ApiResult allocAuthorities(@Valid @RequestBody AuthoritiesReq authoritiesReq) {
        return ApiResult.success(authorityService.allocClientAuthorities(authoritiesReq));
    }

    @ApiOperation("获取应用权限")
    @PostMapping("/getAuthorities")
    @ResponseBody
    public ApiResult getAuthorities(String clientId) {
        return ApiResult.success(authorityService.getClientAuthorities(clientId));
    }
}
