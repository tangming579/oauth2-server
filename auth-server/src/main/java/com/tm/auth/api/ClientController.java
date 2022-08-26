package com.tm.auth.api;

import com.tm.auth.common.api.CommonPage;
import com.tm.auth.common.api.CommonResult;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.dto.AuthClientRequest;
import com.tm.auth.service.AuthClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    private AuthClientService clientService;

    @ApiOperation("创建应用")
    @PostMapping("/create")
    @ResponseBody
    public CommonResult create(@Valid @RequestBody AuthClientRequest authClientRequest) {
        return CommonResult.success(clientService.createClient(authClientRequest));
    }

    @ApiOperation("删除应用")
    @PostMapping("/delete")
    @ResponseBody
    public CommonResult delete(String clientId) {
        return CommonResult.success(clientService.deleteClient(clientId));
    }

    @ApiOperation("修改应用")
    @PostMapping("/update")
    @ResponseBody
    public CommonResult update(@RequestBody OauthClientDetails authClientRequest) {
        return CommonResult.success(clientService.updateClient(authClientRequest));
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
    public void authorities() {

    }
}
