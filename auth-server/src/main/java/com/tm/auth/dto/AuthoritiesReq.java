package com.tm.auth.dto;

import com.tm.auth.pojo.Authority;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author tangming
 * @date 2022/9/2
 */
@Data
public class AuthoritiesReq {
    @NotBlank(message = "不能为空")
    private String clientId;

    private List<Authority> authorities;
}
